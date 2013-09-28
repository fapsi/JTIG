/**
 * 
 */
package grammar.readXML;

import grammar.buildJtigGrammar.DeepestLeftmostAnchor;
import grammar.buildJtigGrammar.Lexicon;
import grammar.buildJtigGrammar.TIGRule;
import grammar.transform.lisp2xml.LispParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import parser.early.JTIGParser;

/**
 * Parses all lexicalised {@link TIGRule}'s for a tree insertion grammar from a given XML input file. 
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
							+ e.getLocalizedMessage());
		}
	}
	/**
	 * Reads the XML-file with a {@link SAXParser}.
	 * Main work is done by the {@link XMLHandler}.
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public Lexicon read() throws SAXException, IOException, ParserConfigurationException {
		Lexicon lexicon = null;

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
			
			lexicon = handler.getLexicon();
			time1 = System.currentTimeMillis() - time1;
			
			if (JTIGParser.getProperties().getProperty("debug").equals("true"))
				System.out.println("Read "+lexicon.size()+" grammar rule trees from XML-file. ("+time1+" ms)");
			

		return lexicon;
	}
}
