
public class JuliusGrammarArc {
	
	public int start_node;
	public int end_node;
	public int word;
	public boolean isAccepting;
	
	public JuliusGrammarArc(int startNode, int endNode, int word,
			boolean isAccepting) {
		super();
		start_node = startNode;
		end_node = endNode;
		this.word = word;
		this.isAccepting = isAccepting;
	}
	
	
}
