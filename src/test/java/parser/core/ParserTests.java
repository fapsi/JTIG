/**
 * 
 */
package parser.core;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import parser.early.JTIGParser;
import parser.early.run.ParseResult;
import parser.early.run.ParseRun;
import tools.GeneralTools;
import tools.tokenizer.MorphAdornoSentenceTokenizer;
import tools.tokenizer.Token;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class ParserTests {
	
	private JTIGParser parser;
	private MorphAdornoSentenceTokenizer st;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		parser = new JTIGParser("");
		st = new MorphAdornoSentenceTokenizer();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		System.out.println("Tear down.");
		File dir = new File("data/runs/");
		GeneralTools.deleteDirectory(dir);	
	}

	@Test
	public void testLexicon_234_pt2() {
		URL url_lexicon = ParserTests.class.getResource("./lexicon/234-pt2.xml");
		URL url_input_file = ParserTests.class.getResource("./input/input_234_pt2");
		try {
			JTIGParser.setProperty("grammar.lexicon.path", url_lexicon.toURI().getPath());
		} catch (URISyntaxException e) {
			fail("Couldn't find lexicon 234-pt2.xml");
		}
		
		parser.readLexicon();
		
		ParseResult run = null;
		try {
			Token[] tokens = st.getTokens(url_input_file.toURI().getPath(), 1);
			
			run = parser.parseSentence(st.getSentence(tokens),tokens);
			
			System.out.println("Read Sentence: "+st.getSentence(tokens));
		} catch (FileNotFoundException | URISyntaxException e) {
			fail(e.getMessage());
		}
		
		System.out.println(run.getLog());
		
	}

}
