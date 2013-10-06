/**
 * 
 */
package parser.early;

/**
 * 
 * @author Fabian Gallenkamp
 */
public interface Filter<T> {
	
	public boolean apply(T item);

}
