	import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;


@SuppressWarnings("serial")
public class ActionManager extends JPanel {
	
	private List<ActionPattern> patterns;
	private final ActionManager this_manager; 
	private JButton add_pattern_button;
	private boolean dragging;
	
	public ActionManager()
	{
		this_manager=this;
		
		patterns=new LinkedList<ActionPattern>();
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add_pattern_button = new JButton("Add pattern");
		add_pattern_button.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		add_pattern_button.addActionListener(new AbstractAction() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				ActionPattern act=new ActionPattern(this_manager);
				addPattern(act);				
				act.revalidate();				
			}
		});		
		
		add(Box.createRigidArea(new Dimension(0,10)));
		add(add_pattern_button);
		
		load();
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
	
	void load()
	{
		File file;
		DocumentBuilderFactory dbf;
		DocumentBuilder db;
		Document document;
		try {
			file = new File("action_manager.xml");
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			document = db.parse(file);
		} catch(Exception e) {return;}
		
		try {
			
			patterns.clear();
			this.removeAll();			
			add(Box.createRigidArea(new Dimension(0,10)));
			add(add_pattern_button);						
			
			document.getDocumentElement().normalize();
			
			if(document.getDocumentElement().getNodeName()!="ActionManager")
				throw(new Exception("Root is not ActionManager!"));
			
			NodeList pattern_list = document.getElementsByTagName("Pattern");

			for (int i = 0; i < pattern_list.getLength(); i++) {
				Element pattern = (Element)pattern_list.item(i);
				String expr=pattern.getAttribute("Expression");
				ActionPattern pat=new ActionPattern( expr , this);
				addPattern(pat);
				
				NodeList activities_list=pattern.getElementsByTagName("Activity");
				for(int j=0; j<activities_list.getLength(); j++)
				{
					Element activity=(Element)activities_list.item(j);
					ActionActivityType type=actionStringToType(activity.getAttribute("Type"));
					String argument=activity.getChildNodes().item(0).getNodeValue();
					ActionActivity act=new ActionActivity(type, argument, pat);
					pat.addActivity(act);
				}								
			}
			
			this.revalidate();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"action_manager.xml parsing error:\n"+e);
		}
	}
	
	void save()
	{
		try{
			DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
				
			Element root=doc.createElement("ActionManager");
			doc.appendChild(root);
			
			Iterator<ActionPattern> i=patterns.iterator();
			while(i.hasNext())
			{
				ActionPattern pattern=i.next();
				Element node=doc.createElement("Pattern");
				node.setAttribute("Expression", pattern.getPattern());
				root.appendChild(node);								
				
				Iterator<ActionActivity> j=pattern.getActivities();
				while(j.hasNext())
				{
					ActionActivity act=j.next();
					Element el=doc.createElement("Activity");
					el.setAttribute("Type", actionTypeToString(act.getType()));
					
					Text args=doc.createTextNode(act.getArgument());
					el.appendChild(args);
					
					node.appendChild(el);
				}
			}
			
			TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.INDENT, "yes");
            
            Source source = new DOMSource(doc);
            Result result = new StreamResult(new File("action_manager.xml"));
            trans.transform(source, result);
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,""+e);
		}
	}
	
	String actionTypeToString(ActionActivityType type)
	{
		switch(type)
		{
		case DIALOG: return "DIALOG";
		case SYSTEM: return "SYSTEM";
		case MOUSE: return "MOUSE";
		case KEYBOARD: return "KEYBOARD";
		}
		return "UNKNOWN";
	}
	
	ActionActivityType actionStringToType(String str)
	{
		if(str.equals("DIALOG")) return ActionActivityType.DIALOG;
		if(str.equals("SYSTEM")) return ActionActivityType.SYSTEM;
		if(str.equals("MOUSE")) return ActionActivityType.MOUSE;
		if(str.equals("KEYBOARD")) return ActionActivityType.KEYBOARD;
		return ActionActivityType.DIALOG;
	}
	
	public void processRecognition(String recognition)
	{
		Iterator<ActionPattern> i=patterns.iterator();
		while(i.hasNext())
		{
			ActionPattern pat=i.next();
			if(pat.matches(recognition))
			{
				pat.executeAllActivities(pat.getGroups());
				break;
			}
		}
	}
	
	public boolean getDragging()
	{
		return dragging;
	}
	
	public void setDragging(boolean b)
	{
		dragging=b;
	}
	
	public void processDnD()
	{
		ActionPattern pat;
		int start=-1,end=-1,count=0;;
		Iterator<ActionPattern> it=patterns.iterator();
		while(it.hasNext())
		{
			pat=it.next();
			if(pat.isDnDStart()) start=count;
			if(pat.isDnDEnd()) end=count;
			pat.clearDnD();
			count++;
		}
		
		if(start>=0 && end>=0 && start!=end)
		{
			end++;//we wanna add after, not before
			if(end>start) end--; //if start is smaller than end, end is gonna reduce after removing start
			pat=patterns.get(start);
			patterns.remove(start);
			patterns.add(end,pat);			
		}
		
		this.removeAll();			
		add(Box.createRigidArea(new Dimension(0,10)));
		add(add_pattern_button);		
		
		it=patterns.iterator();
		while(it.hasNext())
		{
			pat=it.next();
			add(pat,getComponentCount()-2);
		}	
	}
}
