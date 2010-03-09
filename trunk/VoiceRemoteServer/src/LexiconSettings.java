import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class LexiconSettings extends JPanel {
	
	private String lexicon_desc_text="<html>Here you need to define the phonetic transcriptions of the words used in the grammar. " +
			"They will be inserted automatically if they are present in the cache. Missing words are marked in red. If you make " +
			"a transcription error the field will turn yellow. After entering the transcription press check to see if the AM has all " +
			"the triphones.</html>";
	
	GrammarSettings grammar;
	JPanel words_panel;
	GrammarWizard wizard;
	
	LexiconSettings(GrammarSettings grammar, GrammarWizard wizard)
	{
		this.grammar=grammar;
		this.wizard=wizard;
		
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
		
		JButton save=new JButton("Save and activate grammar and lexicon");
		save.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent arg0) {
				if(checkAll())
				{
					save();
					Main.julius_starter.terminate();
					try{
						Thread.sleep(1000);
					}catch(Exception e){}
					Main.start_julius();
					displayGraph();
				}
			}
		});
		buttons_bottom.add(Box.createHorizontalGlue());
		buttons_bottom.add(save);
		buttons_bottom.add(Box.createHorizontalGlue());
		
		add(buttons_bottom,BorderLayout.PAGE_END);
	}
	
	public void displayGraph()
	{
		wizard.displayGraph();
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
		if(dictionary!=null) dictionary.save("julius/grammar/main.dict");
	}
	
	public boolean checkAll()
    {
            JuliusDictionary dictionary=grammar.getDictionary();
            
            if(dictionary==null) return false;
            
            BufferedReader reader=null;
            try{
                    reader=new BufferedReader(new InputStreamReader(new FileInputStream("julius/acoustic_model/tiedlist")));
            }catch(Exception e){
                    JOptionPane.showMessageDialog(null, "Cannot open tiedlist!");
                    return false;
            }
            
            Set<String> tied_set=new HashSet<String>();
            
            try{
                    String line=reader.readLine();
                    int num;
                    while(line!=null)
                    {
                            num=line.indexOf(' ');
                            if(num>0) line=line.substring(0,num);
                            tied_set.add(line);
                            line=reader.readLine();
                    }
                    
                    reader.close();
            }catch(Exception e){
                    JOptionPane.showMessageDialog(null,"Error reading tiedlist: "+e.getMessage());
                    return false;
            }
            
            List<String>errors=new LinkedList<String>(); 
            
            Iterator<JuliusDictionaryWord> it=dictionary.getWords();
            while(it.hasNext())
            {
                    JuliusDictionaryWord word=it.next();
                    String trans[] = word.getTranscription();
                    if(trans==null)
                    {
                    	errors.add(word.getWord()+" transcription errors");
                    	continue;
                    }
                    
                    if(trans.length==1)
                    {
                            if(!tied_set.contains(trans[0]))
                                    errors.add(word.getWord()+" missing phoneme "+trans[0]);
                    }
                    
                    String left=null,middle=null,right=null;
                    String triphone;
                    for(int i=0; i<trans.length; i++)
                    {
                            left=middle;
                            middle=trans[i];
                            if(i<trans.length-1) right=trans[i+1];
                            else right=null;
                            triphone="";
                            if(left!=null) triphone=left+"-";
                            triphone+=middle;
                            if(right!=null) triphone+="+"+right;
                            if(!tied_set.contains(triphone))
                                    errors.add(word.getWord()+" missing triphone "+triphone);
                    }
            }
            
            if(errors.size()>0)                    
            {
                    String msg="Cannot save due to following errors:\n";
                    Iterator<String> sit=errors.iterator();
                    while(sit.hasNext())
                    {
                            msg+=sit.next()+"\n";
                    }
                    JOptionPane.showMessageDialog(null,msg);
                    return false;
            }
            
            return true;
    }
}
