/**
 * 
 */
package grammar.derivationtree;

import parser.lookup.ActivatedElementaryTree;
import grammar.buildJtigGrammar.ElementaryTree;
import grammar.buildJtigGrammar.Layer;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class SubstitutionDerivationEdge extends DerivationEdge {

	protected SubstitutionDerivationEdge(ActivatedElementaryTree first, ActivatedElementaryTree second, Layer connector) {
		super(first, second, connector);
	}
	
	

}
