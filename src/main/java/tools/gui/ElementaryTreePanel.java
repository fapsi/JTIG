package tools.gui;

import grammar.buildjtiggrammar.ElementaryTree;
import grammar.buildjtiggrammar.Entry;
import grammar.buildjtiggrammar.Layer;
import grammar.buildjtiggrammar.NodeType;

import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;

import parser.derivedtree.DerivedTree;

import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class ElementaryTreePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DerivedTree	 rule;

	private mxGraph graph;

	private Object parent;
	
	public ElementaryTreePanel(DerivedTree rule) {
		this.rule = rule;
		graph = new mxGraph();
		parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		try
		{
			rule.paintWithJGraphX(graph);
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
}
