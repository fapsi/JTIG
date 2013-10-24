/**
 * 
 */
package parser.early.inferencerules;

import grammar.buildJtigGrammar.NodeType;

import java.util.List;
import java.util.PriorityQueue;

import parser.early.DefaultItemFactory;
import parser.early.DerivationType;
import parser.early.Item;
import parser.early.ItemDerivation;
import parser.lookup.ActivatedLexicon;
import parser.lookup.ActivatedTIGRule;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class PredictSubstitution extends InferenceRule {

	private ActivatedLexicon activatedlexicon;

	public PredictSubstitution(DefaultItemFactory factory,
			PriorityQueue<Item> agenda,ActivatedLexicon activatedlexicon) {
		super(factory, null, agenda);
		this.activatedlexicon = activatedlexicon;
	}

	/* (non-Javadoc)
	 * @see parser.early.inferencerules.InferenceRule#apply(parser.early.Item)
	 */
	@Override
	public void apply(Item item) {
		List<ActivatedTIGRule> result = activatedlexicon.get(item.getNextEntry().getLabel());
		for (ActivatedTIGRule element : result){
			
			Item newitem = factory.createItemInstance(element,item.getRight());
			newitem.addDerivation(new ItemDerivation(DerivationType.PredictSubstitution,item));
			
			agenda.add(newitem);
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
