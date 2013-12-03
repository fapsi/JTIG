/**
 * 
 */
package grammar.readXML;

import java.util.HashSet;
import java.util.Set;

import grammar.buildjtiggrammar.AnchorStrategy;
import grammar.buildjtiggrammar.Lexicon;
import grammar.buildjtiggrammar.NodeType;
import grammar.buildjtiggrammar.IRTreeNode;
import grammar.buildjtiggrammar.UnvalidElementaryTreeException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Callback Handler for SAX-Parser.
 * Parses from the XML-File a List of {@link TIGRule} for processing on later in the main parser.
 * @author Fabian Gallenkamp
 */

public class XMLHandler extends DefaultHandler {
	
	/**
	 * True, if currently processing a grammar
	 */
	private boolean inltig;
	
	/**
	 * True, if currently processing the start-symbols
	 */
	private boolean instartsymbols;
	
	/**
	 * True, if currently processing a start-symbol
	 */
	private boolean insymbol;
	
	/**
	 * True, if currently processing a tree.
	 */
	private boolean intree;
	
	/**
	 * Contains current node in the tree, which should be parsed. 
	 */
	private IRTreeNode actnode;
	
	/**
	 * Stores the current tree id
	 */
	private long tree_id;
	
	/**
	 * Stores the current tree frequency
	 */
	private long tree_frequency;
	
	/**
	 * Stores the current tree probability
	 */
	private double tree_probability;
	
	/**
	 * Stores all startsymbols found while reading
	 */
	private Set<String> startsymbols;
	
	/**
	 * Stores current depth in tree
	 */
	private int depth;
	
	/**
	 * Strategy for finding anchor elements in a TreeNode.
	 */
	private AnchorStrategy anchorstrategy;

	/**
	 * Stores a list of {@link TIGRule}'s, found so far.
	 */
	private Lexicon lexicon;
	
	/**
	 * Creates a {@link XMLHandler}.
	 * @param anchorstrategy Strategy for finding anchor elements in a TreeNode.
	 */
	public XMLHandler(AnchorStrategy anchorstrategy){
		lexicon = new Lexicon();
		this.anchorstrategy = anchorstrategy;
		this.startsymbols = new HashSet<String>();
	}
	
	/**
	 * Receive notification of the start/end of an element. 
	 * Builds together with {@link #endElement(String, String, String)} the tree for a grammar rule.
	 * Those tree rules are converted into {@link TIGRule}- Objects. 
	 * Then they are added and stored in this object until the document is completely parsed.
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		
		if (qName.equals("ltig")){
			
			inltig = true;
		} else if (qName.equals("start-symbols")){
			
			if (!inltig)
				throw new SAXException("Declaration of 'start-symbols' has to be in 'ltig'-scope.");
			
			instartsymbols = true;
		} else if (qName.equals("symbol")){
			
			if (!instartsymbols)
				throw new SAXException("Declaration of 'symbol' has to be in 'start-symbols'-scope.");
			
			startsymbols.add(attributes.getValue("type"));
			insymbol = true;
		} else if (qName.equals("tree")){
			
			if (!inltig)
				throw new SAXException("Declaration of 'tree' has to be in 'ltig'-scope.");
			
			intree = true;
			tree_id = Long.parseLong(attributes.getValue("id"));
			tree_frequency = Long.parseLong(attributes.getValue("freq"));
			tree_probability = Double.parseDouble(attributes.getValue("prob"));
			depth = 0;
		} else if (qName.equals("node")){
			
			if (!intree)
				throw new SAXException("Declaration of 'node' has to be in 'tree'-scope.");
			
			IRTreeNode n = createfromXMLNode(this.actnode,attributes);
			if (actnode != null)
				actnode.addChild(n);
			actnode = n;
			depth++;
		} else
			throw new SAXException("Unknown qualified name '" + qName+"' in XML-file."+uri);
	}
	
	/**
	 * See: {@link #startElement(String, String, String, Attributes)}
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (qName.equals("ltig")){ // TODO: avoid new open "ltig"-tag
			if (!inltig || intree || instartsymbols || insymbol)
				throw new SAXException("Invalid 'start-symbols'-end tag in XML-file.");
			inltig = false;
		} else if (qName.equals("start-symbols")){
			if (!inltig || !instartsymbols || intree || insymbol)
				throw new SAXException("Invalid 'start-symbols'-end tag in XML-file.");
			instartsymbols = false;
			lexicon.setStartSymbols(this.startsymbols);
			
		} else if (qName.equals("symbol")){
			if (!inltig || !instartsymbols || !insymbol || intree)
				throw new SAXException("Invalid 'symbol'-end tag in XML-file.");
			insymbol = false;
			
		} else if (qName.equals("tree") && inltig){
			
			//store RuleTree
			lexicon.add(actnode.convertToElementaryTree(tree_id,tree_frequency,tree_probability,anchorstrategy));
			//forget real tree representation
			actnode = null;
			intree = false;
			
		} else if (qName.equals("node") && intree){
			IRTreeNode parent = actnode.getParent();
			if (parent != null)
				actnode = parent;
			depth--;
		}
	}
	/**
	 * Converts the XML-node into an {@link IRTreeNode}.
	 * @param parent the parent tree node parsed out of the XML
	 * @param attributes the attributes of the xml-node-element
	 * @return the tree node for the xml node, with the specified type, label
	 * @throws IllegalStateException if type is unknown.
	 */
	private IRTreeNode createfromXMLNode(IRTreeNode parent,Attributes attributes){
		NodeType treenodetype = null;
		for (NodeType nt : NodeType.values()){
			if (attributes.getValue("type").equals(nt.toString().toLowerCase())){
				treenodetype = nt;
			}
		}
		if (treenodetype == null)
			throw new UnvalidElementaryTreeException("Tree "+tree_id + ": Type '"+attributes.getValue("type")+"' unknown.");
		
		String label;
		if (treenodetype == NodeType.TERM)
			label = attributes.getValue("label");
		else
			label = attributes.getValue("cat");
		if (label.trim().isEmpty())
			throw new UnvalidElementaryTreeException("Tree "+tree_id + ": value of '"
		+ (treenodetype == NodeType.TERM?"label":"cat") +"' is empty.");
		
		return new IRTreeNode(parent,treenodetype,label,depth);
	}
	
	/**
	 * @return a lexicon with all extracted rule trees in the grammar.
	 */
	public Lexicon getLexicon() {
		return this.lexicon;
	}
}
