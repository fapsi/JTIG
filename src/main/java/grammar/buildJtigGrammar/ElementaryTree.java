/**
 * 
 */
package grammar.buildJtigGrammar;

import java.util.Arrays;
import java.util.List;

import tools.GeneralTools;

/**
 * A grammatical rule in the tree insertion grammar. 
 * Implicitly describes a tree with nodes of several types. 
 * These trees can be combined with two operations adjunction and substitution.
 * Consists of an list of context free grammar rules (also production rules) (see {@link Layer}), 
 * an unique index, an lexical anchor,...
 * @author Fabian Gallenkamp
 */
public class ElementaryTree {
	
	protected TreeType type;
	
	/**
	 * Unique index in the language of this TIG-rule.
	 */
	protected long index;
	
	/**
	 * CFG-production-rules describing implicitly the TIG-rule tree.
	 */
	protected List<Layer> layers;
	
	/**
	 * A lexical anchor is a lexical index (one or more words/units), the tree is connected to.
	 */
	protected List<String> lexicalanchors;

	/**
	 * Frequency in which this TIG-rule occurs in the language.
	 */
	protected long frequency;
	
	/**
	 * Probability in which this TIG-rule occurs in language.
	 */
	protected double probability;
	
	/**
	 * Constructs a rule tree.
	 * @param index {@link #index}
	 * @param layers {@link #layers}
	 * @param lexicalanchors {@link #lexicalanchors}
	 * @param frequency {@link #frequency}
	 * @param probability {@link #probability}
	 */
	public ElementaryTree(TreeType type,long index,List<Layer> productionrules, List<String> lexicalanchors,long frequency, double probability){
		this.type = type;
		this.index = index;
		this.layers = productionrules;
		this.lexicalanchors = lexicalanchors;
		this.frequency = frequency;
		this.probability = probability;
	}
	
	public List<String> getLexicalAnchors(){
		return lexicalanchors;
	}
	
	public int[] getFootAddress(){
		if (type == TreeType.Initial)
			return null;
		for (Layer layer : layers){
			for (int i = 0 ; i < layer.entrys.length ; i++){
				if (layer.entrys[i].nodetype == NodeType.LFOOT || layer.entrys[i].nodetype == NodeType.RFOOT){
					return GeneralTools.AppendToIntArray(layer.gornnumber, i);
				}
			}
		}
		throw new UnvalidTreeException("A tree of type "+ type.toString() + " should have a FOOT-element.");
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
	 * @return the frequency
	 */
	public long getFrequency() {
		return frequency;
	}

	/**
	 * @return the probability
	 */
	public double getProbability() {
		return probability;
	}
	
	public Layer getLayer(int i){
		if (this.layers.size() <= 0)
			throw new IllegalArgumentException("Rule tree hasn't any production rules.");
		if (i < 0 || i >= this.layers.size())
			throw new IllegalArgumentException("There exists no layer with such index.");
		Layer l = this.layers.get(i);
		return l;
	}
	
	public Layer getLayer(int[] address){
		for (Layer element : layers){
			if (Arrays.equals(element.gornnumber,address))
				return element;
		}
		return null;
	}
	
	/**
	 * @return the nonterminal on the left side of the first production rule
	 */
	public String getRootSymbol() {
		if (this.layers.size() <= 0)
			throw new UnvalidTreeException("Elementary tree hasn't any production rules.");
		Layer l = this.layers.get(0);
		Entry e = l.entrys[0];
		if (e != null)
			return e.label;
		return null;
	}
	
	public boolean isInitial(){
		return this.type == TreeType.Initial;
	}
	
	public boolean isAuxiliary() {
		return this.type == TreeType.LeftAuxiliary || this.type == TreeType.RightAuxiliary;
	}

	public TreeType getType() {
		return type;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (index ^ (index >>> 32));
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ElementaryTree other = (ElementaryTree) obj;
		if (index != other.index)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(index);
		sb.append(" ");
		for (String actstr : lexicalanchors){
			sb.append(actstr);
			sb.append(" ");
		}
		return sb.toString();
	}
}
