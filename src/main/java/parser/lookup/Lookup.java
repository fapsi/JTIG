/**
 * 
 */
package parser.lookup;

import java.util.LinkedList;
import java.util.List;

import grammar.buildJtigGrammar.Lexicon;
import grammar.buildJtigGrammar.RuleTree;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class Lookup {

	private String segment;
	
	private Lexicon lexicon;
	
	public Lookup(String segment,Lexicon lexicon){
		this.segment = segment;
		this.lexicon = lexicon;
	}
	
	private String[] tokenize(){
		//TODO: add tokenizer stuff
		return segment.split(" ");
	}
	
	public List<RuleTree> findlongestmatches(){
		
		List<RuleTree> results = new LinkedList<RuleTree>();
		List<String> searchwords = new LinkedList<String>();
		
		int p = 0;
		List<RuleTree> result;
		String[] tokens = tokenize();
		
		for (int i = 0; i < tokens.length; i++){
			searchwords.clear();
			searchwords.add(tokens[i]);
			p = i + 1;
			
			result = lexicon.find(searchwords,0);
			while ( (result == null || result.size()<=0) && p < tokens.length){
				
				searchwords.add(tokens[p]);
				p++;
				result = lexicon.find(searchwords,0);
			}

			if (result != null && result.size() > 0){
				results.addAll(result);
				i = p - 1;
			}
			
		}
		return results;
	}
}
