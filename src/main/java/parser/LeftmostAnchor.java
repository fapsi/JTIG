/**
 * 
 */
package parser;

import java.util.List;

import xml.TreeNode;

/**
 * Concrete strategy for finding a lexical anchor. (See for Description: {@link #getlexicalanchors(TreeNode)})
 * @author Fabian Gallenkamp
 */
public class LeftmostAnchor implements AnchorStrategy {

	/**
	 * Finds the leftmost connected chain of terminal nodes in the tree.
	 * @param root root node of the tree to find anchors in
	 * @return the anchor elements satisfying the specification
	 */
	public List<Entry> getlexicalanchors(TreeNode root) {
		// TODO Auto-generated method stub
		return null;
	}

}
