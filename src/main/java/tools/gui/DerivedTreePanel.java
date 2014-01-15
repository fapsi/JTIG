package tools.gui;


import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import parser.output.derivedtree.DerivedTree;

import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class DerivedTreePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private mxGraph graph;

	private Object parent;
	
	public DerivedTreePanel(DerivedTree rule) {
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTH;
		
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
		
		add(graphComponent,gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0;
		gbc.gridx = 1;
		add(createLegend(),gbc);
		
		validate();
		
	}

	private JPanel createLegend() {
		JPanel pane = new JPanel();
		pane.setBorder(new TitledBorder("Legend"));
		pane.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.NORTH;
		{
			gbc.gridx = 0;
			gbc.gridy = 0;
			JTextField field_normal = new JTextField("  ");
			field_normal.setColumns(2);
			field_normal.setEditable(false);
			field_normal.setBackground(new Color(0xF5F5F5));
			field_normal.setFocusable(false);
			field_normal.setMinimumSize(field_normal.getPreferredSize());
		
			pane.add(field_normal,gbc);
			
			gbc.gridx = 1;
			gbc.gridy = 0;
			JLabel label_normal = new JLabel(" inner node");
			pane.add(label_normal,gbc);
		}
		{
			gbc.gridx = 0;
			gbc.gridy = 1;
			JTextField field_term = new JTextField("  ");
			field_term.setColumns(2);
			field_term.setEditable(false);
			field_term.setBackground(new Color(0x9ACD320));
			field_term.setFocusable(false);
			field_term.setMinimumSize(field_term.getPreferredSize());
			
			pane.add(field_term,gbc);
			
			gbc.gridx = 1;
			gbc.gridy = 1;
			JLabel label_term = new JLabel(" term node");
			pane.add(label_term,gbc);
		}
		{
			gbc.gridx = 0;
			gbc.gridy = 2;
			JTextField field_subst = new JTextField("\u2193");
			field_subst.setColumns(2);
			field_subst.setEditable(false);
			field_subst.setBackground(new Color(0xFF8C00));
			field_subst.setFocusable(false);
			field_subst.setMinimumSize(field_subst.getPreferredSize());

			pane.add(field_subst,gbc);
			
			gbc.gridx = 1;
			gbc.gridy = 2;
			JLabel label_subst = new JLabel(" substitution node");
			pane.add(label_subst,gbc);
		}
		{
			gbc.gridx = 0;
			gbc.gridy = 3;
			JTextField field_adj = new JTextField("*");
			field_adj.setColumns(2);
			field_adj.setEditable(false);
			field_adj.setBackground(new Color(0xDC143C));
			field_adj.setFocusable(false);
			field_adj.setMinimumSize(field_adj.getPreferredSize());
		
			pane.add(field_adj,gbc);
			
			gbc.gridx = 1;
			gbc.gridy = 3;
			JLabel label_adj = new JLabel(" adjunction node");
			pane.add(label_adj,gbc);
		}
		{
			gbc.gridx = 0;
			gbc.gridy = 4;
			JTextField field_eps = new JTextField("\u03B5");
			field_eps.setColumns(2);
			field_eps.setEditable(false);
			field_eps.setBackground(new Color(0x008080));
			field_eps.setFocusable(false);
			field_eps.setMinimumSize(field_eps.getPreferredSize());
		
			pane.add(field_eps,gbc);
			
			gbc.gridx = 1;
			gbc.gridy = 4;
			JLabel label_eps = new JLabel(" epsilon node");
			pane.add(label_eps,gbc);
		}
		return pane;
	}
}
