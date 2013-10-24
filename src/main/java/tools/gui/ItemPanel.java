package tools.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import parser.early.DerivationType;
import parser.early.Item;
import parser.early.ItemDerivation;

public class ItemPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Item item;

	private mxGraph graph;

	private Object parent;
	
	public ItemPanel(Item item){
		this.item = item;
		setLayout(new GridBagLayout());
	}
	
	public void drawItem(){
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1;
		gbc.weighty = 1;
		//gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTH;
		graph = new mxGraph();
		parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		try
		{
			printItem(item,null,null);
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
		
		add(graphComponent,gbc);
		
		
		validate();
	}
	
	private void printItem(Item item,Object p,DerivationType type) {
		String style="";
		if (item.getActivatedTIGRule() == null)
			style = "fillColor=red";
		if (p == null)
			style = "fillColor=gray";
		Object v1 = graph.insertVertex(parent, null,item.getDottedRuleString(), 0, 0, 80,20,style);
		
		if (p != null)
			graph.insertEdge(parent, null, type.toString(), p, v1);

		for (ItemDerivation derivation : item.getDerivations()){
			if (derivation.getType() == DerivationType.PredictTraversation
					|| derivation.getType() == DerivationType.PredictSubstitution)
				return;
			
			for (Item current : derivation.getItems()) {
				printItem(current,v1,derivation.getType());
			}
		}
		
	}
}
