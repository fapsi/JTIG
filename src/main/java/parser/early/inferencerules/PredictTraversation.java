/**
 * 
 */
package parser.early.inferencerules;

import grammar.treeinsertion.Layer;
import grammar.treeinsertion.NodeType;
import parser.early.components.DerivationType;
import parser.early.components.Item;
import parser.early.components.ItemDerivation;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class PredictTraversation extends InferenceRule {

	public PredictTraversation() {
		super();
	}

	/* (non-Javadoc)
	 * @see parser.early.inferencerules.InferenceRule#apply(parser.early.Item)
	 */
	@Override
	public void apply(Item item) {
		// Retrieve the next layer element
		Layer nextlayer = item.getNextLayer();
		// Construct the new item out of the old one with the retrieved layer
		Item newitem = factory.createItemInstance(item.getRight(), item.getRight(), 1 , nextlayer, item.getActivatedElementaryTree(),1.0d);
		newitem.addDerivation(new ItemDerivation(DerivationType.PredictTraversation, item));
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
	public String toString() {
		return "PredictTraversation";
	}

}
