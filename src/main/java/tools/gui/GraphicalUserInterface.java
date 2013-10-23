package tools.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import grammar.buildJtigGrammar.Entry;
import grammar.buildJtigGrammar.Lexicon;
import grammar.buildJtigGrammar.NodeType;
import grammar.buildJtigGrammar.Layer;
import grammar.buildJtigGrammar.TIGRule;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import parser.early.DerivationType;
import parser.early.Item;
import parser.early.ItemDerivation;
import parser.early.JTIGParser;
import tools.tokenizer.MorphAdornoSentenceTokenizer;
import tools.tokenizer.Token;

import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class GraphicalUserInterface extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private mxGraph graph;
	
	private Object parent;
	
	private JPanel mainpanel;
	
	
	private JFileChooser filechooser;
	
	private JButton lexiconfile_button;
	
	private JTextField parse_input;

	private JButton parse_button;
	

	private JLabel lexiconfile_label;
	
	private JTIGParser jtigparser;

	private JMenuItem menuItem_preferences;

	private JPanel panel_preferences;

	private PreferencesDialog preferences;

	
	public GraphicalUserInterface() throws IOException {
		super("Visualize JTIG forest");
		
		jtigparser = new JTIGParser();
		
		JMenuBar menuBar = new JMenuBar();

		JMenu menu = new JMenu("Parser");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription("");
		menuBar.add(menu);
		
		menuItem_preferences = new JMenuItem("Preferences",KeyEvent.VK_T);
		menuItem_preferences.addActionListener(this);
		menuItem_preferences.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK));
		menu.add(menuItem_preferences);
		
		this.setJMenuBar(menuBar);
		
		preferences = new PreferencesDialog();
		preferences.setLocationRelativeTo(this);
		

		

		
		
		JPanel input_parse_panel = new JPanel();
		
		parse_input = new JTextField("DER MANN SIEHT",10);
		parse_input.setSize(100, 10);
		input_parse_panel.add(parse_input);
		
		parse_button = new JButton("Start parsing");
		parse_button.addActionListener(this);
		input_parse_panel.add(parse_button);
		
		JPanel panel2 = new JPanel();
		
		
		getContentPane().add(panel2,BorderLayout.PAGE_START);

		panel2.add(input_parse_panel,BorderLayout.WEST);
		
		mainpanel = new JPanel();

		getContentPane().add(mainpanel,BorderLayout.CENTER);
	}
		/*
	private void createJTIGParser() throws IOException{
		JTIGParser parser = new JTIGParser();
		parser.readLexicon();
		Lexicon l = parser.getLexicon();
		
		MorphAdornoSentenceTokenizer st = new MorphAdornoSentenceTokenizer();
		Token[] tokens = st.getTokens(parse_input.getText());
		
		List<TIGRule> result = l.find(Arrays.asList(tokens), 0);
		if (result == null || result.size() <= 0){
			graph.insertVertex(parent, null, "NOT FOUND", 0, 0, 80,20);
			return;
		}
		visualize(result.get(0));
	}
	*//*
	private void visualize(TIGRule rule){	
		List<Layer> all = rule.getLayers();
		Layer first = rule.getLayer(0);
		Object v1 = graph.insertVertex(parent, null, first.getEntry(0).getLabel(), 0, 0, 80,20);
		visualize(first,all,v1);
	}
	*//*
	public void visualize(Layer beyond,List<Layer> all,Object p2){
		int i = 0 ;
		for (Entry entry : beyond.getEntrys()){
			if (i == 0){
				i++;
				continue;
				}
			String style = "";
			if (entry.getNodeType() == NodeType.EPS)
				style = "fillColor=green";
			if (entry.getNodeType() == NodeType.SUBST)
				style = "fillColor=yellow";
			if (entry.getNodeType() == NodeType.TERM)
				style = "fillColor=red";
			Object v1 = graph.insertVertex(parent, null, entry.getLabel(), 0, 0, 80,20, style);
			graph.insertEdge(parent, null, "", p2, v1);
			
			Layer prod = findchildren(beyond,all,entry);
			if (prod != null)
				visualize(prod,all,v1);
			
			i++;
		}
	}
	*//*
	private Layer findchildren(Layer rule,List<Layer> all, Entry e){
		for (Layer elem : all ){
			int[] a = Arrays.copyOfRange(elem.getGornNumber(), 0, elem.getGornNumber().length-1);
			int[] b = rule.getGornNumber();
			if (Arrays.equals(a, b) && elem.getEntry(0).getLabel().equals(e.getLabel())){
				return elem;
			}
		}
		return null;
	}*/
	
	public static void main(String[] args) throws IOException
	{
		GraphicalUserInterface frame = new GraphicalUserInterface();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == parse_button){   
			MorphAdornoSentenceTokenizer st = new MorphAdornoSentenceTokenizer();
			Token[] tokens = st.getTokens(parse_input.getText());
			jtigparser.readLexicon();
			Item item = jtigparser.parseSentence(parse_input.getText(), tokens);
			if (item != null)
				printItems(item);
		} else if (e.getSource() == menuItem_preferences){
			
			preferences.setVisible(true);
		}
	}

	private void printItems(Item item) {
		mainpanel.setVisible(false);
		getContentPane().remove(mainpanel);
		mainpanel = new JPanel();

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
		
		mainpanel.add(graphComponent);
		
		getContentPane().add(mainpanel,BorderLayout.CENTER);
		getContentPane().validate();
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
