/**
 * 
 */
package parser.derivationtree;

import parser.lookup.ActivatedElementaryTree;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class SubstitutionDerivationEdge extends DerivationEdge {



	protected SubstitutionDerivationEdge(ActivatedElementaryTree first, ActivatedElementaryTree second, Integer[] connector) {
		super(first, second, connector);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SubstitutionDerivationEdge [first=" + first + ", second="
				+ second + ", connector=" + connector + "]";
	}

}
