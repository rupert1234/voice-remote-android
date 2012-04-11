import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;


public class ActionSettings extends JPanel {
	
	private String actions_desc_text="<html>This tab is used for binding recognized utterances to specific actions. " +
			"First text-field contains the regular expression that, when matched with the utterance, will initiate the actions below. " +
			"You can move around actions and patterns by clicking and dragging them to desired positions.</html>";
	
	private JScrollPane scrollPane;
	
	ActionManager manager;
	
	public ActionSettings()
	{
		
		setLayout(new BorderLayout());
		JLabel actions_description = new JLabel();
		actions_description.setText(actions_desc_text);
		actions_description.setPreferredSize(new Dimension(Integer.MAX_VALUE,50));
		actions_description.setVerticalAlignment(SwingConstants.CENTER);
		actions_description.setHorizontalAlignment(SwingConstants.CENTER);
		actions_description.setBorder(BorderFactory.createEtchedBorder());
		add(actions_description,BorderLayout.PAGE_START);
		
		manager = new ActionManager();		
		scrollPane=new JScrollPane(manager);		
		add(scrollPane,BorderLayout.CENTER);
		
		JPanel buttons_bottom=new JPanel();
		
		JButton button_save=new JButton("Save All");
		button_save.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				manager.save();				
			}
		});
		
		buttons_bottom.add(button_save);
		add(buttons_bottom,BorderLayout.PAGE_END);
	}
	
	public void processRecognition(String recognition)
	{
		manager.processRecognition(recognition);
	}
	
}
