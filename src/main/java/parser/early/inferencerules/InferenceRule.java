package parser.early.inferencerules;

import parser.early.components.Chart;
import parser.early.components.DefaultItemFactory;
import parser.early.components.Item;
import parser.early.components.agenda.Agenda;
import parser.lookup.ActivatedLexicon;

public abstract class InferenceRule {
	
	protected DefaultItemFactory factory;
	
	protected Chart chart;
	
	protected Agenda agenda;

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

	public void setAgenda(Agenda agenda) {
		this.agenda = agenda;
	}

	public void setActivatedLexicon(ActivatedLexicon activatedlexicon) {
		this.activatedlexicon = activatedlexicon;
	}
}
