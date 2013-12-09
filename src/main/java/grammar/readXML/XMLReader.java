/**
 * 
 */
package grammar.readXML;

import grammar.buildjtiggrammar.AnchorStrategy;
import grammar.buildjtiggrammar.DeepestLeftmostAnchor;
import grammar.buildjtiggrammar.Lexicon;
import grammar.transform.lisp2xml.LispParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLStreamException;

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
	 * @throws FileNotFoundException 
	 */
	public XMLReader(String inputpath) {
		this.inputpath = inputpath;
	}
	/**
	 * If the input file ends with ".lisp" the lisp format is transformed into an xml file.
	 * See also {@link grammar.transform.lisp2xml.LispParser}
	 * @throws FileNotFoundException 
	 * @throws XMLStreamException 
	 */
	private void transformtoXML() throws FileNotFoundException, XMLStreamException {
		String newinputpath = this.inputpath.replaceAll("\\.lisp", ".xml");
		LispParser lp;
		try {
			lp = new LispParser(this.inputpath, newinputpath);
		} catch (IOException e) {
			throw new FileNotFoundException(
					"Couldn't read file "
							+ e.getMessage());
		}
		this.inputpath = newinputpath;
		lp.parse();
	}
	/**
	 * Reads the XML-file with a {@link SAXParser}.
	 * Main work is done by the {@link XMLHandler}.
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws XMLStreamException 
	 */
	public Lexicon read() throws SAXException, IOException, ParserConfigurationException, XMLStreamException {
		// if the file is in Lisp Format, transform to XML
		if (LispParser.isLispFormat(inputpath))
			transformtoXML();
		
			Lexicon lexicon = null;
			long time1 = System.currentTimeMillis();
			
			// load anchor strategy dynamically from classname in properties-file
			AnchorStrategy as = null;
			String classname = "grammar.buildjtiggrammar.DeepestLeftmostAnchor";
			try {
				classname = JTIGParser.getProperty("grammar.build.anchorstrategy");
				as = (AnchorStrategy) Class.forName(classname).newInstance();
			} catch (InstantiationException | IllegalAccessException
					| ClassNotFoundException e) {
				as = new DeepestLeftmostAnchor();
			}
			// create sax equipment
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			XMLHandler handler = new XMLHandler(as);
			
			// open xml file to parse
	   	    File file = new File(inputpath);
	   	    FileInputStream inputStream= new FileInputStream(file);
    	    Reader reader = new InputStreamReader(inputStream,"UTF-8");
    	    InputSource is = new InputSource(reader);
    	    is.setEncoding("UTF-8");
    	    
    	    //parse
			saxParser.parse(is, handler);
			
			// store resulting lexicon
			lexicon = handler.getLexicon();
			
			time1 = System.currentTimeMillis() - time1;
			
			if (JTIGParser.getBooleanProperty("grammar.readXML.output"))
				JTIGParser.getStringBuilder().append("Read "+lexicon.size()+" grammar rule trees from XML-file. ("+time1+" ms)");
			
		return lexicon;
	}
}
