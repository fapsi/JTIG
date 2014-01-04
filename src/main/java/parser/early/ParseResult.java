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

	private long timeforest = -1;

	private long timeIDT = -1;

	private long timeDDT = -1;

	private long timeDT = -1;
	
	public ParseResult(ParseRun run,ParseLevel level,ParseLevel goallevel,Forest forest){
		this(run,level,goallevel,forest,null,null,null,-1,-1,-1,-1);
	}
	public ParseResult(ParseRun run,ParseLevel level,ParseLevel goallevel,Forest forest,
			List<IndependentDerivationTree> id_derivationtrees,List<DependentDerivationTree> d_derivationtrees,
			List<DerivedTree> derivedtrees, long timeforest,long timeIDT,long timeDDT,long timeDT){
		this.run = run;
		this.level = level;
		this.goallevel = goallevel;
		this.forest = forest;
		this.id_derivationtrees = id_derivationtrees;
		this.d_derivationtrees = d_derivationtrees;
		this.derivedtrees = derivedtrees;
		this.timeforest = timeforest;
		this.timeIDT = timeIDT;
		this.timeDDT = timeDDT;
		this.timeDT = timeDT;
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
	
	public String getParseDetails(){
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append("<hr/><h3>General</h3>");
			sb.append("<table style='border-collapse:collapse;border:1px solid black;width:100%;'>");
				sb.append("<tr>");
					sb.append("<td>Level reached</td>");
						sb.append("<td>"+level.toString()+"</td>");
				sb.append("</tr>");
				sb.append("<tr>");
					sb.append("<td>Goal level</td>");
					sb.append("<td>"+goallevel.toString()+"</td>");
				sb.append("</tr>");
				sb.append("<tr>");
					sb.append("<td>Amount items created</td>");
					sb.append("<td>"+getAmountItemsCreated()+"</td>");
				sb.append("</tr>");
			sb.append("</table>");
		sb.append("<hr/><h3>Time</h3>");
			sb.append("<table style='border-collapse:collapse;border:1px solid black;width:100%;'>");
				sb.append("<tr>");
					sb.append("<td>Construct forest</td>");
					sb.append(timeforest+"ms");
				sb.append("</tr>");
				sb.append("<tr>");
					sb.append("<td>Independent<br/>derivation trees</td>");
					sb.append(timeIDT+"ms");
				sb.append("</tr>");
				sb.append("<tr>");
					sb.append("<td>Dependent<br/>derivation trees</td>");
					sb.append(timeDDT+"ms");
				sb.append("</tr>");
				sb.append("<tr>");
					sb.append("<td>Derived trees</td>");
					sb.append(timeDT+"ms");
				sb.append("</tr>");
			sb.append("</table>");
		sb.append("<hr/><h3>Other</h3>");
			sb.append("<table style='border-collapse:collapse;border:1px solid black;width:100%;'>");
			sb.append("<tr>");
				sb.append("<td>Log-level</td>");
				sb.append(run.getLogger().getLevel());
			sb.append("</tr>");
		sb.append("</html>");
		return sb.toString();
	}
}
