/**
 * 
 */
package tools.gui;

import java.awt.GridBagConstraints;

import javax.swing.JPanel;

import parser.output.derivationtree.DerivationTree;

import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class DerivationTreePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private DerivationTree dtree;
	
	public DerivationTreePanel(DerivationTree dtree){
		this.dtree = dtree;
	}
	
	public void paint(){
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTH;
		mxGraph graph = new mxGraph();

		graph.getModel().beginUpdate();
		try
		{
			dtree.paintWithJGraphX(graph);
		}
		finally
		{
			graph.getModel().endUpdate();
		}
		
		mxCompactTreeLayout layout = new mxCompactTreeLayout(graph);
		layout.setHorizontal(false);
		layout.setEdgeRouting(false);
		layout.execute(graph.getDefaultParent());

		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		
		add(graphComponent,gbc);
		
		validate();
		
	}
}
