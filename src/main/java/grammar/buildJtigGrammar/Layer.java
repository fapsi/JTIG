/**
 * 
 */
package grammar.buildJtigGrammar;

import java.util.Arrays;

/**
 * Stores one rule of a context free grammar with the associated Gorn-number.
 * @author Fabian Gallenkamp
 */
public class Layer {
	/**
	 * Gorn number.
	 */
	private Integer[] gornnumber;
	
	/**
	 * CFG-rule represented as a array of {@link Entry}'s.
	 */
	private Entry[] nodes;
	
	public Layer(Integer[] gornnumber,Entry[] nodes){
		this.nodes = nodes;
		this.gornnumber = gornnumber;
	}

	@Override
	public String toString() {
		return "Layer [" + Arrays.toString(gornnumber) + ","
				+ Arrays.toString(nodes) + "]\n";
	}

}
