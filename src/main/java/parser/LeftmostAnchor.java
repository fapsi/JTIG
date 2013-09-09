/**
 * 
 */
package parser;

import java.util.LinkedList;
import java.util.List;

import xml.NodeType;
import xml.TreeNode;

/**
 * Concrete strategy for finding a lexical anchor. (See for Description: {@link #getlexicalanchors(TreeNode)})
 * @author Fabian Gallenkamp
 */
public class LeftmostAnchor implements AnchorStrategy {

	private List<Entry> anchors;
	
	private boolean interruptedchain;
	
	/**
	 * Finds the leftmost connected chain of terminal nodes in the tree.
	 * @param root root node of the tree to find anchors in
	 * @return the anchor elements satisfying the specification
	 */	
	public List<Entry> getlexicalanchors(TreeNode root) {
		anchors = new LinkedList<Entry>();
		interruptedchain = false;
		findanchors(root);
		return anchors;
	}
	/**
	 * Traverses the tree in pre-order. Adds TERM nodes to anchor list, if there arn't other leaf nodes between them.
	 * @param node tree root of the tree to search in.
	 */
	private void findanchors(TreeNode node){
		if (node.gettype() == NodeType.TERM && !interruptedchain){
			anchors.add(new Entry(node.gettype(),node.getlabel()));
		} else if ((node.gettype() == NodeType.SUBST 
				|| node.gettype() == NodeType.LFOOT 
				|| node.gettype() == NodeType.RFOOT) && anchors.size() > 0){
			interruptedchain = true;
		}
			
		for(TreeNode child : node.getchildren()){
			findanchors(child);
		}
	}

}
