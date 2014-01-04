/**
 * 
 */
package parser.early;

import java.util.List;

import parser.output.derivationtree.DependentDerivationTree;
import parser.output.derivationtree.IndependentDerivationTree;
import parser.output.derivedtree.DerivedTree;
import parser.output.forest.Forest;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class ParseResult {

	/**
	 * 
	 */
	private ParseRun run;
	
	private ParseLevel level;
	
	private ParseLevel goallevel;
	
	private Forest forest;
	
	private List<IndependentDerivationTree> id_derivationtrees = null;
	
	private List<DependentDerivationTree> d_derivationtrees = null;
	
	private List<DerivedTree> derivedtrees = null;
	
	public ParseResult(ParseRun run,ParseLevel level,ParseLevel goallevel,Forest forest){
		this(run,level,goallevel,forest,null,null,null);
	}
	public ParseResult(ParseRun run,ParseLevel level,ParseLevel goallevel,Forest forest,
			List<IndependentDerivationTree> id_derivationtrees,List<DependentDerivationTree> d_derivationtrees,
			List<DerivedTree> derivedtrees){
		this.run = run;
		this.level = level;
		this.goallevel = goallevel;
		this.forest = forest;
		this.id_derivationtrees = id_derivationtrees;
		this.d_derivationtrees = d_derivationtrees;
		this.derivedtrees = derivedtrees;
	}
	
	public ParseLevel getLevel(){
		return level;
	}
	
	public ParseLevel getGoalLevel(){
		return goallevel;
	}
	
	public ParseRun getParseRun(){
		return run;
	}
	
	public long getAmountItemsCreated(){
		return run.factory.getAmountCreatedItems();
	}
	
	public Forest getForest() {
		return forest;
	}
	
	public List<IndependentDerivationTree> getIndependentDerivationTrees(){
		return id_derivationtrees;
	}
	
	public List<DependentDerivationTree> getDependentDerivationTrees(){
		return d_derivationtrees;
	}
	
	public List<DerivedTree> getDerivedTrees() {
		return derivedtrees;
	}
	
	public String getLog() {
		return run.getLog();
	}
}
