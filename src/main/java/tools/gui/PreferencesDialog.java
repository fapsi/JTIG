/**
 * 
 */
package tools.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

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
	
	private JPanel panel_preferences;
	
	private JFileChooser filechooser;

	private JLabel lexiconfile_label;

	private JButton lexiconfile_button;

	private JButton close_button;
	
	private JTIGParser jtigparser;
	
	public PreferencesDialog(JTIGParser jtigparser){
		this.jtigparser = jtigparser;
		setModalityType(DEFAULT_MODALITY_TYPE);
		
		setTitle("JTIG Preferences");
		setResizable(false);
		
		JTabbedPane tabbedpane = new JTabbedPane(JTabbedPane.TOP,JTabbedPane.SCROLL_TAB_LAYOUT );
		panel_preferences = new JPanel(new GridBagLayout());
		filechooser = new JFileChooser();
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		
		c.insets = new Insets(5,5,5,5);
		c.weightx = 0.5;
		
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 2;
		
		JLabel lexiconfile_descriptionlabel = new JLabel("Lexicon path:");
		panel_preferences.add(lexiconfile_descriptionlabel,c);
		
		c.gridx = 1;
		c.gridy = 0;
		c.gridheight = 1;
		
		lexiconfile_label = new JLabel(jtigparser.readLexicon()?JTIGParser.getProperty("grammar.lexicon.path")+" (valid)":"Lexicon not found");
		panel_preferences.add(lexiconfile_label,c);
		
		
		c.gridx = 1;
		c.gridy = 1;
		
		lexiconfile_button = new JButton("Choose path");
		lexiconfile_button.addActionListener(this);
		panel_preferences.add(lexiconfile_button,c);
		
		
			
		tabbedpane.add( "General" , panel_preferences);
		tabbedpane.add( "Modules" , new JPanel());
		tabbedpane.add( "Outputs" , new JPanel());
		
		JPanel panel_tabbed = new JPanel(new GridBagLayout());
		
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
		} else if(e.getSource() == close_button){
			setVisible(false);
		}
	}

}
