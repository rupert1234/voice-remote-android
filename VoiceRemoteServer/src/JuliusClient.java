import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JOptionPane;


public class JuliusClient implements Runnable {
	
	private String hostname;
	private int port;
	private JuliusOutputParser parser;
	private ActionSettings actions;
	private BufferedWriter writer;
	private Object mutex;
	
	public JuliusClient(String hostname, int port, ActionSettings actions) {
		this.hostname = hostname;
		this.port = port;
		this.actions=actions;
		parser=new JuliusOutputParser();
		writer=null;
		mutex=new Object();
	}

	public void run()
	{		
		Socket connection=null;
		BufferedReader stream=null;
		try{
			connection=new Socket(hostname,port);
			stream=new BufferedReader(new InputStreamReader(connection.getInputStream()));
			synchronized (mutex) {
				writer=new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
			}
		}catch(Exception e){
			JOptionPane.showMessageDialog(null,"Cannot connect:"+e);
			return;
		}
		
		Main.settings.setStatusText("Julius is running.");
		while(connection.isConnected())
		{
			String line;
			try {
				line=stream.readLine();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,"Error reading socket: "+e);
				break;
			}
			if(line!=null && parser.addLine(line))
			{
				String ret=parser.getRecognitionOutput();
				System.out.println("\""+ret+"\"");
				actions.processRecognition(ret);
			}
		}
		Main.settings.setStatusText("Julius is NOT running.");
		
		try {
			stream.close();
			stream=null;	
			synchronized (mutex) {
				writer.close();
				writer=null;
			}
		} catch (IOException e) {}
		
	}
	
	public void reloadGrammars()
	{
		synchronized (mutex) {
			if(writer==null) return;
			try {
				writer.write("CHANGEGRAM grammar/main\nACTIVATEGRAM grammar/main\n");
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "ERROR: cannot reload grammar: "+e.getMessage());
			}
		}
	}
	
}
