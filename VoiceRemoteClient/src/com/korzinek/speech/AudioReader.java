package com.korzinek.speech;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Vector;

import android.media.AudioRecord;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class AudioReader implements Runnable {

	Handler console;
	AudioRecord audioInput;
	String server;
	int port;
	boolean running;
	
	public AudioReader(Handler console, AudioRecord audioInput, String server, int port) 
	{
		this.console = console;
		this.audioInput = audioInput;
		this.server = server;
		this.port = port;
	}
	
	public void say(String text)
	{
		Message msg = console.obtainMessage();
        Bundle b = new Bundle();
        b.putString("msg", text);
        msg.setData(b);
        console.sendMessage(msg);
	}
	
	public void run() {		
		
		Socket client=null;
		try {
			client=new Socket();
			client.bind(null);
			client.connect(new InetSocketAddress(server,port),1000);
		} catch (UnknownHostException e) {
			say("Cannot connect to "+server+":"+port+"!");	
			return;
		} catch (IOException e) {
			say("Socket error: "+e.getMessage());
			return;
		}
		
		
		if(client==null) return;
		
		DataOutputStream output=null;
		try {
			output=new DataOutputStream(client.getOutputStream());
		} catch (IOException e) {
			say(e.getMessage()+"\n");
		}
		
		if(output==null) return;
		
		AudioSender sender=new AudioSender(console,output);
		Thread th=new Thread(sender);
		th.start();
		
		audioInput.startRecording();		
		
		running=true;
		
		byte buffer[]=new byte[2048];
		int ret;
		
		while(running && sender.isRunning())
		{			
			ret=audioInput.read(buffer, 0, 2048);
			if(ret>0)
			{
				sender.addBuffer(buffer);
			}
		}
		
		sender.stopRunning();
		
		audioInput.stop();				
	}
	
	public static int reverse(int x) {  
        ByteBuffer bbuf = ByteBuffer.allocate(4);  
        bbuf.order(ByteOrder.BIG_ENDIAN);  
        bbuf.putInt(x);  
        bbuf.order(ByteOrder.LITTLE_ENDIAN);  
        return bbuf.getInt(0);  
    }  
	
	public void stop()
	{
		running=false;
	}

}

class AudioSender implements Runnable
{
	Handler console;
	DataOutputStream output;
	boolean running,stop;
	Vector<byte[]> buffers;
	
	AudioSender(Handler console, DataOutputStream output)
	{
		this.console=console;
		this.output=output;
		running=true;
		stop=false;
		buffers=new Vector<byte[]>();
	}
	
	public void run() {		
		
		boolean buffers_empty;
		
		while(!stop)
		{
			
			synchronized (buffers) {
				buffers_empty=buffers.isEmpty();
			}
			
			if(buffers_empty)
			{
				try{
					Thread.sleep(100);
				}catch(Exception e) {}
			}
			
			synchronized (buffers) {
				buffers_empty=buffers.isEmpty();
			}
			
			while(!buffers_empty)
			{
				byte [] buffer;
				synchronized (buffers) {					
					buffer = buffers.get(0);
					buffers.remove(0);
				}
				
				int ret = buffer.length;
				
				try {
					output.writeInt(AudioReader.reverse(ret));
					output.write(buffer, 0, ret);
				} catch (IOException e) {
					running=false;
					say(e.getMessage());
					return;
				}
				
				synchronized (buffers) {
					buffers_empty=buffers.isEmpty();
				}
			}
		}
		
		
		try {
			output.writeInt(0);
		} catch (IOException e) {}
		

		try {
			output.close();
		} catch (IOException e) {
		}
		
	}
	
	public void say(String text)
	{
		Message msg = console.obtainMessage();
        Bundle b = new Bundle();
        b.putString("msg", text);
        msg.setData(b);
        console.sendMessage(msg);
	}
	
	public void addBuffer(byte[] buffer)
	{
		byte [] copy=buffer.clone();
		synchronized (buffers) {			
			buffers.add(copy);
		}
	}
	
	public boolean isRunning()
	{
		return running;
	}
	
	public void stopRunning()
	{
		stop=true;
	}
}
