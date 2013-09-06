/**
 * 
 */
package parser;

import java.util.Arrays;

/**
 * 
 * 
 * @author Fabian Gallenkamp
 */
public class Layer {
	
	private Integer[] gornnumber;
	
	private Entry[] nodes;
	
	public Layer(Integer[] gornnumber,Entry[] nodes){
		this.nodes = nodes;
		this.gornnumber = gornnumber;
	}

	@Override
	public String toString() {
		return "Layer [gornnumber=" + Arrays.toString(gornnumber) + ", nodes="
				+ Arrays.toString(nodes) + "]\n";
	}
	
	

}
