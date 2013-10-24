package parser.early.inferencerules;

import java.util.PriorityQueue;

import parser.early.Chart;
import parser.early.DefaultItemFactory;
import parser.early.Item;

public abstract class InferenceRule {
	
	protected DefaultItemFactory factory;
	
	protected Chart chart;
	
	protected PriorityQueue<Item> agenda;
	
	public InferenceRule(DefaultItemFactory factory,Chart chart,PriorityQueue<Item> agenda){
		this.factory = factory;
		this.chart = chart;
		this.agenda = agenda;
	}
	
	public abstract void apply(Item item);
	
	public abstract boolean isApplicable(Item item);
	
	protected void addtoagenda(Item item){
		agenda.add(item);
	}
}
