/**
 * 
 */
package parser.early;

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
		width = tokens.length;
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
	
	public List<Item> getChartItems(Filter<Item> filter){
		return null;
	}
	
	
}
