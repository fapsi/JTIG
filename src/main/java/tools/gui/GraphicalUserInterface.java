package tools.gui;

import grammar.treeinsertion.ElementaryTree;
import grammar.treeinsertion.Lexicon;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import parser.early.JTIGParser;
import parser.early.run.ParseLevel;
import parser.early.run.ParseResult;
import parser.output.derivationtree.DependentDerivationTree;
import parser.output.derivationtree.DerivationTree;
import parser.output.derivationtree.IndependentDerivationTree;
import parser.output.derivedtree.DerivedTree;
import parser.output.forest.Forest;
import tools.GeneralTools;
import tools.tokenizer.MorphAdornoSentenceTokenizer;
import tools.tokenizer.Token;

public class GraphicalUserInterface extends JFrame implements ActionListener {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -2464216667581969610L;

	private JTabbedPane mainpanel;
		
	private JTextField parse_input;

	private JButton parse_button;
	
	private JTIGParser jtigparser;

	private JMenuItem menuItem_preferences;

	private PreferencesDialog preferences;

	private JMenuItem menuItem_showlexicon;

	private JCheckBoxMenuItem menuItem_computeIDT;

	private JCheckBoxMenuItem menuItem_computeDDT;

	private JCheckBoxMenuItem menuItem_computePT;

	private JFileChooser filechooser;

	private JLabel lexiconfile_label;

	private JButton lexiconfile_button;

	private JCheckBoxMenuItem menuItem_computeFOREST;
	
	public GraphicalUserInterface(JTIGParser jtigparser) throws Exception {
		super("JTIG Parser");
		
		this.jtigparser = jtigparser;
		
		createMenu();
		
		createDialogs();
		
		createBody();
		
	}

	private void createBody() {
		JPanel action_parse_panel = new JPanel();
		action_parse_panel.setBorder(new TitledBorder("Actions"));
		
		
		fillActionParsePanel(action_parse_panel);
		
		mainpanel = new JTabbedPane();
		mainpanel.setBorder(new TitledBorder("Output"));
		
		getContentPane().setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(5,5,5,5);
		
		c.weightx = 5;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 1;
		c.gridwidth = 1;
		
		getContentPane().add(createLexiconPanel(),c);
		c.weightx = 10;
		c.gridx = 1;
		
		getContentPane().add(createInputPanel(),c);
		c.weightx = 0;
		c.gridx = 2;
		getContentPane().add(action_parse_panel,c);
		
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1000;
		c.gridx = 0;
		c.gridy = 1;
		c.gridheight = 1;
		c.gridwidth = 3;
		
		getContentPane().add(mainpanel,c);
	}

	private JPanel createLexiconPanel() {
		filechooser = new JFileChooser();
		filechooser.setDialogTitle("Choose lexicon path...");
		filechooser.setAcceptAllFileFilterUsed(false);
		filechooser.addChoosableFileFilter(new FileNameExtensionFilter("Lexicon files", "xml", "lisp"));
		
		JPanel lexicon_panel = new JPanel();
		lexicon_panel.setBorder(new TitledBorder("Lexicon"));
		lexicon_panel.setLayout(new GridBagLayout());
		
        
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.NORTHWEST;
		
		c.insets = new Insets(5,5,5,5);
		c.weightx = 0;
		c.weighty = 0;		
		
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 1;
		
		boolean success = jtigparser.readLexicon();
		if (!success)
        	JOptionPane.showMessageDialog(this, "Invalid lexicon. " + jtigparser.getLastError());
		
		String description = "<html>"
        		+(success?"<font color=green>"+GeneralTools.getEndOfPath(JTIGParser.getProperty("grammar.lexicon.path"))+" (valid)</font>":
            			"<font color=red>Lexicon not found</font>")
            		+"</html>";
		
		lexiconfile_label = new JLabel(description);
		lexicon_panel.add(lexiconfile_label,c);
		
		c.weighty = 4;
		c.gridx = 0;
		c.gridy = 1;
		
		lexiconfile_button = new JButton("Choose...");
		lexiconfile_button.addActionListener(this);
		lexicon_panel.add(lexiconfile_button,c);
		
		
		return lexicon_panel;
	}

