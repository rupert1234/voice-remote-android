import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class ActionSettings extends JPanel {
	
	private String actions_desc_text="This tab is used for binding recognized utterances to specific actions.";
	private JScrollPane scrollPane;
	
	public ActionSettings()
	{
		
		setLayout(new BorderLayout());
		JLabel actions_description = new JLabel();
		actions_description.setText(actions_desc_text);
		actions_description.setPreferredSize(new Dimension(Integer.MAX_VALUE,50));
		actions_description.setVerticalAlignment(JLabel.CENTER);
		actions_description.setHorizontalAlignment(JLabel.CENTER);
		actions_description.setBorder(BorderFactory.createEtchedBorder());
		add(actions_description,BorderLayout.PAGE_START);
		
		ActionManager manager = new ActionManager();		
				
		scrollPane=new JScrollPane(manager);		
		add(scrollPane,BorderLayout.CENTER);				
	}
	
}
