public class Main {

	public static void main(String[] args) {

		ServerSettings settings=new ServerSettings();
		settings.setVisible(true);
		
		JuliusClient client=new JuliusClient("localhost",5530,settings.getActions());
		Thread th = new Thread(client);
		th.start();
		
	}

}
