/**
 * 
 */
package parser.early;

import grammar.buildJtigGrammar.Lexicon;

import java.util.List;
import java.util.PriorityQueue;

import parser.lookup.ActivatedLexicon;
import parser.lookup.ActivatedRuleTree;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class Agenda {

	
	private PriorityQueue<Item> queue;
		
	public Agenda(){	
		queue = new PriorityQueue<>(0, null);
	}
	
	public void initialize(List<ActivatedRuleTree> activatedStartRuleTrees,
			DefaultItemFactory factory) {
		// TODO Auto-generated method stub
		
	}

	public void add(Item item) {
		queue.add(item);
	}
	
}
