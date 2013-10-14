package tools.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import grammar.buildJtigGrammar.Entry;
import grammar.buildJtigGrammar.Lexicon;
import grammar.buildJtigGrammar.NodeType;
import grammar.buildJtigGrammar.ProductionRule;
import grammar.buildJtigGrammar.TIGRule;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import parser.early.JTIGParser;
import tools.tokenizer.MorphAdornoSentenceTokenizer;
import tools.tokenizer.Token;

import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class GraphicalUserInterface extends JFrame implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private mxGraph graph;
	
	private Object parent;
	
	private JPanel mainpanel;
	
	private JTextField input;
	
	public GraphicalUserInterface()
	{
		super("Visualize JTIG");
		
		JPanel panel = new JPanel();
		input = new JTextField("DER MANN",10);
		input.setSize(100, 10);
		panel.add(input);
		JButton button = new JButton("Visualize");
		panel.add(button);
		
		getContentPane().add(panel,BorderLayout.PAGE_START);
		button.addMouseListener(this);
		
		
		mainpanel = new JPanel();

		getContentPane().add(mainpanel,BorderLayout.CENTER);
	}
	
	private void showTIGRule(){
		mainpanel.setVisible(false);
		getContentPane().remove(mainpanel);
		mainpanel = new JPanel();

		graph = new mxGraph();
		parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		try
		{
			createJTIGParser();
		}
		finally
		{
			graph.getModel().endUpdate();
		}
		
		mxCompactTreeLayout layout = new mxCompactTreeLayout(graph);
		layout.setHorizontal(false);
		layout.execute(graph.getDefaultParent());

		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		
		mainpanel.add(graphComponent);
		
		getContentPane().add(mainpanel,BorderLayout.CENTER);
		getContentPane().validate();
	}
	
	
	private void createJTIGParser(){
		JTIGParser parser = new JTIGParser();
		parser.readLexicon();
		Lexicon l = parser.getLexicon();
		
		MorphAdornoSentenceTokenizer st = new MorphAdornoSentenceTokenizer();
		Token[] tokens = st.getTokens(input.getText());
		
		List<TIGRule> result = l.find(Arrays.asList(tokens), 0);
		if (result == null || result.size() <= 0){
			graph.insertVertex(parent, null, "NOT FOUND", 0, 0, 80,20);
			return;
		}
		visualize(result.get(0));
	}
	
	private void visualize(TIGRule rule){	
		List<ProductionRule> all = rule.getProductionRules();
		ProductionRule first = rule.getProductionRules().get(0);
		Object v1 = graph.insertVertex(parent, null, first.getEntry(0).getLabel(), 0, 0, 80,20);
		visualize(first,all,v1);
	}
	
	public void visualize(ProductionRule beyond,List<ProductionRule> all,Object p2){
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
			
			ProductionRule prod = findchildren(beyond,all,entry);
			if (prod != null)
				visualize(prod,all,v1);
			
			i++;
		}
	}
	
	private ProductionRule findchildren(ProductionRule rule,List<ProductionRule> all, Entry e){
		for (ProductionRule elem : all ){
			int[] a = Arrays.copyOfRange(elem.getGornNumber(), 0, elem.getGornNumber().length-1);
			int[] b = rule.getGornNumber();
			if (Arrays.equals(a, b) && elem.getEntry(0).getLabel().equals(e.getLabel())){
				return elem;
			}
		}
		return null;
	}
	
	public static void main(String[] args)
	{
		GraphicalUserInterface frame = new GraphicalUserInterface();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 320);
		frame.setVisible(true);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		showTIGRule();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
