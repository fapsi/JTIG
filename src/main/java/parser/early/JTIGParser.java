/**
 * 
 */
package parser.early;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.xml.sax.SAXException;

import parser.derivationtree.DerivationTree;
import tools.gui.GraphicalUserInterface;
import tools.tokenizer.MorphAdornoSentenceTokenizer;
import tools.tokenizer.Token;
import grammar.readXML.XMLReader;
import grammar.tiggrammar.Lexicon;
/**
 * 
 * @author Fabian Gallenkamp
 */
public class JTIGParser {
	
	private static final String parserpropertypath = "resources/parser.properties";
	
	private final StringBuilder log = new StringBuilder();
	
	private static final Properties  parserproperties = new Properties();
	
	private Lexicon lexicon;

	private ParseRun lastrun;
	
	private String lasterror;
	
	public JTIGParser() throws IOException{
		createLogDirectory();
		
		createRunsDirectory();
		
		// Load preferences from property-file
		readproperties();
	}

	private void createLogDirectory() throws IOException{
		File dir = new File("data/log/");
		dir.mkdirs();
	}

	private void createRunsDirectory() {
		File dir = new File("data/runs/");
		dir.mkdirs();
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
			writeIntoLog("Read lexicon '" + getLexiconPaths()[0] + "' successfully.");
		} catch (SAXException | IOException | IllegalArgumentException | ParserConfigurationException | XMLStreamException e) {
			lasterror = e.getMessage();
			writeIntoLog("Can't read lexicon. Error: "+lasterror);
			this.lexicon = null;
			return false;
		}
		return true;
	}
	
	public ParseRun parseSentence(String originalsentence, Token[] tokens){
		//TODO multithreading => synchro in lexicon
		ParseRun parserun = new ParseRun(lexicon, originalsentence, tokens);
		
		writeIntoLog("Parsing sentence: '"+originalsentence+"'.");
		
		parserun.parse();
		
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		return sb.toString();
	}

	
	public static void main(String[] args) throws IOException, XMLStreamException {
		String input_sentence = "";
		String lexicon_filepath = "";
		int input_file_line = -1;
		String input_filepath = null;
		
		boolean fileinput =  false;
		
		if (args.length < 1) {
			System.err.println("Wrong usage. Use -h for detailed description.");
			return;
		}
		
		if ("-h".equals(args[0])){
			System.out.println("Usage: jtig -g | jtig lexicon_file (input_sentence|input_file)\n"
					+ "See recources/parser.properties");
			return;			
		} else if ("-g".equals(args[0])){
			{
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						GraphicalUserInterface frame = null;
						try {
							frame = new GraphicalUserInterface();
							frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
							frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
							frame.setVisible(true);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
			}
			return;
		} else {
			lexicon_filepath = args[0];
		}
		
		if (args.length < 2){
			System.err.println("Wrong usage. Use -h for detailed description.");
			return;
		}
		
		if ("-f".equals(args[1])){
			if (args.length != 4) {
				System.err.println("Wrong usage. Use -h for detailed description.");
				return;
			}
			fileinput = true;
			input_filepath = args[2];
			try{
				input_file_line = Integer.parseInt(args[3]);
			} catch (NumberFormatException nfe){
				System.err.println("Wrong usage. Use -h for detailed description.");
				return;
			}
		} else {
			if (args.length != 2) {
				System.err.println("Wrong usage. Use -h for detailed description.");
				return;
			}
			input_sentence = args[1];
		}
		
		//Create components
		MorphAdornoSentenceTokenizer st = new MorphAdornoSentenceTokenizer();
		
		JTIGParser parser = new JTIGParser();
		
		// get tokens out of string
		Token[] tokens = null;
		if (fileinput)
			tokens = st.getTokens(input_filepath,input_file_line);
		else 
			tokens = st.getTokens(input_sentence);
		
		// set lexicon
		JTIGParser.setProperty("grammar.lexicon.path", lexicon_filepath);	
		
		// read lexicon
		parser.readLexicon();
		
		ParseRun run = parser.parseSentence(input_sentence, tokens);

	}
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
	}
	
	public static boolean getBooleanProperty(String key){
		return "true".equals(JTIGParser.getProperty(key).toLowerCase());
	}
	
	public static boolean canExecute(ParseLevel want){
		String prop = getProperty("parser.core.parselevel");
		ParseLevel can = ParseLevel.valueOf(prop);
		if (can == null || can == ParseLevel.INIT || can == ParseLevel.FAILED)
			throw new IllegalArgumentException("Property parser.core.parselevel not valid.");
		switch(want){
			case LOOKUP:
				return true;
			case FOREST:
				return !(can == ParseLevel.LOOKUP);
			case INDEPENDENTDTREE:
				return !(can == ParseLevel.LOOKUP || can == ParseLevel.FOREST);
			case DEPENDENTDTREE:
				return !(can == ParseLevel.LOOKUP || can == ParseLevel.FOREST 
				|| can == ParseLevel.INDEPENDENTDTREE);
			case DERIVEDTREE:
				return !(can == ParseLevel.LOOKUP || can == ParseLevel.FOREST 
				|| can == ParseLevel.INDEPENDENTDTREE || can == ParseLevel.DEPENDENTDTREE);
			default: 
				return false;
		}
	}
	
	public StringBuilder getLog(){
		return log;
	}

	public void writeIntoLog(String message) {
		try {
			File file = new File("data/log/log");
			BufferedWriter output = new BufferedWriter(new FileWriter(file,true));
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			output.write(sdf.format(new Date()) + " : " +message);
			output.newLine();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ParseRun getLastRun() {
		return lastrun;
	}
	
	public String getLastError(){
		return lasterror;
	}
}
