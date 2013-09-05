package xml;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import parser.Layer;
import parser.NodeType;
import parser.RuleTree;
import lisp.LispParser;

public class XMLParser {

	private String inputpath;
	
	private Integer[] spine;
	
	private List<parser.Node> lexicalanchors;
	
	private boolean foundfirst;

	public XMLParser(String inputpath) {
		this.inputpath = inputpath;
		// if the file is in Lisp Format, transform to XML
		if (LispParser.isLispFormat(inputpath))
			transformtoXML();
	}

	private void transformtoXML() {
		String newinputpath = this.inputpath.replaceAll("\\.lisp", ".xml");
		LispParser lp = new LispParser(this.inputpath, newinputpath);
		this.inputpath = newinputpath;
		try {
			lp.parse();
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"Couldn't transform LISP-format into XML-format. "
							+ e.getMessage());
		}
	}

	public void parse() {
		try {

			List<RuleTree> rt = new LinkedList<RuleTree>();
			File fXmlFile = new File(inputpath);

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("tree");

			for (int i = 0; i < nList.getLength(); i++) {

				Node tree = nList.item(i);
				
				Node treeroot = tree.getFirstChild();
				
				spine = null;
				foundfirst = false;
				lexicalanchors = new LinkedList<parser.Node>();
				List<Layer> layers = new LinkedList<Layer>(); //TODO: may use an array-list for performance
				
				Stack<Integer> gornnumbers = new Stack<Integer>();
				gornnumbers.push(new Integer(0));
				parseTree(treeroot,layers, gornnumbers);
				
				NamedNodeMap attributes = tree.getAttributes();
				String att_id = attributes.getNamedItem("id").getNodeValue();
				String att_freq = attributes.getNamedItem("freq").getNodeValue();
				String att_prob = attributes.getNamedItem("prob").getNodeValue();
				
				rt.add(new RuleTree(Long.parseLong(att_id),layers,lexicalanchors,Integer.parseInt(att_freq),Double.parseDouble(att_prob),spine));
			}
			System.out.println(rt);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void parseTree(Node node, List<Layer> layers, Stack<Integer> gornnumbers) {
		if (node.hasChildNodes()){
		// list for CFG-rule created on tree structure
		List<parser.Node> nodes = new LinkedList<parser.Node>();
		// add root node as 0th literal in CFG-rule
		nodes.add(createLayerNodefromXMLNode(node));
		
		for (Node childNode = node.getFirstChild(); childNode != null;) {
			Node nextChild = childNode.getNextSibling();
			
			// create Layer Node from XML node + find spine
			parser.Node n = createLayerNodefromXMLNode(childNode);
			if (n.getnodeType() == NodeType.RFOOT || n.getnodeType() == NodeType.LFOOT){
				spine = gornnumbers.toArray(new Integer[0]);
			}
			// add children as i-th literal in CFG- Rule
			nodes.add(n);
			
			childNode = nextChild;
		}
		if (nodes.size() > 0){
			parser.Node[] layernodes = nodes.toArray(new parser.Node[0]);
			Integer[] layergornnumber = gornnumbers.toArray(new Integer[0]);
			layers.add(new Layer(layergornnumber,layernodes));
		}
		int i = 1;
		for (Node childNode = node.getFirstChild(); childNode != null;) {
			Node nextChild = childNode.getNextSibling();
			
			gornnumbers.push(new Integer(i));
			parseTree(childNode,layers,gornnumbers);
			gornnumbers.pop();
			
			childNode = nextChild;
			i++;
			}
		} else {
			// Code to find anchor elements TODO: export in XML- structure and preprocess
			parser.Node ln = createLayerNodefromXMLNode(node);
			if (!foundfirst && ln.getnodeType() == NodeType.TERM){
				node = node.getParentNode();
				
				for (Node childNode = node.getFirstChild(); childNode != null;) {
				Node nextChild = childNode.getNextSibling();
					
				ln = createLayerNodefromXMLNode(childNode);
				if (ln.getnodeType() == NodeType.TERM)
					this.lexicalanchors.add(ln);
				childNode = nextChild;
				}
				foundfirst = true;
			}
		}
	}
	private parser.Node createLayerNodefromXMLNode(Node n){
		NamedNodeMap attributes = n.getAttributes();
		
		Node attn = attributes.getNamedItem("type");
		String type = attn.getNodeValue();
		
		NodeType nt = null;
		String label = null;
		
		if (type.equals(NodeType.NONTERM.toString().toLowerCase())) {
			nt = NodeType.NONTERM;
			attn = attributes.getNamedItem("cat");
			label = attn.getNodeValue();
		} else if (type.equals(NodeType.SUBST.toString().toLowerCase())){
			nt = NodeType.SUBST;
			attn = attributes.getNamedItem("cat");
			label = attn.getNodeValue();
		} else if (type.equals(NodeType.LFOOT.toString().toLowerCase())){
			nt = NodeType.LFOOT;
			attn = attributes.getNamedItem("cat");
			label = attn.getNodeValue();		
		} else if (type.equals(NodeType.RFOOT.toString().toLowerCase())){
			nt = NodeType.RFOOT;
			attn = attributes.getNamedItem("cat");
			label = attn.getNodeValue();
		} else if (type.equals(NodeType.TERM.toString().toLowerCase())){
			nt = NodeType.TERM;
			attn = attributes.getNamedItem("label");
			label = attn.getNodeValue();
		}
		return new parser.Node(nt,label);
	}

	public static void main(String[] args) {
		XMLParser xp = new XMLParser("/home/fapsi/example3.lisp");
		//xp.parse();
	}
}
