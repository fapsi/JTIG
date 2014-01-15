/**
 * 
 */
package parser.early.components;

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
		return 0;
	}
	@Override
	public int getEnd() {
		return inputlength;
	}
	@Override
	public ItemStatus getStatus() {
		return ItemStatus.Passive;
	}

}
