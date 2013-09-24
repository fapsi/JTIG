/**
 * 
 */
package grammar.buildJtigGrammar;

import java.util.List;

/**
 * A grammatical rule in the tree insertion grammar. 
 * Consists of an list of CFG rules (see {@link Layer}), 
 * an unique index, an lexical anchor,...
 * @author Fabian Gallenkamp
 */
public class RuleTree {
	
	/**
	 * Unique index of the rule tree.
	 */
	private long index;
	
	/**
	 * CFG-rules extracted of the tree.
	 */
	private List<Layer> layers;
	
	/**
	 * Lexical anchors for this tree.
	 */
	private List<String> lexicalanchors;

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
	public RuleTree(long index,List<Layer> layers, List<String> lexicalanchors,long freq, double prob,Integer[] spine){
		this.index = index;
		this.layers = layers;
		this.lexicalanchors = lexicalanchors;
		this.freq = freq;
		this.prob = prob;
		this.spine = spine;
	}
	
	public List<String> getlexicalanchors(){
		return lexicalanchors;
	}
	
	
	/**
	 * @return the index
	 */
	public long getIndex() {
		return index;
	}

	/**
	 * @return the layers
	 */
	public List<Layer> getLayers() {
		return layers;
	}

	/**
	 * @return the freq
	 */
	public long getFreq() {
		return freq;
	}

	/**
	 * @return the prob
	 */
	public double getProb() {
		return prob;
	}

	/**
	 * @return the spine
	 */
	public Integer[] getSpine() {
		return spine;
	}
	
	public String getRootSymbol() {
		if (this.layers.size() <= 0)
			throw new IllegalArgumentException("Rule tree hasn't any layers.");
		Layer l = this.layers.get(0);
		Entry e = l.getEntry(0);
		if (e != null)
			return e.getLabel();
		return null;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(index);
		return sb.toString();
	}
}
