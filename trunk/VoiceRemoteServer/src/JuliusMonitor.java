
public class JuliusMonitor implements Runnable {

	@Override
	public void run() {
		while(true)
		{			
			if(Main.julius_starter!=null)
			{
				if(Main.julius_starter.isRunning())
					Main.settings.setStatusText("Julius is running.");
				else
					Main.settings.setStatusText("Julius is NOT running.");
			}
			
			try{
				Thread.sleep(1000);
			}catch(Exception e){}
		}

	}

}
