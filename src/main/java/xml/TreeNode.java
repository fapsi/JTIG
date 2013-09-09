/**
 * 
 */
package xml;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import parser.AnchorStrategy;
import parser.Layer;
import parser.Entry;
import parser.RuleTree;

/**
 * Tree structure, needed for parsing efficiently a grammar rule tree in the xml-format.
 * @author Fabian Gallenkamp
 */

public class TreeNode {

	/**
	 * Node type of this node. 
	 */
	private NodeType type;
	
	/**
	 * Label of this node.
	 */
	private String label;
	/**
	 * The parent for this tree node, NULL for root.
	 */
	private TreeNode parent;
	
	/**
	 * Children 
	 */
	private List<TreeNode> children;
	
	/**
	 * Creates a tree-node with a parent node and a specific type
	 * @param parent the parent for this tree node, NULL for root.
	 * @param label a label for this tree node
	 */
	public TreeNode(TreeNode parent,NodeType type, String label){
		this.parent = parent;
		this.type = type;
		this.children = new LinkedList<TreeNode>();
		this.label = label;
	}
	
	/**
	 * Adds a child to tree-node.
	 * @param child the child to be added
	 */
	public void addchild(TreeNode child){
		this.children.add(child);
	}
	
	/**
	 * @return the children of this node
	 */
	public List<TreeNode> getchildren(){
		return children;
	}
	
	/**
	 * 
	 * @return if tree has children
	 */
	public boolean haschild(){
		return !this.children.isEmpty();
	}
	
	/**
	 * 
	 * @return the parent of this tree node
	 */
	public TreeNode getparent(){
		return this.parent;
	}
	
	/**
	 * 
	 * @return label of this node
	 */
	public String getlabel(){
		return this.label;
	}
	
	/**
	 * 
	 * @return type of this node.
	 */
	public NodeType gettype() {
		return this.type;
	}

	public RuleTree converttoruletree(long index,long treefreq,double prob,AnchorStrategy strategy){
		List<Layer> layers = new LinkedList<Layer>();
		Stack<Integer> gornnumbers = new Stack<Integer>();
		Stack<Integer> spine = new Stack<Integer>();
		gornnumbers.push(new Integer(0));
		
		parseLayers(layers,gornnumbers,spine);
		
		return new RuleTree(index, layers, 
				strategy.getlexicalanchors(this), 
				treefreq, prob, spine.empty()?null:spine.toArray(new Integer[0]));
	}
	/**
	 * 
	 * @param layers
	 * @param gornnumbers
	 * @param spine
	 */
	private void parseLayers(List<Layer> layers, Stack<Integer> gornnumbers, Stack<Integer> spine){
		if (haschild()){
			
			// Add 0th element of CFG Rule
			List<Entry> ruleentrys = new LinkedList<Entry>();
			ruleentrys.add(new Entry(this.type,this.label));
			
			// Add other elemnts of cfg rule
			for (TreeNode tn : this.children){
				ruleentrys.add(new Entry(tn.gettype(),tn.getlabel()));
			}
			// Create layer out of rules and gornnumber and add to list
			Entry[] layernodes = ruleentrys.toArray(new Entry[0]);
			Integer[] layergornnumber = gornnumbers.toArray(new Integer[0]);
			layers.add(new Layer(layergornnumber,layernodes));
						
			int i = 1;
			//make recursive calls
			for (TreeNode tn : this.children){
				gornnumbers.push(new Integer(i));
				tn.parseLayers(layers, gornnumbers, spine);
				gornnumbers.pop();
				i++;
			}
		} else {
			//find spine and add
			if (type == NodeType.RFOOT || type == NodeType.LFOOT){
				spine.addAll(gornnumbers);
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("TreeNode [type=" + type + ", label=" + label + ", children="+ children+"]");
		return sb.toString();
	}

}
