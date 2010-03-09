import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;


public class GrammarWizard extends JPanel {
	
	private GrammarSettings utterance_list;
	private AdvancedGrammarEditor advanced_grammars_panel;
	private LexiconSettings lexicon_panel;
	private JTabbedPane tabbedPane;
	
	public GrammarWizard ()
	{
		setLayout(new GridLayout());
		
		utterance_list = new GrammarSettings(this);
		
		advanced_grammars_panel = new AdvancedGrammarEditor();
		
		lexicon_panel = new LexiconSettings(utterance_list,this);
		
		tabbedPane = new JTabbedPane();
		tabbedPane.add("Utterance list",utterance_list);
		tabbedPane.add("Word lexicon",lexicon_panel);
		tabbedPane.add("Graph",advanced_grammars_panel);
		
		add(tabbedPane);
		
	}

	public void displayLexicon()
	{
		tabbedPane.setSelectedIndex(1);
		lexicon_panel.displayWords();
	}
	
	public void displayGraph()
	{
		tabbedPane.setSelectedIndex(2);
		advanced_grammars_panel.refresh();
	}
}
