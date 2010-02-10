import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class AdvancedGrammarEditor extends JPanel {
	
	private String adv_grammar_desc_text="Here you can view and modifiy the grammar DFA.";
	
	AdvancedGrammarEditor()
	{
		setLayout(new BorderLayout());
		
		JLabel grammars_description = new JLabel();
		grammars_description.setText(adv_grammar_desc_text);
		grammars_description.setPreferredSize(new Dimension(Integer.MAX_VALUE,50));
		grammars_description.setVerticalAlignment(JLabel.CENTER);
		grammars_description.setHorizontalAlignment(JLabel.CENTER);
		grammars_description.setBorder(BorderFactory.createEtchedBorder());
		add(grammars_description,BorderLayout.PAGE_START);
		
		
		
		JPanel buttons_bottom=new JPanel();
		buttons_bottom.setLayout(new BoxLayout(buttons_bottom, BoxLayout.X_AXIS));
		
		JButton load_button=new JButton("Load");
		buttons_bottom.add(load_button);
		
		JButton activate_button=new JButton("Activate");
		buttons_bottom.add(activate_button);
		
		JButton load_other_button=new JButton("Load other");
		buttons_bottom.add(load_other_button);
		
		JButton save_as_button=new JButton("Save as");
		buttons_bottom.add(save_as_button);
		
		add(buttons_bottom,BorderLayout.PAGE_END);
	}
}
