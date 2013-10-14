/**
 * 
 */
package grammar.readXML;

import grammar.buildJtigGrammar.AnchorStrategy;
import grammar.buildJtigGrammar.Entry;
import grammar.buildJtigGrammar.Layer;
import grammar.buildJtigGrammar.NodeType;
import grammar.buildJtigGrammar.TIGRule;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import tools.GeneralTools;

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
	 * 
	 */
	private int depth;
	
	/**
	 * Creates a tree-node with a parent node and a specific type
	 * @param parent the parent for this tree node, NULL for root.
	 * @param label a label for this tree node
	 */
	public TreeNode(TreeNode parent,NodeType type, String label, int depth){
		this.parent = parent;
		this.type = type;
		this.children = new LinkedList<TreeNode>();
		this.label = label;
		this.depth = depth;
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
	
	/**
	 * @return the depth of this node in the tree.
	 */
	public int getdepth() {
		return this.depth;
	}

	public TIGRule converttoruletree(long index,long treefreq,double prob,AnchorStrategy strategy){
		List<Layer> layers = new LinkedList<Layer>();
		Stack<Integer> gornnumbers = new Stack<Integer>();
		Stack<Integer> spine = new Stack<Integer>();
		gornnumbers.push(new Integer(0));
		
		extractlayers(layers,gornnumbers,spine);
		
		return new TIGRule(index, layers, 
				strategy.getlexicalanchors(this), 
				treefreq, prob, spine.empty()?null:GeneralTools.ListToIntArray(spine));
	}
	/**
	 * 
	 * @param layers
	 * @param gornnumbers
	 * @param spine
	 */
	private void extractlayers(List<Layer> layers, Stack<Integer> gornnumbers, Stack<Integer> spine){
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
			int[] layergornnumber = GeneralTools.ListToIntArray(gornnumbers);
			
			layers.add(new Layer(layergornnumber,layernodes));
						
			int i = 1;
			//make recursive calls
			for (TreeNode tn : this.children){
				gornnumbers.push(new Integer(i));
				tn.extractlayers(layers, gornnumbers, spine);
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
