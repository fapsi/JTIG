package lisp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;


public class LispParser {

	private String inputpath;
	
	private String outputpath ="";
	
	private Map<String, TokenType> keywords;
	
	private BufferedReader br;
	
	private String actline;
	
	private int actpos;
	
	private int actlinecnt;
	
	private StringBuilder actstr;
	
	private List<String> initsymbols;
	
	private XMLEventWriter writer;
	
	private XMLEventFactory eventFactory;
	
	private LexMode mode;
	
	private long treecount = 0;
	
	private boolean initialtree;
	
	private List<XMLEvent> events;

	private int starts;

	private int ends;

	public LispParser(String inputpath,String outputpath) throws IOException {
		this.inputpath = inputpath;
		this.outputpath = outputpath;
		keywords = new HashMap<String, TokenType>();
		keywords.put("(", TokenType.LBRACE);
		keywords.put(")", TokenType.RBRACE);
		keywords.put(":lfoot", TokenType.LFOOT);
		keywords.put(":rfoot", TokenType.RFOOT);
		keywords.put(":subst", TokenType.SUBST);
		//keywords.put(":eps", TokenType.EPS);
		keywords.put("setq", TokenType.SETQ);
		
		br = new BufferedReader(new FileReader(this.inputpath));
		actline = br.readLine();
		actpos = 0;
		actlinecnt = 1;
		mode = LexMode.DEFAULT;
		actstr = new StringBuilder();
	}
	
	public static boolean isLispFormat(String inputpath){
		//TODO: May lex + parse file to ensure that the file is in lisp format
		return inputpath.endsWith(".lisp");
	}

	private Token next(){
		if (actline == null)
			return null;
		boolean eol = actpos == (actline.length() - 1);
		char actchar = actline.charAt(actpos);
		Token next = null;
		
		switch (mode) {
		case DEFAULT:
			if (keywords.containsKey(String.valueOf(actchar))) {
				next = new Token(String.valueOf(actchar), keywords.get(String.valueOf(actchar)) , actlinecnt , actpos);
			}

			if ("\"".equals(String.valueOf(actchar)) ) {
				mode = LexMode.STRING;
			}
			if (":".equals(String.valueOf(actchar)) || Character.isLetter(actchar)) {
				actstr.append(actchar);
				mode = LexMode.ATTRIBUTE;
			}

			if ("*".equals(String.valueOf(actchar)) || ";".equals(String.valueOf(actchar))) {
				mode = LexMode.COMMENT;
			}
			if (Character.isDigit(actchar)) {
				actstr.append(actchar);
				if (eol){
					next = new Token(Integer.valueOf(actstr.toString()), TokenType.INT , actlinecnt , actpos);
					actstr = new StringBuilder();
				} else {
					if (!(".").equals(String.valueOf(actline.charAt(actpos + 1 ))))
						mode = LexMode.INT;
					else
						mode = LexMode.FLOAT;
				
				}
			}
			break;
		case STRING:
			if ("\"".equals(String.valueOf(actchar))) {
				String found = actstr.toString();
				if (keywords.containsKey(found))
					next = new Token(found, keywords.get(found) , actlinecnt , actpos);
				else
					next = new Token(found, TokenType.LABEL , actlinecnt , actpos);

				mode = LexMode.DEFAULT;
				actstr = new StringBuilder();
			} else
				actstr.append(actchar);
			break;
		case ATTRIBUTE:
			if (!Character.isLetter(actchar)) {
				String found = actstr.toString().toLowerCase();
				if (keywords.containsKey(found))
					next = new Token(found, keywords.get(found),actlinecnt,actpos);
				else
					next = new Token(found, TokenType.LABEL,actlinecnt,actpos);
				mode = LexMode.DEFAULT;
				actstr = new StringBuilder();

				actpos--;
			} else
				actstr.append(actchar);
			break;
		case COMMENT:
			if ("*".equals(String.valueOf(actchar)) || eol) {
				mode = LexMode.DEFAULT;
			}
			break;
		case INT:
			if (!Character.isDigit(actchar) || eol) {
				if (Character.isDigit(actchar))
					actstr.append(actchar);
				String found = actstr.toString();
				next = new Token(Integer.valueOf(found), TokenType.INT,actlinecnt,actpos);
				mode = LexMode.DEFAULT;
				actstr = new StringBuilder();
				if (eol && !Character.isDigit(actchar)){
					actpos--;
				}
			} else
				actstr.append(actchar);
			break;
		case FLOAT:
			if (!(Character.isDigit(actchar) || (".").equals(String.valueOf(actchar)) ||("e").equals(String.valueOf(actchar)) || ("-").equals(String.valueOf(actchar))) || eol) {
				if (Character.isDigit(actchar) || (".").equals(String.valueOf(actchar)) ||("e").equals(String.valueOf(actchar)) || ("-").equals(String.valueOf(actchar)))
						actstr.append(actchar);
				String found = actstr.toString();
				next =new Token(Double.valueOf(found),TokenType.FLOAT,actlinecnt,actpos);
				mode = LexMode.DEFAULT;
				actstr = new StringBuilder();
				actpos--;
			} else
				actstr.append(actchar);
			break;
		}
		
		actpos++;
		
		if (actpos >= actline.length()){
			try {
				actline = br.readLine();
				actlinecnt++;
				actpos = 0;
				mode  = LexMode.DEFAULT;
			} catch (IOException e) {
				actline = null;
			}
			if (actline == null)
				return null;
		}
		if (mode == LexMode.DEFAULT && next != null){
			return next;
		}
		else 
			return next();
	}
	
