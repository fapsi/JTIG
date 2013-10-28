/**
 * 
 */
package parser.lookup;

import java.util.Arrays;

import parser.early.Item;
import grammar.buildJtigGrammar.Entry;
import grammar.buildJtigGrammar.Layer;
import grammar.buildJtigGrammar.NodeType;
import grammar.buildJtigGrammar.TIGRule;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class ActivatedTIGRule extends TIGRule{
		
	/**
	 * 
	 */
	private int left;
	
	/**
	 * 
	 */
	private int right;
	
	/**
	 * 
	 * @param tree
	 * @param left
	 * @param right
	 */
	public ActivatedTIGRule(TIGRule tree,int left,int right){
		super(tree.getIndex(), tree.getLayers(), tree.getlexicalanchors(), tree.getFrequency(), tree.getProbability(), tree.getSpine());
		this.left = left;
		this.right = right;
	}

	public int getLeft(){
		return left;
	}
	
	public int getRight(){
		return right;
	}

	public boolean isAdjuntionCompatible(Item item,NodeType auxtype) {
		if (auxtype != NodeType.LFOOT && auxtype != NodeType.RFOOT)
			throw new IllegalArgumentException("Expected LFOOT or RFOOT, found: "+ auxtype.toString());
		
		Layer l = getLayer(Arrays.copyOfRange(spine, 0, spine.length-1));
		Entry spine_entry = l.getEntry(spine.length-1);
		
		return isAuxiliary() 
				&& spine_entry.getNodeType() == auxtype;
	}	
}
