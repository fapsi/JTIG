/**
 * 
 */
package parser.early.inferencerules;

import java.util.PriorityQueue;

import parser.early.Chart;
import parser.early.DefaultItemFactory;
import parser.early.Item;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class CompleteSubstitution extends InferenceRule {

	public CompleteSubstitution(DefaultItemFactory factory, Chart chart,
			PriorityQueue<Item> agenda) {
		super(factory, chart, agenda);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see parser.early.inferencerules.InferenceRule#apply(parser.early.Item)
	 */
	@Override
	public void apply(Item item) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see parser.early.inferencerules.InferenceRule#isApplicable(parser.early.Item)
	 */
	@Override
	public boolean isApplicable(Item item) {
		// TODO Auto-generated method stub
		return false;
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
