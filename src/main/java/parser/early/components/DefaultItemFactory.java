/**
 * 
 */
package parser.early.components;

import parser.lookup.ActivatedElementaryTree;
import grammar.treeinsertion.Entry;
import grammar.treeinsertion.Layer;
import grammar.treeinsertion.NodeType;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class DefaultItemFactory {

	private int item_cnt;
	
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
		Layer layer = new Layer(address, entrys,false);
		// create the item and return it
		Item item = new Item(left,left + 1, 1 , layer, null,1.0,item_cnt);
		return item;
	}
	
	public Item createItemInstance(ActivatedElementaryTree activatedruletree, int start){
		// increase counter for new instance
		increase();
		Item item = new Item(start , start , 1, activatedruletree.getLayer(0),activatedruletree,1.0,item_cnt);
		return item;
	}
		
	public Item createItemInstance(int left,int right,int dotposition, Layer layer,ActivatedElementaryTree activatedtigrule, double probability) {
		// increase counter for new instance
		increase();
		
		Item newitem = new Item(
				left,
				right,
				dotposition,
				layer,
				activatedtigrule,
				probability,
				item_cnt
				);
		return newitem;
	}
	
	private void increase(){
		item_cnt++;
	}

	public void reset() {
		item_cnt = 0;
	}
	public int getAmountCreatedItems(){
		return item_cnt;
	}
}
