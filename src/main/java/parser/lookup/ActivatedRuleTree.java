/**
 * 
 */
package parser.lookup;

import grammar.buildJtigGrammar.RuleTree;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class ActivatedRuleTree {
	
	/**
	 * 
	 */
	private RuleTree tree;
	
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
	public ActivatedRuleTree(RuleTree tree,int left,int right){
		this.tree = tree;
		this.left = left;
		this.right = right;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "(" + tree + ", left=" + left
				+ ", right=" + right + ")";
	}
	
	
}
