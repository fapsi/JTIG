/**
 * 
 */
package parser.early.inferencerules;

import parser.early.components.DerivationType;
import parser.early.components.Item;
import parser.early.components.ItemDerivation;
import parser.early.components.ItemFilter;
import parser.early.components.ItemStatus;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class CompleteTraversation extends InferenceRule {

	public CompleteTraversation() {
		super();
	}

	/* (non-Javadoc)
	 * @see parser.early.inferencerules.InferenceRule#apply(parser.early.Item)
	 */
	@Override
	public void apply(final Item item) {
		ItemFilter filter = null;
		boolean ispassive = item.isPassive();
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
				
				/**
				 * True, if<br>
				 *  - both item's have the same underlying tree's (same index)<br>
				 *  - the gornnumber of the outer passive item is a subset of the gornnumber of the input item<br>
				 *  - the left hand side of the outer passive item is identical with the actual entry in the input item<br>
				 */
				@Override
				public boolean apply(Item x) {
					return item.getActivatedElementaryTree().equals(x.getActivatedElementaryTree())
							&& x.hasParentGornNumber(item)
							&& item.getLeftHandSide().equals(x.getNextEntry());
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
				/**
				 * True, if<br>
				 *  - both item's have the same underlying tree's (same index)<br>
				 *  - the gornnumber of the input item is a subset of the gornnumber of the outer active item<br>
				 *  - the left hand side of the input item is identical with the actual entry of the outer active item<br>
				 */
				@Override
				public boolean apply(Item x) {					
					return item.getActivatedElementaryTree().equals(x.getActivatedElementaryTree())
							&& item.hasParentGornNumber(x)
							&& x.getLeftHandSide().equals(item.getNextEntry());
				}

			};
		}
		
		for (Item candidate : chart.getChartItems(filter)){
			Item newitem = factory.createItemInstance(
					 ispassive ? candidate.getLeft() : item.getLeft(), 
					 ispassive ? item.getRight() : candidate.getRight(), 
					(ispassive ? candidate.getDotPosition() : item.getDotPosition()) + 1, 
					 ispassive ? candidate.getLayer() : item.getLayer(), 
					 candidate.getActivatedElementaryTree(), 
					 candidate.getProbability());
			newitem.addDerivation(new ItemDerivation(
					DerivationType.CompleteTraversation, 
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
		return true; // improve it
	}

	@Override
	public String toString() {
		return "CompleteTraversation";
	}

}
