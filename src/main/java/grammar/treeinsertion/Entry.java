/**
 * 
 */
package grammar.treeinsertion;


/**
 * An Entry of a context free rule, stored in the different {@link Layer}'s.
 * @author Fabian Gallenkamp
 */
public class Entry {

	/**
	 * type of the Entry. Possible values, see :{@link NodeType}.
	 */
	protected NodeType type;
	
	/**
	 * Label of the Entry.
	 */
	protected String label;
	
	public Entry (NodeType type,String label){
		this.label = label;
		this.type = type;
	}
	
	/**
	 * @return the {@link #label} of this {@link Entry}
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * @return the {@link #type} of this {@link Entry}
	 */
	public NodeType getNodeType() {
		return type;
	}
	
	
	/**
	 * Sets the {@link #type} of this {@link Entry}
	 * @param type
	 */
	public void setType(NodeType type) {
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		if (type != other.type)
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "(" + type + ", '" + label + "')";
	}


}
