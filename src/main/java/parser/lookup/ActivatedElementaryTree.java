/**
 * 
 */
package parser.lookup;


import parser.early.Item;
import grammar.buildJtigGrammar.ElementaryTree;
import grammar.buildJtigGrammar.Entry;
import grammar.buildJtigGrammar.Layer;
import grammar.buildJtigGrammar.TreeType;

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
		super(tree.getType(),tree.getIndex(), tree.getLayers(), tree.getlexicalanchors(), tree.getFrequency(), tree.getProbability());
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
}
