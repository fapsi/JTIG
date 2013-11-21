/**
 * 
 */
package grammar.derivationtree;

import parser.lookup.ActivatedElementaryTree;
import grammar.buildJtigGrammar.Layer;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class SubstitutionDerivationEdge extends DerivationEdge {



	protected SubstitutionDerivationEdge(ActivatedElementaryTree first, ActivatedElementaryTree second, int[] connector) {
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
