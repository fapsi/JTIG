/**
 * 
 */
package grammar.tiggrammar.anchors;

import grammar.tiggrammar.intermediate.IRTreeNode;

import java.util.List;

/**
 * A Strategy to find different anchor elements for a {@link TIGRule}.
 * @author Fabian Gallenkamp
 */
public interface AnchorStrategy {
	/**
	 * 
	 * @param root
	 * @return
	 */
	public List<String> getLexicalAnchors(IRTreeNode root);
}
