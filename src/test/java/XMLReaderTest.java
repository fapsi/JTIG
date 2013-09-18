import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import parser.Parser;


public class XMLReaderTest {

	@Before
	public void setUp() throws Exception {
		System.out.println("Test environement up.");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testexample1() {
		System.out.println();
		String[] s = {"-l",System.getProperty("user.dir") + "/src/test/resources/example.xml","test"};
		Parser.main(s);
	}

}
