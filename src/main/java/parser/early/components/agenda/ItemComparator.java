/**
 * 
 */
package parser.early.components.agenda;

import java.util.Comparator;

import parser.early.components.DerivationType;
import parser.early.components.Item;
import parser.early.components.ItemDerivation;
import parser.early.components.ItemFilter;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class ItemComparator implements Comparator<Item> {
	
	private static int getWeight(DerivationType type){
		switch(type){
		case Consume:
			return 50;
		case PredictTraversation:
			return 500;
		case PredictSubstitution:
		case PredictLeftAux: 
		case PredictRightAux:
			return 300;
		case CompleteTraversation:
			return 150;
		case CompleteSubstitution:
			return 100;
		case CompleteLeftAdjunction:
		case CompleteRightAdjunction:
			return 100;
		default: return 0;
		}
	}

	private ItemFilter terminationcriterion;

	public ItemComparator(ItemFilter terminationcriterion){
		this.terminationcriterion = terminationcriterion;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Item a, Item b) {
		boolean term_a = terminationcriterion.apply(a);
		boolean term_b = terminationcriterion.apply(b);
		if (term_a && term_b)
			return 0;
		if (term_a)
			return -100000;
		if (term_b)
			return 100000;	
		
		int imp_a = 0; int imp_b = 0;
		for (ItemDerivation deriv : a.getDerivations()){
			imp_a += getWeight(deriv.getType());
		}
		for (ItemDerivation deriv : b.getDerivations()){
			imp_b += getWeight(deriv.getType());
		}
		int distance = Math.abs(imp_a - imp_b);
		if (imp_a < imp_b)
			return -distance*10;
		if (imp_a > imp_b) 
			return distance*10;
		/*
		int distancea = Math.abs(a.getLeft() - a.getRight());
		int distanceb = Math.abs(b.getLeft() - b.getRight());
		if (distancea < distanceb)
			return -Math.abs(distancea-distanceb)*10;
		if (distancea > distanceb)
			return Math.abs(distancea-distanceb)*10;
			*/
		/*	
		int dotdistance = Math.abs(a.getDotPosition()-b.getDotPosition());
		if (a.getDotPosition() < b.getDotPosition())
			return -dotdistance;
		if (a.getDotPosition() > b.getDotPosition())
			return dotdistance;*/
		return 0;
	}

}
