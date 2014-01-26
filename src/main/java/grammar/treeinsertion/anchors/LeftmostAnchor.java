/**
 * 
 */
package grammar.treeinsertion.anchors;

import grammar.treeinsertion.NodeType;
import grammar.treeinsertion.intermediate.IRTreeNode;

import java.util.LinkedList;
import java.util.List;

/**
 * Concrete strategy for finding a lexical anchor. (See for Description: {@link #getlexicalanchors(IRTreeNode)})
 * @author Fabian Gallenkamp
 */
public class LeftmostAnchor implements AnchorStrategy {
	
	/**
	 * All anchor-elements found so far.
	 */
	private List<String> anchor;
	
	private boolean interruptedchain;
	
	/**
	 * Finds the leftmost connected chain of terminal nodes in the tree.
	 * @param root root node of the tree to find anchors in
	 * @return the anchor elements satisfying the specification
	 */	
	public List<String> getLexicalAnchor(IRTreeNode root) {
		anchor = new LinkedList<String>();
		interruptedchain = false;
		findAnchor(root);
		return anchor;
	}
	/**
	 * Traverses the tree in pre-order. Adds TERM nodes to anchor list, if there arn't other leaf nodes between them.
	 * @param node tree root of the tree to search in.
	 */
	private void findAnchor(IRTreeNode node){
		
		if (node.getType() == NodeType.TERM && !interruptedchain){
			anchor.add(node.getLabel());
		} else if ((node.getType() == NodeType.SUBST 
				|| node.getType() == NodeType.LFOOT 
				|| node.getType() == NodeType.RFOOT) && anchor.size() > 0){
			interruptedchain = true;
		}
			
		for(IRTreeNode child : node.getChildren()){
			findAnchor(child);
		}
	}

}
