/**
 * 
 */
package parser.early.inferencerules;

import grammar.buildJtigGrammar.Entry;
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

	public Scanning(DefaultItemFactory factory,Chart chart,PriorityQueue<Item> agenda) {
		super(factory,chart,agenda);
	}

	/* (non-Javadoc)
	 * @see parser.early.inferencerules.InferenceRule#apply(parser.early.Item)
	 */
	@Override
	public void apply(Item item) {
		
		final Entry actualentry = item.getNextEntry();
		NodeType type = actualentry.getNodeType();
		
		Item newitem = null;
		if (type != NodeType.TERM){
			
			// just skip it
			newitem = factory.createItemInstance(
					item.getLeft(),item.getRight(),item.getDotPosition()+1,item.getLayer(),item.getActivatedTIGRule(), item.getProbability());
			newitem.addDerivation(new ItemDerivation(DerivationType.Consume, item));
			agenda.add(newitem);
			
		} else {
			ItemFilter filter= new ItemFilter() {
				
				@Override
				public ItemStatus getStatus() {
					return ItemStatus.Passive;
				}
				
				@Override
				public boolean apply(Item item) {
					return item.getActivatedTIGRule() == null && 
							actualentry.getLabel().equals(item.getLeftHandSide().getLabel());
				}
			};

			for (Item chartitem : chart.getChartItems(filter)){
				newitem = factory.createItemInstance(
					item.getLeft(),chartitem.getRight(), item.getDotPosition()+1 ,item.getLayer(),item.getActivatedTIGRule(),item.getProbability());
				newitem.addDerivation(new ItemDerivation(DerivationType.Consume, item));
				agenda.add(newitem);
			}
		}
		
	}

	@Override
	public boolean isApplicable(Item item) {
		
		Entry e = item.getNextEntry();
		NodeType type = ( e == null? null : e.getNodeType());
		
		return type == NodeType.EPS || type == NodeType.LFOOT || type == NodeType.RFOOT || type == NodeType.TERM;
	}
	
	/* (non-Javadoc)
	 * @see parser.early.inferencerules.InferenceRule#getName()
	 */
	@Override
	public String getName() {
		return null;
	}
}
