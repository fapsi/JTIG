/**
 * 
 */
package parser.early.components.agenda;

import java.util.PriorityQueue;

import parser.early.components.Item;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class Agenda extends PriorityQueue<Item> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8052555247609953527L;

	public Agenda(int i, ItemComparator itemcomp) {
		super(i, itemcomp);
	}
}
