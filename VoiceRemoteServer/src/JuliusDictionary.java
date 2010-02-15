import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;


public class JuliusDictionary {
	
	private List<JuliusDictionaryWord> words;
	
	public JuliusDictionary()
	{
		words=new LinkedList<JuliusDictionaryWord>();
			
	}
	
	public void load(String file)
	{
		BufferedReader reader=null;
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
				
				word.setCategory_id(Integer.parseInt(ret.substring(0, s-1)));
				word.setWord(ret.substring(s+1,e));
				word.setTranscription(ret.substring(e+1));								
				
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
	
	public void save(String file)
	{
		BufferedWriter writer=null;
		try
		{
			writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, "Cannot save dictionary: "+e.getMessage());
			return;
		}
		
		Iterator<JuliusDictionaryWord> it=words.iterator();
		while(it.hasNext())
		{
			JuliusDictionaryWord w=it.next();
			
			try{
				writer.write(w.getCategory_id()+"\t["+w.getWord()+"]\t");
				String transcription[] = w.getTranscription();
				if(transcription!=null)
				{
					Main.transcription_cache.setTranscription(w.getWord(), transcription);
					for(int i=0; i<transcription.length-1; i++)
					{
						writer.write(transcription[i]+" ");
					}
					if(transcription.length>0) 
						writer.write(transcription[transcription.length-1]);
				}
				writer.write("\n");
				
			}catch(Exception e){
				JOptionPane.showMessageDialog(null, "Cannot write dictionary: "+e.getMessage());
				return;
			}			
		}
		
		try{
			writer.close();
		}catch(Exception e){}
		Main.transcription_cache.save();
	}

	public String getWord(int num)
	{
		Iterator<JuliusDictionaryWord> i=words.iterator();
		while(i.hasNext())
		{
			JuliusDictionaryWord w=i.next();
			if(w.getCategory_id()==num)
				return w.getWord();
		}
		return null;
	}
	
	public int getWord(String word)
	{
		Iterator<JuliusDictionaryWord> i=words.iterator();
		while(i.hasNext())
		{
			JuliusDictionaryWord w=i.next();
			if(w.getWord().equals(word))
				return w.getCategory_id();
		}
		return -1;
	}
	
	public void addWord(String word, int id)
	{
		JuliusDictionaryWord w=new JuliusDictionaryWord(word,id);
		words.add(w);
	}
	
	public Iterator<JuliusDictionaryWord> getWords()
	{
		return words.iterator();
	}
}
