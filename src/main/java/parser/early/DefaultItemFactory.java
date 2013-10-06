/**
 * 
 */
package parser.early;

import grammar.buildJtigGrammar.Entry;
import grammar.buildJtigGrammar.NodeType;
import grammar.buildJtigGrammar.ProductionRule;

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
		Item i = new Item(left,left + 1, 0 , layer,
				null, ItemStatus.Passive,1.0,item_cnt,null);
		return i;
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
