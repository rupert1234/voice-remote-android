import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
	
	public void save(String file)
	{
		BufferedWriter writer=null;
		try
		{
			writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, "Cannot save grammar: "+e.getMessage());
			return;
		}
		
		Iterator<JuliusGrammarArc> it=arcs.iterator();
		
		while(it.hasNext())
		{
			JuliusGrammarArc arc=it.next();
			
			try{
				writer.write(arc.end_node+" "+arc.word+" "+arc.start_node+" ");
				if(arc.isAccepting) writer.write("1\n");
				else writer.write("0\n");
			}catch(Exception e){
				JOptionPane.showMessageDialog(null, "Cannot write grammar: "+e.getMessage());
				return;
			}
		}
		
		try{
			writer.close();
		}catch(Exception e){}
	}
	
	public void addArc(int from, int to, int word_cat)
	{
		nodes.add(from);
		nodes.add(to);
		JuliusGrammarArc arc=new JuliusGrammarArc(from, to, word_cat, false);
		arcs.add(arc);
	}
	
	public void addArc(int from, int to, int word_cat, boolean accepting)
	{
		nodes.add(from);
		nodes.add(to);
		JuliusGrammarArc arc=new JuliusGrammarArc(from, to, word_cat, accepting);
		arcs.add(arc);
	}
	
	public int find(int from, int word_cat)
	{
		Iterator<JuliusGrammarArc> it=arcs.iterator();
		while(it.hasNext())
		{
			JuliusGrammarArc arc=it.next();
			if(arc.start_node==from && arc.word==word_cat)
				return arc.end_node;
		}
		return -1;
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
