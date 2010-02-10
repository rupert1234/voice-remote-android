import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class LexiconSettings extends JPanel {
	
	private String lexicon_desc_text="Here you need to define the phonetic transcriptions of the words used in the grammar.";
	
	LexiconSettings()
	{
		setLayout(new BorderLayout());
		
		JLabel lexicon_description = new JLabel();
		lexicon_description.setText( lexicon_desc_text);
		lexicon_description.setPreferredSize(new Dimension(Integer.MAX_VALUE,50));
		lexicon_description.setVerticalAlignment(JLabel.CENTER);
		lexicon_description.setHorizontalAlignment(JLabel.CENTER);
		lexicon_description.setBorder(BorderFactory.createEtchedBorder());
		add(lexicon_description,BorderLayout.PAGE_START);
		
		
		
		JPanel buttons_bottom=new JPanel();
		buttons_bottom.setLayout(new BoxLayout(buttons_bottom, BoxLayout.X_AXIS));
		
		
		add(buttons_bottom,BorderLayout.PAGE_END);
	}
}
