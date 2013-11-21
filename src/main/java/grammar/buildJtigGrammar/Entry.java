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
	protected NodeType nodetype;
	/**
	 * Label of the Entry.
	 */
	protected String label;
	
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result
				+ ((nodetype == null) ? 0 : nodetype.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entry other = (Entry) obj;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (nodetype != other.nodetype)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "(" + nodetype + ", '" + label + "')";
	}
}
