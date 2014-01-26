/**
 * 
 */
package parser.lookup;


import parser.early.components.Item;
import grammar.treeinsertion.ElementaryTree;
import grammar.treeinsertion.Entry;
import grammar.treeinsertion.Layer;
import grammar.treeinsertion.TreeType;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class ActivatedElementaryTree extends ElementaryTree{
		


	/**
	 * 
	 */
	private int left;
	
	/**
	 * 
	 */
	private int right;
	
	/**
	 * 
	 * @param tree
	 * @param left
	 * @param right
	 */
	public ActivatedElementaryTree(ElementaryTree tree,int left,int right){
		super(tree.getType(),tree.getIndex(), tree.getLayers(), tree.getLexicalAnchor(), tree.getFrequency(), tree.getProbability());
		this.left = left;
		this.right = right;
	}

	public int getLeft(){
		return left;
	}
	
	public int getRight(){
		return right;
	}

	// TODO move to ElementaryTree.java
	public boolean isAdjuntionCompatible(Item item) {
		if (type != TreeType.Initial){
			ActivatedElementaryTree activatedtree = item.getActivatedElementaryTree();
			if (activatedtree.type != TreeType.Initial
					&& activatedtree.type != type){
				return ! isOnSpine(activatedtree,item.getLeftHandSide());
			}
			return true;
		}
		return false;
	}

	private boolean isOnSpine(ActivatedElementaryTree activatedtree, Entry entry) {
		for (Layer layer : layers){
			if (layer.isOnSpine() && entry.equals(layer.getEntry(0)))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + left;
		result = prime * result + right;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActivatedElementaryTree other = (ActivatedElementaryTree) obj;
		if (index != other.index)
			return false;
		if (left != other.left)
			return false;
		if (right != other.right)
			return false;
		return true;
	}	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return super.toString() + "{" + left + ","+right+"}";
	}
	
	
}
