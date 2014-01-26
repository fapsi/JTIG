/**
 * 
 */
package parser.lookup;

import java.util.LinkedList;
import java.util.List;

import parser.early.JTIGParser;
import tools.tokenizer.Token;
import grammar.treeinsertion.ElementaryTree;
import grammar.treeinsertion.Lexicon;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class Lookup {
	
	public Lookup(){
	}
	
	public ActivatedLexicon findlongestmatches(Token[] tokens,Lexicon lexicon){
		@SuppressWarnings("unused") // TODO:
		boolean all = JTIGParser.getBooleanProperty("parser.lookup.exhaustive");
		
		ActivatedLexicon slexicon = new ActivatedLexicon(lexicon,tokens);
		List<Token> searchwords = new LinkedList<Token>();
		List<ElementaryTree> results;
		
		for (int i = 0; i < tokens.length; i++){
			searchwords.clear();
			
			// TODO: add non exhaustive distinction
			for (int p = i; p < tokens.length; p++){
				searchwords.add(tokens[p]);
				
				results = lexicon.find(searchwords,0);
				
				if (results != null && results.size() > 0){
					
					for (ElementaryTree result : results){
						slexicon.add(result.getRootSymbol(), new ActivatedElementaryTree(result, i, p+1));
					}
				}
			}			
		}
		return slexicon;
	}
}
