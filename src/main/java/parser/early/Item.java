/**
 * 
 */
package parser.early;

import java.util.List;

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
	
	private ItemStatus status;
	
	private double prob;
	
	private int index;
	
	private List<Item> previousitems;

	public Item(int left, int right, int completed, ProductionRule prodrule,
			TIGRule tigrule, ItemStatus status, double prob, int index,
			List<Item> previousitems) {
		this.left = left;
		this.right = right;
		this.completed = completed;
		this.prodrule = prodrule;
		this.tigrule = tigrule;
		this.status = status;
		this.prob = prob;
		this.index = index;
		this.previousitems = previousitems;
	}

	public boolean isactive() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
		result = prime * result + left;
		result = prime * result + right;
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		if (status != other.status)
			return false;
		return true;
	}

	
}
