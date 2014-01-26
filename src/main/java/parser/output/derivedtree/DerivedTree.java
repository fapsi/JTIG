/**
 * 
 */
package parser.output.derivedtree;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;

import grammar.treeinsertion.ElementaryTree;
import grammar.treeinsertion.NodeType;

import com.mxgraph.view.mxGraph;

import parser.lookup.ActivatedElementaryTree;
import parser.output.derivationtree.AdjunctionDerivationEdge;
import parser.output.derivationtree.DependentDerivationTree;
import parser.output.derivationtree.DerivationEdge;
import parser.output.derivationtree.SubstitutionDerivationEdge;
import tools.GeneralTools;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class DerivedTree {

	protected DerivedTreeNode root;
	
	public DerivedTree(DependentDerivationTree ddt){
		buildFrom(ddt);
	}
	
	public DerivedTree(ElementaryTree tree){
		root = new DerivedTreeNode(tree);
	}
	
	/**
	 * 
	 * @param ddt
	 */
	private void buildFrom(DependentDerivationTree ddt){
		// First, create a list containing all elementary trees which should be transformed
		Map<ActivatedElementaryTree , DerivedTreeNode> totransform = new HashMap<ActivatedElementaryTree , DerivedTreeNode>();
		
		for (ActivatedElementaryTree aet : ddt.getActivatedElementaryTrees()){
			totransform.put(aet, null);
		}
		
		while (totransform.containsValue(null)){
			// select a not transformed one, where all children are transformed
			ActivatedElementaryTree current = selectCandidate(totransform,ddt);

			// build up the tree for current
			DerivedTreeNode transformed_into = new DerivedTreeNode(current);
			
			
			Map<List<Integer>,DerivationEdge> derivs = ddt.getTreeForConnectors((ActivatedElementaryTree) current);
			
			// Traverse tree
			Queue<DerivedTreeNode> rem = new LinkedList<DerivedTreeNode>();
			rem.add(transformed_into);
			
			while(!rem.isEmpty()){
				DerivedTreeNode currentnode = rem.poll();
				for (DerivedTreeNode child : currentnode.children){
					rem.offer(child);
				}
				// insert all children obtained from dependent derivation tree into the new derived tree
				DerivationEdge tmp = derivs.get(Arrays.asList(GeneralTools.IntArrayToIntegerArray(currentnode.address)));
				
				if (tmp != null){
					DerivedTreeNode head = totransform.get(tmp.getSecond());
					
					if (Arrays.equals(currentnode.address,new int[]{0})){
						transformed_into = head;
					} else {
						currentnode.replaceNodeBeyond(head);
					}
					
					if (tmp instanceof AdjunctionDerivationEdge){
					
						DerivedTreeNode foot = head.getAdjunctionFoot();
						//System.out.println(currentnode.children);
						//connect beneath
						currentnode.replaceNodeBeneath(foot);
						//currentnode.children = null;
					} else if (tmp instanceof SubstitutionDerivationEdge){
						head.entry.setType(NodeType.SUBST);
					}
				}
				
			}
			// insert result into map
			totransform.put(current, transformed_into);
		}
		//System.out.println(totransform);
		root = totransform.get(ddt.getRoot());
		
	}
	/**
	 * 
	 * @param remaintotransform
	 * @param ddt 
	 * @return
	 */
	private ActivatedElementaryTree selectCandidate(Map<ActivatedElementaryTree , DerivedTreeNode> totransform, DependentDerivationTree ddt){
		for (Entry<ActivatedElementaryTree, DerivedTreeNode> current : totransform.entrySet()){
			if (current.getValue() == null){
				boolean childstransformed = true;
				for (ActivatedElementaryTree aet : ddt.getChildTrees(current.getKey())){
					DerivedTreeNode dtn = totransform.get(aet);
					childstransformed = childstransformed && dtn != null;
				}
				if (childstransformed){
					return current.getKey();
				}
			}
		}
		throw new IllegalStateException("Should never happen.");
	}
	
	/**
	 * 
	 * @param graph 
	 */
	public void paintWithJGraphX(mxGraph graph) {
		root.paintWithJGraphX(graph);
	}
	
	/**
	 * 
	 * @param in
	 */
	public void loadFromXML(InputStream in){
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	/**
	 * 
	 * @param stream
	 * @param comment
	 * @throws XMLStreamException
	 */
	public void storeToXML(OutputStream stream, String comment) throws XMLStreamException {
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
		XMLEventWriter writer = outputFactory.createXMLEventWriter(
		  stream );
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		
		writer.add(eventFactory.createStartDocument());		
		
		writer.add(eventFactory.createComment(comment));
		
		root.storeToXML(writer, eventFactory);
		
		writer.add(eventFactory.createEndDocument());
		
		writer.close();
	}
}
