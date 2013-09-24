/**
 * 
 */
package grammar.buildJtigGrammar;

import grammar.readXML.TreeNode;

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
	public List<String> getlexicalanchors(TreeNode root);
}
