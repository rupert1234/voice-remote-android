import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class JuliusClient {
	
	private String hostname;
	private int port;
	private JuliusOutputParser parser;
	
	public JuliusClient(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
		parser=new JuliusOutputParser();
	}

	public void run()
	{
		System.out.println("connecting to Julius...");
		
		Socket connection=null;
		BufferedReader stream=null;
		try{
			connection=new Socket(hostname,port);
			stream=new BufferedReader(new InputStreamReader(connection.getInputStream()));
		}catch(Exception e){
			System.out.println("Cannot connect:"+e);
			return;
		}
		
		System.out.println("connected!");
		
		while(connection.isConnected())
		{
			String line;
			try {
				line=stream.readLine();
			} catch (IOException e) {
				System.out.println("Error reading socket: "+e);
				break;
			}
			if(parser.addLine(line))
				System.out.println(parser.getRecognitionOutput());			
		}
		
		System.out.println("diconnected...");
		
		try {
			stream.close();
		} catch (IOException e) {}
		
	}
	
}
