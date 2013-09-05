package lisp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class LispParser {

	private String inputpath = "example.lisp";
	
	private String outputpath ="";
	
	private Map<String, TokenType> keywords;
	
	private Queue<Token> tokens;
	
	private List<String> initsymbols;
	
	private Document doc;
	
	private long treecount = 0;
	
	private boolean initialtree;

	public LispParser(String inputpath,String outputpath) {
		this.inputpath = inputpath;
		this.outputpath = outputpath;
		keywords = new HashMap<String, TokenType>();
		keywords.put("(", TokenType.LBRACE);
		keywords.put(")", TokenType.RBRACE);
		keywords.put(":lfoot", TokenType.LFOOT);
		keywords.put(":rfoot", TokenType.RFOOT);
		keywords.put(":subst", TokenType.SUBST);
		keywords.put("setq", TokenType.SETQ);

	}
	
	public static boolean isLispFormat(String inputpath){
		//TODO: May lex + parse file to ensure that the file is in lisp format
		return inputpath.endsWith(".lisp");
	}

	public Queue<Token> lex() throws IOException {
		Queue<Token> all = new LinkedList<Token>();

		BufferedReader br = new BufferedReader(new FileReader(inputpath));
		try {
			int linecount = 1;
			String line = br.readLine();
			StringBuilder sb = new StringBuilder();
			while (line != null) {

				LexMode mode = LexMode.DEFAULT;
				for (int i = 0; i < line.length(); i++) {
					char c = line.charAt(i);
					String cc = String.valueOf(c);
					boolean eol = line.length() - 1 == i;

					switch (mode) {
					case DEFAULT:
						if (keywords.containsKey(cc)) {
							all.add(new Token(cc, keywords.get(cc),linecount,i));
						}

						if (cc.equals("\"") ) {
							mode = LexMode.STRING;
						}
						if (cc.equals(":") || Character.isLetter(c)) {
							sb.append(cc);
							mode = LexMode.ATTRIBUTE;
						}

						if (cc.equals("*") || cc.equals(";")) {
							mode = LexMode.COMMENT;
						}
						if (Character.isDigit(c)) {
							char next = line.charAt(line.length() - 1 != i ? i + 1
									: i);
							if (!(".").equals(String.valueOf(next))) {
								mode = LexMode.INT;
							} else
								mode = LexMode.FLOAT;
							sb.append(cc);
							if (eol){
								mode = LexMode.INT;
								i--;
							}
						}
						break;
					case STRING:
						if (cc.equals("\"") || eol) {
							String found = sb.toString();
							if (keywords.containsKey(found))
								all.add(new Token(found, keywords.get(found),linecount,i));
							else
								all.add(new Token(found, TokenType.LABEL,linecount,i));

							mode = LexMode.DEFAULT;
							sb = new StringBuilder();
						} else
							sb.append(cc);
						break;
					case ATTRIBUTE:
						if (!Character.isLetter(c) || eol) {
							String found = sb.toString();
							if (keywords.containsKey(found))
								all.add(new Token(found, keywords.get(found),linecount,i));
							else
								all.add(new Token(found, TokenType.LABEL,linecount,i));
							mode = LexMode.DEFAULT;
							sb = new StringBuilder();
						} else
							sb.append(cc);
						break;
					case COMMENT:
						if (cc.equals("*") || eol) {
							mode = LexMode.DEFAULT;
						}
						break;
					case INT:
						if (!Character.isDigit(c) || eol) {
							if (Character.isDigit(c))
								sb.append(cc);
							String found = sb.toString();
							all.add(new Token(Integer.valueOf(found),
									TokenType.INT,linecount,i));
							mode = LexMode.DEFAULT;
							sb = new StringBuilder();
							i--;
						} else
							sb.append(cc);
						break;
					case FLOAT:
						if (!(Character.isDigit(c) || (".").equals(cc) ||("e").equals(cc) || ("-").equals(cc)) || eol) {
							if (Character.isDigit(c))
								sb.append(cc);
							String found = sb.toString();
							all.add(new Token(Double.valueOf(found),
									TokenType.FLOAT,linecount,i));
							mode = LexMode.DEFAULT;
							sb = new StringBuilder();
							i--;
						} else
							sb.append(cc);
						break;
					}

				}
				linecount++;
				line = br.readLine();
				mode = LexMode.DEFAULT;
				sb = new StringBuilder();
			}
		} finally {
			br.close();
		}
		return all;
	}

	public boolean parse() throws IOException, ParserConfigurationException, TransformerException {
		this.tokens = lex();
		
		// create xml file
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
		doc = docBuilder.newDocument();
		
		Element rootElement = doc.createElement("ltig");
		
		doc.appendChild(rootElement);
		
		expect(TokenType.LBRACE);
		parseStartSymbols();
		expect(TokenType.RBRACE);

		expect(TokenType.LBRACE);
		parseTrees(rootElement);
		expect(TokenType.RBRACE);
		//write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(outputpath));
		
		transformer.transform(source, result);
		
		System.out.println("Success.");
		return true;
	}
	private void parseTrees(Element rootElement) {
		expect(TokenType.SETQ);
		expect(TokenType.LBRACE);

		while (this.tokens.peek().gettype() == TokenType.LBRACE){
			Element tree = doc.createElement("tree");
			rootElement.appendChild(tree);
			expect(TokenType.LBRACE);
			
			initialtree = true;
			
			parseTree(tree);
			
			// set initial type, if :rfoot and :lfoot aren't in tree
			if (initialtree)
				tree.setAttribute("type", "initial");
			
			tree.setAttribute("id", Long.toString(treecount));
			treecount++;
			Token t = this.tokens.peek();
			tree.setAttribute("freq", ((Integer) t.getstring()).toString());
			expect(TokenType.INT);
			t = this.tokens.peek();
			if (t.gettype() == TokenType.INT){
				expect(TokenType.INT);
				tree.setAttribute("prob", ((Integer) t.getstring()).toString());
			}else{
				expect(TokenType.FLOAT);
				tree.setAttribute("prob", ((Double) t.getstring()).toString());
			}
			expect(TokenType.RBRACE);
			if (tokens.size() % 1000 == 0){
				System.out.println("0");
				System.out.flush();
				}
		}
		expect(TokenType.RBRACE);
	}

	private void parseTree(Element tree) {
		Token t0 = this.tokens.peek();
		if (t0.gettype() == TokenType.LABEL){
			expect(TokenType.LABEL);
			Element node = doc.createElement("node");
			node.setAttribute("type", "term");
			tree.appendChild(node);
			node.setAttribute("label", (String) t0.getstring());
		} else {
		expect(TokenType.LBRACE);
		Token t = this.tokens.peek();
		switch (t.gettype()){
		case LABEL:
			expect(TokenType.LABEL);
			Element node = doc.createElement("node");
			tree.appendChild(node);
			node.setAttribute("cat", (String) t.getstring());
			node.setAttribute("type", "nonterm");
			Token t2 = this.tokens.peek();
			while (t2.gettype() != TokenType.RBRACE ){
				parseTree(node);
				t2 = this.tokens.peek();
			}
		break;
		case SUBST:
			expect(TokenType.SUBST);
			t = this.tokens.peek();
			node = doc.createElement("node");
			tree.appendChild(node);
			node.setAttribute("type", "subst");
			node.setAttribute("cat", (String) t.getstring());
			expect(TokenType.LABEL);
		break;
		case LFOOT:
			expect(TokenType.LFOOT);
			t = this.tokens.peek();
			node = doc.createElement("node");
			tree.appendChild(node);
			node.setAttribute("type", "lfoot");
			node.setAttribute("cat", (String) t.getstring());
			expect(TokenType.LABEL);
			initialtree = false;
		break;
		case RFOOT:
			expect(TokenType.RFOOT);
			t = this.tokens.peek();
			node = doc.createElement("node");
			tree.appendChild(node);
			node.setAttribute("type", "rfoot");
			node.setAttribute("cat", (String) t.getstring());
			expect(TokenType.LABEL);
			initialtree = false;
		break;
		default:
		break;
		}
		expect(TokenType.RBRACE);
		}
	}

	private void parseStartSymbols() {
		initsymbols = new LinkedList<String>();
		expect(TokenType.SETQ);
		expect(TokenType.LBRACE);
		while (this.tokens.peek().gettype() == TokenType.LABEL){
			initsymbols.add((String) this.tokens.peek().getstring());
			expect(TokenType.LABEL);
		}
		expect(TokenType.RBRACE);
	}

	private void expect(TokenType tt){
		if (this.tokens.size() == 0)
			throw new IllegalArgumentException("Wrong Lisp-File Format. Expected: "+tt.toString()+" , Found: nothing");
		if (this.tokens.peek().gettype() != tt)
			throw new IllegalArgumentException("Wrong Lisp-File Format. Expected: "+tt.toString()+" , Found: "+this.tokens.peek().toString());
			
		this.tokens.remove();
	}

	public static void main(String[] args) throws IOException, ParserConfigurationException, TransformerException {
		LispParser lp = new LispParser("/home/fapsi/example.lisp","/home/fapsi/example.xml");
		lp.parse();
	}
}
