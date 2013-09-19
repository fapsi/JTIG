import java.net.URISyntaxException;
import java.net.URL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import parser.early.Parser;


public class XMLReaderTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testexample1() throws URISyntaxException {
		URL url = getClass().getResource("example.xml");
		String[] s = {"-l", url.toURI().getPath(),"test"};
		Parser.main(s);
	}
	
	@Test
	public void testexample2() throws URISyntaxException {
		URL url = getClass().getResource("example2.xml");
		String[] s = {"-l", url.toURI().getPath(),"test"};
		Parser.main(s);
	}
	
	@Test
	public void testexample3() throws URISyntaxException {
		URL url = getClass().getResource("example3.xml");
		String[] s = {"-l", url.toURI().getPath(),"die"};
		Parser.main(s);
	}
	
	@Test
	public void testexample4() throws Exception {
		URL url = getClass().getResource("example4.lisp");
		String[] s = {"-l", url.toURI().getPath(),"i can not give it to you"};
		Parser.main(s);
	}
}
