package parser.early.inferencerules;

import java.util.PriorityQueue;

import parser.early.Chart;
import parser.early.DefaultItemFactory;
import parser.early.Item;
import parser.lookup.ActivatedLexicon;

public abstract class InferenceRule {
	
	protected DefaultItemFactory factory;
	
	protected Chart chart;
	
	protected PriorityQueue<Item> agenda;

	protected ActivatedLexicon activatedlexicon;
	
	protected InferenceRule(){}
	
	public abstract void apply(Item item);
	
	public abstract boolean isApplicable(Item item);
	
	protected void addtoagenda(Item item){
		agenda.add(item);
	}

	public void setFactory(DefaultItemFactory factory) {
		this.factory = factory;
	}

	public void setChart(Chart chart) {
		this.chart = chart;
	}

	public void setAgenda(PriorityQueue<Item> agenda) {
		this.agenda = agenda;
	}

	public void setActivatedLexicon(ActivatedLexicon activatedlexicon) {
		this.activatedlexicon = activatedlexicon;
	}
}
