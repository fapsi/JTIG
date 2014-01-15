/**
 * 
 */
package parser.early.components;
import java.util.LinkedList;
import java.util.List;

import tools.tokenizer.Token;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class Chart {

	/**
	 * Array handling a quadratic two dimensional Array for holding the {@link ItemSet} with the specified span
	 */
	private ItemSet[] entrys;
	
	private int width;
	
	private long uniqueitems;
	
	private long processeditems;
	
	public Chart(){
		
	}
	
	public void initialize(Token[] tokens, DefaultItemFactory factory){
		width = tokens.length + 1;
		entrys =  new ItemSet[(width * width) +1];
		
		// create items for tokens occurring in original sentence and add to chart
		for (int i = 0; i < tokens.length; i++ ) {
			Item item = factory.createTerminalItemInstance(tokens[i].getLabel(), i);
			addItem(item);
		}
	}
	
	public boolean addItem(Item item){
		int index = item.getRight() * width + item.getLeft();
		if (entrys[index] == null)
			entrys[index] = new ItemSet();
		
		processeditems++;
		
		boolean added = entrys[index].add(item);
		
		if (added)
			uniqueitems++;
		
		return added;
	}
	
	public ItemSet getItemset(int left, int right){
		if ( left >= width || right >= width || left < 0 || right < 0 ){
			return null;
		}
		return entrys[right * width + left];
	}
	
	public List<Item> getChartItems(ItemFilter filter){
		List<Item> result = new LinkedList<Item>();
		
		if (filter.getStart() >= 0){
			
			if (filter.getEnd() < 0){
				for (int right = filter.getStart(); right < width;right++){
					ItemSet itemset = getItemset(filter.getStart() , right);
					if (itemset != null)
						itemset.getItems(result,filter);
				}
				
			} else {
				ItemSet itemset = getItemset(filter.getStart() , filter.getEnd());
				if (itemset != null)
					itemset.getItems(result,filter);
			}
		} else {
			if (filter.getEnd() >= 0){
				
				for (int left = 0; left <= filter.getEnd();left++){
					ItemSet itemset = getItemset(left , filter.getEnd());
					if (itemset != null)
						itemset.getItems(result,filter);
				}
				
			} else {
				
				for (int right = 0; right < width;right++){
					for (int left = 0; left < width;left++){
						ItemSet itemset = getItemset(left,right);
						if (itemset != null)
							itemset.getItems(result,filter);
					}
				}
				
			}
		}
		return result;
	}
	
	public long getAmountUniqueItems(){
		return uniqueitems;
	}
	
	public long getAmountProcessedItems(){
		return processeditems;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int right = 0; right < width;right++){
			for (int left = 0; left < width;left++){
				sb.append("[");
				sb.append(left);
				sb.append(",");
				sb.append(right);
				sb.append("]");
				sb.append(":");
				sb.append(getItemset(left,right)!=null?getItemset(left,right):"");
				sb.append("\n");
			}
		}
		return sb.toString();
	}
}
