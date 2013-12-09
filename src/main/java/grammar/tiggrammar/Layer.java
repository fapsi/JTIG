/**
 * 
 */
package grammar.tiggrammar;

import java.util.Arrays;

/**
 * Stores a production rule of the context free grammar from a TIG-Rule.
 * For accessing properly, it also contains a unique Gorn-number.
 * @author Fabian Gallenkamp
 */
public class Layer {
	/**
	 * Gorn number.
	 */
	protected int[] gornnumber;
	
	/**
	 * CFG-rule represented as a array of {@link Entry}'s.
	 */
	protected Entry[] entrys;

	protected boolean isonspine;
	
	public Layer(int[] gornnumber,Entry[] entrys, boolean isonspine){
		this.entrys = entrys;
		this.gornnumber = gornnumber;
		this.isonspine = isonspine;
	}

	public Entry[] getEntrys(){
		return this.entrys;
	}

	public Entry getEntry(int i) {
		if (i < this.entrys.length)
			return this.entrys[i];
		return null;
	}
	
	public int[] getGornNumber(){
		return gornnumber;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(entrys);
		result = prime * result + Arrays.hashCode(gornnumber);
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Layer other = (Layer) obj;
		if (!Arrays.equals(entrys, other.entrys))
			return false;
		if (!Arrays.equals(gornnumber, other.gornnumber))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Layer [" + Arrays.toString(gornnumber) + ","
				+ Arrays.toString(entrys) + "]\n";
	}

	public boolean isOnSpine() {
		return isonspine;
	}
}
