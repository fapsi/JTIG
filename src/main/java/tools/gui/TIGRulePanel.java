package tools.gui;

import grammar.buildJtigGrammar.Entry;
import grammar.buildJtigGrammar.Layer;
import grammar.buildJtigGrammar.NodeType;
import grammar.buildJtigGrammar.TIGRule;

import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;

import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class TIGRulePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TIGRule rule;

	private mxGraph graph;

	private Object parent;
	
	public TIGRulePanel(TIGRule rule) {
		this.rule = rule;
		graph = new mxGraph();
		parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		try
		{
			visualize();
		}
		finally
		{
			graph.getModel().endUpdate();
		}
		
		mxCompactTreeLayout layout = new mxCompactTreeLayout(graph);
		layout.setHorizontal(false);
		layout.setEdgeRouting(false);
		layout.execute(parent);

		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		
		add(graphComponent);
		
		validate();
		
	}
	
	
	private void visualize(){	
		List<Layer> all = rule.getLayers();
		Layer first = rule.getLayer(0);
		Object v1 = graph.insertVertex(parent, null, first.getEntry(0).getLabel(), 0, 0, 80,20);
		visualize(first,all,v1);
	}
	
	public void visualize(Layer beyond,List<Layer> all,Object p2){
		int i = 0 ;
		for (Entry entry : beyond.getEntrys()){
			if (i == 0){
				i++;
				continue;
				}
			String style = "";
			if (entry.getNodeType() == NodeType.EPS)
				style = "fillColor=green";
			if (entry.getNodeType() == NodeType.SUBST)
				style = "fillColor=yellow";
			if (entry.getNodeType() == NodeType.TERM)
				style = "fillColor=red";
			Object v1 = graph.insertVertex(parent, null, entry.getLabel(), 0, 0, 80,20, style);
			graph.insertEdge(parent, null, "", p2, v1);
			
			Layer prod = findchildren(beyond,all,entry);
			if (prod != null)
				visualize(prod,all,v1);
			
			i++;
		}
	}
	
	private Layer findchildren(Layer rule,List<Layer> all, Entry e){
		for (Layer elem : all ){
			int[] a = Arrays.copyOfRange(elem.getGornNumber(), 0, elem.getGornNumber().length-1);
			int[] b = rule.getGornNumber();
			if (Arrays.equals(a, b) && elem.getEntry(0).getLabel().equals(e.getLabel())){
				return elem;
			}
		}
		return null;
	}

}
