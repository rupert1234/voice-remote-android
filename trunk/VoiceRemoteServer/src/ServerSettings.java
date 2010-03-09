import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;


public class ServerSettings extends JFrame {
	
	private ActionSettings actions_panel;
	private GrammarWizard grammars_panel;
	private JLabel status_panel;

	public ServerSettings() {
		
		super("Sever settings");
		
		URL imageURL = MainMenu.class.getResource("image/icon.png");
		if(imageURL!=null)
		{
			Image iconIMG=new ImageIcon(imageURL,"VoiceRemote").getImage();
			setIconImage(iconIMG);
		}
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setSize(640,480);
		
		Container content = getContentPane();
		content.setLayout(new BorderLayout());
		
		JTabbedPane tabbedPane = new JTabbedPane();
		
		JPanel main_panel = new JPanel();	
		main_panel.setLayout(new GridLayout());
		JEditorPane about=new JEditorPane();
		about.setEditable(false);
		URL path=getClass().getResource("image/about.html");
		try{
			if(path!=null)
				about.setPage(path);
		}catch(Exception e){}
		JScrollPane scroll=new JScrollPane(about);
		main_panel.add(scroll);				
		
		
		grammars_panel = new GrammarWizard();
		
		actions_panel = new ActionSettings();		
		
		tabbedPane.add("About", main_panel);
		tabbedPane.add("Grammars", grammars_panel);		
		tabbedPane.add("Actions", actions_panel);
		
		content.add(tabbedPane,BorderLayout.CENTER);
		
		status_panel=new JLabel("Julius is NOT running.");
		status_panel.setPreferredSize(new Dimension(Integer.MAX_VALUE,20));
		status_panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		content.add(status_panel,BorderLayout.SOUTH);
	}

	
	public ActionSettings getActions()
	{
		return actions_panel;
	}
	
	public void setStatusText(String txt)
	{
		status_panel.setText(txt);
		status_panel.repaint();
	}
}
