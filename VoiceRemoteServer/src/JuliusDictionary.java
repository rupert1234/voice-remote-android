import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;


public class JuliusDictionary {
	
	List<JuliusDictionaryWord> words;
	
	public JuliusDictionary()
	{
		words=new LinkedList<JuliusDictionaryWord>();
	}
	
	public void load(String file)
	{
		BufferedReader reader;
		try{
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		}catch(Exception  e)
		{
			JOptionPane.showMessageDialog(null, "Cannot load dictionary: "+e.getMessage());
			return;
		}
		
		words.clear();
		
		JuliusDictionaryWord word;
		int s,e;
		int ph_num;
		String ph;
		
		try{
			String ret=reader.readLine();
			int num=1;
			while(ret!=null)
			{
				word=new JuliusDictionaryWord();
				
				s=ret.indexOf('[');
				e=ret.indexOf(']');
				if(s<0 || e<0)
				{
					JOptionPane.showMessageDialog(null, "Error in line "+num+" of the dictionary: "+ret);
					ret=reader.readLine();
					continue;
				}
				word.category_id=Integer.parseInt(ret.substring(0, s-1));
				word.word=ret.substring(s+1,e);
				
				StringTokenizer strtok=new StringTokenizer(ret.substring(e+1));
				
				ph_num=strtok.countTokens();
				
				word.transcription=new String[ph_num];
				
				for(int i=0; i<ph_num; i++)
					word.transcription[i]=strtok.nextToken();
				
				words.add(word);

				ret=reader.readLine();
			}
			
			reader.close();
		}catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null, "Dictionary reading error: "+ex.getMessage());
			words.clear();
			return;
		}
	}

	String getWord(int num)
	{
		Iterator<JuliusDictionaryWord> i=words.iterator();
		while(i.hasNext())
		{
			JuliusDictionaryWord w=i.next();
			if(w.category_id==num)
				return w.word;
		}
		return null;
	}
	
}
