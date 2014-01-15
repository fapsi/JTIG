/**
 * 
 */
package parser.early.run;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import parser.output.derivationtree.DependentDerivationTree;
import parser.output.derivationtree.IndependentDerivationTree;
import parser.output.derivedtree.DerivedTree;
import parser.output.forest.Forest;
import tools.tokenizer.Token;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class ParseResult {
	
	private String name;
	
	private Token[] tokens;
	
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
	
	private long uniqueitems = 0;
	
	private long processeditems=0;
	
	private Logger logger;
	
	public ParseResult(ParseRun run,Forest forest){
		this(run,forest,null,null,null);
	}
	public ParseResult(ParseRun run,Forest forest,
			List<IndependentDerivationTree> id_derivationtrees,List<DependentDerivationTree> d_derivationtrees,List<DerivedTree> derivedtrees){
		// store important stuff from parse run
		this.name = run.name;
		this.level = run.level;
		this.logger = run.logger;
		this.uniqueitems = run.chart.getAmountUniqueItems();
		this.processeditems = run.chart.getAmountProcessedItems();
		this.tokens = run.tokens;
		this.goallevel = run.goallevel;
		this.timeforest = run.timeforest;
		this.timeIDT = run.timeIDT;
		this.timeDDT = run.timeDDT;
		this.timeDT = run.timeDT;
		
		// destroy run
		run.destroy();
		
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
	
	public long getAmountUniqueItems(){
		return uniqueitems;
	}
	public long getAmountProcessedItems(){
		return processeditems;
	}
	
	public Forest getForest() {
		return forest;
	}
	
	public Token[] getTokens(){
		return tokens;
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
					sb.append("<td>Amount unique items</td>");
					sb.append("<td>"+getAmountUniqueItems()+"</td>");
				sb.append("</tr>");
				sb.append("<tr>");
					sb.append("<td>Amount processed items</td>");
					sb.append("<td>"+getAmountProcessedItems()+"</td>");
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
				sb.append(logger.getLevel());
			sb.append("</tr>");
		sb.append("</html>");
		return sb.toString();
	}
	
	public String getLog() {
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
}
