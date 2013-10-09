/**
 * 
 */
package parser.early;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class ActiveFilter implements Filter<Item> {

	@Override
	public boolean apply(Item item) {
		return item.isActive();
	}

}
