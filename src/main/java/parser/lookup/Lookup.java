/**
 * 
 */
package parser.lookup;

import java.util.LinkedList;
import java.util.List;
import parser.early.JTIGParser;
import tools.tokenizer.Token;
import grammar.buildJtigGrammar.Lexicon;
import grammar.buildJtigGrammar.TIGRule;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class Lookup {

	private Token[] tokens;
	
	private Lexicon lexicon;
	
	public Lookup(Token[] tokens,Lexicon lexicon){
		this.tokens = tokens;
		this.lexicon = lexicon;
	}
	
	public ActivatedLexicon findlongestmatches(){
		boolean all = JTIGParser.getProperties().getProperty("parser.lookup.findalllongestmatches").equals("true");
		
		ActivatedLexicon slexicon = new ActivatedLexicon(this.tokens);
		List<Token> searchwords = new LinkedList<Token>();
		
		int p = 0;
		List<TIGRule> results;
		
		for (int i = 0; i < tokens.length; i++){
			searchwords.clear();
			searchwords.add(tokens[i]);
			p = i + 1;
			
			results = this.lexicon.find(searchwords,0);
			while ( (results == null || results.size()<=0) && p < tokens.length){
				
				searchwords.add(tokens[p]);
				p++;
				results = this.lexicon.find(searchwords,0);
			}

			if (results != null && results.size() > 0){
				
				for (TIGRule result : results){
					slexicon.add(result.getRootSymbol(), new ActivatedRuleTree(result, i, p));
				}
				if(!all)
					i = p - 1;
			}
			
		}
		return slexicon;
	}
}