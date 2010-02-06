import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;


public class ActionManager extends JPanel {
	
	private List<ActionPattern> patterns;
	private final ActionManager this_manager; 
	public ActionManager()
	{
		this_manager=this;
		
		patterns=new LinkedList<ActionPattern>();
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JButton add_pattern = new JButton("Add pattern");
		add_pattern.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		add_pattern.addActionListener(new AbstractAction() {			
			public void actionPerformed(ActionEvent e) {
				ActionPattern act=new ActionPattern(this_manager);
				addPattern(act);				
				act.revalidate();				
			}
		});		
		
		add(Box.createRigidArea(new Dimension(0,10)));
		add(add_pattern);
	}
	
	public void addPattern(ActionPattern pat)
	{
		patterns.add(pat);
		add(pat,getComponentCount()-2);
	}
	
	public void removePattern(ActionPattern pat)
	{
		patterns.remove(pat);
		remove(pat);
	}

}
