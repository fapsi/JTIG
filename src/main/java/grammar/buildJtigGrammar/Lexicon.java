/**
 * 
 */
package grammar.buildJtigGrammar;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class Lexicon {

	private List<RuleTree> content;
	
	private HashMap<String,Lexicon> entrys;
	
	public Lexicon() {
		content = new LinkedList<RuleTree>();
		entrys = new HashMap<String,Lexicon>();
	}
	
	public void add(RuleTree toadd){
		List<String> remaining = toadd.getlexicalanchors();
		add (toadd,remaining);
	}
	
	private void add(RuleTree toadd,List<String> remaining){
		if (remaining.size() <= 0)
			return;
		
		String anchor = remaining.get(0);
		Lexicon sublexicon = entrys.get(anchor);
		
		if (sublexicon == null){
			sublexicon = new Lexicon();
			entrys.put(anchor,sublexicon);
		}
		
		if (remaining.size() > 1){
			remaining.remove(0);
			sublexicon.add(toadd, remaining);
		} else {
			sublexicon.content.add(toadd);
		}
	}
	
	public RuleTree find(List<String> index){
		return null;
	}

	public int size() {
		return 0;
	}
	
	@Override
	public String toString() {
		return null;
	}
}
