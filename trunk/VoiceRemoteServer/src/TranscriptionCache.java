import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JOptionPane;

import org.apache.commons.collections15.map.HashedMap;


public class TranscriptionCache {
	private Map<String, String> cache;
	
	public TranscriptionCache()
	{
		cache=new HashedMap<String, String>();
	}
	
	public String getTranscription(String word)
	{
		return cache.get(word);
	}
	
	public void setTranscription(String word, String transcription)
	{
		cache.put(word, transcription);
	}
	
	public void setTranscription(String word, String [] transcription)
	{
		String trans="";
		for(int i=0; i<transcription.length; i++)
			trans+=transcription[i]+" ";
		setTranscription(word, trans);
	}
	
	public void save()
	{
		try{
			BufferedWriter writer=new BufferedWriter(new FileWriter(new File("transcription_cache.csv")));
			
						
			Iterator<Map.Entry<String, String>> it=cache.entrySet().iterator();
			while(it.hasNext())
			{
				Map.Entry<String, String> pair=it.next();				
				writer.write(pair.getKey()+" "+pair.getValue()+"\n");				
			}
			
			writer.close();
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,""+e);
		}
	}
	
	public void load()
	{
		
		try{
			BufferedReader reader=new BufferedReader(new FileReader(new File("transcription_cache.csv")));
			
			cache.clear();
			
			String line,word,trans;
			int pos;
			while((line=reader.readLine())!=null)
			{
				pos=line.indexOf(' ');
				word=line.substring(0,pos);
				trans=line.substring(pos+1);
				setTranscription(word, trans);
			}
			
			reader.close();
			
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null,""+e);
		}
	}
}
