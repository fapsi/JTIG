/**
 * 
 */
package lexicon.valid;

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
public class LexiconValidTest {

		@Test
		public void testAllValidLexicon() {
			JTIGParser parser = null;
			try {
				parser = new JTIGParser("");
			} catch (Exception e) {
				fail("Parser couldn't be created.");
			}
			URL url = LexiconValidTest.class.getResource("./");
			File dir = null;
			try {
				dir = new File(url.toURI().getPath());
			} catch (URISyntaxException e) {
				fail("Couldn't read directory with valid lexicons.");
			}
			for (File nextFile : dir.listFiles()) {
				if (nextFile.isFile() && (nextFile.getName().endsWith(".xml") || nextFile.getName().endsWith(".lisp"))){
					System.out.println(nextFile.getName());
					JTIGParser.setProperty("grammar.lexicon.path", nextFile.getAbsolutePath());
					if (!parser.readLexicon()){
						System.out.println(parser.getLastError());
						fail("The lexicon '"+nextFile.getName()+"' is valid, but not detected.");
					}
					
				}
			}
		}

}
