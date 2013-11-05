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
	
	public Lookup(){
	}
	
	public ActivatedLexicon findlongestmatches(Token[] tokens,Lexicon lexicon){
		boolean all = "true".equals(JTIGParser.getProperty("parser.lookup.findalllongestmatches"));
		
		ActivatedLexicon slexicon = new ActivatedLexicon(lexicon,tokens);
		List<Token> searchwords = new LinkedList<Token>();
		
		int p = 0;
		List<ElementaryTree> results;
		
		for (int i = 0; i < tokens.length; i++){
			searchwords.clear();
			searchwords.add(tokens[i]);
			p = i + 1;
			
			results = lexicon.find(searchwords,0);
			while ( (results == null || results.size()<=0) && p < tokens.length){
				
				searchwords.add(tokens[p]);
				p++;
				results = lexicon.find(searchwords,0);
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
