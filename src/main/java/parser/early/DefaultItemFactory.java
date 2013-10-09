/**
 * 
 */
package parser.early;

import parser.lookup.ActivatedRuleTree;
import grammar.buildJtigGrammar.Entry;
import grammar.buildJtigGrammar.NodeType;
import grammar.buildJtigGrammar.ProductionRule;
import grammar.buildJtigGrammar.TIGRule;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class DefaultItemFactory {

	int item_cnt;
	
	public DefaultItemFactory(){
		reset();
	}
	
	public Item createTerminalItemInstance(String terminal,int left){
		// increase counter for new instance
		increase();
		// build gorn number for terminal element
		int[] address = new int[]{0};
		// build layer production rule for terminal element
		Entry[] entrys = new Entry[]{new Entry(NodeType.TERM, terminal)};
		ProductionRule layer = new ProductionRule(address, entrys);
		// create the item and return it
		Item item = new Item(left,left + 1, 1 , layer, null,1.0,item_cnt,null);
		return item;
	}
	
	public Item createItemInstance(ActivatedRuleTree aruletree){
		// increase counter for new instance
		increase();
		TIGRule ruletree = aruletree.getTIGRuleTree();
		Item item = new Item(aruletree.getLeft(),aruletree.getRight() // TODO: not sure about this
				, 1 , ruletree.getProductionRules().get(0), ruletree, 1.0 , item_cnt, null);
		return item;
	}
	
	public Item createItemInstance(Item i1,Item i2){
		// increase counter for new instance
		increase();
		return null;
	}
	
	private synchronized void increase(){
		item_cnt++;
	}

	public synchronized void reset() {
		item_cnt = 0;
	}
}
