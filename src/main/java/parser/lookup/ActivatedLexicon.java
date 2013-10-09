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
	private HashMap<String, List<ActivatedRuleTree>> possibletrees;
	
	private Token[] tokens;
	
	/**
	 * @param tokens 
	 * 
	 */
	public ActivatedLexicon (Token[] tokens){
		this.possibletrees = new HashMap<String, List<ActivatedRuleTree>>();
		this.tokens = tokens;
	}
	
	/**
	 * 
	 */
	public void add(String rootlabel,ActivatedRuleTree content){
		List<ActivatedRuleTree> results = this.possibletrees.get(rootlabel);
		if (results == null){
			List<ActivatedRuleTree> toput = new LinkedList<ActivatedRuleTree>();
			toput.add(content);
			this.possibletrees.put(rootlabel, toput);
		} else {
			results.add(content);
		}
		
	}
	
	public List<ActivatedRuleTree> get(String symbol){
		return possibletrees.get(symbol);
	}
	
	public Token[] getTokens(){
		return this.tokens;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Activated Lexicon:\n");
		for (Entry<String, List<ActivatedRuleTree>> l : this.possibletrees.entrySet()){
			sb.append(l.getKey());
			sb.append(":");
			sb.append(l.getValue());
			sb.append("\n");
		}
		return sb.toString();
	}
	
	
}
