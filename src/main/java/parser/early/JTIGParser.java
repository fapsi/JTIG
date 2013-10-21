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
import java.util.InvalidPropertiesFormatException;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import parser.early.inferencerules.InferenceRule;
import parser.early.inferencerules.PredictTraversation;
import parser.early.inferencerules.Scanning;
import parser.lookup.ActivatedLexicon;
import parser.lookup.ActivatedTIGRule;
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
	
	private List<InferenceRule> inferencerules;
	
	public JTIGParser() throws IOException{
		// Load preferences from property-file
		readproperties();
	}
	
	private void readproperties() throws InvalidPropertiesFormatException, IOException {
		InputStream is = null;
		is = new FileInputStream(parserpropertypath);
		JTIGParser.parserproperties.loadFromXML(is);
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
		// extract all important TIGRule's
		preprocessSentence(originalsentence,tokens);
		
		// Create necessary objects
		ItemFilter isterm = new StopFilter();
		DefaultItemFactory factory = new DefaultItemFactory();
		Chart chart = new Chart();
		ItemComparator itemcomp = new ItemComparator();
		PriorityQueue<Item> agenda = new PriorityQueue<Item>(1, itemcomp);
		
		// Initialize inference rules, which should be used in the parsing process
		initializeinferencerules(factory,chart,agenda);
		
		// initialize the chart with items created by the tokens
		chart.initialize(tokens , factory);
		
		System.out.println(chart);
		
		//initialize the agenda with items created by the activated ruletrees with start-symbols
		if (!initializeAgenda(agenda , factory))
			return false;
		
		// Main loop
		Item current;
		while ((current = agenda.poll()) != null){
			chart.addItem(current);
			
			for (InferenceRule inferencerule : inferencerules){
				
				if (inferencerule.isApplicable(current)){
					System.out.println("Yes");
					inferencerule.apply(current);
					}
			}
			System.out.println(current);
			if (isterm.apply(current))
				break;
		}
		
		//System.out.println(agenda);
		
		return true;
	}
	
	private void initializeinferencerules( DefaultItemFactory factory,Chart chart,PriorityQueue<Item> agenda) {
		inferencerules = new LinkedList<InferenceRule>();
		
		inferencerules.add(new PredictTraversation(factory, agenda));
		inferencerules.add(new Scanning(factory,chart, agenda));
	}
	
	private boolean initializeAgenda(PriorityQueue<Item> agenda, DefaultItemFactory factory) {
		boolean added = false;
		for (String startsymbol : lexicon.getStartSymbols()){
			List<ActivatedTIGRule> result = activatedlexicon.get(startsymbol);
			
			if (result != null)
				for (ActivatedTIGRule art : result){
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
	
}
