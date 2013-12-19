/**
 * 
 */
package parser.output.derivationtree;

import java.util.Arrays;

import parser.lookup.ActivatedElementaryTree;

/**
 * 
 * @author Fabian Gallenkamp
 */
public abstract class DerivationEdge {
	
	protected ActivatedElementaryTree first;
	
	protected ActivatedElementaryTree second;
	
	protected Integer[] connector;
		
	public DerivationEdge(ActivatedElementaryTree first, ActivatedElementaryTree second,Integer[] connector){
		this.first = first;
		this.second = second;
		this.connector = connector;
	}
	
	public ActivatedElementaryTree getFirst(){
		return first;
	}
	
	public ActivatedElementaryTree getSecond(){
		return second;
	}
	
	public Integer[] getConnector(){
		return connector;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(connector);
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
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
		DerivationEdge other = (DerivationEdge) obj;
		if (!Arrays.equals(connector, other.connector))
			return false;
		if (first == null) {
			if (other.first != null)
				return false;
		} else if (!first.equals(other.first))
			return false;
		if (second == null) {
			if (other.second != null)
				return false;
		} else if (!second.equals(other.second))
			return false;
		return true;
	}
}
