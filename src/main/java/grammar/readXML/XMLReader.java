/**
 * 
 */
package grammar.readXML;

import grammar.buildJtigGrammar.DeepestLeftmostAnchor;
import grammar.buildJtigGrammar.LeftmostAnchor;
import grammar.buildJtigGrammar.Lexicon;
import grammar.buildJtigGrammar.RuleTree;
import grammar.transform.lisp2xml.LispParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;

/**
 * Parses all lexicalised {@link RuleTree}'s for a tree insertion grammar from a given XML input file. 
 * @author Fabian Gallenkamp
 */
public class XMLReader {

	/**
	 * Location from the input file
	 */
	private String inputpath;
	
	/**
	 * 
	 * @param inputpath
	 */
	public XMLReader(String inputpath) {
		this.inputpath = inputpath;
		// if the file is in Lisp Format, transform to XML
		if (LispParser.isLispFormat(inputpath))
			transformtoXML();
	}
	/**
	 * If the input file ends with ".lisp" the lisp format is transformed into an xml file.
	 * See also {@link grammar.transform.lisp2xml.LispParser}
	 */
	private void transformtoXML() {
		String newinputpath = this.inputpath.replaceAll("\\.lisp", ".xml");
		LispParser lp;
		try {
			lp = new LispParser(this.inputpath, newinputpath);
		} catch (IOException e) {
			throw new IllegalArgumentException(
					"Couldn't read file "
							+ e.getMessage());
		}
		this.inputpath = newinputpath;
		try {
			lp.parse();
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"Couldn't transform LISP-format into XML-format. "
							+ e.getMessage());
		}
	}
	/**
	 * Reads the XML-file with a {@link SAXParser}.
	 * Main work is done by the {@link XMLHandler}.
	 */
	public Lexicon read() {
		Lexicon lexicon = null;
		try {
			long time1 = System.currentTimeMillis();
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			XMLHandler handler = new XMLHandler(new DeepestLeftmostAnchor());
	   	    File file = new File(inputpath);
    	    InputStream inputStream= new FileInputStream(file);
    	    Reader reader = new InputStreamReader(inputStream,"UTF-8");
    	    InputSource is = new InputSource(reader);
    	    is.setEncoding("UTF-8");
			saxParser.parse(is, handler);
			
			lexicon = handler.getruletrees();
			time1 = System.currentTimeMillis() - time1;
			
			System.out.println(lexicon.toString());
			System.out.println("Parsed "+lexicon.size()+" rules from XML-file. ("+time1+" ms)");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return lexicon;
	}
}
