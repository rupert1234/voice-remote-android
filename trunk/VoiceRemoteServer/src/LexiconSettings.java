import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class LexiconSettings extends JPanel {
	
	private String lexicon_desc_text="Here you need to define the phonetic transcriptions of the words used in the grammar.";
	
	GrammarSettings grammar;
	JPanel words_panel;
	
	LexiconSettings(GrammarSettings grammar)
	{
		this.grammar=grammar;
		
		setLayout(new BorderLayout());
		
		JLabel lexicon_description = new JLabel();
		lexicon_description.setText(lexicon_desc_text);
		lexicon_description.setPreferredSize(new Dimension(Integer.MAX_VALUE,50));
		lexicon_description.setVerticalAlignment(JLabel.CENTER);
		lexicon_description.setHorizontalAlignment(JLabel.CENTER);
		lexicon_description.setBorder(BorderFactory.createEtchedBorder());
		add(lexicon_description,BorderLayout.PAGE_START);
		
		words_panel=new JPanel();
		words_panel.setLayout(new BoxLayout(words_panel, BoxLayout.Y_AXIS));
		JScrollPane scroll=new JScrollPane(words_panel);
		add(scroll,BorderLayout.CENTER);
		
		
		JPanel buttons_bottom=new JPanel();
		buttons_bottom.setLayout(new BoxLayout(buttons_bottom, BoxLayout.X_AXIS));
		
		JButton refresh=new JButton("Refresh");
		refresh.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent arg0) {
				displayWords();
			}
		});
		buttons_bottom.add(refresh);
		
		JButton save=new JButton("Save");
		save.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent arg0) {
				save();
			}
		});
		buttons_bottom.add(Box.createHorizontalGlue());
		buttons_bottom.add(save);
		
		add(buttons_bottom,BorderLayout.PAGE_END);
	}
	
	public void displayWords()
	{
		words_panel.removeAll();
		
		JuliusDictionary dictionary=grammar.getDictionary();
		
		if(dictionary==null) return;
		
		Iterator<JuliusDictionaryWord> it=dictionary.getWords();
		while(it.hasNext())
		{
			JuliusDictionaryWord word=it.next();
			words_panel.add(word);	
			word.refresh();
		}
		
		words_panel.revalidate();
		words_panel.repaint();
	}
	
	public void save()
	{
		JuliusDictionary dictionary=grammar.getDictionary();
		if(dictionary!=null) dictionary.save("julius_quickstart/grammar/main.dict");
	}
}
