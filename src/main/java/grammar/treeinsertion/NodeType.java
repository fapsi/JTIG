/**
 * 
 */
package grammar.treeinsertion;

/**
 * Possible types of nodes contained in a production rule:
 * <ul><li>{@link NodeType#RFOOT}</li>
 * <li>{@link NodeType#LFOOT}</li>
 * <li>{@link NodeType#TERM}</li>
 * <li>{@link NodeType#SUBST}</li>
 * <li>{@link NodeType#NONTERM}</li>
 * <li>{@link NodeType#EPS}</li></ul>
 * 
 * @author Fabian Gallenkamp
 */
public enum NodeType {
	RFOOT , LFOOT , TERM , SUBST , NONTERM , EPS
}
