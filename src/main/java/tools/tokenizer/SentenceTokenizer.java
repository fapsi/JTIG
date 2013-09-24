/**
 * 
 */
package tools.tokenizer;

import java.util.List;

/**
 * 
 * @author Fabian Gallenkamp
 */
public abstract class SentenceTokenizer {
	
	/**
	 * 
	 */
	protected String sentence; 
	
	/**
	 * 
	 * @param sentence
	 */
	public SentenceTokenizer(String sentence){
		this.sentence = sentence;
	}
	
	/**
	 * 
	 * @return
	 */
	public abstract List<Token> getTokens(); 
	
}
