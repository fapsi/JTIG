/**
 * 
 */
package parser.early.inferencerules;

import grammar.buildJtigGrammar.Layer;
import grammar.buildJtigGrammar.NodeType;
import java.util.PriorityQueue;
import parser.early.DefaultItemFactory;
import parser.early.Item;
import parser.early.ItemDerivation;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class PredictTraversation extends InferenceRule {

	public PredictTraversation(DefaultItemFactory factory,PriorityQueue<Item> agenda) {
		// needs no chart
		super(factory,null,agenda);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see parser.early.inferencerules.InferenceRule#apply(parser.early.Item)
	 */
	@Override
	public void apply(Item item) {
		// Retrieve the next layer element
		Layer nextlayer = item.getNextLayer();
		// Construct the new item out of the old one with the retrieved layer
		Item newitem = factory.createItemInstance(item.getLeft(), item.getRight(), 1 , nextlayer, item.getActivatedTIGRule(),1.0d);
		newitem.addDerivation(new ItemDerivation(null, item));
		// add to agenda
		addtoagenda(newitem);
	}
	/* (non-Javadoc)
	 * @see parser.early.inferencerules.InferenceRule#isApplicable(parser.early.Item)
	 */
	@Override
	public boolean isApplicable(Item item) {
		return item.isActive() && item.getNextEntry() != null && item.getNextEntry().getNodeType() == NodeType.NONTERM;
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
