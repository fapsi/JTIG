/**
 * 
 */
package parser.early;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import parser.lookup.ActivatedLexicon;
import parser.lookup.ActivatedRuleTree;
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
	
	private static final PrintStream printstream = System.out;
	
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
	
	public static String getProperty(String key){
		String s = JTIGParser.parserproperties.getProperty(key);
		if (s != null)
			return s.trim();
		return null;
	}
	
	public static boolean getBooleanProperty(String key){
		return "true".equals(JTIGParser.getProperty(key).toLowerCase());
	}
	
	public static PrintStream getPrintStream(){
		return JTIGParser.printstream;
	}
	
	public boolean readLexicon(){		
		// TODO possibly read more than one file to lexicon
		XMLReader xp = new XMLReader(getLexiconPaths()[0]);
		try {
			this.lexicon = xp.read();
		} catch (SAXException | IOException | ParserConfigurationException e) {
			return false;
		}
		return true;
	}
	
	private void preprocessSentence(String originalsentence, Token[] tokens){
		this.originalsentence = originalsentence;
		
		Lookup l = new Lookup(tokens , lexicon);
		this.activatedlexicon = l.findlongestmatches();
	}
	
	public boolean parseSentence(String originalsentence, Token[] tokens){
		preprocessSentence(originalsentence,tokens);
		System.out.println(this);
		// create item factory
		DefaultItemFactory factory = new DefaultItemFactory();
		// Create chart object
		Chart chart = new Chart();
		// Create Agenda
		Agenda agenda = new Agenda();
		
		// initialize the chart with items created by the tokens
		chart.initialize(tokens , factory);
		
		System.out.println(chart);
		
		//initialize the agenda with items created by the activated ruletrees with start-symbols
		if (!initializeAgenda(agenda , factory))
			return false;
		
		System.out.println(agenda);
		
		return true;
	}
	
	private boolean initializeAgenda(Agenda agenda, DefaultItemFactory factory) {
		boolean added = false;
		for (String startsymbol : lexicon.getStartSymbols()){
			List<ActivatedRuleTree> result = activatedlexicon.get(startsymbol);
			
			if (result != null)
				for (ActivatedRuleTree art : result){
					Item item = factory.createItemInstance(art);
					agenda.add(item);
					
					if (!added)
						added = true;
				}
		}
		return added;
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
	
}
