/**
 * 
 */
package grammar.buildJtigGrammar;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import tools.tokenizer.Token;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class Lexicon {

	private List<RuleTree> content;
	
	private HashMap<String,Lexicon> entrys;
	
	public Lexicon() {
		content = new LinkedList<RuleTree>();
		entrys = new HashMap<String,Lexicon>();
	}
	
	public void add(RuleTree toadd){
		List<String> remaining = toadd.getlexicalanchors();
		add (toadd,remaining);
	}
	
	private void add(RuleTree toadd,List<String> remaining){
		if (remaining.size() <= 0)
			return;
		
		String anchor = remaining.get(0);
		Lexicon sublexicon = entrys.get(anchor);
		
		if (sublexicon == null){
			sublexicon = new Lexicon();
			entrys.put(anchor,sublexicon);
		}
		
		if (remaining.size() > 1){
			remaining.remove(0);
			sublexicon.add(toadd, remaining);
		} else {
			sublexicon.content.add(toadd);
		}
	}
	
	public List<RuleTree> find(List<Token> index,int pos){
		Lexicon found = this.entrys.get(index.get(pos).getLabel());
		if (found == null)
			return null;
		if (pos < index.size() - 1){
			return found.find(index,pos+1);
		}
		return found.content;
	}

	public int size() {
		int i = this.content.size();
		for (Entry<String, Lexicon> l : this.entrys.entrySet()){
			i += l.getValue().size();
		}
		return i;
	}
	
	@Override
	public String toString() {
		return "Lexicon:<\n" + toString(0)+">";
	}
	
	public String toString(int depth){
		StringBuilder sb = new StringBuilder();
		sb.append("");

		for (Entry<String, Lexicon> l : this.entrys.entrySet()){
			indent(sb,depth);
			sb.append("'"+l.getKey()+"'");
			sb.append(" ");
			Lexicon lex = l.getValue();
			sb.append(lex.content);
			if (lex.entrys.size() >0)
				sb.append(":");
			sb.append("\n");
			sb.append(l.getValue().toString(depth+1));
		}
		/*indent(sb,depth);
		sb.append(" ");
		sb.append(this.content);
		*/
		return sb.toString();
	}
	
	private void indent(StringBuilder sb, int i){
		for (int k=0;k < i;k++){
			sb.append("\t");
		}
	}
}
