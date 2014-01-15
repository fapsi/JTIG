/**
 * 
 */
package parser.early.inferencerules;

import java.util.Arrays;
import java.util.List;

import parser.early.components.DerivationType;
import parser.early.components.Item;
import parser.early.components.ItemDerivation;
import parser.lookup.ActivatedElementaryTree;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class PredictRightAdjunction extends InferenceRule {

	/* (non-Javadoc)
	 * @see parser.early.inferencerules.InferenceRule#apply(parser.early.Item)
	 */
	@Override
	public void apply(Item item) {
		//TODO: identical to PREDICTLEFTADJ ??
		List<ActivatedElementaryTree> result = activatedlexicon.get(item.getLeftHandSide().getLabel());
		if (result == null)
			return;
		
		for (ActivatedElementaryTree element : result){
			
			if (item.getRight() <= element.getLeft() && 
					element.isAdjuntionCompatible(item)){ 
				Item newitem = factory.createItemInstance(element, item.getRight());
				newitem.addDerivation(new ItemDerivation(DerivationType.PredictRightAux,item));
				agenda.add(newitem);
			}
			
		}

	}

	/* (non-Javadoc)
	 * @see parser.early.inferencerules.InferenceRule#isApplicable(parser.early.Item)
	 */
	@Override
	public boolean isApplicable(Item item) {
		return item.isPassive()
		&& !(Arrays.equals(item.getLayer().getGornNumber(), new int[]{0}) && item.hasAuxiliaryTypeTree());
	}
	
	@Override
	public String toString() {
		return "PredictRightAdjunction";
	}

}
