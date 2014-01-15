/**
 * 
 */
package parser.early.components.agenda;

import java.util.Comparator;

import parser.early.components.Item;
import parser.early.components.ItemFilter;

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
			
		
		int distancea = Math.abs(a.getLeft() - a.getRight());
		int distanceb = Math.abs(b.getLeft() - b.getRight());
		if (distancea < distanceb)
			return -Math.abs(distancea-distanceb);
		if (distancea > distanceb)
			return Math.abs(distancea-distanceb);
			
		return 0;
	}

}
