/**
 * 
 */
package tools.tokenizer;

import java.io.FileNotFoundException;


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
	public Token[] getTokens(String sentence); 
	
	/**
	 * 
	 * @param filename
	 * @param line
	 * @return
	 * @throws FileNotFoundException 
	 */
	public Token[] getTokens(String filename, int line) throws FileNotFoundException;

	public String getSentence(Token[] tokens);
	
}
