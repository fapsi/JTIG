/**
 * 
 */
package parser.early.inferencerules;

import grammar.buildJtigGrammar.NodeType;

import java.util.PriorityQueue;

import parser.early.Chart;
import parser.early.DefaultItemFactory;
import parser.early.DerivationType;
import parser.early.Item;
import parser.early.ItemDerivation;
import parser.early.ItemFilter;
import parser.early.ItemStatus;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class Scanning extends InferenceRule {

	public Scanning() {
		super();
	}

	/* (non-Javadoc)
	 * @see parser.early.inferencerules.InferenceRule#apply(parser.early.Item)
	 */
	@Override
	public void apply(final Item item) {
		
		NodeType type = item.getNextEntryType();
		
		Item newitem = null;
		if (type != NodeType.TERM){ // CASE type == LFOOT | RFOOT | EPS  [if isApplicable was called first]
			
			// just skip it
			newitem = factory.createItemInstance(
					item.getLeft(),item.getRight(),item.getDotPosition()+1,item.getLayer(),item.getActivatedElementaryTree(), item.getProbability());
			newitem.addDerivation(new ItemDerivation(DerivationType.Consume, item));
			agenda.add(newitem);
			
		} else {
			ItemFilter filter= new ItemFilter() {
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
					return item.getRight()+1;
				}
				@Override
				public boolean apply(Item x) {
					return x.getActivatedElementaryTree() == null && 
							//TODO: ask if it's correct
							item.getActivatedElementaryTree().getLeft() <= x.getLeft() && 
									item.getActivatedElementaryTree().getRight() >= x.getLeft() &&
							item.getNextEntry().getLabel().equals(x.getLeftHandSide().getLabel());
				}
			};

			for (Item candidate : chart.getChartItems(filter)){
				newitem = factory.createItemInstance(
					item.getLeft(),
					candidate.getRight(), //TODO ?????
					item.getDotPosition()+1 ,
					item.getLayer(),
					item.getActivatedElementaryTree(),
					item.getProbability());
				newitem.addDerivation(new ItemDerivation(DerivationType.Consume, item , candidate));
				agenda.add(newitem);
			}
		}
		
	}

	@Override
	public boolean isApplicable(Item item) {
		NodeType type = item.getNextEntryType();
		return type == NodeType.EPS || type == NodeType.LFOOT || type == NodeType.RFOOT || type == NodeType.TERM;
	}
	
	@Override
	public String toString() {
		return "Scanning";
	}
}
