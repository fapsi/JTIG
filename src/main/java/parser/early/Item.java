/**
 * 
 */
package parser.early;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import parser.lookup.ActivatedTIGRule;
import grammar.buildJtigGrammar.Entry;
import grammar.buildJtigGrammar.Layer;
import grammar.buildJtigGrammar.TIGRule;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class Item {
	
	private int left;
	
	private int right;
	
	private int dotposition;
	
	private Layer layer;
	
	private ActivatedTIGRule activatedruletree;
	
	private double probability;
	
	private int index;
	
	private Set<ItemDerivation> derivations;

	public Item(int left, int right, int dotposition, Layer layer,
			ActivatedTIGRule activatedruletree, double probability, int index) {
		this.left = left;
		this.right = right;
		this.dotposition = dotposition; //TODO: maybe always 1 by construction --> always active on construction
		this.layer = layer;
		this.activatedruletree = activatedruletree;
		this.probability = probability;
		this.index = index;

		this.derivations = new HashSet<ItemDerivation>();
	}

	public void addDerivation(){
		
	}
	
	public void moveDotLeft(){
		if (isActive())
			++dotposition;
		return;
	}
	
	public boolean isActive() {
		return dotposition < layer.getEntrys().length;
	}
	
	public boolean isPassive(){
		return !isActive();
	}
	
	public Entry getLeftHandSide(){
		return layer.getEntry(0);
	}
	
	public Entry[] getRightHandSide(){
		Entry[] entrys = layer.getEntrys();
		if (entrys != null && entrys.length > 1){
			return Arrays.copyOfRange(layer.getEntrys(), 1, entrys.length);
		}
		return null;
	}
	
	public ActivatedTIGRule getActivatedTIGRule(){
		return activatedruletree;
	}
	
	public int getLeft(){
		return left;
	}
	
	public int getRight(){
		return right;
	}
	// Probability
	public int getProbability(){
		return right;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((activatedruletree == null) ? 0 : activatedruletree
						.hashCode());
		result = prime * result + dotposition;
		result = prime * result + ((layer == null) ? 0 : layer.hashCode());
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
		Item other = (Item) obj;
		if (activatedruletree == null) {
			if (other.activatedruletree != null)
				return false;
		} else if (!activatedruletree.equals(other.activatedruletree))
			return false;
		if (dotposition != other.dotposition)
			return false;
		if (layer == null) {
			if (other.layer != null)
				return false;
		} else if (!layer.equals(other.layer))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Item [dotposition=" + dotposition + ", tigrule=" + activatedruletree
				+ ", index=" + index + "]";
	}

	
}
