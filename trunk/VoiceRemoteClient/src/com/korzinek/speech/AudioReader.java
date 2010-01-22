package com.korzinek.speech;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

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
		
		say("Connecting to server...");
		
		Socket client=null;
		try {
			client=new Socket();
			client.bind(null);
			client.connect(new InetSocketAddress(server,port),1000);
		} catch (UnknownHostException e) {
			say("Cannot connect to "+server+":"+port+"!\n");	
			return;
		} catch (IOException e) {
			say("Socket error: "+e.getMessage()+"\n");
			return;
		}
		
		say("Connected!\n");
		
		if(client==null) return;
		
		DataOutputStream output=null;
		try {
			output=new DataOutputStream(client.getOutputStream());
		} catch (IOException e) {
			say(e.getMessage()+"\n");
		}
		
		if(output==null) return;
		
		say("recording started...\n");		
		audioInput.startRecording();		
		
		
		running=true;
		
		byte buffer[]=new byte[2048];
		int ret;
		
		while(running)
		{			
			ret=audioInput.read(buffer, 0, 2048);
			if(ret>0)
			{
				try {
					output.writeInt(reverse(ret));
					output.write(buffer, 0, ret);
				} catch (IOException e) {
					say("Write buf:"+e.getMessage()+"\n");
					break;
				}
			}
		}
				
		try {
			output.writeInt(0);
		} catch (IOException e) {
			say("Write end: "+e.getMessage()+"\n");
		}
		
		audioInput.stop();		
		say("recording done\n");
		
		try {
			output.close();
		} catch (IOException e) {
		}
		
		

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
