import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JButton;
import javax.swing.JFrame;

public class MicrophoneInput extends JFrame implements MouseListener,Runnable{

	private boolean running;

	public MicrophoneInput()
	{
		JButton button=new JButton("record");

		button.addMouseListener(this);

		add(button);
		
		setSize(new Dimension(300,100));
		
		setVisible(true);
	}

	private void say(String msg)
	{
		System.out.println(msg);
	}

	private AudioFormat getFormat() {
		float sampleRate = 16000;
		int sampleSizeInBits = 16;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = false;
		return new AudioFormat(sampleRate, 
				sampleSizeInBits, channels, signed, bigEndian);
	}

	public void run() {

		running = true;

		Socket client=null;
		try {
			client=new Socket();
			client.bind(null);
			client.connect(new InetSocketAddress("localhost",5555),1000);
		} catch (UnknownHostException e) {
			say("Cannot connect to "+"localhost"+":"+5555+"!");	
			return;
		} catch (IOException e) {
			say("Socket error: "+e.getMessage());
			return;
		}

		say("Connected!");

		if(client==null) return;

		DataOutputStream output=null;
		try {
			output=new DataOutputStream(client.getOutputStream());
		} catch (IOException e) {
			say(e.getMessage()+"\n");
		}

		if(output==null) return;

		try {
			
			final AudioFormat format = getFormat();
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
			final TargetDataLine line = (TargetDataLine)
			AudioSystem.getLine(info);
			line.open(format);
			line.start();

			int bufferSize = (int)format.getSampleRate() * format.getFrameSize();
			byte buffer[] = new byte[bufferSize];

			say("recording started...");

			while (running) {
				int count = 
					line.read(buffer, 0, buffer.length);
				if (count > 0) {
					output.writeInt(reverse(count));
					output.write(buffer, 0, count);
				}
			}			
		} catch (LineUnavailableException e) {
			say("Line unavailable: " + e);
		} catch (IOException e) {
			say("I/O problems: " + e);
		}
		
		try{
			output.writeInt(0);
			output.close();
		} catch (Exception e) {
		}
	}
	
	public void stopRunning()
	{
		running=false;
	}
	
	public static int reverse(int x) {  
        ByteBuffer bbuf = ByteBuffer.allocate(4);  
        bbuf.order(ByteOrder.BIG_ENDIAN);  
        bbuf.putInt(x);  
        bbuf.order(ByteOrder.LITTLE_ENDIAN);  
        return bbuf.getInt(0);  
    }

	public void mouseClicked(MouseEvent e) {
	}
	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		Thread thread=new Thread(this);
		thread.start();
	}

	public void mouseReleased(MouseEvent e) {
		stopRunning();
	}
}
