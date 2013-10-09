package tools.tokenizer;

import java.util.LinkedList;
import java.util.List;

import edu.northwestern.at.morphadorner.corpuslinguistics.tokenizer.DefaultWordTokenizer;
import edu.northwestern.at.morphadorner.corpuslinguistics.tokenizer.WordTokenizer;

public class MorphAdornoSentenceTokenizer implements SentenceTokenizer{

	private List<Token> tokens;
	
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
		this.tokens = tokenlist;
		return tokenlist.toArray(new Token[0]);
	}

}
