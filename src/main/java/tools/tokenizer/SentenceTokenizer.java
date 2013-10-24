/**
 * 
 */
package tools.tokenizer;


/**
 * 
 * @author Fabian Gallenkamp
 */
public interface SentenceTokenizer {
	
	/**
	 * 
	 * @param sentence
	 * @return
	 */
	public abstract Token[] getTokens(String sentence); 
	
}
