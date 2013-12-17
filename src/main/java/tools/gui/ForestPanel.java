package tools.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import parser.output.forest.Forest;

public class ForestPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8210620830144304280L;

	private final Forest forest;
	
	private final int pos;
	
	public ForestPanel(Forest forest, int i) {
		this.pos = i;
		this.forest = forest;
		setLayout(new GridBagLayout());
	}

	public void drawItem(){
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTH;
		mxGraph graph = new mxGraph();

		graph.getModel().beginUpdate();
		try
		{
			forest.paintWithJGraphX(graph,pos);
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
			JTextField field_root = new JTextField("  ");
			field_root.setColumns(2);
			field_root.setEditable(false);
			field_root.setBackground(new Color(0xF4A460));
			field_root.setFocusable(false);
			field_root.setMinimumSize(field_root.getPreferredSize());
		
			pane.add(field_root,gbc);
			
			gbc.gridx = 1;
			gbc.gridy = 0;
			JLabel label_root = new JLabel(" root item");
			pane.add(label_root,gbc);
		}
		{
			gbc.gridx = 0;
			gbc.gridy = 1;
			JTextField field_laitem = new JTextField("  ");
			field_laitem.setColumns(2);
			field_laitem.setEditable(false);
			field_laitem.setBackground(new Color(0x97FFFF));
			field_laitem.setFocusable(false);
		
			pane.add(field_laitem,gbc);
			
			gbc.gridx = 1;
			gbc.gridy = 1;
			JLabel label_laitem = new JLabel(" lexical anchor item");
			pane.add(label_laitem,gbc);
		}
		{
			gbc.gridx = 0;
			gbc.gridy = 2;
			JTextField field_active = new JTextField("  ");
			field_active.setColumns(2);
			field_active.setEditable(false);
			field_active.setBackground(new Color(0xFA8072));
			field_active.setFocusable(false);
		
			pane.add(field_active,gbc);
			
			gbc.gridx = 1;
			gbc.gridy = 2;
			JLabel label_active = new JLabel(" active item");
			pane.add(label_active,gbc);
		}
		{
			gbc.gridx = 0;
			gbc.gridy = 3;
			JTextField field_passive = new JTextField("  ");
			field_passive.setColumns(2);
			field_passive.setEditable(false);
			field_passive.setBackground(new Color(0xC6E2FF));
			field_passive.setFocusable(false);
		
			pane.add(field_passive,gbc);
			
			gbc.gridx = 1;
			gbc.gridy = 3;
			JLabel label_passive = new JLabel(" passive item");
			pane.add(label_passive,gbc);
		}
		return pane;
	}
}
