/**
 * 
 */
package parser.early.inferencerules;

import java.util.Arrays;

import parser.early.components.DerivationType;
import parser.early.components.Item;
import parser.early.components.ItemDerivation;
import parser.early.components.ItemFilter;
import parser.early.components.ItemStatus;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class CompleteRightAdjunction extends InferenceRule {

	/* (non-Javadoc)
	 * @see parser.early.inferencerules.InferenceRule#apply(parser.early.Item)
	 */
	@Override
	public void apply(final Item item) {
		ItemFilter filter = null;
		boolean ispassive = isApplicablePassive(item);
		if (ispassive){
			filter = new ItemFilter() {
				@Override
				public ItemStatus getStatus() {
					return ItemStatus.Passive;
				}
				@Override
				public int getStart() {
					return -1;
				}

				@Override
				public int getEnd() {
					return item.getLeft();
				}
				
				@Override
				public boolean apply(Item x) {
					// see CompleteLeftAdjunction#isApplicableActive
					return CompleteRightAdjunction.this.isApplicableActive(x)
							// left-hand-sides have equal labels
							&& x.getLeftHandSide().getLabel().equals(item.getLeftHandSide().getLabel())
							// trees are adjunction compatible
							&& item.getActivatedElementaryTree()!=null?item.getActivatedElementaryTree().isAdjuntionCompatible(x):false;	
				}
			};
		} else {
			filter = new ItemFilter() {
				@Override
				public ItemStatus getStatus() {
					return ItemStatus.Passive;
				}
				@Override
				public int getStart() {
					return item.getRight();
				}

				@Override
				public int getEnd() {
					return -1;
				}
				
				@Override
				public boolean apply(Item x) {
					// see CompleteLeftAdjunction#isApplicableActive
					return CompleteRightAdjunction.this.isApplicablePassive(x)
							// left-hand-sides have equal labels
							&& item.getLeftHandSide().getLabel().equals(x.getLeftHandSide().getLabel())
							// trees are adjunction compatible
							&& x.getActivatedElementaryTree()!=null?item.getActivatedElementaryTree().isAdjuntionCompatible(item):false;	
				}
			};
		}
		
		for (Item candidate : chart.getChartItems(filter)){
			Item newitem = factory.createItemInstance(
					 ispassive ? candidate.getLeft() : item.getLeft(), 
					 ispassive ? item.getRight() : candidate.getRight(), 
					(ispassive ? candidate.getDotPosition() : item.getDotPosition()), 
					 ispassive ? candidate.getLayer() : item.getLayer(), 
					 ispassive ? candidate.getActivatedElementaryTree() : item.getActivatedElementaryTree(), 
					 candidate.getProbability() * item.getProbability());
			newitem.addDerivation(new ItemDerivation(
					DerivationType.CompleteRightAdjunction, 
					ispassive? candidate : item,
					ispassive? item : candidate));
			agenda.add(newitem);
		}
	}

	/* (non-Javadoc)
	 * @see parser.early.inferencerules.InferenceRule#isApplicable(parser.early.Item)
	 */
	@Override
	public boolean isApplicable(Item item) {
		return (item.isPassive() && isApplicableActive(item)) //TODO: really passive???
				|| ( item.isPassive() && isApplicablePassive(item));
	}
	
	private boolean isApplicableActive(Item item) {
		return 	// not auxiliary tree root node
				!(item.hasAuxiliaryTypeTree() && 
						Arrays.equals(item.getLayer().getGornNumber(), new int[]{0}));
	}

	private boolean isApplicablePassive(Item item){
		return item.hasRightAuxiliaryTypeTree()
				// completed auxiliary tree, which can be adjoined into something
				&& Arrays.equals(item.getLayer().getGornNumber(), new int[]{0});
	}
	
	@Override
	public String toString() {
		return "CompleteRightAdjunction";
	}
}