	private JPanel createInputPanel() {
		JPanel input_parse_panel = new JPanel();
		input_parse_panel.setBorder(new TitledBorder("Input sentence"));
		input_parse_panel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.NORTHWEST;
		
		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 0;
				
		parse_input = new JTextField("a pretty pretty boy sleeps often",80);
		parse_input.setMinimumSize(parse_input.getPreferredSize());
		input_parse_panel.add(parse_input,c);
		
		return input_parse_panel;
	}

	private void fillActionParsePanel(JPanel action_parse_panel) {
		parse_button = new JButton("Start parsing");
		parse_button.addActionListener(this);
		action_parse_panel.add(parse_button);
	}

	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();

		JMenu menu_parser = new JMenu("Parser");
		menu_parser.setMnemonic(KeyEvent.VK_P);
		menuBar.add(menu_parser);
		JMenu menu_lexicon = new JMenu("Lexicon");
		menu_lexicon.setMnemonic(KeyEvent.VK_L);
		menuBar.add(menu_lexicon);
		
		JMenuItem parselevel = new JMenu("Level");
		menu_parser.add(parselevel);
		
		menuItem_computeFOREST = new JCheckBoxMenuItem("forest");
		menuItem_computeFOREST.addActionListener(this);
		parselevel.add(menuItem_computeFOREST);
		
		menuItem_computeIDT = new JCheckBoxMenuItem("independent derivation trees");
		menuItem_computeIDT.addActionListener(this);
		parselevel.add(menuItem_computeIDT);
		
		menuItem_computeDDT = new JCheckBoxMenuItem("dependent derivation trees");
		menuItem_computeDDT.addActionListener(this);
		parselevel.add(menuItem_computeDDT);
		
		menuItem_computePT = new JCheckBoxMenuItem("parse trees");
		menuItem_computePT.addActionListener(this);
		parselevel.add(menuItem_computePT);
		
		setParseLevelMenuItems();
		
		menu_parser.addSeparator();
		
		menuItem_preferences = new JMenuItem("Preferences",KeyEvent.VK_T);
		menuItem_preferences.addActionListener(this);
		menuItem_preferences.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		menu_parser.add(menuItem_preferences);
		
		menuItem_showlexicon = new JMenuItem("Show lexicon-entry...",KeyEvent.VK_T);
		menuItem_showlexicon.addActionListener(this);
		menuItem_showlexicon.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		menu_lexicon.add(menuItem_showlexicon);
		
