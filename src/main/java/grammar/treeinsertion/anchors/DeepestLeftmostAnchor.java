/**
 * 
 */
package grammar.treeinsertion.anchors;

import grammar.treeinsertion.NodeType;
import grammar.treeinsertion.intermediate.IRTreeNode;

import java.util.LinkedList;
import java.util.List;

/**
 * See: {@link DeepestLeftmostAnchor#getLexicalAnchors(IRTreeNode)}.
 * @author Fabian Gallenkamp
 */
public class DeepestLeftmostAnchor implements AnchorStrategy {

	/**
	 * All anchor-elements found so far.
	 */
	private LinkedList<String> anchor;
	
	/**
	 * Indicates if a terminal-chain gets interrupted.
	 */
	private boolean interruptedchain;
	
	/**
	 * Stores the current depth.
	 */
	private int lastdepth;

	/**
	 * Finds the deepest, leftmost connected chain of terminal nodes in the tree.
	 * @param root root node of the tree to find anchors in
	 * @return the anchor elements satisfying the specification
	 */	
	@Override
	public List<String> getLexicalAnchor(IRTreeNode root) {
		anchor = new LinkedList<String>();
		interruptedchain = false;
		lastdepth = -1;
		findAnchors(root);
		return anchor;
	}

	/**
	 * Traverses the tree in pre-order. 
	 * Adds term nodes to anchor list, if there aren't other leaf nodes between them.
	 * If there is a term node and there is already a term chain the anchors list, 
	 * use it if it is a deeper node than the previous ones.
	 * @param node tree root of the tree to search in.
	 */
	private void findAnchors(IRTreeNode node) {
		
		if (node.getType() == NodeType.TERM && !interruptedchain){
			anchor.add(node.getLabel());
			lastdepth = Math.max(lastdepth,node.getDepth());
		} else if (node.getType() == NodeType.TERM && (lastdepth < node.getDepth())){
				interruptedchain = false;
				lastdepth = Math.max(lastdepth,node.getDepth()); 
				anchor.clear();
				anchor.add(node.getLabel());
		} else if ((node.getType() == NodeType.SUBST 
				|| node.getType() == NodeType.LFOOT 
				|| node.getType() == NodeType.RFOOT) && anchor.size() > 0){
			interruptedchain = true;
		}
		for(IRTreeNode child : node.getChildren()){
			findAnchors(child);
		}
	}

}
