/**
 * 
 */
package parser.lookup;

import grammar.buildJtigGrammar.TIGRule;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class ActivatedTIGRule extends TIGRule{
		
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
	public ActivatedTIGRule(TIGRule tree,int left,int right){
		super(tree.getIndex(), tree.getLayers(), tree.getlexicalanchors(), tree.getFrequency(), tree.getProbability(), tree.getSpine());
		this.left = left;
		this.right = right;
	}

	public int getLeft(){
		return left;
	}
	
	public int getRight(){
		return right;
	}
	
}
