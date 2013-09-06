/**
 * 
 */
package parser;

import java.util.List;
import xml.TreeNode;

/**
 * A Strategy to find different anchor elements for a {@link RuleTree}.
 * @author Fabian Gallenkamp
 */
public interface AnchorStrategy {
	/**
	 * 
	 * @param root
	 * @return
	 */
	public List<Entry> getlexicalanchors(TreeNode root);
}
