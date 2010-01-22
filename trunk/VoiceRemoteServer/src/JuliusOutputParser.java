
public class JuliusOutputParser {
	
	String recognition_output;
	
	public JuliusOutputParser()
	{
		recognition_output="";
	}
	
	public Boolean addLine(String line)
	{
		if(line.equals("</RECOGOUT>"))
		{
			return true;
		}
		
		line=line.trim();
		if(line.startsWith("<WHYPO"))
		{
			int pos=line.indexOf('"', 13);
			String word=line.substring(13,pos);
			recognition_output+=word+" ";
		}
		
		return false;
	}
	
	public String getRecognitionOutput()
	{
		String ret=recognition_output;
		recognition_output="";
		return ret;
	}

}
