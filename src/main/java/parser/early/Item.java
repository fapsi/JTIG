/**
 * 
 */
package parser.early;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
	
	private TIGRule tigrule;
	
	private double prob;
	
	private int index;
	
	private Set<ItemDerivation> derivations;

	public Item(int left, int right, int dotposition, Layer layer,
			TIGRule tigrule, double prob, int index) {
		this.left = left;
		this.right = right;
		this.dotposition = dotposition; //TODO: maybe always 1 by construction --> always active on construction
		this.layer = layer;
		//TODO maybe the activated tig rule???
		this.tigrule = tigrule;
		this.prob = prob;
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
	
	public TIGRule getTIGRule(){
		return tigrule;
	}
	
	public int getLeft(){
		return left;
	}
	
	public int getRight(){
		return right;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
		result = prime * result + left;
		result = prime * result + right;
		result = prime * result + (isActive()?1:0);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (!tigrule.equals(other.tigrule))
			return false;
		if (layer != other.layer) // TODO equals method in layer
			return false;
		if (dotposition != other.dotposition)
			return false;
		//if (this.isActive() ^ other.isActive())
		//	return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Item [dotposition=" + dotposition + ", tigrule=" + tigrule
				+ ", index=" + index + "]";
	}

	
}
