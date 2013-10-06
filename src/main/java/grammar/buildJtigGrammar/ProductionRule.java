/**
 * 
 */
package grammar.buildJtigGrammar;

import java.util.Arrays;

/**
 * Stores a production rule of the context free grammar from a TIG-Rule.
 * For accessing properly, it also contains a unique Gorn-number.
 * @author Fabian Gallenkamp
 */
public class ProductionRule {
	/**
	 * Gorn number.
	 */
	private int[] gornnumber;
	
	/**
	 * CFG-rule represented as a array of {@link Entry}'s.
	 */
	private Entry[] entrys;
	
	public ProductionRule(int[] gornnumber,Entry[] entrys){
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
