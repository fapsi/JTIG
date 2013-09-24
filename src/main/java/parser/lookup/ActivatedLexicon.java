/**
 * 
 */
package parser.lookup;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
/**
 * 
 * @author Fabian Gallenkamp
 */
public class ActivatedLexicon {
	/**
	 * 
	 */
	HashMap<String, List<ActivatedRuleTree>> possibletrees;
	
	/**
	 * 
	 */
	public ActivatedLexicon (){
		this.possibletrees = new HashMap<String, List<ActivatedRuleTree>>();
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
