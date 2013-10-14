/**
 * 
 */
package parser.early;

import java.util.Arrays;

/**
 *  
 * @author Fabian Gallenkamp
 */
public class ItemDerivation {

	/**
	 * derivation type
	 */
	private DerivationType type;
	
	/**
	 * items
	 */
	private Item[] items;
	
	public ItemDerivation(DerivationType type, Item a, Item b){
		items = new Item[]{a,b};
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ItemDerivation [type=" + type + ", items="
				+ Arrays.toString(items) + "]";
	}
}
