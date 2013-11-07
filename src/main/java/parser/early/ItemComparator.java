/**
 * 
 */
package parser.early;

import java.util.Comparator;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class ItemComparator implements Comparator<Item> {

	private ItemFilter terminationcriterion;

	public ItemComparator(ItemFilter terminationcriterion){
		this.terminationcriterion = terminationcriterion;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Item a, Item b) {
		boolean term_a = terminationcriterion.apply(a);
		boolean term_b = terminationcriterion.apply(b);
		if (term_a && term_b)
			return 0;
		if (term_a)
			return -100;
		if (term_b)
			return 100;
		
		/*int dotdistance = Math.abs(a.getDotPosition()-b.getDotPosition());
		if (a.getDotPosition() < b.getDotPosition())
			return -dotdistance;
		if (a.getDotPosition() > b.getDotPosition())
			return dotdistance;*/
			
		
		int iddistance = Math.abs(a.getID() - b.getID());
		if (a.getID() < b.getID())
			return -iddistance;
		if (a.getID() > b.getID())
			return iddistance;
			
		return 0;
	}

}
