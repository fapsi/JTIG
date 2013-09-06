/**
 * 
 */
package parser;

import xml.NodeType;

/**
 * 
 * 
 * @author Fabian Gallenkamp
 */
public class Entry {

	private NodeType nodetype;
	
	private String label;
	
	public Entry (NodeType nodetype,String label){
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
