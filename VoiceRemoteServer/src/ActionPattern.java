import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


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
	private JPanel buttons_bar;

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
		
		buttons_bar=new JPanel();
		buttons_bar.setBackground(new Color(129, 231, 222));
		buttons_bar.setLayout(new BoxLayout(buttons_bar, BoxLayout.X_AXIS));
		buttons_bar.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JButton add_activity=new JButton("Add activity");
		add_activity.addActionListener(new AbstractAction() {			
			@Override
			public void actionPerformed(ActionEvent e) {		
				ActionActivity act=new ActionActivity(this_pattern);				
				this_pattern.addActivity(act);
				act.revalidate();
			}
		});		
		buttons_bar.add(add_activity);
		
		JButton test=new JButton("Test");
		test.addActionListener(new AbstractAction() {			
			@Override
			public void actionPerformed(ActionEvent e) {		
				this_pattern.executeAllActivities(null);
			}
		});		
		buttons_bar.add(test);
		
		buttons_bar.add(Box.createHorizontalGlue());

		JButton remove_pattern=new JButton("Remove pattern");
		remove_pattern.addActionListener(new AbstractAction() {			
			@Override
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

	@Override
	public void changedUpdate(DocumentEvent e) {
	}
	@Override
	public void insertUpdate(DocumentEvent e) {
		pattern=pattern_field.getText();
	}
	@Override
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

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if(parent.getDragging() && !drag_start)
		{
			drag_end=true;
			setBorder(BorderFactory.createLineBorder(Color.blue,5));
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if(parent.getDragging() && !drag_start)
		{
			drag_end=false;
			setBorder(null);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {		
		setBorder(BorderFactory.createLineBorder(Color.red,5));
		parent.setDragging(true);
		drag_start=true;
	}

	@Override
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
		ActionActivity act;
		int start=-1,end=-1,count=0;;
		Iterator<ActionActivity> it=activities.iterator();
		while(it.hasNext())
		{
			act=it.next();
			if(act.isDnDStart()) start=count;
			if(act.isDnDEnd()) end=count;
			act.clearDnD();
			count++;
		}
		
		if(start>=0 && end>=0 && start!=end)
		{
			end++;//we wanna add after, not before
			if(end>start) end--; //if start is smaller than end, end is gonna reduce after removing start
			act=activities.get(start);
			activities.remove(start);
			activities.add(end,act);			
		}
		
		this.removeAll();		
		add(pattern_field);
		add(Box.createRigidArea(new Dimension(0,10)));
		add(buttons_bar);		
		
		it=activities.iterator();
		while(it.hasNext())
		{
			act=it.next();
			add(act,getComponentCount()-2);
		}	
		
	}
}
