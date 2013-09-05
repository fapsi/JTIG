package parser;

public class Node {

	private NodeType nodetype;
	private String label;
	
	public Node (NodeType nodetype,String label){
		this.label = label;
		this.nodetype = nodetype;
	}
	
	public String getlabel() {
		return label;
	}
	public NodeType getnodeType() {
		return nodetype;
	}

	@Override
	public String toString() {
		return "Node (type=" + nodetype + ", label=" + label + ")";
	}
}
