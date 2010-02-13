import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javax.swing.JOptionPane;


public class JuliusGrammar {

	List<JuliusGrammarArc> arcs;
	Set<Integer> nodes;
	
	JuliusGrammar()
	{
		arcs=new LinkedList<JuliusGrammarArc>();
		nodes=new TreeSet<Integer>();
	}
	
	public void load(String file)
	{
		BufferedReader reader;
		try{
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		}catch(Exception  e)
		{
			JOptionPane.showMessageDialog(null, "Cannot load grammar: "+e.getMessage());
			return;
		}
		
		arcs.clear();
		nodes.clear();
		
		int start,end,word,status;
		boolean accept;
		
		try{
			String ret=reader.readLine();
			int num=1;
			while(ret!=null)
			{
				StringTokenizer strtok=new StringTokenizer(ret);
				if(strtok.countTokens()<4)
				{
					JOptionPane.showMessageDialog(null, "Error in line "+num+" of the grammar: "+ret);	
				}
				else
				{
					end=Integer.parseInt(strtok.nextToken());					
					word=Integer.parseInt(strtok.nextToken());
					start=Integer.parseInt(strtok.nextToken());
					status=Integer.parseInt(strtok.nextToken());
					if((status&1)!=0) accept=true;
					else accept=false;
					arcs.add(new JuliusGrammarArc(start, end, word, accept));
					nodes.add(start);
					nodes.add(end);
				}
				ret=reader.readLine();
			}
			
			reader.close();
		}catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Grammar reading error: "+e.getMessage());
			arcs.clear();
			nodes.clear();
			return;
		}
	}
	
	public Iterator<Integer> getNodes()
	{
		return nodes.iterator();
	}
	
	public Iterator<JuliusGrammarArc> getArcs()
	{
		return arcs.iterator();
	}
}
