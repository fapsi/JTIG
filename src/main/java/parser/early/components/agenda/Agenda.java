/**
 * 
 */
package parser.early.components.agenda;

import java.util.concurrent.PriorityBlockingQueue;

import parser.early.components.Item;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class Agenda extends PriorityBlockingQueue<Item> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8052555247609953527L;

	public Agenda(int i, ItemComparator itemcomp) {
		super(i, itemcomp);
	}
}
