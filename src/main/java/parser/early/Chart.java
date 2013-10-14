/**
 * 
 */
package parser.early;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import tools.tokenizer.Token;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class Chart {

	/**
	 * Array handling a quadratic two dimensional Array for holding the {@link Itemset} with the specified span
	 */
	private Itemset[] entrys;
	
	private int width;
	
	public Chart(){
		
	}
	
	public void initialize(Token[] tokens, DefaultItemFactory factory){
		width = tokens.length +1 ;
		entrys =  new Itemset[width*width];
		
		// create items for tokens occurring in original sentence and add to chart
		for (int i = 0; i < tokens.length; i++ ) {
			Item item = factory.createTerminalItemInstance(tokens[i].getLabel(), i);
			addItem(item);
		}
	}
	
	public void addItem(Item item){
		int index = item.getRight() * width + item.getLeft();
		if (entrys[index] == null)
			entrys[index] = new Itemset();
		entrys[index].add(item);
	}
	
	public Itemset getItemset(int left, int right){
		return entrys[right * width + left];
	}
	
	public List<Item> getChartItems(ItemFilter filter){
		List<Item> result = new LinkedList<Item>();
		for (int right = 0; right < width;right++){
			for (int left = 0; left < width;left++){
				getItemset(left,right).getItems(result,filter);
			}
		}
		return result;
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
