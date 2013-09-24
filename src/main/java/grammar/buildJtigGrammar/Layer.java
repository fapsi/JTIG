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
	private Entry[] entrys;
	
	public Layer(Integer[] gornnumber,Entry[] entrys){
		this.entrys = entrys;
		this.gornnumber = gornnumber;
	}

	public Entry[] getEntrys(){
		return this.entrys;
	}

	public Entry getEntry(int i) {
		if (i < this.entrys.length)
			return this.entrys[i];
		return null;
	}
	
	@Override
	public String toString() {
		return "Layer [" + Arrays.toString(gornnumber) + ","
				+ Arrays.toString(entrys) + "]\n";
	}
}
