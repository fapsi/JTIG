/**
 * 
 */
package parser.early.components;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import parser.lookup.ActivatedElementaryTree;
import grammar.treeinsertion.Entry;
import grammar.treeinsertion.Layer;
import grammar.treeinsertion.NodeType;
import grammar.treeinsertion.TreeType;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class Item {
	
	private int left;
	
	private int right;
	
	private int dotposition;
	
	private Layer layer;
	
	private ActivatedElementaryTree activatedruletree;
	
	private double probability;
	
	private int index;
	
	private Set<ItemDerivation> derivations;

	public Item(int left, int right, int dotposition, Layer layer,
			ActivatedElementaryTree activatedruletree, double probability, int index) {
		this.left = left;
		this.right = right;
		this.dotposition = dotposition; //TODO: maybe always 1 by construction --> always active on construction
		this.layer = layer;
		this.activatedruletree = activatedruletree;
		this.probability = probability;
		this.index = index;

		this.derivations = new HashSet<ItemDerivation>();
	}

	public void addDerivation(ItemDerivation itemDerivation){
		this.derivations.add(itemDerivation);
	}

	public void addDerivations(Set<ItemDerivation> derivations_param) {
		for (ItemDerivation deriv : derivations_param)
			if (!derivations.contains(deriv))
				derivations.add(deriv);
	}
	
	public void moveDotLeft(){
		if (isActive())
			++dotposition;
		return;
	}
	
	public boolean isActive() {
		return dotposition < layer.getEntrys().length;
	}
	
	public boolean isPassive(){
		return !isActive();
	}
	
	public Entry getLeftHandSide(){
		return layer.getEntry(0);
	}
	
	public boolean inLexicalAnchor(String label){
		return activatedruletree.getLexicalAnchor().contains(label);
	}
	
	public boolean hasParentGornNumber(Item item){
		if (layer == null)
			return false;
		int[] gornnumber = item.getLayer() != null? item.getLayer().getGornNumber() : null;
		int[] truncatedgornnumber = Arrays.copyOfRange(gornnumber, 0, gornnumber.length-1);
		return Arrays.equals(truncatedgornnumber, layer.getGornNumber());
	}
	
	public boolean hasInitialTypeTree(){
		return activatedruletree!=null?activatedruletree.isInitial():false;
	}

	public boolean hasAuxiliaryTypeTree() {
		return activatedruletree!=null?activatedruletree.isAuxiliary():false;
	}
	
	public boolean hasLeftAuxiliaryTypeTree() {
		return activatedruletree!=null?activatedruletree.getType() == TreeType.LeftAuxiliary:false;
	}
	
	public boolean hasRightAuxiliaryTypeTree() {
		return activatedruletree!=null?activatedruletree.getType() == TreeType.RightAuxiliary:false;
	}
	
	public Entry[] getRightHandSide(){
		Entry[] entrys = layer.getEntrys();
		if (entrys != null && entrys.length > 1){
			return Arrays.copyOfRange(layer.getEntrys(), 1, entrys.length);
		}
		return null;
	}
	public Entry getNextEntry(){
		return  layer.getEntry(dotposition);
	}
	
	public NodeType getNextEntryType(){
		Entry nextentry = getNextEntry();
		if (nextentry != null)
			return nextentry.getNodeType();
		return null;
	}
	
	public Layer getNextLayer(){
		
		int[] address = Arrays.copyOf(layer.getGornNumber(), layer.getGornNumber().length+1);
		address[address.length-1] = dotposition;
		
		return this.activatedruletree.getLayer(address);
	}

	public Layer getLayer() {
		return layer;
	}
	
	public ActivatedElementaryTree getActivatedElementaryTree(){
		return activatedruletree;
	}
	
	public int getLeft(){
		return left;
	}
	
	public int getRight(){
		return right;
	}
	// Probability
	public double getProbability(){
		return probability;
	}

	public int getDotPosition() {
		return dotposition;
	}
	
	public Set<ItemDerivation> getDerivations() {
		return derivations;
	}
	public int getID() {
		return index;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((activatedruletree == null) ? 0 : activatedruletree
						.hashCode());
		result = prime * result + dotposition;
		result = prime * result + ((layer == null) ? 0 : layer.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (activatedruletree == null) {
			if (other.activatedruletree != null)
				return false;
		} else if (!activatedruletree.equals(other.activatedruletree))
			return false;
		if (dotposition != other.dotposition)
			return false;
		if (layer == null) {
			if (other.layer != null)
				return false;
		} else if (!layer.equals(other.layer))
			return false;
		return true;
	}

	public String getDottedRuleString(){
		StringBuilder sb = new StringBuilder();
		Entry[] entrys = layer.getEntrys();
		for (int i = 0; i < entrys.length; i++){
			if (dotposition == i)
				sb.append(".");
			sb.append(entrys[i].getLabel()+" ");
			if (i == 0)
				sb.append("=>");
		}
		if (dotposition == entrys.length)
			sb.append(".");
		return sb.toString();
	}
	
	public String toStringUgly(){
		return "Item [left=" + left + ", right=" + right + ", dotposition="
				+ dotposition + ", layer=" + layer + ", activatedruletree="
				+ activatedruletree + ", probability=" + probability
				+ ", index=" + index + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
			return "["+(isActive()?"A":"P")+" "+getDottedRuleString()+" RuleID=" + activatedruletree
					+ ", ID=" + index + ",("+left+","+right+"), "+Arrays.toString(layer.getGornNumber())+"]";
	}
}
