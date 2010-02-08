import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class JuliusStarter implements Runnable {

	private String path;
	private Process proc;
	private List<String> output;
	private int hist_num;
	boolean ready;
	private JTextArea output_list;
	private StreamReader stdout,stderr;
	
	public JuliusStarter()
	{
		ready=false;
		hist_num=500;
		path=System.getProperty("user.dir")+"/julius/";
		output=new LinkedList<String>();
		output_list=null;
	}	
	
	public void run()
	{
		try {
			proc=Runtime.getRuntime().exec("./julius -C julian.jconf");			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Failed to start Julius ASR! Check your settings!\n"+e);
			proc=null;
			return;
		}
		
		ready=true;
		
		stdout=new StreamReader(this,new BufferedReader(new InputStreamReader(proc.getInputStream())));
		stderr=new StreamReader(this,new BufferedReader(new InputStreamReader(proc.getErrorStream())));
		
		stderr.start();
		
		stdout.run();
		
		while(stderr.isAlive())
		{
			try{
				Thread.sleep(1000);
			}catch(Exception e){}
		}
		
		JOptionPane.showMessageDialog(null,"Julius stopped running!");
		proc=null;
		
		terminate();		
	}	
	
	public void kill()
	{
		stdout.terminate();
		stderr.terminate();
	}
	
	public void terminate()
	{
		if(proc!=null)
		{
			proc.destroy();
		}
		proc=null;
		ready=false;
	}
	
	public boolean isReady()
	{
		return ready;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getHistNum() {
		return hist_num;
	}

	public void setHistNum(int histNum) {
		hist_num = histNum;
	}

	class ExitListener extends WindowAdapter
	{
		public void windowClosing(WindowEvent ev)
		{
			synchronized(output)
			{
				output_list=null;
			}
		}
	}
	
	public void displayOutput()
	{
		if(output_list!=null) return;
		
		JFrame window = new JFrame("Julius ASR output");
		
		synchronized(output)
		{
			output_list=new JTextArea();
			JScrollPane scroll=new JScrollPane(output_list);
			window.add(scroll);
			
			window.setSize(new Dimension(500,300));
			window.setLocation(new Point(650,0));
			output_list.setEditable(false);
			
		
			Iterator<String> i=output.iterator();
			while(i.hasNext())
			{
				String line=i.next();
				output_list.append(line+"\n");
			}
			
		}
		
		window.addWindowListener(new ExitListener());
		window.setVisible(true);
	}
	
	public void addLine(String line)
	{
		System.out.println(line);
		
		synchronized(output)
		{				
			output.add(line);
			if(output.size()>hist_num)
				output.remove(0);
			
			if(output_list!=null)
			{
				output_list.append(line+"\n");
				output_list.revalidate();
			}
		}
	}
	
	class StreamReader extends Thread
	{
		private JuliusStarter starter;
		private BufferedReader stream;
		private boolean running;
		
		public StreamReader(JuliusStarter starter, BufferedReader stream)
		{
			this.starter=starter;
			this.stream=stream;
		}
		
		public void run()
		{
			String line;
			
			running=true;
			while(running)
			{
				try{
					line=stream.readLine();											
				}catch(Exception e){
					JOptionPane.showMessageDialog(null,"Error reading Julius:\n"+e);
					break;
				}
				
				if(line==null)
				{
					break;
				}
				
				starter.addLine(line);
			}			
		}
		
		public void terminate()
		{
			running=false;
			try{
			stream.close();
			}catch(Exception e){}
		}
	}
	
}
