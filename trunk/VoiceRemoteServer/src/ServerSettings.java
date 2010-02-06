import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;


public class ServerSettings extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1057013266790426531L;

	public ServerSettings() {
		
		super("Sever settings");
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setSize(640,480);
		
		Container content = getContentPane();
		
		JTabbedPane tabbedPane = new JTabbedPane();
		
		JPanel general_panel = new JPanel();
		general_panel.setLayout(new BoxLayout(general_panel, BoxLayout.Y_AXIS));
		JLabel general_description = new JLabel();
		general_description.setText(general_desc_text);
		general_description.setMaximumSize(new Dimension(Integer.MAX_VALUE,50));
		general_description.setVerticalAlignment(JLabel.CENTER);
		general_description.setHorizontalAlignment(JLabel.CENTER);
		general_description.setBorder(BorderFactory.createEtchedBorder());
		general_panel.add(general_description);
		
		JPanel grammars_panel = new JPanel();
		grammars_panel.setLayout(new BoxLayout(grammars_panel, BoxLayout.Y_AXIS));
		JLabel grammars_description = new JLabel();
		grammars_description.setText(gramamr_desc_text);
		grammars_description.setMaximumSize(new Dimension(Integer.MAX_VALUE,50));
		grammars_description.setVerticalAlignment(JLabel.CENTER);
		grammars_description.setHorizontalAlignment(JLabel.CENTER);
		grammars_description.setBorder(BorderFactory.createEtchedBorder());
		grammars_panel.add(grammars_description);
		
		ActionSettings actions_panel = new ActionSettings();		
		
		tabbedPane.add("General", general_panel);
		tabbedPane.add("Grammars", grammars_panel);
		tabbedPane.add("Actions", actions_panel);
		
		content.add(tabbedPane);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				/*
				 * TODO: this needs to be changed later.
				 */
				System.exit(0);
			}
		});
	}
	
	private String general_desc_text="This tab contains the general settings for the server.";
	private String gramamr_desc_text="This tab allows to change the words that the system will recognize.";

}
