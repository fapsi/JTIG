/**
 * 
 */
package grammar.treeinsertion;

import grammar.treeinsertion.exceptions.UnvalidElementaryTreeException;

import java.util.Arrays;
import java.util.List;

import tools.GeneralTools;

/**
 * A grammatical rule in the tree insertion grammar.
 * Implicitly describes a tree with nodes of several types. 
 * These trees can be combined with two operations adjunction and substitution.
 * Consists of: 
 * <ul><li>a list of context-free grammar rules (also production rules) (see {@link Layer})</li> 
 * <li>an {@link #index}</li> 
 * <li>a {@link #lexicalanchor}</li> 
 * <li>a {@link TreeType}</li> 
 * <li>a {@link #frequency}</li> 
 * <li>a certain {@link #probability}</li></ul>
 * @author Fabian Gallenkamp
 */
public class ElementaryTree {
	
	/**
	 * {@link TreeType} of this {@link ElementaryTree}, can't be null.
	 */
	protected TreeType type;
	
	/**
	 * Unique index of this {@link ElementaryTree} in the dictionary of the tree insertion grammar.
	 */
	protected long index;
	
	/**
	 * CFG-production-rules describing implicitly the TIG-rule tree.
	 */
	protected List<Layer> layers;
	
	/**
	 * A lexical-anchor is an index (one or more words/units), the tree is connected to.
	 */
	protected List<String> lexicalanchor;

	/**
	 * Frequency in which the lexical-anchor occurs in the tree insertion grammar.
	 */
	protected long frequency;
	
	/**
	 * Probability in which this {@link ElementaryTree} occurs in the tree insertion grammar.
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
	public ElementaryTree(TreeType type,long index,List<Layer> layers, List<String> lexicalanchor,long frequency, double probability){
		if (type == null)
			throw new UnvalidElementaryTreeException("Tree has type null.");
		this.type = type;
		
		if (index < 0 )
			throw new UnvalidElementaryTreeException("Index has to be greater than 0.");
		this.index = index;
		
		if (layers == null || layers.size() <= 0)
			throw new UnvalidElementaryTreeException("No layers are specified.");
		this.layers = layers;
		
		if (lexicalanchor == null || lexicalanchor.size() <= 0)
			throw new UnvalidElementaryTreeException("No lexical anchor is specified.");
		this.lexicalanchor = lexicalanchor;

		if (frequency < 0)
			throw new UnvalidElementaryTreeException("Frequency has to be greater than 0.");
		this.frequency = frequency;
		
		if (probability < 0 || probability > 1)
			throw new UnvalidElementaryTreeException("Probability has to be between 0 and 1.");
		this.probability = probability;
	}
	
	/**
	 * @return {@link TreeType} of this {@link ElementaryTree}
	 */
	public TreeType getType() {
		return type;
	}
	
	/**
	 * @return the index
	 */
	public long getIndex() {
		return index;
	}

	/**
	 * @return all layers of this {@link ElementaryTree}.
	 */
	public List<Layer> getLayers() {
		return layers;
	}
	
	/**
	 * @param i - position in the layer collection
	 * @return the {@link Layer} at the i-th position in the layer collection of this {@link ElementaryTree}
	 */
	public Layer getLayer(int i){
		if (this.layers.size() <= 0)
			throw new IllegalArgumentException("Rule tree hasn't any production rules.");
		if (i < 0 || i >= this.layers.size())
			throw new IllegalArgumentException("There exists no layer with such index.");
		Layer l = this.layers.get(i);
		return l;
	}
	
	/**
	 * @param address - unique Gorn-number of the layer searching for
	 * @return the {@link Layer} with the specified Gorn-number of this {@link ElementaryTree}
	 */
	public Layer getLayer(int[] address){
		for (Layer element : layers){
			if (Arrays.equals(element.gornnumber,address))
				return element;
		}
		return null;
	}
	
	/**
	 * @param layer - the parent layer in this {@link ElementaryTree}
	 * @param position - the position of the child-{@link Layer}
	 * @return the child of the given parent-{@link Layer} at the given position
	 */
	public Layer getChildLayer(Layer layer,int position){
		for (Layer element : layers){
			int end = element.gornnumber.length-1;
			int[] tmp = Arrays.copyOfRange(element.gornnumber, 0, end);
			if (Arrays.equals(tmp, layer.gornnumber) && element.gornnumber[end] == position){
				return element;
			}
		}
		return null;
	}
	
	/**
	 * @return the lexical-anchor of this {@link ElementaryTree}.
	 */
	public List<String> getLexicalAnchor(){
		return lexicalanchor;
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
	
	/**
	 * @return the nonterminal on the left side of the first production rule
	 */
	public String getRootSymbol() {
		if (this.layers.size() <= 0)
			throw new UnvalidElementaryTreeException("No layers are specified.");
		Layer l = this.layers.get(0);
		Entry e = l.entrys[0];
		if (e != null)
			return e.label;
		return null;
	}
	
	/**
	 * @return if {@link #type} of {@link ElementaryTree} is {@link TreeType#Initial} 
	 * returns the Gorn-number of {@link NodeType#LFOOT} or {@link NodeType#RFOOT} {@link Entry},
	 * else return null.
	 */
	public int[] getFootAddress(){
		if (type == TreeType.Initial)
			return null;
		for (Layer layer : layers){
			for (int i = 0 ; i < layer.entrys.length ; i++){
				if (layer.entrys[i].type == NodeType.LFOOT || layer.entrys[i].type == NodeType.RFOOT){
					return GeneralTools.AppendToIntArray(layer.gornnumber, i);
				}
			}
		}
		throw new UnvalidElementaryTreeException("A tree of type "+ type.toString() + " should have a FOOT-element.");
	}
	
	/**
	 * @return if {@link ElementaryTree} is of {@link TreeType#Initial}.
	 */
	public boolean isInitial(){
		return this.type == TreeType.Initial;
	}
	
	/**
	 * @return if {@link ElementaryTree} is of {@link TreeType#LeftAuxiliary} or {@link TreeType#RightAuxiliary}.
	 */
	public boolean isAuxiliary() {
		return this.type == TreeType.LeftAuxiliary || this.type == TreeType.RightAuxiliary;
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

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(index);
		sb.append(" ");
		for (String actstr : lexicalanchor){
			sb.append(actstr);
			sb.append(" ");
		}
		return sb.toString();
	}
}
