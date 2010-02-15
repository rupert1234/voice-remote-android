import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;


public class AdvancedGrammarEditor extends JPanel {
	
	private String adv_grammar_desc_text="Here you can view and modifiy the grammar DFA.";
	
	private JuliusGrammar grammar;
	private JuliusDictionary dictionary;
	private JPanel draw_area;
	
	AdvancedGrammarEditor()
	{
		grammar=new JuliusGrammar();
		dictionary=new JuliusDictionary();
		
		setLayout(new BorderLayout());
		
		JLabel grammars_description = new JLabel();
		grammars_description.setText(adv_grammar_desc_text);
		grammars_description.setPreferredSize(new Dimension(Integer.MAX_VALUE,50));
		grammars_description.setVerticalAlignment(JLabel.CENTER);
		grammars_description.setHorizontalAlignment(JLabel.CENTER);
		grammars_description.setBorder(BorderFactory.createEtchedBorder());
		add(grammars_description,BorderLayout.PAGE_START);
		
		draw_area=new JPanel();
		JScrollPane scroll_pane=new JScrollPane(draw_area);
		add(scroll_pane,BorderLayout.CENTER);
		
		JPanel buttons_bottom=new JPanel();
		buttons_bottom.setLayout(new BoxLayout(buttons_bottom, BoxLayout.X_AXIS));
		
		JButton load_button=new JButton("Load");
		load_button.addActionListener(new AbstractAction() {			
			public void actionPerformed(ActionEvent e) {
				grammar.load("julius_quickstart/grammar/main.dfa");
				dictionary.load("julius_quickstart/grammar/main.dict");
				drawGrammar(grammar,dictionary);
			}
		});
		buttons_bottom.add(load_button);
		
		JButton activate_button=new JButton("Activate");
		buttons_bottom.add(activate_button);
		
		JButton load_other_button=new JButton("Load other");
		buttons_bottom.add(load_other_button);
		
		JButton save_as_button=new JButton("Save as");
		buttons_bottom.add(save_as_button);
		
		add(buttons_bottom,BorderLayout.PAGE_END);
	}
	
	class EdgeTransformer implements Transformer<String, String>
	{

		JuliusDictionary dictionary; 
		
		public EdgeTransformer(JuliusDictionary dictionary)
		{
			this.dictionary=dictionary;
		}
		
		public String transform(String str) {
			int num=Integer.parseInt(str.substring(3));
			return dictionary.getWord(num);
		}
		
	}
	
	public void drawGrammar(JuliusGrammar grammar, JuliusDictionary dictionary)
	{
		DirectedGraph<Integer,String> g=new DirectedSparseMultigraph<Integer, String>();
		
		int i;
		Iterator<Integer> nit=grammar.getNodes();
		while(nit.hasNext())
		{
			i=nit.next();
			if(i<0) continue;
			g.addVertex(i);
		}
		
		JuliusGrammarArc a;
		Iterator<JuliusGrammarArc> ait=grammar.getArcs();
		i=1;
		while(ait.hasNext())
		{
			a=ait.next();
			if(a.start_node<0 || a.end_node<0) continue;
			g.addEdge(i+". "+a.word, a.start_node, a.end_node);
			i++;
		}
		
		Layout<Integer,String> layout=new FRLayout<Integer, String>(g);
		layout.setSize(new Dimension(600,300));
		
		BasicVisualizationServer<Integer, String> vv=new BasicVisualizationServer<Integer, String>(layout);
		vv.setPreferredSize(layout.getSize());
		EdgeTransformer edge_transformer = new EdgeTransformer(dictionary);
	
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Integer>());
		vv.getRenderContext().setEdgeLabelTransformer(edge_transformer);
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR); 		
		
		draw_area.removeAll();
		draw_area.add(vv);
		draw_area.setSize(layout.getSize());
		draw_area.revalidate();
		draw_area.repaint();
		
	}
}
