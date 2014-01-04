/**
 * 
 */
package lexicon.unvalid;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Test;

import parser.early.JTIGParser;
/**
 * 
 * @author Fabian Gallenkamp
 */
public class LexiconUnvalidTest {

	@Test
	public void testAllUnvalidLexicon() {
		JTIGParser parser = null;
		try {
			parser = new JTIGParser("");
		} catch (Exception e) {
			fail("Parser couldn't be created.");
		}
		URL url = LexiconUnvalidTest.class.getResource("./");
		File dir = null;
		try {
			dir = new File(url.toURI().getPath());
		} catch (URISyntaxException e) {
			fail("Couldn't read directory with unvalid lexicons.");
		}
		for (File nextFile : dir.listFiles()) {
			if (nextFile.isFile() && (nextFile.getName().endsWith(".xml") || nextFile.getName().endsWith(".lisp"))){
				JTIGParser.setProperty("grammar.lexicon.path", nextFile.getAbsolutePath());
				if (parser.readLexicon()){
					fail("The lexicon '"+nextFile.getName()+"' is unvalid, but not detected.");
				}
			}
		}
	}

}
