import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class ActionPattern extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private String pattern;
	private List<ActionActivity> activities;
	private final ActionManager parent;
	private final ActionPattern this_pattern;

	public ActionPattern(ActionManager parent)
	{
		this("",parent);
	}
	
	public ActionPattern(String pattern, ActionManager parent_manager) {
		parent=parent_manager;
		this_pattern=this;
		this.pattern=pattern;
		
		activities=new LinkedList<ActionActivity>();
		
		setBackground(new Color(129, 231, 222));
		
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		JTextField pattern_field=new JTextField();
		pattern_field.setMaximumSize(new Dimension(Integer.MAX_VALUE,30));
		pattern_field.setText(pattern);
		pattern_field.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(pattern_field);
		
		JPanel buttons_bar=new JPanel();
		buttons_bar.setBackground(new Color(129, 231, 222));
		buttons_bar.setLayout(new BoxLayout(buttons_bar, BoxLayout.X_AXIS));
		buttons_bar.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JButton add_activity=new JButton("Add activity");
		add_activity.addActionListener(new AbstractAction() {			
			public void actionPerformed(ActionEvent e) {		
				ActionActivity act=new ActionActivity(this_pattern);				
				this_pattern.addActivity(act);
				act.revalidate();
			}
		});		
		buttons_bar.add(add_activity);
		
		JButton test=new JButton("Test");
		test.addActionListener(new AbstractAction() {			
			public void actionPerformed(ActionEvent e) {		
				this_pattern.executeAllActivities();
			}
		});		
		buttons_bar.add(test);
		
		buttons_bar.add(Box.createHorizontalGlue());

		JButton remove_pattern=new JButton("Remove pattern");
		remove_pattern.addActionListener(new AbstractAction() {			
			public void actionPerformed(ActionEvent e) {			
				parent.remove(this_pattern);
				parent.revalidate();
				parent.repaint();
			}
		});
		buttons_bar.add(remove_pattern);
		
		add(buttons_bar);
		add(Box.createRigidArea(new Dimension(0,10)));

		setAlignmentX(Component.LEFT_ALIGNMENT);
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	public void addActivity(ActionActivity act)
	{
		activities.add(act);
		add(act,this_pattern.getComponentCount()-2);
	}
	
	public void removeActivity(ActionActivity act)
	{
		activities.remove(act);
		remove(act);
	}
	
	public void executeAllActivities()
	{
		Iterator<ActionActivity> i=activities.iterator();
		ActionActivity act;
		while(i.hasNext())
		{
			act=i.next();
			act.execute();
		}
	}

}