		setJMenuBar(menuBar);
	}
	
	private void createDialogs() {
		preferences = new PreferencesDialog();
		preferences.setLocationRelativeTo(this);
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO improve code style
		if (e.getSource() == parse_button){   
			MorphAdornoSentenceTokenizer st = new MorphAdornoSentenceTokenizer();
			Token[] tokens = st.getTokens(parse_input.getText());
			if(jtigparser.hasLexicon()){
				
				ParseResult result = jtigparser.parseSentence(parse_input.getText(), tokens);
				
				List<IndependentDerivationTree> tmpidt = result.getIndependentDerivationTrees();
				
				List<DependentDerivationTree> tmpddt = result.getDependentDerivationTrees();	
				
				List<DerivedTree> tmpdt = result.getDerivedTrees();
				
				
				mainpanel.removeAll();
				
				addLogPanel(result.getLog(),result.getParseDetails());
				
				if (JTIGParser.getBooleanProperty("gui.forest.show"))
					printItems(result.getForest());
				if (JTIGParser.getBooleanProperty("gui.independentderivationtree.show"))
					printDerivationTrees(tmpidt);
				if (JTIGParser.getBooleanProperty("gui.dependentderivationtree.show"))
					printDerivationTrees(tmpddt);
				if (JTIGParser.getBooleanProperty("gui.derivedtree.show"))
					printDerivedTrees(tmpdt);
			} else {
				addLogPanel("There exists no lexicon. Aborted.","");
			}
				//pack();
		} else if (e.getSource() == menuItem_preferences){
			
			preferences.setVisible(true);
		} else if (e.getSource() == menuItem_showlexicon){
			String result = JOptionPane.showInputDialog("Lexical anchor of the lexicon element(s) to be searched.");
			if (result != null && !result.trim().isEmpty() && jtigparser.hasLexicon()){
				Lexicon l = jtigparser.getLexicon();
				MorphAdornoSentenceTokenizer st = new MorphAdornoSentenceTokenizer();
				Token[] tokens = st.getTokens(result);
				// TODO use printDerivedTrees
				int i = 0;
				for (ElementaryTree r : l.find(Arrays.asList(tokens), 0)){
					DerivedTreePanel tigrulepanel = new DerivedTreePanel(new DerivedTree(r));
					addTab(result+" "+r.getIndex(),tigrulepanel);
					i++;
				}
				System.out.println(i);
			}
		} else if (e.getSource() == lexiconfile_button) {
			
			int returnVal = filechooser.showOpenDialog(this);

	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = filechooser.getSelectedFile();
	            JTIGParser.setProperty("grammar.lexicon.path", file.getAbsolutePath());

	            boolean success = jtigparser.readLexicon();
	            lexiconfile_label.setText(
	            		"<html>"
	            		+(success?"<font color=green>"+GeneralTools.getEndOfPath(JTIGParser.getProperty("grammar.lexicon.path"))+" (valid)</font>":
	            			"<font color=red>Lexicon not found</font>")
	            		+"</html>"
	            		);
	            if (!success)
	            	JOptionPane.showMessageDialog(this, "Invalid lexicon. " + jtigparser.getLastError());
	        }
		} else if (e.getSource() == menuItem_computeFOREST) {
			JTIGParser.setProperty("parser.core.parselevel",JTIGParser.canExecute(ParseLevel.FOREST)?ParseLevel.LOOKUP.toString():ParseLevel.FOREST.toString());
			setParseLevelMenuItems();
		} else if (e.getSource() == menuItem_computeIDT) {
			JTIGParser.setProperty("parser.core.parselevel",JTIGParser.canExecute(ParseLevel.INDEPENDENTDTREE)?ParseLevel.FOREST.toString():ParseLevel.INDEPENDENTDTREE.toString());
			setParseLevelMenuItems();
		} else if (e.getSource() == menuItem_computeDDT) {
			JTIGParser.setProperty("parser.core.parselevel",JTIGParser.canExecute(ParseLevel.DEPENDENTDTREE)?ParseLevel.INDEPENDENTDTREE.toString():ParseLevel.DEPENDENTDTREE.toString());
			setParseLevelMenuItems();
		} else if (e.getSource() == menuItem_computePT) {
			JTIGParser.setProperty("parser.core.parselevel",JTIGParser.canExecute(ParseLevel.DERIVEDTREE)?ParseLevel.DEPENDENTDTREE.toString():ParseLevel.DERIVEDTREE.toString());
			setParseLevelMenuItems();
		}
	}

	private void setParseLevelMenuItems() {
		menuItem_computeFOREST.setSelected(JTIGParser.canExecute(ParseLevel.FOREST));
		menuItem_computeIDT.setSelected(JTIGParser.canExecute(ParseLevel.INDEPENDENTDTREE));
		menuItem_computeDDT.setSelected(JTIGParser.canExecute(ParseLevel.DEPENDENTDTREE));
		menuItem_computePT.setSelected(JTIGParser.canExecute(ParseLevel.DERIVEDTREE));
	}

	private void printDerivationTrees(List<? extends DerivationTree> trees) {
		if (trees == null)
			return;
		int i = 1;
		for (DerivationTree dtree :  trees){
			DerivationTreePanel actualpanel = new DerivationTreePanel(dtree);
			actualpanel.paint();
			addTab(dtree.toString()+" "+i,actualpanel);
			i++;
		}
	}
	
	private void printDerivedTrees(List<DerivedTree> trees){
		if (trees == null)
			return;
		for (DerivedTree dtree : trees ){
			DerivedTreePanel derivedtreepanel = new DerivedTreePanel(dtree);
			addTab("Parse tree",derivedtreepanel);
		}
	}

	private void addLogPanel(String log,String details){
		LogPanel logpanel = new LogPanel(log,details);
		addTab("Log",logpanel);
	}
	
	private void printItems(Forest forest) {

		for (int i = 0; i < forest.getRootDimension(); i++){
			ForestPanel actualpanel = new ForestPanel(forest,i);
			actualpanel.drawItem();
			addTab("Forest item "+(i+1),actualpanel);
		}
		
	}
	
	private void addTab(String description,JPanel tab){
		mainpanel.add(description,tab);
		mainpanel.setTabComponentAt(mainpanel.getTabCount()-1,new ButtonTabComponent(mainpanel));
		
	}
}
