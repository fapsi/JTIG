/**
 * 
 */
package parser.early.components;

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
	
	public ItemDerivation(DerivationType type, Item... a ){
		items = a;
		this.type = type;
	}
	
	public Item[] getItems(){
		return items;
	}
	
	public DerivationType getType(){
		return type;
	}
	
	private boolean hasSameItems(Item[] other){
		//TODO: ugly fast fix
		if (items.length != other.length)
			return false;
		if (items.length == 2)
			return (items[0].equals(other[0]) && items[1].equals(other[1])) ||
					(items[0].equals(other[1]) && items[1].equals(other[0]));
		else if(items.length == 1)
			return items[0].equals(other[0]);
		return false;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(items);
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		//TODO: improve
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemDerivation other = (ItemDerivation) obj;
		if (!hasSameItems(other.items))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String str = "";
		for (Item i : items)
			str += i.toStringUgly()+"\n";
		
		return "ItemDerivation [type=" + type + ", items="
				+ str + "]";
	}
}