	public boolean parse() throws XMLStreamException, FileNotFoundException {

		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
		writer = outputFactory.createXMLEventWriter(
		  new FileOutputStream(outputpath ) );
		eventFactory = XMLEventFactory.newInstance();
		
		writer.add(eventFactory.createStartDocument());

		writer.add(eventFactory.createStartElement( "", "", "ltig" ));

		
		expect(TokenType.LBRACE);
		parseStartSymbols();
		expect(TokenType.RBRACE);

		expect(TokenType.LBRACE);
		parseTrees();

		
		writer.add(eventFactory.createEndElement( "", "", "ltig" ));
		
		writer.add(eventFactory.createEndDocument());
		
		writer.close();
		
		System.out.println("Success.");
		System.out.println("Parsed "+treecount+" trees.");
		return true;
	}
	
	private void parseTrees() throws XMLStreamException {
		expect(TokenType.SETQ);
		expect(TokenType.LBRACE);

		Token tk = next();
		while (tk.gettype() == TokenType.LBRACE){

			writer.add(eventFactory.createStartElement( "",  "", "tree" ));
			writer.add(eventFactory.createAttribute( "id", String.valueOf(treecount)));
			expect(TokenType.LBRACE);
			
			events = new LinkedList<XMLEvent>();
			
			initialtree = true;
			
			parseTree();
				
			treecount++;

			Token t = expect(TokenType.INT);
			
			events.add(0,eventFactory.createAttribute( "freq", ((Integer) t.getstring()).toString() ));
			
			t = next();
			
			if (t.gettype() == TokenType.INT){
				events.add(0,eventFactory.createAttribute("prob", ((Integer) t.getstring()).toString()));
			}else{
				events.add(0,eventFactory.createAttribute("prob", ((Double) t.getstring()).toString()));
			}
			if (initialtree)
				events.add(0,eventFactory.createAttribute( "type", "initial" ));
			
			for (XMLEvent e : events)
				writer.add(e);
			events.clear();
			

			writer.add(eventFactory.createEndElement( "",  "", "tree" ));
			expect(TokenType.RBRACE);
			tk = next();
		}

	}

	private void parseTree() throws XMLStreamException {
		Token t = next();
		switch (t.gettype()){
		case LABEL:
			events.add(eventFactory.createStartElement( "", "", "node" ));
			events.add(eventFactory.createAttribute("cat", (String) t.getstring()));
			events.add(eventFactory.createAttribute("type", "nonterm"));
			Token tk = next();
			if (tk.gettype() == TokenType.LABEL){
				events.add(eventFactory.createStartElement( "", "", "node" ));
				events.add(eventFactory.createAttribute("type", "term"));
				events.add(eventFactory.createAttribute("label", (String) tk.getstring()));
				events.add(eventFactory.createEndElement( "", "", "node" ));
				events.add(eventFactory.createEndElement( "", "", "node" ));
				expect(TokenType.RBRACE);
				return;
			}

			
			while (tk.gettype() == TokenType.LBRACE){
				parseTree();
				tk = next();
			}
			events.add(eventFactory.createEndElement( "", "", "node" ));
		return;
		case SUBST:
			t = expect(TokenType.LABEL);
			events.add(eventFactory.createStartElement( "", "", "node" ));
			events.add(eventFactory.createAttribute("type", "subst"));
			events.add(eventFactory.createAttribute("cat", (String) t.getstring()));
			events.add(eventFactory.createEndElement( "", "", "node" ));
		break;
		case LFOOT:
			t = expect(TokenType.LABEL);
			events.add(eventFactory.createStartElement( "", "", "node" ));
			events.add(eventFactory.createAttribute("type", "lfoot"));
			events.add(eventFactory.createAttribute("cat", (String) t.getstring()));
			events.add(eventFactory.createEndElement( "", "", "node" ));
			initialtree = false;
		break;
		case RFOOT:
			t = expect(TokenType.LABEL);
			events.add(eventFactory.createStartElement( "", "", "node" ));
			events.add(eventFactory.createAttribute("type", "rfoot"));
			events.add(eventFactory.createAttribute("cat", (String) t.getstring()));
			events.add(eventFactory.createEndElement( "", "", "node" ));
			initialtree = false;
		break;
		case EPS:
			t = expect(TokenType.LABEL);
			events.add(eventFactory.createStartElement( "", "", "node" ));
			events.add(eventFactory.createAttribute("type", "e"));
			events.add(eventFactory.createAttribute("cat", (String) t.getstring()));
			events.add(eventFactory.createEndElement( "", "", "node" ));
		break;
		default:
			System.out.println("nich gut");
		break;
		}
		expect(TokenType.RBRACE);
	}

	private void parseStartSymbols() {
		initsymbols = new LinkedList<String>();
		expect(TokenType.SETQ);
		expect(TokenType.LBRACE);
		Token t;
		while ((t = next()) != null && t.gettype() == TokenType.LABEL){
			initsymbols.add((String) t.getstring());
		}
	}

	private Token expect(TokenType tt){
		Token t = next();
		if (t == null)
			throw new IllegalArgumentException("Wrong Lisp-File Format. Expected: "+tt.toString()+" , Found: end of file");
		if (t.gettype() != tt)
			throw new IllegalArgumentException("Wrong Lisp-File Format. Expected: "+tt.toString()+" , Found: "+t.toString());
		return t;
	}
	
	public static void main(String[] args) throws XMLStreamException, IOException {
		LispParser lp = new LispParser("/home/fapsi/example3.lisp","/home/fapsi/example3.xml");
		lp.parse();
	}
}
