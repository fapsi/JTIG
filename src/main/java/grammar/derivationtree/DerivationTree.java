/**
 * 
 */
package grammar.derivationtree;

import grammar.buildJtigGrammar.ElementaryTree;

import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import com.mxgraph.view.mxGraph;

import parser.early.DerivationType;
import parser.early.Item;
import parser.early.ItemDerivation;
import parser.lookup.ActivatedElementaryTree;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class DerivationTree {
	/**
	 * All {@link ElementaryTree} occurring in the {@link DerivationTree} and
	 * the Object it's printed into
	 */
	private Map<ActivatedElementaryTree, Object> nodesprinted;

	/**
	 * Connections between two {@link ElementaryTree}'s with the underlying
	 * operation details (see {@link DerivationEdge},
	 * {@link SubstitutionDerivationEdge},{@link AdjunctionDerivationEdge}).
	 */
	private List<DerivationEdge> edges;

	private static long actindex = 1;

	private long index = 1;

	/**
	 * 
	 * @param items
	 *            - a list with all {@link Item} representing different
	 *            derivations.
	 * @return All {@link DerivationTree}'s resulting from the item-structure
	 *         obtained by the parsing process.
	 */
	public static List<DerivationTree> createDerivationTrees(List<Item> items) {
		actindex = 1;
		// list for result
		List<DerivationTree> derivationtrees = new LinkedList<DerivationTree>();

		// iterate over all items to be analyzed
		for (Item actualitem : items) {
			// create derivation tree add to result
			DerivationTree actualtree = new DerivationTree(DerivationTree.actindex++);
			derivationtrees.add(actualtree);


			Stack<Item> itemstack = new Stack<Item>();
			itemstack.add(actualitem);
			// call recursion
			DerivationTree.analyzeItemsRecursively(derivationtrees, actualtree,	itemstack);
		}
		//System.out.println(derivationtrees.get(0).edges);

		return derivationtrees;
	}

	private static void analyzeItemsRecursively(List<DerivationTree> derivationtrees, DerivationTree acttree, Stack<Item> itemstack) {
		while (!itemstack.isEmpty()) {
			Item actitem = itemstack.pop();
			boolean first = true;
			Stack<Item> tmpstack = new Stack<Item>();
			tmpstack.addAll(itemstack);
			for (ItemDerivation itemderiv : actitem.getDerivations()) {		
				// create variables for !first
				DerivationEdge tobeadded = null;
				DerivationTree other = null;
				Stack<Item> newstack = null;	
				boolean predict = false;
				// calculate DerivationEdge on completion, stop on prediction, continue on traversation
				switch (itemderiv.getType()) {
					// predictions => stop criterion
					case PredictLeftAux:
					case PredictRightAux:
					case PredictSubstitution:
					case PredictTraversation:
						predict = true;
						break;
					// completions => add nodes, make edges
					case CompleteSubstitution:
					case CompleteLeftAdjunction:
					case CompleteRightAdjunction:
						//TODO : make own function!!
						ActivatedElementaryTree inserted = null;
						for (Item deriveditem : itemderiv.getItems()) {
							ActivatedElementaryTree atree = deriveditem.getActivatedElementaryTree();
							if (!actitem.getActivatedElementaryTree().equals(atree))
								inserted = atree;
						}
						if (inserted == null)
							throw new IllegalStateException();
						// Add derivation edge, substitution or adjunction
						if (itemderiv.getType() == DerivationType.CompleteSubstitution)
							tobeadded = new SubstitutionDerivationEdge(actitem.getActivatedElementaryTree(), inserted,null);
						else
							tobeadded = new AdjunctionDerivationEdge(actitem.getActivatedElementaryTree(), inserted,null);
						break;
						// Traversation => do nothing but traverse
					default:

						break;
				}
				if(predict)
					continue;
				
				if (!first) { // more than one derivation case
					other = acttree.copy(); // copy actual derivation and make new one
					derivationtrees.add(other);
					
					newstack = new Stack<Item>();
					newstack.addAll(tmpstack);
				}
				
				if (tobeadded != null)
					if (first)
						acttree.addDerivation(tobeadded);
					else
						other.addDerivation(tobeadded);

				// push items to right stack
				Item[] items = itemderiv.getItems();
				for (int i = items.length-1;i>=0;i--){
					if (first)
						itemstack.push(items[i]);
					else
						newstack.push(items[i]);
				}

				// make recursive calls for all non-first derivations
				if (!first) { // more than one derivation case
					analyzeItemsRecursively(derivationtrees, other, newstack);
				}
				

				if (first) // set !first
					first = false;
			}
		}
	}

	public DerivationTree(long index) {
		this.index = index;
		this.edges = new LinkedList<DerivationEdge>();
		this.nodesprinted = new HashMap<ActivatedElementaryTree, Object>();
	}

	public DerivationTree(long index,Map<ActivatedElementaryTree, Object> nodesprinted,List<DerivationEdge> edges) {
		this.index = index;
		this.edges = edges;
		this.nodesprinted = nodesprinted;
	}

	public void addDerivation(DerivationEdge edge) {
		// add trees, if not present
		if (!nodesprinted.containsKey(edge.first))
			nodesprinted.put(edge.first, null);
		if (!nodesprinted.containsKey(edge.second))
			nodesprinted.put(edge.second, null);
		// add edges
		edges.add(edge);
	}

	public void storeToXML(OutputStream stream, String comment) {

	}

	public void paintWithJGraphX(mxGraph graph) {
		resetNodesPrinted();
		Object parent = graph.getDefaultParent();
		// print nodes
		for (Entry<ActivatedElementaryTree, Object> eset : nodesprinted.entrySet()) {
			Object o = graph.insertVertex(parent, eset.getKey().toString(),	eset.getKey().toString(), 0, 0, 100, 20);
			eset.setValue(o);
		}
		// print edges
		int i = 1;
		for (DerivationEdge edge : edges) {
			String description = edge instanceof SubstitutionDerivationEdge ? "Substitution": "Adjunction";
			graph.insertEdge(parent, Integer.toString(i++), description,nodesprinted.get(edge.first), nodesprinted.get(edge.second));
		}
	}

	private void resetNodesPrinted() {
		if (nodesprinted == null)
			return;
		for (Entry<ActivatedElementaryTree, Object> eset : nodesprinted.entrySet()) {
			eset.setValue(null);
		}
	}

	public DerivationTree copy() {
		Map<ActivatedElementaryTree, Object> copynodesprinted = new HashMap<ActivatedElementaryTree, Object>();
		copynodesprinted.putAll(nodesprinted);
		// TODO: deep copy ???
		List<DerivationEdge> copyedges = new LinkedList<DerivationEdge>();
		copyedges.addAll(edges);
		return new DerivationTree(DerivationTree.actindex++, copynodesprinted,	copyedges);
	}

	public long getIndex() {
		return index;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DerivationTree [index=" + index + ", nodesprinted="
				+ nodesprinted + ", edges=" + edges + "]";
	}

}
