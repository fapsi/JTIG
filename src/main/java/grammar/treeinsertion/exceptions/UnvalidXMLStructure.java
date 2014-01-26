/**
 * 
 */
package grammar.treeinsertion.exceptions;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class UnvalidXMLStructure extends IllegalArgumentException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8193245525816709873L;
	
	public UnvalidXMLStructure(String string) {
		super(string);
	}
}
