import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;


public class MainMenu {

	CompatibleMainMenu comp;
	
	public MainMenu()
	{
		comp=null;
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		if(!SystemTray.isSupported())
		{
			JOptionPane.showMessageDialog(null, "Cannot create system tray!");
			comp=new CompatibleMainMenu();
			return;
		}
		
		PopupMenu popup=new PopupMenu();
		URL imageURL = MainMenu.class.getResource("image/icon.png");
		if(imageURL==null)
		{
			JOptionPane.showMessageDialog(null, "Cannot create system tray!");
			comp=new CompatibleMainMenu();
			return;
		}
		Image iconIMG=new ImageIcon(imageURL,"VoiceRemote").getImage();
		TrayIcon icon=new TrayIcon(iconIMG);
		icon.setImageAutoSize(true);
		SystemTray tray=SystemTray.getSystemTray();
		
		MenuItem settings=new MenuItem("Settings");
		settings.addActionListener(new AbstractAction() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Main.settings.setVisible(true);				
			}
		});
		
		PopupMenu julius=new PopupMenu("Julius");
		
		MenuItem console=new MenuItem("Show console");
		console.addActionListener(new AbstractAction() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Main.julius_starter.displayOutput();				
			}
		});
		
		MenuItem start=new MenuItem("Start");
		start.addActionListener(new AbstractAction() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Main.start_julius();							
			}
		});
		
		MenuItem stop=new MenuItem("Stop");
		stop.addActionListener(new AbstractAction() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Main.julius_starter.terminate();			
			}
		});
		
		MenuItem restart=new MenuItem("Restart");
		restart.addActionListener(new AbstractAction() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Main.julius_starter.terminate();	
				try{
					Thread.sleep(1000);
				}catch(Exception e){}
				Main.start_julius();
			}
		});
		
		MenuItem mic=new MenuItem("Show microphone");
		mic.addActionListener(new AbstractAction() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.microphone_input.setVisible(true);				
			}
		});
		
		MenuItem exit=new MenuItem("Exit");
		exit.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		
		popup.add(settings);
		julius.add(console);
		julius.add(start);
		julius.add(stop);
		julius.add(restart);
		popup.add(julius);
		popup.add(mic);
		popup.addSeparator();
		popup.add(exit);
		
		icon.setPopupMenu(popup);
		icon.setToolTip("VoiceRemote");
		icon.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.microphone_input.setVisible(true);				
			}
		});
		
		try {
			tray.add(icon);
		} catch (AWTException e) {
			JOptionPane.showMessageDialog(null, "Cannot create system tray!");
			comp=new CompatibleMainMenu();
			return;
		}		
	}
	
}
