package tools.tokenizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import edu.northwestern.at.morphadorner.corpuslinguistics.tokenizer.DefaultWordTokenizer;
import edu.northwestern.at.morphadorner.corpuslinguistics.tokenizer.WordTokenizer;

public class MorphAdornoSentenceTokenizer implements SentenceTokenizer{

	@Override
	public Token[] getTokens(String sentence){
		List<Token> tokenlist = new LinkedList<Token>();
		int i = 0;
		
		WordTokenizer wordTokenizer = new DefaultWordTokenizer();
		List<String> words = wordTokenizer.extractWords(sentence);
		int[] offsets = wordTokenizer.findWordOffsets(sentence, words );
		
		for (String w : words){
			tokenlist.add(new Token(offsets[i], w));
			i++;
		}
		return tokenlist.toArray(new Token[0]);
	}
	
	@Override
	public Token[] getTokens(String filename, int line) throws FileNotFoundException {
		List<Token> tokenlist = new LinkedList<Token>();
		
		File f = new File(filename);
		FileInputStream fis = new FileInputStream(f);
		Scanner scanner = new Scanner(fis);
		
		int currentline_cnt = 0;
		String currentline ="";
		
		while (scanner.hasNextLine() && line >= currentline_cnt){
			currentline = scanner.nextLine();
			System.out.println(currentline);
			currentline_cnt++;
		}
		
		scanner.close();
		
		WordTokenizer wordTokenizer = new DefaultWordTokenizer();
		List<String> words = wordTokenizer.extractWords(currentline);
		int[] offsets = wordTokenizer.findWordOffsets(currentline, words );
		int i = 0;
		for (String w : words){
			tokenlist.add(new Token(offsets[i], w));
			i++;
		}
		return tokenlist.toArray(new Token[0]);
	}

	@Override
	public String getSentence(Token[] tokens){
		StringBuilder sb = new StringBuilder();
		for (Token t : tokens){
			sb.append(t.getLabel());
			sb.append(" ");
		}
		return sb.toString();
	}

}
