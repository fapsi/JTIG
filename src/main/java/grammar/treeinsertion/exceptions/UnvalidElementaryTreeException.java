/**
 * 
 */
package grammar.treeinsertion.exceptions;

import grammar.treeinsertion.ElementaryTree;

import java.io.Serializable;

/**
 * This exception is thrown, if the {@link ElementaryTree} isn't well formed. Possibilities:
 * <ul>
 * <li>Tree has type null.</li>
 * <li>Index has to be greater than 0.</li>
 * <li>No layers are specified.</li>
 * <li>No lexical-anchor is specified.</li>
 * <li>Frequency has to be greater than 0.</li>
 * <li>Probability has to be between 0 and 1.</li>
 * <li>A tree of type 'X' should have a FOOT-element.</li>
 * </ul>
 * @author Fabian Gallenkamp
 */
public class UnvalidElementaryTreeException extends IllegalArgumentException {

	/**
	 * Exceptions are {@link Serializable}.
	 */
	private static final long serialVersionUID = 1889140538025392649L;

	/**
	 * @param string - reason
	 */
	public UnvalidElementaryTreeException(String string) {
		super(string);
	}
}
