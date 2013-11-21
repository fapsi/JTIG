/**
 * 
 */
package grammar.derivationtree;

import grammar.buildJtigGrammar.Layer;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import parser.lookup.ActivatedElementaryTree;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class DependentDerivationTree extends DerivationTree {
	
	public DependentDerivationTree (IndependentDerivationTree independent) {
		copyFrom(independent);
		transformFromIndependent();
	}

	private void transformFromIndependent(){
		for (ActivatedElementaryTree tree : nodesprinted.keySet()){
			// collect all adjunction edges with same connector
			Map<int[], List<AdjunctionDerivationEdge>> nondeterministicedges = collectAdjDerivEdgesWithSameConnectors(tree);
			
			// iterate over them
			for (Entry<int[], List<AdjunctionDerivationEdge>> act : nondeterministicedges.entrySet()){
				// sort them by occurrence 
				/** TODO: redundant code with {@link DerivationTree#paintWithJGraphX} **/
				Collections.sort(act.getValue(),new Comparator<DerivationEdge>() {
					@Override
					public int compare(DerivationEdge o1, DerivationEdge o2) {
						return o1.second.getLeft() - o2.second.getLeft() ;
					}
				});
				ListIterator<AdjunctionDerivationEdge> iter = act.getValue().listIterator();
				AdjunctionDerivationEdge previous = null;
				while(iter.hasNext()){
					AdjunctionDerivationEdge element = iter.next();
					if (previous == null){
						previous = element;
						continue;
						}
					// create new edge
					addDerivation(new AdjunctionDerivationEdge(previous.second,element.second,element.second.getFootAddress()));
					// delete old one
					removeDerivation(element);
					
					previous = element; // move on
				}
			}
		}
	}

	/**
	 * 
	 * @param tree
	 * @return a mapping between all connectors as {@link Layer} and all {@link AdjunctionDerivationEdge}'s occurring at that connector.
	 */
	private Map<int[],List<AdjunctionDerivationEdge>> collectAdjDerivEdgesWithSameConnectors(ActivatedElementaryTree tree){
		
		Map<int[], List<AdjunctionDerivationEdge>> result = new HashMap<int[],List<AdjunctionDerivationEdge>>();
		
		for (DerivationEdge edge : edges){
			if (edge.first.equals(tree) && edge instanceof AdjunctionDerivationEdge){
				if (result.containsKey(edge.connector)){
					result.get(edge.connector).add((AdjunctionDerivationEdge) edge);
				} else {
					LinkedList<AdjunctionDerivationEdge> toadd = new LinkedList<AdjunctionDerivationEdge>();
					toadd.add((AdjunctionDerivationEdge) edge);
					result.put(edge.connector, toadd);
				}
			}
		}
		
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Dependent derivation";
	}
	
	
}
