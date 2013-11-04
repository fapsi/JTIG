/**
 * 
 */
package grammar.buildJtigGrammar;

import grammar.readXML.TreeNode;

import java.util.LinkedList;
import java.util.List;

/**
 * Concrete strategy for finding a lexical anchor. (See for Description: {@link #getlexicalanchors(TreeNode)})
 * @author Fabian Gallenkamp
 */
public class LeftmostAnchor implements AnchorStrategy {

	private List<String> anchors;
	
	private boolean interruptedchain;
	
	/**
	 * Finds the leftmost connected chain of terminal nodes in the tree.
	 * @param root root node of the tree to find anchors in
	 * @return the anchor elements satisfying the specification
	 */	
	public List<String> getlexicalanchors(TreeNode root) {
		anchors = new LinkedList<String>();
		interruptedchain = false;
		findanchors(root);
		return anchors;
	}
	/**
	 * Traverses the tree in pre-order. Adds TERM nodes to anchor list, if there arn't other leaf nodes between them.
	 * @param node tree root of the tree to search in.
	 */
	private void findanchors(TreeNode node){
		
		if (node.getType() == NodeType.TERM && !interruptedchain){
			anchors.add(node.getLabel());
		} else if ((node.getType() == NodeType.SUBST 
				|| node.getType() == NodeType.LFOOT 
				|| node.getType() == NodeType.RFOOT) && anchors.size() > 0){
			interruptedchain = true;
		}
			
		for(TreeNode child : node.getChildren()){
			findanchors(child);
		}
	}

}
