/**
 * 
 */
package parser.early;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import parser.lookup.ActivatedLexicon;
import parser.lookup.Lookup;
import tools.tokenizer.MorphAdornoSentenceTokenizer;
import tools.tokenizer.Token;
import grammar.buildJtigGrammar.Lexicon;
import grammar.readXML.XMLReader;
/**
 * 
 * @author Fabian Gallenkamp
 */
public class JTIGParser {
	
	private static final String parserpropertypath = "resources/parser.properties";
	
	private static final Properties  parserproperties = new Properties();
	
	private Lexicon lexicon;
	
	private ActivatedLexicon activatedlexicon;
	
	private String originalsentence;
	
	public JTIGParser(){
		// Load preferences from property-file
		readproperties();
	}
	
	private void readproperties() {
		InputStream is = null;
		try {
			is = new FileInputStream(parserpropertypath);
			parserproperties.loadFromXML(is);
		} catch (FileNotFoundException e) {
			//TODO
		} catch (IOException e) {
			//TODO
		}
	}
	
	public static Properties getProperties(){
		return JTIGParser.parserproperties;
	}
	
	public boolean readLexicon(){		
		// TODO read more than one file to lexicon
		XMLReader xp = new XMLReader(getLexiconPaths()[0]);
		try {
			this.lexicon = xp.read();
		} catch (SAXException | IOException | ParserConfigurationException e) {
			return false;
		}
		return true;
	}
	
	public void preprocessSentence(String originalsentence, List<Token> tokens){
		this.originalsentence = originalsentence;
		Lookup l = new Lookup(tokens , lexicon);
		this.activatedlexicon = l.findlongestmatches();
	}
	
	private String[] getLexiconPaths(){
		// select lexicon-path from properties
		String pathtolexicon = parserproperties.getProperty("grammar.lexicon.path");
		File f = new File(pathtolexicon);
		if (f.isDirectory()){ // TODO: return all paths of the xml files located in that directory
			throw new UnsupportedOperationException("Directory's not yet implemented as lexicon source.");
		} else{
			String [] ret = {pathtolexicon}; 
			return ret;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("----------------------------------\n");
		sb.append(this.lexicon);
		sb.append("----------------------------------\n");
		sb.append("Sentence to be parsed:\n");
		sb.append("'"+this.originalsentence+"'\n");
		sb.append("----------------------------------\n");
		sb.append(this.activatedlexicon);
		return sb.toString();
	}

	public static void main(String[] args) {
		String input = null;
		
		for (int i = 0;i<args.length;i++){
			if ("-h".equals(args[i])){
				
				System.out.println("Usage: jtig [OPTIONS] text\n"
						+ "Options:\n"
						+ "See recources/parser.properties");
				return;			
			} else if (i == (args.length-1)){
				input = args[i];
			} else {
				System.err.println("Wrong usage. Use -h for detailed description.");
				return;
			}
		}
		//Create components
		MorphAdornoSentenceTokenizer st = new MorphAdornoSentenceTokenizer(input);
		JTIGParser parser = new JTIGParser();
		// get tokens out of string
		List<Token> tokens = st.getTokens();
		// read lexicon
		if (parser.readLexicon())
			parser.preprocessSentence(input, tokens);
		System.out.println(parser.toString());
	}
	
}
