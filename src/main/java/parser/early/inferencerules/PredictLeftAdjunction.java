/**
 * 
 */
package parser.early.inferencerules;


import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

import parser.early.Chart;
import parser.early.DefaultItemFactory;
import parser.early.DerivationType;
import parser.early.Item;
import parser.early.ItemDerivation;
import parser.lookup.ActivatedLexicon;
import parser.lookup.ActivatedElementaryTree;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class PredictLeftAdjunction extends InferenceRule {

	private ActivatedLexicon activatedlexicon;
	
	public PredictLeftAdjunction(DefaultItemFactory factory, Chart chart,
			PriorityQueue<Item> agenda,ActivatedLexicon activatedlexicon) {
		super(factory, chart, agenda);
		
		this.activatedlexicon = activatedlexicon;
	}

	/* (non-Javadoc)
	 * @see parser.early.inferencerules.InferenceRule#apply(parser.early.Item)
	 */
	@Override
	public void apply(Item item) {

		List<ActivatedElementaryTree> result = activatedlexicon.get(item.getLeftHandSide().getLabel());
		if (result == null)
			return;
		
		for (ActivatedElementaryTree element : result){
			
			if (item.getRight() <= element.getLeft() && 
					element.isAdjuntionCompatible(item)){ 
				Item newitem = factory.createItemInstance(element, item.getRight());
				newitem.addDerivation(new ItemDerivation(DerivationType.PredictLeftAux,item));
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
				&& item.getDotPosition() == 1 //dot is leftmost
				&& !(Arrays.equals(item.getLayer().getGornNumber(), new int[]{0}) && item.hasAuxiliaryTypeTree());
	}

}
