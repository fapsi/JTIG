/**
 * 
 */
package parser.early;

import java.util.Arrays;
import java.util.List;

import grammar.buildJtigGrammar.Entry;
import grammar.buildJtigGrammar.ProductionRule;
import grammar.buildJtigGrammar.TIGRule;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class Item {
	
	private int left;
	
	private int right;
	
	private int completed;
	
	private ProductionRule prodrule;
	
	private TIGRule tigrule;
	
	private double prob;
	
	private int index;
	
	private List<Item> previousitems;

	public Item(int left, int right, int completed, ProductionRule prodrule,
			TIGRule tigrule, double prob, int index,
			List<Item> previousitems) {
		this.left = left;
		this.right = right;
		this.completed = completed; //TODO: maybe always 1 by construction --> always passive on construction
		this.prodrule = prodrule;
		this.tigrule = tigrule;
		this.prob = prob;
		this.index = index;
		this.previousitems = previousitems;
	}

	public boolean isActive() {
		return completed < prodrule.getEntrys().length;
	}
	
	public Entry getLeftHandSide(){
		return prodrule.getEntry(0);
	}
	
	public Entry[] getRightHandSide(){
		Entry[] entrys = prodrule.getEntrys();
		if (entrys != null && entrys.length > 1){
			return Arrays.copyOfRange(prodrule.getEntrys(), 1, entrys.length);
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
		if (index != other.index)
			return false;
		if (left != other.left)
			return false;
		if (right != other.right)
			return false;
		if (this.isActive() ^ other.isActive())
			return false;
		return true;
	}

	
}
