/**
 * 
 */
package tools.tokenizer;
/**
 * 
 * @author Fabian Gallenkamp
 */
public class Token {

	private int pos;
	
	private String label;
	/**
	 * Constructs a token
	 * @param pos
	 * @param label
	 */
	public Token(int pos,String label){
		this.pos = pos;
		this.label = label;
	}
	
	/**
	 * @return the pos
	 */
	public int getPos() {
		return pos;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return "Token [pos=" + pos + ", label=" + label + "]";
	}
}
