package tools;
import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import tools.GeneralTools;

/**
 * 
 */

/**
 * 
 * @author Fabian Gallenkamp
 */
public class GeneralToolsTest {

	@Test
	public void test() {
		List<Integer> l = new LinkedList<Integer>();
		l.add(new Integer(12));
		l.add(new Integer(1));
		l.add(new Integer(12));
		l.add(new Integer(4));
		int [] actuals =  GeneralTools.ListToIntArray(l);
		int[] expecteds = new int[]{12,1,12,4};
		assertArrayEquals(expecteds, actuals);
		
	}

}
