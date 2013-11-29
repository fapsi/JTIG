/**
 * 
 */
package parser.derivedtree;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import grammar.buildjtiggrammar.ElementaryTree;
import grammar.buildjtiggrammar.NodeType;

import com.mxgraph.view.mxGraph;

import parser.derivationtree.AdjunctionDerivationEdge;
import parser.derivationtree.DependentDerivationTree;
import parser.derivationtree.DerivationEdge;
import parser.derivationtree.SubstitutionDerivationEdge;
import parser.lookup.ActivatedElementaryTree;
import tools.GeneralTools;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class DerivedTree {

	protected DerivedTreeNode root;
	
	public DerivedTree(DependentDerivationTree ddt){
		root = createfromdependentderivationtree(ddt,ddt.getRoot());
	}
	
	public DerivedTree(ElementaryTree tree){
		root = new DerivedTreeNode(tree);
	}

	private DerivedTreeNode createfromdependentderivationtree(DependentDerivationTree ddt,ActivatedElementaryTree current) {
		// create tree for current elementarytree
		DerivedTreeNode currenttreepart = new DerivedTreeNode(current);
		
		// get the derivations
		Map<List<Integer>,DerivationEdge> derivs = ddt.getTreeForConnectors(current);
		// traverse currenttreepart
		
		Queue<DerivedTreeNode> rem = new LinkedList<DerivedTreeNode>();
		
		rem.add(currenttreepart);
		
		while(!rem.isEmpty()){
			DerivedTreeNode currentnode = rem.poll();
			for (DerivedTreeNode child : currentnode.children){
				rem.offer(child);
			}
			DerivationEdge tmp = derivs.get(Arrays.asList(GeneralTools.IntArrayToIntegerArray(currentnode.address)));
			if (tmp != null){
				DerivedTreeNode head = createfromdependentderivationtree(ddt, tmp.getSecond());
				// connect beyond 
				//TODO: what if address [0] !!!
				// replace child of parent
				//if (tmp instanceof SubstitutionDerivationEdge)
					currentnode.replaceNodeBeyond(head);
				
				if (tmp instanceof AdjunctionDerivationEdge){
				
					DerivedTreeNode foot = head.getAdjunctionFoot();
					//connect beneath
					currentnode.replactNodeBeneath(foot);
					//currentnode.children = null;
				} else if (tmp instanceof SubstitutionDerivationEdge){
					head.entry.setType(NodeType.SUBST);
				}
			}
		
		}
		return currenttreepart;
	}
	
	public void paintWithJGraphX(mxGraph graph) {
		root.paintWithJGraphX(graph);
	}
}
