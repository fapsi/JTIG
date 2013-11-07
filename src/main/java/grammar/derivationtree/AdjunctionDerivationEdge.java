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
public class AdjunctionDerivationEdge extends DerivationEdge {

	protected AdjunctionDerivationEdge(ActivatedElementaryTree first, ActivatedElementaryTree second ,Layer connector) {
		super(first, second, connector);
		
	}

}
