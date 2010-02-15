import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
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


public class GrammarSettings extends JPanel {

	private String gramamr_desc_text="This tab allows to change the words that the system will recognize.";
	
	private JPanel grammar_list;
	private JButton add_line;
	private JuliusGrammar grammar;
	private JuliusDictionary dictionary;
	
	GrammarSettings()
	{
		setLayout(new BorderLayout());
		
		JLabel grammars_description = new JLabel();
		grammars_description.setText(gramamr_desc_text);
		grammars_description.setPreferredSize(new Dimension(Integer.MAX_VALUE,50));
		grammars_description.setVerticalAlignment(JLabel.CENTER);
		grammars_description.setHorizontalAlignment(JLabel.CENTER);
		grammars_description.setBorder(BorderFactory.createEtchedBorder());
		add(grammars_description,BorderLayout.PAGE_START);
		
		grammar_list=new JPanel();
		grammar_list.setLayout(new BoxLayout(grammar_list,BoxLayout.Y_AXIS));
		
		
		add_line=new JButton("Add");
		add_line.setAlignmentX(Component.LEFT_ALIGNMENT);
		grammar_list.add(add_line);
		
		add_line.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				JTextField grammar_line=new JTextField();
				grammar_line.setMaximumSize(new Dimension(Integer.MAX_VALUE,30));
				grammar_line.setAlignmentX(Component.LEFT_ALIGNMENT);
				grammar_list.add(grammar_line,grammar_list.getComponentCount()-1);
				grammar_list.revalidate();
			}
		});
		
		JScrollPane scroll_pane=new JScrollPane(grammar_list);
		
		add(scroll_pane,BorderLayout.CENTER);
		
		JPanel buttons_bottom=new JPanel();
		buttons_bottom.setLayout(new BoxLayout(buttons_bottom, BoxLayout.X_AXIS));
		
		JButton button_save=new JButton("Save");
		button_save.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				clearEmptyLines();
				save();
			}
		});
		
		JButton button_compile=new JButton("Process and Activate");
		button_compile.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				clearEmptyLines();
				process();
			}
		});
		
		buttons_bottom.add(button_save);
		buttons_bottom.add(Box.createHorizontalGlue());
		buttons_bottom.add(button_compile);
		add(buttons_bottom,BorderLayout.PAGE_END);
		
		load();
	}
	
	public void clearEmptyLines()
	{
		Stack<JTextField> to_delete=new Stack<JTextField>();
		int num=grammar_list.getComponentCount()-1;
		for(int i=0; i<num; i++)
		{
			JTextField tf=(JTextField)grammar_list.getComponent(i);
			if(tf.getText().trim().length()==0)
				to_delete.push(tf);
		}
		
		while(!to_delete.empty())
		{
			grammar_list.remove(to_delete.pop());
		}
		
		grammar_list.revalidate();
		grammar_list.repaint();
	}
	
	public void save()
	{
		try{
			DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
				
			Element root=doc.createElement("GrammarList");
			doc.appendChild(root);
						
			int num=grammar_list.getComponentCount()-1;
			for(int i=0; i<num; i++)
			{
				JTextField tf=(JTextField)grammar_list.getComponent(i);
				
				Element el=doc.createElement("Item");

				Text args=doc.createTextNode(tf.getText());
				el.appendChild(args);
				
				root.appendChild(el);
			}
			
			TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.INDENT, "yes");
            
            Source source = new DOMSource(doc);
            Result result = new StreamResult(new File("grammar_settings.xml"));
            trans.transform(source, result);
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,""+e);
		}
	}
	
	public void load()
	{
		File file;
		DocumentBuilderFactory dbf;
		DocumentBuilder db;
		Document document;
		try {
			file = new File("grammar_settings.xml");
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			document = db.parse(file);
		} catch(Exception e) {return;}
		
		try {
			
			grammar_list.removeAll();			
			grammar_list.add(add_line);						
			
			document.getDocumentElement().normalize();
			
			if(document.getDocumentElement().getNodeName()!="GrammarList")
				throw(new Exception("Root is not GrammarList!"));
			
			NodeList item_list = document.getElementsByTagName("Item");

			for (int i = 0; i < item_list.getLength(); i++) {
				Element line = (Element)item_list.item(i);				
				String line_text=line.getChildNodes().item(0).getNodeValue();
				JTextField grammar_line=new JTextField(line_text);
				grammar_line.setMaximumSize(new Dimension(Integer.MAX_VALUE,30));
				grammar_line.setAlignmentX(Component.LEFT_ALIGNMENT);
				grammar_list.add(grammar_line,grammar_list.getComponentCount()-1);				
			}
			
			grammar_list.revalidate();
			grammar_list.repaint();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"grammar_settings.xml parsing error:\n"+e);
		}
	}
	
	public void process()
	{
		List<String> sent_list=new LinkedList<String>();
		int num=grammar_list.getComponentCount()-1;
		for(int i=0; i<num; i++)
		{
			JTextField tf=(JTextField)grammar_list.getComponent(i);
			sent_list.add(tf.getText());
		}
		
		if(sent_list.size()==0)
		{
			JOptionPane.showMessageDialog(null,"Won't create empty grammar!");
			return;
		}
		
		grammar=new JuliusGrammar();
		dictionary=new JuliusDictionary();
		String w;
		int id,state,next,state_num=4;
		num=2;
		
		dictionary.addWord("<s>", 0);
		dictionary.addWord("</s>", 1);
		grammar.addArc(1, 0, 1);
		grammar.addArc(2, 3, 0);
		grammar.addArc(-1, 2, -1, true);
		
		Iterator<String> i=sent_list.iterator();
		while(i.hasNext())
		{
			StringTokenizer strtok= new StringTokenizer(i.next());
			state=3;
			while(strtok.hasMoreElements())
			{
				w=strtok.nextToken();
				id=dictionary.getWord(w);
				if(id<0)
				{
					id=num;
					dictionary.addWord(w, id);
					num++;
				}
				
				next=grammar.find(state, id);
				if(next<0)
				{
					if(strtok.hasMoreElements())
					{
						next=state_num;
						state_num++;
					}
					else next=1;
					grammar.addArc(state, next, id);
				}
				
				state=next;
			}
			
		}
		
		grammar.save("julius_quickstart/grammar/main.dfa");
	}
	
	public JuliusDictionary getDictionary()
	{
		return dictionary;
	}
	
}
