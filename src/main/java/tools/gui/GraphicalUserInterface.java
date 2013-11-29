package tools.gui;

import grammar.buildjtiggrammar.ElementaryTree;
import grammar.buildjtiggrammar.Lexicon;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;

import parser.derivationtree.DependentDerivationTree;
import parser.derivationtree.DerivationTree;
import parser.derivationtree.IndependentDerivationTree;
import parser.derivedtree.DerivedTree;
import parser.early.Item;
import parser.early.JTIGParser;
import parser.early.ParseRun;
import tools.tokenizer.MorphAdornoSentenceTokenizer;
import tools.tokenizer.Token;

public class GraphicalUserInterface extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	private JTabbedPane mainpanel;
		
	private JTextArea parse_input;

	private JButton parse_button;
	
	private JTIGParser jtigparser;

	private JMenuItem menuItem_preferences;

	private PreferencesDialog preferences;

	private JMenuItem menuItem_showlexicon;

	private JCheckBoxMenuItem menuItem_computeIDT;

	private JCheckBoxMenuItem menuItem_computeDDT;

	private JCheckBoxMenuItem menuItem_computePT;
	
	public GraphicalUserInterface() throws IOException {
		super("JTIG Parser");
		
		jtigparser = new JTIGParser();
		
		createMenu();
		
		createDialogs();
		
		createBody();
		
	}

	private void createBody() {
		JPanel input_parse_panel = new JPanel();
		input_parse_panel.setBorder(new TitledBorder("Input"));
		
		JPanel action_parse_panel = new JPanel();
		action_parse_panel.setBorder(new TitledBorder("Actions"));
		
		parse_input = new JTextArea("a pretty pretty boy sleeps often",5,100);
		parse_input.setSize(100, 10);
		input_parse_panel.add(parse_input);
		
		fillActionParsePanel(action_parse_panel);
		
		mainpanel = new JTabbedPane();
		mainpanel.setBorder(new TitledBorder("Output"));
		
		getContentPane().setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(5,5,5,5);
		c.weightx = 100;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 2;
		c.gridwidth = 2;
		
		getContentPane().add(input_parse_panel,c);
		
		c.weighty = 1;
		c.weightx = 0;
		c.gridx = 2;
		c.gridy = 0;
		c.gridheight = 2;
		c.gridwidth = 1;
		
		getContentPane().add(action_parse_panel,c);
		
		c.weighty = 100;
		c.weightx = 100;
		c.gridx = 0;
		c.gridy = 2;
		c.gridheight = 1;
		c.gridwidth = 3;
		
		getContentPane().add(mainpanel,c);
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
		
		menuItem_computeIDT = new JCheckBoxMenuItem("independent derivation trees");
		menuItem_computeIDT.addActionListener(this);
		menu_parser.add(menuItem_computeIDT);
		
		menuItem_computeDDT = new JCheckBoxMenuItem("dependent derivation trees");
		menuItem_computeDDT.addActionListener(this);
		menu_parser.add(menuItem_computeDDT);
		
		menuItem_computePT = new JCheckBoxMenuItem("parse trees");
		menuItem_computePT.addActionListener(this);
		menu_parser.add(menuItem_computePT);
		
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
		preferences = new PreferencesDialog(jtigparser);
		preferences.setLocationRelativeTo(this);
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO improve code style
		if (e.getSource() == parse_button){   
			MorphAdornoSentenceTokenizer st = new MorphAdornoSentenceTokenizer();
			Token[] tokens = st.getTokens(parse_input.getText());
			if(jtigparser.hasLexicon()){
				
				ParseRun run = jtigparser.parseSentence(parse_input.getText(), tokens);
				
				List<IndependentDerivationTree> tmpidt = run.retrieveIndependentDerivationTrees();
				
				List<DependentDerivationTree> tmpddt = run.retrieveDependentDerivationTrees();	
				
				List<DerivedTree> tmpdt = run.retrieveDerivedTrees();
				
				mainpanel.removeAll();
				
				addLogPanel(run.getLog());
				if (JTIGParser.getBooleanProperty("gui.forest.show"))
					printItems(run.getItemList());
				if (JTIGParser.getBooleanProperty("gui.independentderivationtree.show"))
					printDerivationTrees(tmpidt);
				if (JTIGParser.getBooleanProperty("gui.dependentderivationtree.show"))
					printDerivationTrees(tmpddt);
				if (JTIGParser.getBooleanProperty("gui.parsetree.show"))
					printDerivedTrees(tmpdt);
			} else {
				addLogPanel("There exists no lexicon. Aborted.");
			}
				
		} else if (e.getSource() == menuItem_preferences){
			
			preferences.setVisible(true);
		} else if (e.getSource() == menuItem_showlexicon){
			String result = JOptionPane.showInputDialog("Lexical anchor of the lexicon element(s) to be searched.");
			if (result != null && !result.trim().isEmpty() && jtigparser.hasLexicon()){
				Lexicon l = jtigparser.getLexicon();
				MorphAdornoSentenceTokenizer st = new MorphAdornoSentenceTokenizer();
				Token[] tokens = st.getTokens(result);
				int i = 1;
				// TODO use printDerivedTrees
				for (ElementaryTree r : l.find(Arrays.asList(tokens), 0)){
					ElementaryTreePanel tigrulepanel = new ElementaryTreePanel(new DerivedTree(r));
					addTab(result+" "+i,tigrulepanel);
					i++;
				}
			}
		}
		
	}

	private void printDerivationTrees(List<? extends DerivationTree> trees) {
		int i = 1;
		for (DerivationTree dtree :  trees){
			DerivationTreePanel actualpanel = new DerivationTreePanel(dtree);
			actualpanel.paint();
			addTab(dtree.toString()+" "+i,actualpanel);
			i++;
		}
	}
	
	private void printDerivedTrees(List<DerivedTree> trees){
		for (DerivedTree dtree : trees ){
			ElementaryTreePanel derivedtreepanel = new ElementaryTreePanel(dtree);
			addTab("Parse tree",derivedtreepanel);
		}
	}

	private void addLogPanel(String alternative){
		LogPanel logpanel = new LogPanel(alternative == null?jtigparser.getLog():alternative);
		addTab("Log",logpanel);
	}
	
	private void printItems(List<Item> items) {
				
		int i = 1;
		for (Item item : items){
			ItemPanel actualpanel = new ItemPanel(item);
			actualpanel.drawItem();
			addTab("Forest item "+i,actualpanel);
			i++;
		}
		
	}
	
	private void addTab(String description,JPanel tab){
		mainpanel.add(description,tab);
		mainpanel.setTabComponentAt(mainpanel.getTabCount()-1,new ButtonTabComponent(mainpanel));
		
	}
	
	public static void main(String[] args) throws IOException {
		GraphicalUserInterface frame = new GraphicalUserInterface();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
	}
}
