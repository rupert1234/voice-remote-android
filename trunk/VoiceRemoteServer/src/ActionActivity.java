import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/* TODO: parameters */

public class ActionActivity extends JPanel implements DocumentListener,MouseListener{
	
	private ActionActivityType type;
	private String argument;
	
	private final ActionPattern parent;
	private final ActionActivity this_activity;
	
	private JTextField argument_field;
	private JComboBox typesList;
	
	private boolean drag_start,drag_end;
	
	public ActionActivity(ActionPattern parent)
	{
		this(ActionActivityType.DIALOG,"",parent);
	}
	
	public ActionActivity(ActionActivityType type, String argument, ActionPattern action_parent) {
		parent=action_parent;
		this_activity=this;
		this.type = type;
		this.argument = argument;
		
		setAlignmentX(Component.LEFT_ALIGNMENT);
		setBackground(new Color(231, 129, 185));
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		String[] types={"Dialog","System","Mouse","Keyboard"};
		typesList=new JComboBox(types);
		typesList.setMaximumSize(new Dimension(100,20));
		typesList.addActionListener(new AbstractAction() {			
			public void actionPerformed(ActionEvent e) {
				this_activity.updateActionType();				
			}
		});
		add(typesList);
		
		switch(type)
		{
		case DIALOG: typesList.setSelectedIndex(0); break;
		case SYSTEM: typesList.setSelectedIndex(1); break;
		case MOUSE: typesList.setSelectedIndex(2); break;
		case KEYBOARD: typesList.setSelectedIndex(3); break;
		}
		
		argument_field=new JTextField(argument);
		argument_field.setMaximumSize(new Dimension(Integer.MAX_VALUE,30));
		argument_field.getDocument().addDocumentListener(this);
				
		add(argument_field);
		
		add(Box.createRigidArea(new Dimension(50,0)));
		
		JButton remove = new JButton("Remove");
		remove.addActionListener(new AbstractAction() {			
			public void actionPerformed(ActionEvent e) {
				parent.removeActivity(this_activity);
				parent.revalidate();
				parent.repaint();				
			}
		});
		add(remove);
		
		addMouseListener(this);
		
		drag_start=false;
		drag_end=false;
	}

	public ActionActivityType getType() {
		return type;
	}

	public void setType(ActionActivityType type) {
		this.type = type;
	}

	public String getArgument() {
		return argument;
	}

	public void setArgument(String argument) {
		this.argument = argument;
	}
	
	public void execute(String [] groups)
	{
		
		String arg=argument;
		if(groups!=null && groups.length>0)
		{
			for(int i=0; i<groups.length; i++)
			{
				arg=arg.replaceAll("\\$"+i, groups[i]);
			}
		}
		
		arg=arg.replaceAll("\\$.","");
		
		switch(type) {
		
		case DIALOG:
			JOptionPane.showMessageDialog(null, arg);
			break;
		case SYSTEM:
			try{
				Runtime.getRuntime().exec(arg);
			}catch(Exception e)
			{
				JOptionPane.showMessageDialog(null, "Error running process: "+e.getMessage());
			}
			break;
		case MOUSE:
			JOptionPane.showMessageDialog(null, "Mouse action not implemented yet!\nArgs: "+arg);
			break;
		case KEYBOARD:
			JOptionPane.showMessageDialog(null, "Keyboard action not implemented yet!\nArgs: "+arg);
			break;
		default:
			JOptionPane.showMessageDialog(null, "Don't know how to do "+type+"!\nArgs: "+arg);
		}
	}

	public void changedUpdate(DocumentEvent e) {
	}
	public void insertUpdate(DocumentEvent e) {
		argument=argument_field.getText();
	}
	public void removeUpdate(DocumentEvent e) {
		argument=argument_field.getText();
	}
	
	public void updateActionType()
	{
		String str=(String)typesList.getSelectedItem();
		if(str=="Dialog") type=ActionActivityType.DIALOG;
		else if(str=="System") type=ActionActivityType.SYSTEM;
		else if(str=="Mouse") type=ActionActivityType.MOUSE;
		else if(str=="Keyboard") type=ActionActivityType.KEYBOARD;
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
}
