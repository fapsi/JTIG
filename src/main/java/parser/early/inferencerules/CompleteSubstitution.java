/**
 * 
 */
package parser.early.inferencerules;

import grammar.buildJtigGrammar.NodeType;

import java.util.Arrays;
import parser.early.DerivationType;
import parser.early.Item;
import parser.early.ItemDerivation;
import parser.early.ItemFilter;
import parser.early.ItemStatus;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class CompleteSubstitution extends InferenceRule {

	public CompleteSubstitution() {
		super();
	}

	/* (non-Javadoc)
	 * @see parser.early.inferencerules.InferenceRule#apply(parser.early.Item)
	 */
	@Override
	public void apply(final Item item) {
		boolean ispassive = item.isPassive();
		ItemFilter filter;
		if (ispassive){
			filter = new ItemFilter() {
				
				@Override
				public ItemStatus getStatus() {
					return ItemStatus.Active;
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
					return x.getNextEntryType() == NodeType.SUBST
							&& x.getNextEntry().getLabel().equals(item.getLeftHandSide().getLabel());
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
					return x.hasInitialTypeTree()
							&& Arrays.equals(x.getLayer().getGornNumber(), new int[]{0})
							&& item.getNextEntry().getLabel().equals(x.getLeftHandSide().getLabel());
				}
			};
		}
		
	for (Item candidate : chart.getChartItems(filter)){
		
			Item newitem = factory.createItemInstance(
					 ispassive ? candidate.getLeft() : item.getLeft(), 
					 ispassive ? item.getRight() : candidate.getRight(), 
					(ispassive ? candidate.getDotPosition() : item.getDotPosition()) + 1, 
					 ispassive ? candidate.getLayer() : item.getLayer(), 
					 ispassive ? candidate.getActivatedElementaryTree() : item.getActivatedElementaryTree(), 
					 candidate.getProbability() * item.getProbability());
			
			newitem.addDerivation(new ItemDerivation(
					DerivationType.CompleteSubstitution, 
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
		return isApplicableActive(item) || isApplicablePassive(item);
	}
	
	private boolean isApplicablePassive(Item item){
		return item.isPassive() 
				&& Arrays.equals(item.getLayer().getGornNumber(), new int[]{0})
				&& item.hasInitialTypeTree();
	}

	private boolean isApplicableActive(Item item){
		return item.isActive()
				&& item.getNextEntryType() == NodeType.SUBST;
	}

	@Override
	public String toString() {
		return "CompleteSubstitution";
	}

}
