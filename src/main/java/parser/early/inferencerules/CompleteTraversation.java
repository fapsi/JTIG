/**
 * 
 */
package parser.early.inferencerules;

import java.util.Arrays;
import java.util.PriorityQueue;

import parser.early.Chart;
import parser.early.DefaultItemFactory;
import parser.early.DerivationType;
import parser.early.Item;
import parser.early.ItemDerivation;
import parser.early.ItemFilter;
import parser.early.ItemStatus;
import parser.lookup.ActivatedTIGRule;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class CompleteTraversation extends InferenceRule {

	public CompleteTraversation(DefaultItemFactory factory, Chart chart,
			PriorityQueue<Item> agenda) {
		super(factory, chart, agenda);
	}

	/* (non-Javadoc)
	 * @see parser.early.inferencerules.InferenceRule#apply(parser.early.Item)
	 */
	@Override
	public void apply(final Item item) {
		//final Item itemcopy = item;
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
					return item.getActivatedTIGRule().equals(x.getActivatedTIGRule())
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
					return item.getActivatedTIGRule().equals(x.getActivatedTIGRule())
							&& item.hasParentGornNumber(x)
							&& x.getLeftHandSide().equals(item.getNextEntry());
				}

			};
		}
		
		//System.out.println("Filter: "+filter.getStart()+":"+filter.getEnd());
		
		for (Item candidate : chart.getChartItems(filter)){
			//System.out.println("\tCandidate for '"+item+"': "+candidate);
			Item newitem = factory.createItemInstance(
					 ispassive ? candidate.getLeft() : item.getLeft(), 
					 ispassive ? item.getRight() : candidate.getRight(), 
					(ispassive ? candidate.getDotPosition() : item.getDotPosition()) + 1, 
					 ispassive ? candidate.getLayer() : item.getLayer(), 
					 candidate.getActivatedTIGRule(), 
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

	/* (non-Javadoc)
	 * @see parser.early.inferencerules.InferenceRule#getName()
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
