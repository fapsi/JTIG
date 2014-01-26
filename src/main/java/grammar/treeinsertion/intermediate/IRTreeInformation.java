/**
 * 
 */
package grammar.treeinsertion.intermediate;

import grammar.treeinsertion.TreeType;



/**
 * 
 * @author Fabian Gallenkamp
 */
public class IRTreeInformation {
	
	boolean actualonspine;
	
	TreeType treetype;
	
	public IRTreeInformation (TreeType treetype){
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
