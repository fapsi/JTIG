/**
 * 
 */
package parser.lookup;

import java.util.LinkedList;
import java.util.List;

import tools.tokenizer.Token;
import grammar.buildJtigGrammar.Lexicon;
import grammar.buildJtigGrammar.TIGRule;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class Lookup {

	private List<Token> tokens;
	
	private Lexicon lexicon;
	
	public Lookup(List<Token> tokens,Lexicon lexicon){
		this.tokens = tokens;
		this.lexicon = lexicon;
	}
	
	public ActivatedLexicon findlongestmatches(){
		
		ActivatedLexicon slexicon = new ActivatedLexicon();
		List<Token> searchwords = new LinkedList<Token>();
		
		int p = 0;
		List<TIGRule> results;
		
		for (int i = 0; i < tokens.size(); i++){
			searchwords.clear();
			searchwords.add(tokens.get(i));
			p = i + 1;
			
			results = this.lexicon.find(searchwords,0);
			while ( (results == null || results.size()<=0) && p < tokens.size()){
				
				searchwords.add(tokens.get(p));
				p++;
				results = this.lexicon.find(searchwords,0);
			}

			if (results != null && results.size() > 0){
				
				for (TIGRule result : results){
					slexicon.add(result.getRootSymbol(), new ActivatedRuleTree(result, i, p));
				}
				i = p - 1;
			}
			
		}
		return slexicon;
	}
}
