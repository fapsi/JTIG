/**
 * 
 */
package parser.early;

import grammar.tiggrammar.Lexicon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import javax.xml.stream.XMLStreamException;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

import parser.early.inferencerules.InferenceRule;
import parser.lookup.ActivatedElementaryTree;
import parser.lookup.ActivatedLexicon;
import parser.lookup.Lookup;
import parser.output.derivationtree.DependentDerivationTree;
import parser.output.derivationtree.IndependentDerivationTree;
import parser.output.derivedtree.DerivedTree;
import parser.output.forest.Forest;
import tools.tokenizer.Token;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class ParseRun {
	
	private String name;
	
	private ParseLevel level = ParseLevel.INIT;
	
	/** helper variables **/
	private ArrayList<InferenceRule> inferencerules;

	protected DefaultItemFactory factory;
	private Chart chart;
	private PriorityQueue<Item> agenda;
	private ActivatedLexicon activatedlexicon;

	private TerminationCriterion isterm;
	private Token[] tokens;

	private Logger logger;
	
	private boolean executed = false;
	
	public ParseRun(Lexicon lexicon,String originalsentence, Token[] tokens){		
		createRunDirectory();
		try {
			createLogger();
		} catch (IOException e) {
			
		}
		
		this.tokens = tokens;
		
		logger.info("Starting parse-preprocess.");
		logger.info("Sentence: "+ originalsentence);
		logger.info("Tokens: "+ Arrays.toString(tokens));
		// extract all important Elementary Tree's and store in activatedlexicon
		this.activatedlexicon = preprocessSentence(tokens,lexicon);
		
		// Create necessary objects
		this.inferencerules = new ArrayList<InferenceRule>();
		this.isterm = new TerminationCriterion(activatedlexicon.getStartSymbols(),tokens.length);
		
		this.factory = new DefaultItemFactory();
		this.chart = new Chart();
		
		ItemComparator itemcomp = new ItemComparator(isterm);
		this.agenda = new PriorityQueue<Item>(10, itemcomp);
	}

	private void createRunDirectory() {
		int max = 0;
		File runs_dir = new File("data/runs/");
		if (runs_dir.isDirectory()){
			
			for (File f : runs_dir.listFiles()){
				if (f.isDirectory() && f.getName().startsWith("run")){
					int i = Integer.parseInt(f.getName().substring(3));
					max = Math.max(max, i);
				}	
			}
		}
		name = "run"+(++max);
		File dir = new File("data/runs/"+name+"/");
		dir.mkdirs();
	}
	
	private void createLogger() throws IOException {
		logger = Logger.getLogger(name);
		SimpleLayout layout = new SimpleLayout();
	//    ConsoleAppender consoleAppender = new ConsoleAppender( layout );
	//      logger.addAppender( consoleAppender );
	      FileAppender fileAppender = new FileAppender( layout, "data/runs/"+name+"/"+name+".log", false );
	      logger.addAppender( fileAppender );
	      logger.setLevel(Level.ERROR);
	}
	
	public ParseResult parse(){
		logger.info("Starting parse-process.");
		long timeforest = System.currentTimeMillis();
		ParseLevel goallevel = JTIGParser.getParseLevel();
		List<Item> items = new LinkedList<Item>();
		boolean finishedgood = false;
		
		if (executed){
			logger.error("Only one run per instance allowed.");
			return new ParseResult(this, level, goallevel, new Forest(items));
		}
		executed = true;
		
		if (!JTIGParser.canExecute(ParseLevel.FOREST)){
			destroy();
			return new ParseResult(this, level, goallevel, new Forest(items));
		}
		
		// prepare inference rules, setting needed classes
		if (!prepareInferencerules()){
			level = ParseLevel.FAILED;
			logger.error("No inference rules used in parsing process.");
			destroy();
			return new ParseResult(this, level, goallevel, new Forest(items));
		}
		
		// initialize the chart with items created by the tokens
		chart.initialize(tokens , factory);
		logger.info("Chart width: "+ tokens.length);
				
		//initialize the agenda with items created by the activated ruletrees with start-symbols
		if (!initializeAgenda()){
			level = ParseLevel.FAILED;
			logger.error("Agenda hasn't any start items.");
			destroy();
			return new ParseResult(this, level, goallevel, new Forest(items));
		}
		
		// Main loop
		logger.info("Started main loop using following inference rules: "+inferencerules.toString());
		Item current;
		while ((current = agenda.poll()) != null){
			if (factory.getAmountCreatedItems() > 1000000){
				logger.error("Too many items created. Stopping!");
				break;
			}
				
			logger.debug("Actual element: "+current.toStringUgly()+"\n");
			boolean inserted = chart.addItem(current);
			
			if (JTIGParser.getBooleanProperty("parser.stoponfirsttermitem") && isterm.apply(current)){
				items.add(current);
				finishedgood = true;
				break;
			}
			
			if (!inserted)
				continue;
			
			for (InferenceRule inferencerule : inferencerules){
				
				if (inferencerule.isApplicable(current)){
					inferencerule.apply(current);
				}
			}
		}
		if (! JTIGParser.getBooleanProperty("parser.stoponfirsttermitem")){
			items = chart.getChartItems(isterm);
			if (items.size() > 0)
				finishedgood = true;
		}
		logger.info("Finished main loop. "+factory.getAmountCreatedItems()+" items were created.");
		
		if (finishedgood){
			level = ParseLevel.FOREST;
			logger.info("Success.");
		} else {
			level = ParseLevel.FAILED;
			logger.error("Failure.");
		}
		timeforest = System.currentTimeMillis() - timeforest;
		Forest forest = new Forest(items);
		
		long timeIDT = System.currentTimeMillis();
		List<IndependentDerivationTree> id_derivationtrees = null;
		if (JTIGParser.canExecute(ParseLevel.INDEPENDENTDTREE)){
			id_derivationtrees = extractIndependentDerivationTrees(forest);
		}
		timeIDT = System.currentTimeMillis() - timeIDT;
		long timeDDT = System.currentTimeMillis();
		List<DependentDerivationTree> d_derivationtrees = null;
		if (JTIGParser.canExecute(ParseLevel.DEPENDENTDTREE)){
			d_derivationtrees = extractDependentDerivationTrees(id_derivationtrees);
		}
		timeDDT = System.currentTimeMillis() - timeDDT;
		long timeDT = System.currentTimeMillis();
		List<DerivedTree> derivedtrees = null;
		if (JTIGParser.canExecute(ParseLevel.DERIVEDTREE)){
			try {
				derivedtrees = extractDerivedTrees(d_derivationtrees);
			} catch (FileNotFoundException | XMLStreamException e) {
				logger.error("Exception while extracting derived trees: "+ e.getMessage());
			}
		}
		timeDT = System.currentTimeMillis() - timeDT;
		destroy();
		return new ParseResult(this, level, goallevel, forest, id_derivationtrees, d_derivationtrees, derivedtrees,timeforest,timeIDT,timeDDT,timeDT);
	}
	
	private ActivatedLexicon preprocessSentence(Token[] tokens,Lexicon lexicon){
		if (lexicon == null){
			level = ParseLevel.FAILED;
			throw new IllegalArgumentException("Please read some lexicon first.");
		}
		Lookup l = new Lookup();
		ActivatedLexicon tmp = l.findlongestmatches(tokens , lexicon);
		logger.info("Found "+tmp.getSize()+" trees in lexicon which can possibly match in the sentence.");
		logger.info(tmp.toString());
		level = ParseLevel.LOOKUP;
		return tmp;
	}
	
	private boolean prepareInferencerules() {
		
		initializeInferenceRules();
		
		for (InferenceRule rule : inferencerules){
			rule.setFactory(factory);
			rule.setChart(chart);
			rule.setAgenda(agenda);
			rule.setActivatedLexicon(activatedlexicon);
		}
		return inferencerules.size() > 0;
	}
	
	private void initializeInferenceRules() {
		String rulenamescommaseparated = JTIGParser.getProperty("parser.core.inferencerules");
		if (rulenamescommaseparated == null)
			return;
		String[] rulefullqualifiednames = rulenamescommaseparated.split("\\s*,\\s*");
		
		for (int i = 0; i < rulefullqualifiednames.length; i++){
			try {
				inferencerules.add((InferenceRule) Class.forName(rulefullqualifiednames[i]).newInstance());
			} catch (InstantiationException | IllegalAccessException
					| ClassNotFoundException e) {
				logger.error("Could not instantiate class with name: '"+rulefullqualifiednames[i]+"'");
			}
		}
	}
	
	private boolean initializeAgenda() {
		boolean added = false;
		for (String startsymbol : activatedlexicon.getStartSymbols()){
			List<ActivatedElementaryTree> result = activatedlexicon.get(startsymbol);
			
			if (result != null)
				for (ActivatedElementaryTree art : result){
					Item item = factory.createItemInstance(art,0); // initialize trees with span (0,0)
					agenda.add(item);
					
					if (!added)
						added = true;
				}
		}
		return added;
	}
	
	private List<IndependentDerivationTree> extractIndependentDerivationTrees(Forest forest){
		if (!JTIGParser.canExecute(ParseLevel.INDEPENDENTDTREE) || forest == null)
			return null;
		if (level != ParseLevel.FOREST){
			logger.error("Could not extract independent-derivation-trees, because it requires status FOREST; Current value: "+level.toString());
			return null;
		}
		level = ParseLevel.INDEPENDENTDTREE;
		logger.info("Extracting independent-derivation-trees.");
		return forest.createDerivationTrees();
	}
	
	private List<DependentDerivationTree> extractDependentDerivationTrees(List<IndependentDerivationTree> id_derivationtrees){
		if (!JTIGParser.canExecute(ParseLevel.DEPENDENTDTREE) || id_derivationtrees == null)
			return null;
		if (level != ParseLevel.INDEPENDENTDTREE){
			logger.error("Could not extract dependent-derivation-trees, because it requires status INDEPENDENTDTREE; Current value: "+level.toString());
			return null;
		}
		logger.info("Extracting dependent-derivation-trees.");
		level = ParseLevel.DEPENDENTDTREE;
		List<DependentDerivationTree> d_derivationtrees = new LinkedList<DependentDerivationTree>();
		for (IndependentDerivationTree itree : id_derivationtrees){
			d_derivationtrees.add(new DependentDerivationTree(itree));
		}
		return d_derivationtrees;
	}
	
	private List<DerivedTree> extractDerivedTrees(List<DependentDerivationTree> d_derivationtrees) throws FileNotFoundException, XMLStreamException {
		if (!JTIGParser.canExecute(ParseLevel.DERIVEDTREE) || d_derivationtrees == null)
			return null;
		if (level != ParseLevel.DEPENDENTDTREE){
			logger.error("Could not extract derived/parse-trees, because it requires status DEPENDENTDTREE; Current value: "+level.toString());
			return null;
		}
		logger.info("Extracting derived/parse-trees.");
		level = ParseLevel.DERIVEDTREE;
		List<DerivedTree> derivedtrees = new LinkedList<DerivedTree>();
		int i = 1;
		for (DependentDerivationTree ddtree : d_derivationtrees){
			DerivedTree newone = new DerivedTree(ddtree);
			// extract from dependent derivation tree
			derivedtrees.add(newone);
			
			// store them if necessary
			if (JTIGParser.getBooleanProperty("parser.derivedtree.store")){
				File f = new File("data/runs/"+name+"/derivedtree"+i+".xml");
				newone.storeToXML(new FileOutputStream(f , false), "Derived tree " + i);
				logger.info("Saving parsetree 'data/runs/"+name+"/derivedtree"+i+".xml'.");
			}
			i++;
		}
		return derivedtrees;
	}
	
	private void destroy(){
		if (executed){
			//factory = null;
			agenda = null;
			chart = null;
			inferencerules = null;
			isterm=null;
			//activatedlexicon = null;
		}
	}
	
	protected String getLog() {
		StringBuilder sb = new StringBuilder();
		File file = new File("data/runs/"+name+"/"+name+".log");
		try {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = "";
		while ((line = reader.readLine()) != null){
			sb.append(line);
			sb.append(System.getProperty("line.separator"));
		}
		reader.close();
		} catch (IOException e) {
			sb.append("Error reading the log-file.");
		}
		return sb.toString();
	}

	public Logger getLogger() {
		return logger;
	}
}
