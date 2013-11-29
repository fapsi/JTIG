/**
 * 
 */
package parser.early;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.xml.sax.SAXException;

import parser.derivationtree.DerivationTree;
import tools.tokenizer.Token;
import grammar.buildjtiggrammar.Lexicon;
import grammar.readXML.XMLReader;
/**
 * 
 * @author Fabian Gallenkamp
 */
public class JTIGParser {
	
	private static final String parserpropertypath = "resources/parser.properties";
	
	private static final StringBuilder stringbuilder = new StringBuilder();
	
	private static final Properties  parserproperties = new Properties();
	
	private Lexicon lexicon;

	private ParseRun lastrun;
	
	private String lasterror;
	
	public JTIGParser() throws IOException{
		// Load preferences from property-file
		readproperties();
	}

	private void readproperties() throws InvalidPropertiesFormatException, IOException {
		InputStream is = null;
		is = new FileInputStream(parserpropertypath);
		JTIGParser.parserproperties.loadFromXML(is);
	}
	
	public boolean readLexicon(){		
		// TODO possibly read more than one file to lexicon
		XMLReader xp = new XMLReader(getLexiconPaths()[0]);
		try {
			this.lexicon = xp.read();
		} catch (SAXException | IOException | IllegalArgumentException | ParserConfigurationException | XMLStreamException e) {
			lasterror = e.getMessage();
			this.lexicon = null;
			return false;
		}
		return true;
	}
	
	public ParseRun parseSentence(String originalsentence, Token[] tokens){
		//TODO multithreading => synchro in lexicon
		ParseRun parserun = new ParseRun(lexicon, originalsentence, tokens);
		
		parserun.run();
		
		lastrun = parserun;
		
		return parserun;
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
	
	public Lexicon getLexicon(){
		return lexicon;
	}
	

	
	public String getLog() {
		return lastrun != null? lastrun.getLog():"";
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		return sb.toString();
	}
/*
	public static void main(String[] args) throws IOException {
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
		MorphAdornoSentenceTokenizer st = new MorphAdornoSentenceTokenizer();
		JTIGParser parser = new JTIGParser();
		// get tokens out of string
		Token[] tokens = st.getTokens(input);
		// read lexicon
		if (parser.readLexicon()){
			parser.parseSentence(input, tokens);
			//System.out.println();
		}
		
		//System.out.println(parser.toString());
	}

	*/
	public boolean hasLexicon() {
		return lexicon != null;
	}
	
	public static String getProperty(String key){
		String s = JTIGParser.parserproperties.getProperty(key);
		if (s != null)
			return s.trim();
		return null;
	}
	
	public static void setProperty(String key,String value){
		JTIGParser.parserproperties.setProperty(key,value);
		//os = new FileInputStream(parserpropertypath);
		//JTIGParser.parserproperties.storeToXML(parserpropertypath,null );
	}
	
	public static boolean getBooleanProperty(String key){
		return "true".equals(JTIGParser.getProperty(key).toLowerCase());
	}
	
	public static StringBuilder getStringBuilder(){
		return JTIGParser.stringbuilder;
	}

	public ParseRun getLastRun() {
		return lastrun;
	}
	
	public String getLastError(){
		return lasterror;
	}
}
