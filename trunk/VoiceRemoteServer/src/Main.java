public class Main {

	public static JuliusStarter julius_starter;
	public static ServerSettings settings;
	public static TranscriptionCache transcription_cache;
	
	public static void main(String[] args) {

		transcription_cache=new TranscriptionCache();
		transcription_cache.load();
		
		settings=new ServerSettings();
		settings.setVisible(true);
		
		julius_starter=new JuliusStarter();
		Thread js_th=new Thread(julius_starter);
		js_th.start();		
		
		try{
		Thread.sleep(1000);
		}catch(Exception e){}
		
		JuliusClient client=new JuliusClient("localhost",5530,settings.getActions());
		Thread cl_th = new Thread(client);
		cl_th.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			public void run()
			{
				julius_starter.terminate();
			}
		});
		
		new MicrophoneInput();
		
	}

}
