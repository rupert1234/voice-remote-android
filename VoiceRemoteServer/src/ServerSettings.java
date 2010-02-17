import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
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
	
	private ActionSettings actions_panel;
	private GrammarSettings grammars_panel;
	private AdvancedGrammarEditor advanced_grammars_panel;
	private LexiconSettings lexicon_panel;

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
		
		JButton show_console=new JButton("Show Julius console");
		show_console.addActionListener(new AbstractAction() {			
			public void actionPerformed(ActionEvent e) {
				Main.julius_starter.displayOutput();				
			}
		});
		general_panel.add(show_console);
		
		grammars_panel = new GrammarSettings();
		
		advanced_grammars_panel = new AdvancedGrammarEditor();
		
		lexicon_panel = new LexiconSettings(grammars_panel);
		
		actions_panel = new ActionSettings();		
		
		tabbedPane.add("General", general_panel);
		tabbedPane.add("Grammars", grammars_panel);		
		tabbedPane.add("Lexicon",lexicon_panel);
		tabbedPane.add("Adv.GrammarEditor", advanced_grammars_panel);
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

	
	public ActionSettings getActions()
	{
		return actions_panel;
	}
	
}
