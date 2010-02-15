import java.awt.Color;
import java.awt.Dimension;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class JuliusDictionaryWord extends JPanel implements DocumentListener{
	
	private int category_id;
	private String word;
	private String [] transcription;
	
	public JuliusDictionaryWord()
	{
		this("",-1);
	}
	
	private JTextField fid,fword,ftrans;
	Set<String> ph_set;
	
	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int categoryId) {
		category_id = categoryId;
		fid.setText(""+category_id);
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
		fword.setText(word);
	}

	public String[] getTranscription() {
		return transcription;
	}

	public void setTranscription(String trans_str) {
		String str,str2="";
		StringTokenizer strtok=new StringTokenizer(trans_str);		
		int num=strtok.countTokens();		
		transcription=new String[num];		
		for(int i=0; i<num; i++)
		{
			str=strtok.nextToken();
			transcription[i]=str;
			str2+=str+" ";
		}
		ftrans.setText(str2);
	}

	public JuliusDictionaryWord(String w, int id)
	{
		word=w;
		category_id=id;
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		fid=new JTextField(""+id);
		fword=new JTextField(w);
		ftrans=new JTextField();
		
		String trans=Main.transcription_cache.getTranscription(w);
		if(trans!=null)
			setTranscription(trans);
		
		fid.setMaximumSize(new Dimension(50,30));
		fid.setPreferredSize(new Dimension(50,30));
		fid.setMinimumSize(new Dimension(50,30));
		
		fword.setMaximumSize(new Dimension(100,30));
		fword.setPreferredSize(new Dimension(100,30));
		fword.setMinimumSize(new Dimension(100,30));
		
		ftrans.setPreferredSize(new Dimension(10,30));
		ftrans.setMinimumSize(new Dimension(10,30));
		ftrans.setMaximumSize(new Dimension(Integer.MAX_VALUE,30));
		
		fid.getDocument().addDocumentListener(this);
		fword.getDocument().addDocumentListener(this);
		ftrans.getDocument().addDocumentListener(this);
		
		add(fid);
		add(fword);
		add(ftrans);
		
		ph_set=new HashSet<String>();
		
		ph_set.add("sil");
		ph_set.add("sp");
		ph_set.add("ae");
		ph_set.add("ah");
		ph_set.add("ao");
		ph_set.add("aw");
		ph_set.add("ax");
		ph_set.add("ay");
		ph_set.add("b");
		ph_set.add("ch");
		ph_set.add("d");
		ph_set.add("dh");
		ph_set.add("dx");
		ph_set.add("eh");
		ph_set.add("er");
		ph_set.add("ey");
		ph_set.add("f");
		ph_set.add("g");
		ph_set.add("hh");
		ph_set.add("ih");
		ph_set.add("iy");
		ph_set.add("ix");
		ph_set.add("jh");
		ph_set.add("k");
		ph_set.add("l");
		ph_set.add("m");
		ph_set.add("n");
		ph_set.add("ng");
		ph_set.add("ow");
		ph_set.add("oy");
		ph_set.add("p");
		ph_set.add("r");
		ph_set.add("s");
		ph_set.add("sh");
		ph_set.add("t");
		ph_set.add("th");
		ph_set.add("uh");
		ph_set.add("uw");
		ph_set.add("v");
		ph_set.add("w");
		ph_set.add("y");
		ph_set.add("z");
		ph_set.add("zh");
	}

	public void refresh()
	{
		fid.setText(""+category_id);
		fword.setText(word);
		if(transcription!=null)
		{
			String str="";
			for(int i=0; i<transcription.length; i++)
				str+=transcription[i]+" ";
			ftrans.setText(str);
		} else ftrans.setBackground(Color.red);
	}

	public void changedUpdate(DocumentEvent e) {
	}

	public void insertUpdate(DocumentEvent e) {
		updateData();		
	}

	public void removeUpdate(DocumentEvent e) {
		updateData();
	}
	
	public void updateData()
	{
		try{
			category_id=Integer.parseInt(fid.getText());
		}catch(Exception ex)
		{
			fid.setBackground(Color.red);
			return;
		}
		fid.setBackground(Color.white);
		
		word=fword.getText();
		
		if(ftrans.getText().length()==0) return;
		
		if(!checkTrans(ftrans.getText()))
		{
			ftrans.setBackground(Color.yellow);
			transcription=null;
			return;
		}
		
		ftrans.setBackground(Color.white);
		
		StringTokenizer strtok=new StringTokenizer(ftrans.getText());
		int num=strtok.countTokens();		
		transcription=new String[num];		
		for(int i=0; i<num; i++)
			transcription[i]=strtok.nextToken();
	}
	
	public boolean checkTrans(String trans)
	{
		StringTokenizer strtok=new StringTokenizer(trans);
		while(strtok.hasMoreElements())
		{
			if(!ph_set.contains(strtok.nextToken()))
				return false;
		}
		return true;
	}
}
