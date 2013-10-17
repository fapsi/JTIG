/**
 * 
 */
package parser.lookup;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import tools.tokenizer.Token;
/**
 * 
 * @author Fabian Gallenkamp
 */
public class ActivatedLexicon {
	/**
	 * 
	 */
	private HashMap<String, List<ActivatedTIGRule>> possibletrees;
	
	private Token[] tokens;
	
	/**
	 * @param tokens 
	 * 
	 */
	public ActivatedLexicon (Token[] tokens){
		this.possibletrees = new HashMap<String, List<ActivatedTIGRule>>();
		this.tokens = tokens;
	}
	
	/**
	 * 
	 */
	public void add(String rootlabel,ActivatedTIGRule content){
		List<ActivatedTIGRule> results = this.possibletrees.get(rootlabel);
		if (results == null){
			List<ActivatedTIGRule> toput = new LinkedList<ActivatedTIGRule>();
			toput.add(content);
			this.possibletrees.put(rootlabel, toput);
		} else {
			results.add(content);
		}
		
	}
	
	public List<ActivatedTIGRule> get(String symbol){
		return possibletrees.get(symbol);
	}
	
	public Token[] getTokens(){
		return this.tokens;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Activated Lexicon:\n");
		for (Entry<String, List<ActivatedTIGRule>> l : this.possibletrees.entrySet()){
			sb.append(l.getKey());
			sb.append(":");
			sb.append(l.getValue());
			sb.append("\n");
		}
		return sb.toString();
	}
	
	
}
