/**
 * 
 */
package parser.early.inferencerules;

import grammar.treeinsertion.NodeType;

import java.util.List;

import parser.early.components.DerivationType;
import parser.early.components.Item;
import parser.early.components.ItemDerivation;
import parser.lookup.ActivatedElementaryTree;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class PredictSubstitution extends InferenceRule {

	public PredictSubstitution() {
		super();
	}

	/* (non-Javadoc)
	 * @see parser.early.inferencerules.InferenceRule#apply(parser.early.Item)
	 */
	@Override
	public void apply(Item item) {
		List<ActivatedElementaryTree> result = activatedlexicon.get(item.getNextEntry().getLabel());
		if (result == null)
			return;
		for (ActivatedElementaryTree element : result){
			if (item.getRight() <= element.getLeft()){
				Item newitem = factory.createItemInstance(element,item.getRight());
				newitem.addDerivation(new ItemDerivation(DerivationType.PredictSubstitution,item));
			
				agenda.add(newitem);
			}
		}
	}

	/* (non-Javadoc)
	 * @see parser.early.inferencerules.InferenceRule#isApplicable(parser.early.Item)
	 */
	@Override
	public boolean isApplicable(Item item) {
		return item.isActive() 
				&& item.getNextEntryType() == NodeType.SUBST;
	}

	/* (non-Javadoc)
	 * @see parser.early.inferencerules.InferenceRule#getName()
	 */
	@Override
	public String toString() {
		return "PredictSubstitution";
	}

}
