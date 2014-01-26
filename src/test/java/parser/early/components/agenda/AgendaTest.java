/**
 * 
 */
package parser.early.components.agenda;

import static org.junit.Assert.*;
import grammar.treeinsertion.Entry;
import grammar.treeinsertion.Layer;
import grammar.treeinsertion.NodeType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import parser.early.components.Item;
import parser.early.components.ItemFilter;
import parser.early.components.ItemStatus;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class AgendaTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		/*
		Agenda a = new Agenda(10, new ItemComparator(new ItemFilter() {
			
			@Override
			public ItemStatus getStatus() {
				return ItemStatus.Passive;
			}
			
			@Override
			public int getStart() {
				return 0;
			}
			
			@Override
			public int getEnd() {
				return 4;
			}
			
			@Override
			public boolean apply(Item item) {
				return item.getID() == 4;
			}
		}));
		a.offer(new Item(0, 2, 3, new Layer(new int[]{0}, new Entry[]{new Entry(NodeType.NONTERM, "A"),new Entry(NodeType.NONTERM, "B")} , true), null, 0.1, 1));
		a.offer(new Item(0, 4, 3, new Layer(new int[]{0}, new Entry[]{new Entry(NodeType.NONTERM, "A"),new Entry(NodeType.NONTERM, "B")} , true), null, 0.1, 4));
		a.offer(new Item(0, 1, 3, new Layer(new int[]{0}, new Entry[]{new Entry(NodeType.NONTERM, "A"),new Entry(NodeType.NONTERM, "B")} , true), null, 0.1, 5));
		a.offer(new Item(2, 4, 3, new Layer(new int[]{0}, new Entry[]{new Entry(NodeType.NONTERM, "A"),new Entry(NodeType.NONTERM, "B")} , true), null, 0.1, 4));
		
		
		while (!a.isEmpty()){
			System.out.println(a.poll());
		}*/
	}

}
