/**
 * 
 */
package parser.lookup;

import java.util.LinkedList;
import java.util.List;

import parser.early.JTIGParser;
import tools.tokenizer.Token;
import grammar.buildJtigGrammar.ElementaryTree;
import grammar.buildJtigGrammar.Lexicon;

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
		boolean all = "true".equals(JTIGParser.getProperty("parser.lookup.findalllongestmatches"));
		
		ActivatedLexicon slexicon = new ActivatedLexicon(this.tokens);
		List<Token> searchwords = new LinkedList<Token>();
		
		int p = 0;
		List<ElementaryTree> results;
		
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
				
				for (ElementaryTree result : results){
					slexicon.add(result.getRootSymbol(), new ActivatedElementaryTree(result, i, p));
				}
				if(!all)
					i = p - 1;
			}
			
		}
		return slexicon;
	}
}
