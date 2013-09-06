/**
 * 
 */
package xml;

import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import parser.LeftmostAnchor;
import parser.RuleTree;
import lisp.LispParser;

/**
 * Parses all lexicalised {@link RuleTree}'s for a tree insertion grammar from a given XML input file. 
 * @author Fabian Gallenkamp
 */
public class XMLParser {

	/**
	 * Location from the input file
	 */
	private String inputpath;
	
	/**
	 * 
	 * @param inputpath
	 */
	public XMLParser(String inputpath) {
		this.inputpath = inputpath;
		// if the file is in Lisp Format, transform to XML
		if (LispParser.isLispFormat(inputpath))
			transformtoXML();
	}
	/**
	 * If the input file ends with ".lisp" the lisp format is transformed into an xml file.
	 * See also {@link lisp.LispParser}
	 */
	private void transformtoXML() {
		String newinputpath = this.inputpath.replaceAll("\\.lisp", ".xml");
		LispParser lp = new LispParser(this.inputpath, newinputpath);
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
	 * Parses the XML-file with a {@link SAXParser}.
	 * Main work is done by the {@link XMLHandler}.
	 */
	public void parse() {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			
			XMLHandler handler = new XMLHandler(new LeftmostAnchor());
			saxParser.parse(inputpath, handler);
			System.out.println("XML-File successfully parsed.");
			
			List<RuleTree> rules = handler.getruletrees();
			System.out.println(rules);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		XMLParser xp = new XMLParser("/home/fapsi/example2.lisp");
		xp.parse();
	}
}
