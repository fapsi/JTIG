/**
 * 
 */
package parser;

import java.util.Arrays;
import java.util.List;

/**
 * A grammatical rule in the tree insertion grammar. 
 * Consists of an list of CFG rules (see {@link Layer}), 
 * an unique index, an lexical anchor,...
 * @author Fabian Gallenkamp
 */
public class RuleTree {
	
	/**
	 * Unique index of the rule tree
	 */
	private long index;
	
	/**
	 * CFG-rules extracted of the tree
	 */
	private List<Layer> layers;
	
	/**
	 * Lexical anchors for this tree.
	 */
	private List<Entry> lexicalanchors;

	/**
	 * Frequency in which this tree occurs in language.
	 */
	private long freq;
	
	/**
	 * Probability in which this tree occurs in language.
	 */
	private double prob;
	
	/**
	 * Gorn-number of the layer with the leaf-node where the adjunction happens.
	 * Null if the tree isn't an auxiliary tree.
	 */
	private Integer[] spine;

	/**
	 * Constructs a rule tree.
	 * @param index {@link #index}
	 * @param layers {@link #layers}
	 * @param lexicalanchors {@link #lexicalanchors}
	 * @param freq {@link #freq}
	 * @param prob {@link #prob}
	 * @param spine {@link #spine}
	 */
	public RuleTree(long index,List<Layer> layers, List<Entry> lexicalanchors,long freq, double prob,Integer[] spine){
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
