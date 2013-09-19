package parser.early;

import parser.lookup.Lookup;
import grammar.readXML.XMLReader;

public class Parser {
	
	
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
		//System.out.println(System.getProperty("user.dir"));

		XMLReader xp = new XMLReader(lexiconpath);
		Lookup l = new Lookup(input , xp.read());
		System.out.println("Occuring Trees in input: "+l.findlongestmatches());
	}
	
}
