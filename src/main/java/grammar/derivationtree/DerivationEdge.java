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
public abstract class DerivationEdge {
	
	protected ActivatedElementaryTree first;
	
	protected ActivatedElementaryTree second;
	
	protected Layer connector;
		
	public DerivationEdge(ActivatedElementaryTree first, ActivatedElementaryTree second,Layer connector){
		this.first = first;
		this.second = second;
		this.connector = connector;
	}
	
}
