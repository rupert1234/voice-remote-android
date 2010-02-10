import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.w3c.dom.Element;


public class ActionPattern extends JPanel implements DocumentListener,MouseListener{
	
	private static final long serialVersionUID = 1L;
	private String pattern;
	private List<ActionActivity> activities;
	private final ActionManager parent;
	private final ActionPattern this_pattern;
	private JTextField pattern_field;
	private String [] groups;
	private boolean drag_start,drag_end;
	private boolean action_dragging;

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
		
		pattern_field=new JTextField();
		pattern_field.setMaximumSize(new Dimension(Integer.MAX_VALUE,30));
		pattern_field.setText(pattern);
		pattern_field.setAlignmentX(Component.LEFT_ALIGNMENT);
		pattern_field.getDocument().addDocumentListener(this);
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
				this_pattern.executeAllActivities(null);
			}
		});		
		buttons_bar.add(test);
		
		buttons_bar.add(Box.createHorizontalGlue());

		JButton remove_pattern=new JButton("Remove pattern");
		remove_pattern.addActionListener(new AbstractAction() {			
			public void actionPerformed(ActionEvent e) {			
				parent.removePattern(this_pattern);
				parent.revalidate();
				parent.repaint();
			}
		});
		buttons_bar.add(remove_pattern);
		
		add(buttons_bar);
		add(Box.createRigidArea(new Dimension(0,10)));

		setAlignmentX(Component.LEFT_ALIGNMENT);
		
		addMouseListener(this);
		drag_start=false;
		drag_end=false;
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
	
	public void executeAllActivities(String [] groups)
	{
		Iterator<ActionActivity> i=activities.iterator();
		ActionActivity act;
		while(i.hasNext())
		{
			act=i.next();
			act.execute(groups);
		}
	}
	
	public Iterator<ActionActivity> getActivities()
	{
		 return activities.iterator();		
	}

	public void changedUpdate(DocumentEvent e) {
	}
	public void insertUpdate(DocumentEvent e) {
		pattern=pattern_field.getText();
	}
	public void removeUpdate(DocumentEvent e) {
		pattern=pattern_field.getText();
	}

	public boolean matches(String str)
	{
		 Pattern p = Pattern.compile(pattern);
		 Matcher m = p.matcher(str);
		 boolean b = m.matches();
		 
		 if(b)
		 {
			 int num=m.groupCount();
			 groups=new String[num+1];
			 for(int i=0; i<=num; i++)
				 groups[i]=m.group(i);
		 }
		 
		 return b;
	}
	
	public String[] getGroups()
	{
		return groups;
	}

	public void mouseClicked(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {
		if(parent.getDragging() && !drag_start)
		{
			drag_end=true;
			setBorder(BorderFactory.createLineBorder(Color.blue,5));
		}
	}

	public void mouseExited(MouseEvent e) {
		if(parent.getDragging() && !drag_start)
		{
			drag_end=false;
			setBorder(null);
		}
	}

	public void mousePressed(MouseEvent e) {		
		setBorder(BorderFactory.createLineBorder(Color.red,5));
		parent.setDragging(true);
		drag_start=true;
	}

	public void mouseReleased(MouseEvent e) {
		setBorder(null);
		parent.setDragging(false);
		parent.processDnD();
	}
	
	public boolean isDnDStart()
	{
		return drag_start;
	}
	
	public boolean isDnDEnd()
	{
		return drag_end;
	}
	
	public void clearDnD()
	{
		setBorder(null);
		drag_start=false;
		drag_end=false;
	}
	
	public boolean getDragging()
	{
		return action_dragging;
	}
	
	public void setDragging(boolean b)
	{
		action_dragging=b;
	}
	
	public void processDnD()
	{
		int num=getComponentCount()-2;
		ActionActivity pat;
		int start=-1,end=-1;
		for(int i=1; i<num; i++)
		{
			pat=(ActionActivity)getComponent(i);
			if(pat.isDnDStart()) start=i;
			if(pat.isDnDEnd()) end=i;
			pat.clearDnD();
		}
		
		end++;//we wanna add after, not before
		if(end>start) end--; //if start is smaller than end, end is gonna reduce after removing start
		
		if(start>=0 && end>=0 && start!=end)
		{
			pat=(ActionActivity)getComponent(start);
			remove(start);
			add(pat,end);			
		}
		
	}
}
