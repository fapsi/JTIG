/**
 * 
 */
package parser.early;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class StopFilter implements ItemFilter {

	@Override
	public boolean apply(Item item) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ItemStatus getStatus() {
		return ItemStatus.All;
	}

}
