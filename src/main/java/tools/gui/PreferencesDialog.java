/**
 * 
 */
package tools.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import parser.early.JTIGParser;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class PreferencesDialog extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JButton close_button;

	private JCheckBox stopcriterion_checkbox;


	private JCheckBox showprediction_checkbox;


	private JTable table;


	private DefaultTableModel tm;


	private JCheckBox show_forest_checkbox;

	private JCheckBox store_derivedtrees_checkbox;

	private JCheckBox show_derivedtrees_checkbox;

	private JCheckBox show_independent_dtrees_checkbox;

	private JCheckBox show_dependent_dtrees_checkbox;
	
	public PreferencesDialog(){

		setModalityType(DEFAULT_MODALITY_TYPE);
		setTitle("JTIG Preferences");
		setResizable(false);
		
		JTabbedPane tabbedpane = new JTabbedPane(JTabbedPane.TOP,JTabbedPane.SCROLL_TAB_LAYOUT );

		tabbedpane.add( "General" , createGeneralPanel());
		tabbedpane.add( "Inference Rules" , createInferenceRulesPanel());
		tabbedpane.add( "Forest" , createIFRPanel());
		tabbedpane.add( "Derivation trees" , createDTPanel());
		tabbedpane.add( "Derived trees" , createPTPanel());
		
		JPanel panel_tabbed = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		
		c.insets = new Insets(5,5,5,5);
		c.weightx = 0.5;
		//c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth =2;
		panel_tabbed.add(tabbedpane,c);
		
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.LINE_END;
		close_button = new JButton("Close");
		close_button.addActionListener(this);
		panel_tabbed.add(close_button,c);
		
		getContentPane().add(panel_tabbed);
		setSize(new Dimension(500,400));
		pack();
	}

	private JPanel createDTPanel() {
		JPanel pane =  new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(5,5,5,5);
		{
			c.weightx = 1;
			c.weighty = 0;
			c.gridx = 0;
			c.gridy = 0;
		
			JLabel show_iderivationtrees_descriptionlabel = new JLabel("General:");
			pane.add(show_iderivationtrees_descriptionlabel,c);
			
			c.weightx = 1;
			c.gridx = 1;
			c.gridy = 0;

		
			show_independent_dtrees_checkbox = new JCheckBox("Draw independent derivation trees.");
			show_independent_dtrees_checkbox.addActionListener(this);
			show_independent_dtrees_checkbox.setSelected(JTIGParser.getBooleanProperty("gui.independentderivationtree.show"));
			pane.add(show_independent_dtrees_checkbox,c);
		}
		{
			c.weightx = 1;
			c.weighty = 100;
			c.gridx = 0;
			c.gridy = 1;
		
			JLabel show_dderivationtrees_descriptionlabel = new JLabel("");
			pane.add(show_dderivationtrees_descriptionlabel,c);
			
			c.weightx = 1;
			c.gridx = 1;
			c.gridy = 1;

		
			show_dependent_dtrees_checkbox = new JCheckBox("Draw dependent derivation trees.");
			show_dependent_dtrees_checkbox.addActionListener(this);
			show_dependent_dtrees_checkbox.setSelected(JTIGParser.getBooleanProperty("gui.dependentderivationtree.show"));
			pane.add(show_dependent_dtrees_checkbox,c);
		}
		return pane;
	}

	private JPanel createPTPanel() {
		JPanel pane =  new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(5,5,5,5);
		{
			c.weightx = 1;
			c.weighty = 0;
			c.gridx = 0;
			c.gridy = 0;
		
			JLabel show_derivedtrees_descriptionlabel = new JLabel("General:");
			pane.add(show_derivedtrees_descriptionlabel,c);
			
			c.weightx = 1;
			c.gridx = 1;
			c.gridy = 0;

		
			show_derivedtrees_checkbox = new JCheckBox("Draw parse trees.");
			show_derivedtrees_checkbox.addActionListener(this);
			show_derivedtrees_checkbox.setSelected(JTIGParser.getBooleanProperty("gui.derivedtree.show"));
			pane.add(show_derivedtrees_checkbox,c);
		}
		{
			c.weightx = 1;
			c.weighty = 100;
			c.gridx = 0;
			c.gridy = 1;
		
			JLabel store_derivedtrees_descriptionlabel = new JLabel("Save:");
			pane.add(store_derivedtrees_descriptionlabel,c);
		
			c.weightx = 1;
			c.gridx = 1;
			c.gridy = 1;

		
			store_derivedtrees_checkbox = new JCheckBox("Store created parse trees.");
			store_derivedtrees_checkbox.addActionListener(this);
			store_derivedtrees_checkbox.setSelected(JTIGParser.getBooleanProperty("parser.derivedtree.store"));
			pane.add(store_derivedtrees_checkbox,c);
		}
		return pane;
	}

	private JPanel createIFRPanel() {
		JPanel ifrpanel = new JPanel(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(5,5,5,5);
		{
			c.weighty = 0;
			c.weightx = 0;
			c.gridx = 0;
			c.gridy = 0;
		
			JLabel showgeneral_descriptionlabel = new JLabel("General:");
			ifrpanel.add(showgeneral_descriptionlabel,c);
		
			c.weighty = 1;
			c.weightx = 1;
			c.gridx = 1;
			c.gridy = 0;

		
			show_forest_checkbox = new JCheckBox("Draw item forest");
			show_forest_checkbox.addActionListener(this);
			show_forest_checkbox.setSelected(JTIGParser.getBooleanProperty("gui.forest.show"));
			ifrpanel.add(show_forest_checkbox,c);
		}
		{
			c.weighty = 0;
			c.weightx = 0;
			c.gridx = 0;
			c.gridy = 1;

			JLabel showprediction_descriptionlabel = new JLabel("Prediction:");
			ifrpanel.add(showprediction_descriptionlabel,c);

			c.weighty = 10;
			c.weightx = 10;
			c.gridx = 1;
			c.gridy = 1;
			c.gridwidth = 1;
		
			showprediction_checkbox = new JCheckBox("Show predicted items in graph");
			showprediction_checkbox.addActionListener(this);
			showprediction_checkbox.setSelected(JTIGParser.getBooleanProperty("gui.forest.showpredictions"));
			ifrpanel.add(showprediction_checkbox,c);
		}
		return ifrpanel;
	}

	private Component createGeneralPanel() {
		JPanel panel_preferences = new JPanel(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;		
		c.anchor = GridBagConstraints.NORTHWEST;
		c.weighty = 20;
		c.weightx = 0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		
		JLabel stopcriterion_descriptionlabel = new JLabel("Stop criterion:");
		panel_preferences.add(stopcriterion_descriptionlabel,c);
		c.anchor = GridBagConstraints.NORTH;
		c.gridx = 1;
		c.weightx = 10;
		c.gridy = 0;
		c.gridwidth = 2;
		
		stopcriterion_checkbox = new JCheckBox("Stop on first terminal element.");
		stopcriterion_checkbox.addActionListener(this);
		stopcriterion_checkbox.setSelected(JTIGParser.getBooleanProperty("parser.stoponfirsttermitem"));
		panel_preferences.add(stopcriterion_checkbox,c);
		
		//TODO parser.lookup.exhaustive
		
		return panel_preferences;
	}

	private JPanel createInferenceRulesPanel() {
		JPanel newpanel = new JPanel();
		tm = new DefaultTableModel();
		updateTable();
		table = new JTable(tm);
		newpanel.add(new JScrollPane( table ));
		return newpanel;
	}

	private void updateTable(){
		String rulenamescommaseparated = JTIGParser.getProperty("parser.core.inferencerules");
		if (rulenamescommaseparated == null)
			return;
		String[] rulefullqualifiednames = rulenamescommaseparated.split("\\s*,\\s*");
		String[][] dataVector = new String[rulefullqualifiednames.length][2];
		for (int i = 0; i < rulefullqualifiednames.length; i++){
			dataVector[i][0] = ""+(i+1);
			dataVector[i][1] = rulefullqualifiednames[i];
		}
		tm.setDataVector(dataVector, new String[]{"Position","Executed inference rule"});
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == stopcriterion_checkbox){
			JTIGParser.setProperty("parser.stoponfirsttermitem", stopcriterion_checkbox.isSelected()?"true":"false");
		} else if(e.getSource() == showprediction_checkbox){
			JTIGParser.setProperty("gui.forest.showpredictions", showprediction_checkbox.isSelected()?"true":"false");
		} else if(e.getSource() == show_forest_checkbox){
			JTIGParser.setProperty("gui.forest.show", show_forest_checkbox.isSelected()?"true":"false");
		} else if(e.getSource() == close_button){
			setVisible(false);
		} else if (e.getSource() == store_derivedtrees_checkbox){
			JTIGParser.setProperty("parser.derivedtree.store", store_derivedtrees_checkbox.isSelected()?"true":"false");
		} else if (e.getSource() == show_derivedtrees_checkbox){
			JTIGParser.setProperty("gui.derivedtree.show", show_derivedtrees_checkbox.isSelected()?"true":"false");
		} else if (e.getSource() == show_independent_dtrees_checkbox){
			JTIGParser.setProperty("gui.independentderivationtree.show", show_independent_dtrees_checkbox.isSelected()?"true":"false");
		} else if (e.getSource() == show_dependent_dtrees_checkbox){
			JTIGParser.setProperty("gui.dependentderivationtree.show", show_dependent_dtrees_checkbox.isSelected()?"true":"false");
		}
	}

}
