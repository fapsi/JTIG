package tools.tokenizer;

import java.util.LinkedList;
import java.util.List;

import edu.northwestern.at.morphadorner.corpuslinguistics.tokenizer.DefaultWordTokenizer;
import edu.northwestern.at.morphadorner.corpuslinguistics.tokenizer.WordTokenizer;

public class MorphAdornoSentenceTokenizer extends SentenceTokenizer{

	private List<Token> tokens;
	
	public MorphAdornoSentenceTokenizer(String sentence) {
		super(sentence);
		extractTokens();
	}

	@Override
	public List<Token> getTokens() {
		return tokens;
	}
	
	private void extractTokens(){
		List<Token> tokenlist = new LinkedList<Token>();
		int i = 0;
		
		WordTokenizer wordTokenizer = new DefaultWordTokenizer();
		List<String> words = wordTokenizer.extractWords(this.sentence);
		int[] offsets = wordTokenizer.findWordOffsets(this.sentence, words );
		
		for (String w : words){
			tokenlist.add(new Token(offsets[i], w));
			i++;
		}
		this.tokens = tokenlist;
		
	}

}
