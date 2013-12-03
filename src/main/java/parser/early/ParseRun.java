/**
 * 
 */
package parser.early;

import grammar.buildjtiggrammar.Lexicon;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import parser.derivationtree.DependentDerivationTree;
import parser.derivationtree.DerivationTree;
import parser.derivationtree.IndependentDerivationTree;
import parser.derivedtree.DerivedTree;
import parser.early.inferencerules.InferenceRule;
import parser.lookup.ActivatedElementaryTree;
import parser.lookup.ActivatedLexicon;
import parser.lookup.Lookup;
import tools.tokenizer.Token;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class ParseRun {
	
	private ParseLevel level = ParseLevel.INIT;
	
	/** helper variables **/
	private final StringBuilder log;
	private ArrayList<InferenceRule> inferencerules;

	private DefaultItemFactory factory;
	private Chart chart;
	private PriorityQueue<Item> agenda;
	private ActivatedLexicon activatedlexicon;

	private final TerminationCriterion isterm;
	private Token[] tokens;

	/** result variables**/
	
	private List<Item> items;
	private List<IndependentDerivationTree> id_derivationtrees = null;
	private List<DependentDerivationTree> d_derivationtrees = null;
	private LinkedList<DerivedTree> derivedtrees = null;
	
	public ParseRun(Lexicon lexicon,String originalsentence, Token[] tokens){
		this.tokens = tokens;
		
		this.log = new StringBuilder();
		appendToLog("Starting parse-process.");
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
	
	public String getLog() {
		return log.toString();
	}
	
	public void run(){
		items = new LinkedList<Item>();
		boolean finishedgood = false;
		
		// prepare inference rules, setting needed classes
		if (!prepareInferencerules()){
			level = ParseLevel.FAILED;
			appendToLog("No inference rules used in parsing process. Failure.");
			return;
		}
		
		// initialize the chart with items created by the tokens
		chart.initialize(tokens , factory);
				
		//initialize the agenda with items created by the activated ruletrees with start-symbols
		if (!initializeAgenda()){
			level = ParseLevel.FAILED;
			appendToLog("Agenda hasn't any start items. Failure.");
			return;
		}
		
		// Main loop
		appendToLog("Started main loop using following inference rules: "+inferencerules.toString());
		Item current;
		while ((current = agenda.poll()) != null){
			if (factory.getAmountCreatedItems() > 10000){
				appendToLog("Too many items created. Stopping!");
				break;
			}
				
			appendToLog("Actual element: "+current.toStringUgly()+"\n");
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
		appendToLog("Finished main loop. "+factory.getAmountCreatedItems()+" items were created.");
		
		if (finishedgood){
			level = ParseLevel.FOREST;
			appendToLog("Success.");
		} else {
			level = ParseLevel.FAILED;
			appendToLog("Failure.");
		}
	}
	
	private ActivatedLexicon preprocessSentence(Token[] tokens,Lexicon lexicon){
		if (lexicon == null){
			level = ParseLevel.FAILED;
			throw new IllegalArgumentException("Please read some lexicon first.");
		}
		Lookup l = new Lookup();
		ActivatedLexicon tmp = l.findlongestmatches(tokens , lexicon);
		appendToLog("Found "+tmp.getSize()+" trees in lexicon which can possibly match in the sentence.");
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
				appendToLog("Could not instantiate class with name: '"+rulefullqualifiednames[i]+"'");
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
	
	private void extractIndependentDerivationTrees(){
		if (level != ParseLevel.FOREST){
			appendToLog("Could not extract independent-derivation-trees, because it requires status FOREST; Current value: "+level.toString());
			return;
		}
		level = ParseLevel.INDEPENDENTDTREE;
		appendToLog("Extracting independent-derivation-trees.");
		this.id_derivationtrees = IndependentDerivationTree.createDerivationTrees(items);
	}
	
	private void extractDependentDerivationTrees(){
		if (level != ParseLevel.INDEPENDENTDTREE){
			appendToLog("Could not extract dependent-derivation-trees, because it requires status INDEPENDENTDTREE; Current value: "+level.toString());
			return;
		}
		appendToLog("Extracting dependent-derivation-trees.");
		level = ParseLevel.DEPENDENTDTREE;
		d_derivationtrees = new LinkedList<DependentDerivationTree>();
		for (IndependentDerivationTree itree : id_derivationtrees){
			d_derivationtrees.add(new DependentDerivationTree(itree));
		}
	}
	
	private void extractDerivedTrees() {
		if (level != ParseLevel.DEPENDENTDTREE){
			appendToLog("Could not extract derived/parse-trees, because it requires status DEPENDENTDTREE; Current value: "+level.toString());
			return;
		}
		appendToLog("Extracting derived/parse-trees.");
		level = ParseLevel.DERIVEDTREE;
		derivedtrees = new LinkedList<DerivedTree>();
		for (DependentDerivationTree ddtree : d_derivationtrees){
			derivedtrees.add(new DerivedTree(ddtree));
		}
		
	}
	
	public void appendToLog(String message) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		log.append( sdf.format(new Date()) + " : " + message + "\n");
	}

	public List<Item> getItemList() {
		return items;
	}
	
	public List<IndependentDerivationTree> retrieveIndependentDerivationTrees(){
		if (id_derivationtrees == null)
			extractIndependentDerivationTrees();
		return id_derivationtrees;
	}
	
	public List<DependentDerivationTree> retrieveDependentDerivationTrees(){
		if (id_derivationtrees == null)
			return new LinkedList<DependentDerivationTree>();
		if (d_derivationtrees == null)
			extractDependentDerivationTrees();
		return d_derivationtrees;
	}
	
	public List<DerivedTree> retrieveDerivedTrees(){
		extractDerivedTrees();
		return derivedtrees;
	}
	
}
