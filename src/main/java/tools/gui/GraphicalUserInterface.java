package tools.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;

import parser.early.Item;
import parser.early.JTIGParser;
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
		
		preferences = new PreferencesDialog(jtigparser);
		preferences.setLocationRelativeTo(this);
		

		

		
		
		JPanel input_parse_panel = new JPanel();
		input_parse_panel.setBorder(new TitledBorder("Input"));
		
		JPanel action_parse_panel = new JPanel();
		action_parse_panel.setBorder(new TitledBorder("Actions"));
		
		parse_input = new JTextArea("DER MANN SIEHT",5,100);
		parse_input.setSize(100, 10);
		input_parse_panel.add(parse_input);
		
		parse_button = new JButton("Start parsing");
		parse_button.addActionListener(this);
		action_parse_panel.add(parse_button);
		
		mainpanel = new JTabbedPane();
		mainpanel.setBorder(new TitledBorder("Output"));
		
		//JPanel panel2 = new JPanel();
		
		getContentPane().setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5,5,5,5);
		c.weightx = 1;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 2;
		c.gridwidth = 2;
		
		getContentPane().add(input_parse_panel,c);
		
		c.weighty = 0;
		c.gridx = 2;
		c.gridy = 0;
		c.gridheight = 2;
		c.gridwidth = 1;
		
		getContentPane().add(action_parse_panel,c);
		
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 2;
		c.gridheight = 1;
		c.gridwidth = 3;
		getContentPane().add(mainpanel,c);
		
		//getContentPane().add(panel2,BorderLayout.PAGE_START);

		//panel2.add(input_parse_panel,BorderLayout.WEST);
		
		

		
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
			if(jtigparser.hasLexicon()){
				
				List<Item> item = jtigparser.parseSentence(parse_input.getText(), tokens);
				if (item != null && item.size() > 0)
					printItems(item);
			}
		} else if (e.getSource() == menuItem_preferences){
			
			preferences.setVisible(true);
		}
	}

	private void printItems(List<Item> items) {
		
		this.mainpanel.removeAll();
		
		LogPanel logpanel = new LogPanel(jtigparser.getLog());
		this.mainpanel.add("Log",logpanel);
		int i = 1;
		for (Item item : items){
			ItemPanel actualpanel = new ItemPanel(item);
			actualpanel.drawItem();
			this.mainpanel.add("Forest item "+i,actualpanel);
			i++;
		}
		
		/*
		mainpanel.setVisible(false);
		getContentPane().remove(mainpanel);
		mainpanel = new JPanel();

		
		*/
	}
}
