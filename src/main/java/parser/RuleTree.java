package parser;

import java.util.Arrays;
import java.util.List;

public class RuleTree {
	
	private long index;
	
	private List<Layer> layers;
	
	private List<Node> lexicalanchors;

	private int freq;
	
	private double prob;
	
	private Integer[] spine;

	public RuleTree(long index,List<Layer> layers, List<Node> lexicalanchors,int freq, double prob,Integer[] spine){
		this.index = index;
		this.layers = layers;
		this.lexicalanchors = lexicalanchors;
		this.freq = freq;
		this.prob = prob;
		this.spine = spine;
	}
	
	@Override
	public String toString() {
		return "\nRuleTree {\n index=" + index + ",\n layers=" + layers
				+ ",\n lexicalanchors=" + lexicalanchors + ",\n freq=" + freq
				+ ",\n prob=" + prob + ",\n spine=" + Arrays.toString(spine) + "\n}";
	}
}
