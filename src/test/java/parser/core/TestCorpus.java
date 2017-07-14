package parser.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import parser.early.JTIGParser;
import parser.early.run.ParseResult;
import tools.GeneralTools;
import tools.tokenizer.MorphAdornoSentenceTokenizer;
import tools.tokenizer.Token;

public class TestCorpus {
	private static JTIGParser parser;
	private MorphAdornoSentenceTokenizer st;
	private int maxSentenceLength = 40;



	public void tearDown() throws Exception {
		System.out.println("Tear down.");
		File dir = new File("data/runs/");
		GeneralTools.deleteDirectory(dir);	
	}


	public ParseResult callJTIGparser(String sentence, int sentId, BufferedWriter bw) throws IOException {
		st = new MorphAdornoSentenceTokenizer();
		ParseResult parseResult = null;
		Token[] tokens = st.getTokens(sentence);

		if (tokens.length > this.maxSentenceLength) {
			bw.write("sendId: " + sentId + ", " + "too long, " + tokens.length + " > " + this.maxSentenceLength + "\n");
		}
		else {
			parseResult = parser.parseSentence(sentence, tokens);
		}

		return parseResult;

	}


	public static void main(String[] args) {
		TestCorpus tr=  new TestCorpus();
		try {
			String grammar = "/home/neumann/work/JTIG/testJtig/testGrams/english-conll-5000.xml"; //LTIG grammar path
			String textFile = "/home/neumann/work/JTIG/testJtig/testGrams/english-conll-5000-ROOT.txt"; // sentence file
			String targetFileName = "/home/neumann/work/JTIG/testJtig/testGrams/english-conll-5000-stats.txt"; // output file

			BufferedReader br = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(textFile), "UTF8"));
			BufferedWriter bw = new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream(targetFileName), "UTF8"));

			parser = new JTIGParser("");
			JTIGParser.setProperty("grammar.lexicon.path", grammar);
			JTIGParser.setProperty("parser.core.parselevel", "FOREST"); // "LOOKUP"

			System.out.println("Reading grammar: " + grammar);
			bw.write("Reading grammar: " + grammar + "\n");

			parser.readLexicon();

			int sentenceNumber = 1;
			int max = 1000;
			String line = "";
			long time1 = System.currentTimeMillis();

			System.out.println("Start processing corpus: " + textFile);
			bw.write("Start processing corpus: " + textFile + "\n");

			System.out.println("Parsing level: " + JTIGParser.getParseLevel());
			bw.write("Parsing level: " + JTIGParser.getParseLevel() + "\n");		

			System.out.println("Output every next: " + max);
			while((line = br.readLine()) != null){
				long lineTime1 = System.currentTimeMillis();

				ParseResult parseResult = tr.callJTIGparser(line, sentenceNumber, bw);

				if ((sentenceNumber % max) == 0) {
					System.out.println(sentenceNumber + ": " + line);
				}

				long lineTime2 = System.currentTimeMillis();

				if (parseResult != null) {
					bw.write("sentId: " + sentenceNumber + 
							", msec:" +  (lineTime2-lineTime1) 
							+ ", length: " + parseResult.getTokens().length
							+ ", forest: " 
							+ parseResult.getForest().getRootDimension()
							+ ", items: " 
							+ parseResult.getAmountProcessedItems()
							+ " , "
							+ parseResult.getAmountUniqueItems() + "\n");
				}

				bw.flush();
				sentenceNumber++;
			}
			long time2 = System.currentTimeMillis();
			System.out.println("# sentences: " + sentenceNumber 
					+ "total parse time (ms): " + new Long(time2-time1).toString());
			bw.write("# sentences: " + sentenceNumber 
					+ "total parse time (ms): " + new Long(time2-time1).toString() + "\n");

			bw.close();
			br.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}

