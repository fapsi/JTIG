/**
 * 
 */
package grammar.treeinsertion.intermediate;

import grammar.treeinsertion.ElementaryTree;
import grammar.treeinsertion.Entry;
import grammar.treeinsertion.Layer;
import grammar.treeinsertion.NodeType;
import grammar.treeinsertion.TreeType;
import grammar.treeinsertion.anchors.AnchorStrategy;
import grammar.treeinsertion.exceptions.UnvalidElementaryTreeException;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

import tools.GeneralTools;

/**
 * Tree structure, needed for parsing efficiently a grammar rule tree in the xml-format.
 * @author Fabian Gallenkamp
 */

public class IRTreeNode {

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
	private IRTreeNode parent;
	
	/**
	 * Children 
	 */
	private List<IRTreeNode> children;
	
	/**
	 * 
	 */
	private int depth;
	
	/**
	 * Creates a tree-node with a parent node and a specific type
	 * @param parent the parent for this tree node, NULL for root.
	 * @param label a label for this tree node
	 */
	public IRTreeNode(IRTreeNode parent,NodeType type, String label, int depth){
		this.parent = parent;
		this.type = type;
		this.children = new LinkedList<IRTreeNode>();
		this.label = label;
		this.depth = depth;
	}
	
	/**
	 * Adds a child to tree-node.
	 * @param child the child to be added
	 */
	public void addChild(IRTreeNode child){
		this.children.add(child);
	}
	
	/**
	 * @return the children of this node
	 */
	public List<IRTreeNode> getChildren(){
		return children;
	}
	
	/**
	 * 
	 * @return if tree has children
	 */
	public boolean hasChildren(){
		return !this.children.isEmpty();
	}
	
	/**
	 * 
	 * @return the parent of this tree node
	 */
	public IRTreeNode getParent(){
		return this.parent;
	}
	
	/**
	 * 
	 * @return label of this node
	 */
	public String getLabel(){
		return this.label;
	}
	
	/**
	 * 
	 * @return type of this node.
	 */
	public NodeType getType() {
		return this.type;
	}
	
	/**
	 * @return the depth of this node in the tree.
	 */
	public int getDepth() {
		return this.depth;
	}

	public ElementaryTree convertToElementaryTree(long index,long treefreq,double prob,AnchorStrategy strategy){
		List<Layer> layers = new LinkedList<Layer>();
		Stack<Integer> gornnumbers = new Stack<Integer>();
		gornnumbers.push(new Integer(0));
		
		TreeType treetype = getTreeType();
		
		IRTreeInformation information = new IRTreeInformation(treetype);
		extractLayers(information,layers,gornnumbers);
				
		return new ElementaryTree(treetype,index, layers, 
				strategy.getLexicalAnchor(this), 
				treefreq, prob);
	}
	
	private TreeType getTreeType() {
		if (!hasChildren()){
			switch (type){
			case LFOOT:
				return TreeType.LeftAuxiliary;
			case RFOOT:
				return TreeType.RightAuxiliary;
			default:
				return null;
			}
		} else {
			for (IRTreeNode n: children){
				TreeType treetype = n.getTreeType();
				if (treetype != null)
					return treetype;
			}
			if (parent == null)
				return TreeType.Initial;
			else
				return null;
		}	
	}

	/**
	 * 
	 * @param layers
	 * @param gornnumbers
	 * @param spine
	 */
	private void extractLayers(IRTreeInformation information,List<Layer> layers, Stack<Integer> gornnumbers){
		if (hasChildren()){
			if (type != NodeType.NONTERM)
				throw new UnvalidElementaryTreeException("Inner-nodes must have type 'NONTERM'.");
			// Add 0th element of CFG Rule
			List<Entry> ruleentrys = new LinkedList<Entry>();
			ruleentrys.add(new Entry(this.type,this.label));
			
			// Add other elemnts of cfg rule
			for (IRTreeNode tn : this.children){
				ruleentrys.add(new Entry(tn.getType(),tn.getLabel()));
			}
			// Create layer out of rules and gornnumber and add to list
			Entry[] layernodes = ruleentrys.toArray(new Entry[0]);
			int[] layergornnumber = GeneralTools.ListToIntArray(gornnumbers);
			
			layers.add(new Layer(layergornnumber,layernodes,information.getActualOnSpine()));
			
			//make recursive calls either from left to right or otherwise
			if (information.getTreeType() != TreeType.LeftAuxiliary){
				int i = 1;
				for (IRTreeNode tn : this.children){
					gornnumbers.push(new Integer(i));
					tn.extractLayers(information,layers, gornnumbers);
					gornnumbers.pop();
					i++;
				}
			} else {
				int i = this.children.size();
				ListIterator<IRTreeNode> iterator = this.children.listIterator(i);
				while (iterator.hasPrevious()){
					gornnumbers.push(new Integer(i));
					iterator.previous().extractLayers(information,layers, gornnumbers);
					gornnumbers.pop();
					i--;
				}
			}
		} else {
			//find auxiliary tree foot and flip actual on spine information
			if (type == NodeType.RFOOT || type == NodeType.LFOOT){
				information.setActualOnSpine(!information.getActualOnSpine());
			} else {
				if (type == NodeType.NONTERM)
					throw new UnvalidElementaryTreeException("No leaf-nodes of type 'NONTERM' allowed.");
				if (information.getActualOnSpine() && type == NodeType.TERM)
					throw new UnvalidElementaryTreeException("Nodes lying beneath spine must be dominated by epsilon."
							+ "Tree type: " + information.treetype.toString());
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
