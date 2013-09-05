package parser;

import java.util.Arrays;

public class Layer {
	
	private Integer[] gornnumber;
	
	private Node[] nodes;
	
	public Layer(Integer[] gornnumber,Node[] nodes){
		this.nodes = nodes;
		this.gornnumber = gornnumber;
	}

	@Override
	public String toString() {
		return "Layer [gornnumber=" + Arrays.toString(gornnumber) + ", nodes="
				+ Arrays.toString(nodes) + "]\n";
	}
	
	

}
