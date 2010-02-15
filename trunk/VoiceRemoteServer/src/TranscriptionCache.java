import java.io.File;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.collections15.map.HashedMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;


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
			DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
				
			Element root=doc.createElement("Transcriptions");
			doc.appendChild(root);
						
			Iterator<Map.Entry<String, String>> it=cache.entrySet().iterator();
			while(it.hasNext())
			{
				Map.Entry<String, String> pair=it.next();
				
				Element el=doc.createElement("Word");
				el.setAttribute("Text", pair.getKey());

				Text args=doc.createTextNode(pair.getValue());
				el.appendChild(args);
				
				root.appendChild(el);
			}
			
			TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.INDENT, "yes");
            
            Source source = new DOMSource(doc);
            Result result = new StreamResult(new File("transcription_cache.xml"));
            trans.transform(source, result);
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,""+e);
		}
	}
	
	public void load()
	{
		File file;
		DocumentBuilderFactory dbf;
		DocumentBuilder db;
		Document document;
		try {
			file = new File("transcription_cache.xml");
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			document = db.parse(file);
		} catch(Exception e) {return;}
		
		try {		
			
			document.getDocumentElement().normalize();
			
			if(document.getDocumentElement().getNodeName()!="Transcriptions")
				throw(new Exception("Root is not Transriptions!"));
			
			NodeList item_list = document.getElementsByTagName("Word");

			for (int i = 0; i < item_list.getLength(); i++) {
				Element word = (Element)item_list.item(i);		
				String text=word.getAttribute("Text");
				String trans=word.getChildNodes().item(0).getNodeValue();
				cache.put(text,trans);
			}			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"grammar_settings.xml parsing error:\n"+e);
		}
	}
}
