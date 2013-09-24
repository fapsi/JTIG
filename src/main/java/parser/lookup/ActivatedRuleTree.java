/**
 * 
 */
package parser.lookup;

import grammar.buildJtigGrammar.TIGRule;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class ActivatedRuleTree {
	
	/**
	 * 
	 */
	private TIGRule tree;
	
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
	public ActivatedRuleTree(TIGRule tree,int left,int right){
		this.tree = tree;
		this.left = left;
		this.right = right;
	}

	@Override
	public String toString() {
		return "(" + tree + ", left=" + left
				+ ", right=" + right + ")";
	}
	
	
}
