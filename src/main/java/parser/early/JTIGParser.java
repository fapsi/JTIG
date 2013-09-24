/**
 * 
 */
package parser.early;

import java.util.List;

import parser.lookup.ActivatedLexicon;
import parser.lookup.Lookup;
import tools.tokenizer.MorphAdornoSentenceTokenizer;
import tools.tokenizer.Token;
import grammar.buildJtigGrammar.Lexicon;
import grammar.readXML.XMLReader;
/**
 * 
 * @author Fabian Gallenkamp
 */
public class JTIGParser {
	
	private Lexicon lexicon;
	
	private ActivatedLexicon activatedlexicon;
	
	private String originalsentence;
	
	public JTIGParser(String lexiconpath){
		XMLReader xp = new XMLReader(lexiconpath);
		this.lexicon = xp.read();
	}
	
	public void parseSentence(String originalsentence, List<Token> tokens){
		this.originalsentence = originalsentence;
		Lookup l = new Lookup(tokens , lexicon);
		this.activatedlexicon = l.findlongestmatches();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("----------------------------------\n");
		sb.append(this.lexicon);
		sb.append("----------------------------------\n");
		sb.append("Sentence to be parsed:\n");
		sb.append("'"+this.originalsentence+"'\n");
		sb.append("----------------------------------\n");
		sb.append(this.activatedlexicon);
		return sb.toString();
	}

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		boolean debug = false;
		String lexiconpath = null;
		String input = null;
		
		for (int i = 0;i<args.length;i++){
			if ("-h".equals(args[i])){
				
				System.out.println("Usage: jtig [OPTIONS] text\n"
						+ "Options:\n"
						+ "-d \t\t debug-mode: more outputs\n"
						+ "-l PATH \t use lexicon in specified path.\n");
				return;
			} else if ("-d".equals(args[i])){
				
				debug = true;
			
			} else if ("-l".equals(args[i])){
				
				if (i+1 < args.length)
					lexiconpath = args[++i];
				else{
					System.err.println("Option -l requires path parameter.");
					return;
				}
			
			} else if (i == (args.length-1)){
				input = args[i];
			} else {
				
				System.err.println("Wrong usage. Use -h for detailed description.");
				return;
			}
		}
		// Tokenize String
		MorphAdornoSentenceTokenizer st = new MorphAdornoSentenceTokenizer(input);
		List<Token> tokens = st.getTokens();
		// call JTIG PARSER
		JTIGParser parser = new JTIGParser(lexiconpath);
		parser.parseSentence(input, tokens);
		System.out.println(parser.toString());
	}
	
}
