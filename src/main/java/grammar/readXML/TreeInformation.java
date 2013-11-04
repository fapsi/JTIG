/**
 * 
 */
package grammar.readXML;


import grammar.buildJtigGrammar.TreeType;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class TreeInformation {
	
	boolean actualonspine;
	
	TreeType treetype;
	
	public TreeInformation (TreeType treetype){
		this.treetype = treetype;
		this.actualonspine = this.treetype != TreeType.Initial;
	}
	
	public TreeType getTreeType(){
		return treetype;
	}
	
	public void setActualOnSpine(boolean value){
		actualonspine = value;
	}
	
	public boolean getActualOnSpine(){
		return actualonspine;
	}
	
}
