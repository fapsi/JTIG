/**
 * 
 */
package parser.lookup;

import grammar.treeinsertion.Lexicon;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import tools.tokenizer.Token;
/**
 * 
 * @author Fabian Gallenkamp
 */
public class ActivatedLexicon {
	/**
	 * 
	 */
	private HashMap<String, List<ActivatedElementaryTree>> possibletrees;
	
	private Token[] tokens;
	
	private Lexicon lexicon;
	
	/**
	 * @param tokens 
	 * 
	 */
	public ActivatedLexicon (Lexicon lexicon,Token[] tokens){
		this.possibletrees = new HashMap<String, List<ActivatedElementaryTree>>();
		this.tokens = tokens;
		this.lexicon = lexicon;
	}
	
	/**
	 * 
	 */
	public void add(String rootlabel,ActivatedElementaryTree content){
		List<ActivatedElementaryTree> results = this.possibletrees.get(rootlabel);
		if (results == null){
			List<ActivatedElementaryTree> toput = new LinkedList<ActivatedElementaryTree>();
			toput.add(content);
			this.possibletrees.put(rootlabel, toput);
		} else {
			results.add(content);
		}
		
	}
	
	public List<ActivatedElementaryTree> get(String symbol){
		return possibletrees.get(symbol);
	}
	
	public Token[] getTokens(){
		return this.tokens;
	}
	
	public int getSize(){
		int i = 0;
		for ( List<ActivatedElementaryTree> list :possibletrees.values()){
			i += list.size();
		}
		return i;
	}
	
	public Set<String> getStartSymbols(){
		return lexicon.getStartSymbols();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Activated Lexicon:\n");
		for (Entry<String, List<ActivatedElementaryTree>> l : this.possibletrees.entrySet()){
			sb.append(l.getKey());
			sb.append(":");
			sb.append(l.getValue());
			sb.append("\n");
		}
		return sb.toString();
	}
	
	
}
