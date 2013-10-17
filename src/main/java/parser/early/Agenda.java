/**
 * 
 */
package parser.early;

import grammar.buildJtigGrammar.Lexicon;

import java.util.List;
import java.util.PriorityQueue;

import parser.lookup.ActivatedLexicon;
import parser.lookup.ActivatedTIGRule;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class Agenda {

	
	private PriorityQueue<Item> queue;
		
	public Agenda(){	
		ItemComparator itemcomp = new ItemComparator();
		queue = new PriorityQueue<>(1, itemcomp);
	}

	public void add(Item item) {
		queue.add(item);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Agenda [queue=" + queue + "]";
	}
}
