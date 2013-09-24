/**
 * 
 */
package grammar.buildJtigGrammar;


/**
 * An Entry of a context free rule, stored in the different {@link Layer}'s.
 * @author Fabian Gallenkamp
 */
public class Entry {

	/**
	 * Type of the Entry. Possible values, see :{@link NodeType}.
	 */
	private NodeType nodetype;
	/**
	 * Label of the Entry.
	 */
	private String label;
	
	public Entry (NodeType nodetype,String label){
		this.label = label;
		this.nodetype = nodetype;
	}
	
	public String getLabel() {
		return label;
	}
	
	public NodeType getNodeType() {
		return nodetype;
	}

	@Override
	public String toString() {
		return "(" + nodetype + ", '" + label + "')";
	}
}
