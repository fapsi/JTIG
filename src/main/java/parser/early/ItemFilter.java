/**
 * 
 */
package parser.early;

/**
 * 
 * @author Fabian Gallenkamp
 */
public interface ItemFilter {
	
	public boolean apply(Item item);
	
	public int getStart();
	
	public int getEnd();

	public ItemStatus getStatus();
}
