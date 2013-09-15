/**
 * 
 */
package grammar.buildJtigGrammar;

import grammar.readXML.TreeNode;

import java.util.LinkedList;
import java.util.List;

/**
 *  
 * @author Fabian Gallenkamp
 */
public class DeepestLeftmostAnchor implements AnchorStrategy {

	private LinkedList<String> anchors;
	private boolean interruptedchain;
	private int lastdepth;

	/**
	 * Finds the deepest, leftmost connected chain of terminal nodes in the tree.
	 * @param root root node of the tree to find anchors in
	 * @return the anchor elements satisfying the specification
	 */	
	@Override
	public List<String> getlexicalanchors(TreeNode root) {
		anchors = new LinkedList<String>();
		interruptedchain = false;
		lastdepth = -1;
		findanchors(root);
		return anchors;
	}

	/**
	 * Traverses the tree in pre-order. 
	 * Adds term nodes to anchor list, if there aren't other leaf nodes between them.
	 * If there is a term node and there is already a term chain the anchors list, 
	 * use it if it is a deeper node than the previous ones.
	 * @param node tree root of the tree to search in.
	 */
	private void findanchors(TreeNode node) {
		
		if (node.gettype() == NodeType.TERM && !interruptedchain){
			anchors.add(node.getlabel());
			lastdepth = Math.max(lastdepth,node.getdepth());
		} else if (node.gettype() == NodeType.TERM && (lastdepth < node.getdepth())){
				interruptedchain = false;
				lastdepth = Math.max(lastdepth,node.getdepth()); 
				anchors.clear();
				anchors.add(node.getlabel());
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
