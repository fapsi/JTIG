/**
 * 
 */
package parser.early;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Fabian Gallenkamp
 */
public class ItemSet {
	
	private Map<Item,Item> activeitems;
	
	private Map<Item,Item> passiveitems;
	
	public ItemSet(){
		activeitems = new HashMap<Item,Item>();
		passiveitems = new HashMap<Item,Item>();
	}
	
	public void add(Item item) {
		Map<Item,Item> addto = item.isActive()?activeitems:passiveitems;

		if (addto.containsKey(item)){
			Item identicalitem = addto.get(item);
			identicalitem.addDerivations(item.getDerivations());
		} else 
			addto.put(item,item);
		
	}

	public void getItems(List<Item> result,ItemFilter filter) {
		switch (filter.getStatus()) {
		case All:
			for (Map.Entry<Item,Item> kvpair : passiveitems.entrySet()){
				if (filter.apply(kvpair.getKey()))
					result.add(kvpair.getKey());
			}
		case Active:
			for (Map.Entry<Item,Item> kvpair : activeitems.entrySet()){
				if (filter.apply(kvpair.getKey()))
					result.add(kvpair.getKey());
			}
			break;
		case Passive:
			for (Map.Entry<Item,Item> kvpair : passiveitems.entrySet()){
				if (filter.apply(kvpair.getKey()))
					result.add(kvpair.getKey());
			}
			break;
		default:
			break;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (passiveitems.size() > 0)
			sb.append("\n\tPassive Items: ");
		for (Map.Entry<Item,Item> kvpair : passiveitems.entrySet()){
			sb.append(kvpair.getKey().toString());
			sb.append(",");
		}
		if (passiveitems.size() > 0)
			sb.append("\n\tActive Items: ");
		for (Map.Entry<Item,Item> kvpair : activeitems.entrySet()){
			sb.append(kvpair.getKey().toString());
			sb.append(",");
		}
		return sb.toString();
	}
}
