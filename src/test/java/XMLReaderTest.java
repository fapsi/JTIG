import static org.junit.Assert.*;
import grammar.transform.lisp2xml.LispParser;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.stream.FileCacheImageInputStream;

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
	public void testexample() throws URISyntaxException {
		URL url = getClass().getResource("example.xml");
		String[] s = {"-l", url.toURI().getPath(),"test"};
		Parser.main(s);
	}
	
	@Test
	public void testexample2() throws Exception {
		URL url = getClass().getResource("example4.lisp");
		String[] s = {"-l", url.toURI().getPath(),"slowly"};
		Parser.main(s);
	}
	
	@Test
	public void testexample2_find() throws URISyntaxException {
		URL url = getClass().getResource("example2.xml");
		String[] s = {"-l", url.toURI().getPath(),"test"};
		Parser.main(s);
	}

}
