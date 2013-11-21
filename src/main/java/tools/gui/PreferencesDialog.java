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
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
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
	
	
	private JFileChooser filechooser;

	private JLabel lexiconfile_label;

	private JButton lexiconfile_button;

	private JButton close_button;
	
	private JTIGParser jtigparser;


	private JCheckBox stopcriterion_checkbox;


	private JCheckBox showprediction_checkbox;


	private JTable table;


	private DefaultTableModel tm;


	private JCheckBox drawforest_checkbox;
	
	public PreferencesDialog(JTIGParser jtigparser){
		this.jtigparser = jtigparser;
		setModalityType(DEFAULT_MODALITY_TYPE);
		setTitle("JTIG Preferences");
		setResizable(false);
		
		JTabbedPane tabbedpane = new JTabbedPane(JTabbedPane.TOP,JTabbedPane.SCROLL_TAB_LAYOUT );

		tabbedpane.add( "General" , createGeneralPanel());
		tabbedpane.add( "Inference Rules" , createInferenceRulesPanel());
		tabbedpane.add( "Forest" , createIFRPanel());
		tabbedpane.add( "Outputs" , new JPanel());
		
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

	private JPanel createIFRPanel() {
		JPanel ifrpanel = new JPanel(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(5,5,5,5);
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

		
		drawforest_checkbox = new JCheckBox("Draw item forest");
		drawforest_checkbox.addActionListener(this);
		drawforest_checkbox.setSelected(JTIGParser.getBooleanProperty("gui.forest.showpredictions"));
		ifrpanel.add(drawforest_checkbox,c);
		
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
		
		return ifrpanel;
	}

	private Component createGeneralPanel() {
		JPanel panel_preferences = new JPanel(new GridBagLayout());
		filechooser = new JFileChooser();
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.NORTHWEST;
		
		c.insets = new Insets(5,5,5,5);
		c.weightx = 0;
		c.weighty = 0;		
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 2;
		
		JLabel lexiconfile_descriptionlabel = new JLabel("Lexicon path:");
		panel_preferences.add(lexiconfile_descriptionlabel,c);
		
		c.gridx = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.gridy = 0;
		c.weightx = 10;
		c.weighty = 0;
		c.gridheight = 1;
		
		lexiconfile_label = new JLabel(jtigparser.readLexicon()?JTIGParser.getProperty("grammar.lexicon.path")+" (valid)":"Lexicon not found");
		panel_preferences.add(lexiconfile_label,c);
		
		c.weighty = 0;
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 1;
		c.gridy = 1;
		
		lexiconfile_button = new JButton("Choose path");
		lexiconfile_button.addActionListener(this);
		panel_preferences.add(lexiconfile_button,c);
		
		c.anchor = GridBagConstraints.NORTHWEST;
		c.weighty = 20;
		c.weightx = 0;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		
		JLabel stopcriterion_descriptionlabel = new JLabel("Stop criterion:");
		panel_preferences.add(stopcriterion_descriptionlabel,c);
		c.anchor = GridBagConstraints.NORTH;
		c.gridx = 1;
		c.weightx = 10;
		c.gridy = 2;
		c.gridwidth = 2;
		
		stopcriterion_checkbox = new JCheckBox("Stop on first terminal element.");
		stopcriterion_checkbox.addActionListener(this);
		stopcriterion_checkbox.setSelected(JTIGParser.getBooleanProperty("parser.stoponfirsttermitem"));
		panel_preferences.add(stopcriterion_checkbox,c);
		
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
		if (e.getSource() == lexiconfile_button) {
			
			int returnVal = filechooser.showOpenDialog(this);

	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = filechooser.getSelectedFile();
	            JTIGParser.setProperty("grammar.lexicon.path", file.getAbsolutePath());

	            lexiconfile_label.setText(
	            		jtigparser.readLexicon()?JTIGParser.getProperty("grammar.lexicon.path")+" (valid)":"Lexicon not found");
	            pack();
	        } 
		} else if(e.getSource() == stopcriterion_checkbox){
			JTIGParser.setProperty("parser.stoponfirsttermitem", stopcriterion_checkbox.isSelected()?"true":"false");
		} else if(e.getSource() == showprediction_checkbox){
			JTIGParser.setProperty("gui.forest.showpredictions", showprediction_checkbox.isSelected()?"true":"false");
		} else if(e.getSource() == close_button){
			setVisible(false);
		}
	}

}
