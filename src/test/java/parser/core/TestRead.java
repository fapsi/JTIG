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

public class TestRead {
	private static JTIGParser parser;
	private MorphAdornoSentenceTokenizer st;

	
	
	public void tearDown() throws Exception {
		System.out.println("Tear down.");
		File dir = new File("data/runs/");
		GeneralTools.deleteDirectory(dir);	
	}

	
	public void testLexicon_234_pt2( String url_input_file) {
		st = new MorphAdornoSentenceTokenizer();
		ParseResult run = null;
		try {
			
			Token[] tokens = st.getTokens(url_input_file);
			run = parser.parseSentence(url_input_file,tokens);
		
			
			
			//System.out.println(run.getDerivedTrees().size());
			//System.out.println("Read Sentence: "+st.getSentence(tokens));
			
		} catch (Exception  e) {
			System.out.println(e.getMessage());
		}
		
		//System.out.println(run.getLog());
		
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
       TestRead tr=  new TestRead();
       

       
       try {
           parser = new JTIGParser("");
    	String url_lexicon = "/Users/gune00/dfki/JTIG/testJtig/testGrams/english-conll-5000.xml";//lexicon path
    	JTIGParser.setProperty("grammar.lexicon.path", url_lexicon);
    	
   		String path = "/Users/gune00/dfki/JTIG/testJtig/testGrams/english-conll-5000-ROOT.txt";//text file path
   		parser.readLexicon();
   		
   		FileReader fileread = new FileReader(path) ;
   		
    	String sentence;
		BufferedReader br = new BufferedReader(fileread);
		String pathtoWrite = "/Users/gune00/dfki/JTIG/testJtig/testGrams/english-conll-5000-stats.txt";
		FileWriter fw= new FileWriter(pathtoWrite, true);
	    BufferedWriter bw= new BufferedWriter(fw);
		//long time= System.currentTimeMillis();
       //	long time2;
		int sentencenumber = 0;
		String line = br.readLine();
		while(line != null){
			long begin = System.currentTimeMillis();
			sentencenumber = sentencenumber+1;
			tr.testLexicon_234_pt2(line);
			//LocalTime end = LocalTime.now();
			
			
			bw.write("Time to write sentence "+ Integer.toString(sentencenumber) + " is " +  new Long(System.currentTimeMillis() -begin).toString()+" ms." + "\n");
			line = br.readLine();
		}
		bw.close();
		fw.close();
		br.close();
		fileread.close();
		System.exit(1);
		
	
		
		
	
	}
	catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
     
	}

}

