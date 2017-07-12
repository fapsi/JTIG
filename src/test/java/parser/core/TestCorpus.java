package parser.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter; 
import java.io.File;
import java.io.FileReader;

import parser.early.JTIGParser;
import parser.early.run.ParseResult;
import tools.GeneralTools;
import tools.tokenizer.MorphAdornoSentenceTokenizer;
import tools.tokenizer.Token;

public class TestCorpus {
	private static JTIGParser parser;
	private MorphAdornoSentenceTokenizer st;

	
	
	public void tearDown() throws Exception {
		System.out.println("Tear down.");
		File dir = new File("data/runs/");
		GeneralTools.deleteDirectory(dir);	
	}

	
	public ParseResult callJTIGparser(String sentence) {
		st = new MorphAdornoSentenceTokenizer();
		ParseResult parseResult;
		Token[] tokens = st.getTokens(sentence);
		parseResult = parser.parseSentence(sentence, tokens);

		return parseResult;

		//System.out.println(run.getLog());

	}
	
	
	public static void main(String[] args) {
		TestCorpus tr=  new TestCorpus();
		try {
			String grammar = "/home/neumann/work/JTIG/testJtig/testGrams/english-conll-5000.xml"; //LTIG grammar path
			String path = "/home/neumann/work/JTIG/testJtig/testGrams/english-conll-5000-ROOT.txt"; // sentence file
			String pathtoWrite = "/home/neumann/work/JTIG/testJtig/testGrams/english-conll-5000-stats.txt"; // output file

			parser = new JTIGParser("");
			JTIGParser.setProperty("grammar.lexicon.path", grammar);
			JTIGParser.setProperty("parser.core.parselevel", "FOREST"); // "LOOKUP"
			parser.readLexicon();

			BufferedReader br = new BufferedReader(new FileReader(path));
			BufferedWriter bw= new BufferedWriter(new FileWriter(pathtoWrite, false));
			
			int sentenceNumber = 1;
			int max = 1;
			String line = "";
			long time1 = System.currentTimeMillis();
			
			System.out.println("Start processing corpus ... " + path);
			System.out.println("Parsing level ... " + JTIGParser.getParseLevel());
			while((line = br.readLine()) != null){
				long lineTime1 = System.currentTimeMillis();
				
				if ((sentenceNumber % max) == 0) {
					System.out.println(sentenceNumber + ": " + line);
				}

				ParseResult parseResult = tr.callJTIGparser(line);
				if ((sentenceNumber % max) == 0) {
					System.out.println(sentenceNumber 
							+ ": " + " forest: " 
							+ parseResult.getForest().getRootDimension()
							+ ", items: " 
							+ parseResult.getAmountProcessedItems()
							+ ", "
							+ parseResult.getAmountUniqueItems()
							);
				}

				long lineTime2 = System.currentTimeMillis();

				bw.write("Parsed sentence "+ sentenceNumber + 
						" in " +  (lineTime2-lineTime1) +" ms." + "\n");
				sentenceNumber++;
			}
			long time2 = System.currentTimeMillis();
			System.out.println("Complete time: " + new Long(time2-time1).toString()+" ms." + "\n");

			bw.close();
			br.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

}

