/**
 * 
 */
package parser.early;

import java.util.List;

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
	
	private final DefaultItemFactory factory;
	
	public Chart(){
		factory = new DefaultItemFactory();
	}
	
	public void initialize(String[] tokens){
		factory.reset();
		width = tokens.length;
		entrys =  new Itemset[width*width];
	}
	
	public Itemset getItemset(int left, int right){
		return entrys[right * width + left];
	}
	
	public List<Item> getChartItems(Filter<Item> filter){
		return null;
	}
	
	
}
