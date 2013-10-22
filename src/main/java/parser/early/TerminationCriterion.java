/**
 * 
 */
package parser.early;

import java.util.Set;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class TerminationCriterion implements ItemFilter {

	private Set<String> startSymbols;
	
	private int inputlength;
	
	public TerminationCriterion(Set<String> startSymbols, int inputlength) {
		this.startSymbols = startSymbols;
		this.inputlength = inputlength;
		
	}
	/**
	 * 
	 */
	@Override
	public boolean apply(Item item) {
		
		return item.isPassive() 
				&& item.getLeft() == 0
				&& item.getRight() == inputlength
				&& startSymbols.contains(item.getLeftHandSide().getLabel())
				&& item.hasInitialTypeTree();
	}

	@Override
	public int getStart() {
		throw new UnsupportedOperationException("Shouldn't be ever called.");
	}
	@Override
	public int getEnd() {
		throw new UnsupportedOperationException("Shouldn't be ever called.");
	}
	@Override
	public ItemStatus getStatus() {
		throw new UnsupportedOperationException("Shouldn't be ever called.");
	}

}
