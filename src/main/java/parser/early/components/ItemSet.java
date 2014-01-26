/**
 * 
 */
package parser.early.components;

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
	
	public boolean add(Item item) {
		Map<Item,Item> addto = item.isActive()?activeitems:passiveitems;

		if (addto.containsKey(item)){
			Item identicalitem = addto.get(item);
			addto.remove(item); // TODO: necessary?
			identicalitem.addDerivations(item.getDerivations());
			addto.put(identicalitem, identicalitem);
			return false;
		} else {
			addto.put(item,item);
			return true;
		}
		
	}

	public void getItems(List<Item> result,ItemFilter filter) {
		switch (filter.getStatus()) {
		case All:
			for (Map.Entry<Item,Item> kvpair : passiveitems.entrySet()){
				if (filter.apply(kvpair.getKey()))
					result.add(kvpair.getKey());
			}
			// Note: here is no break;
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
		if (activeitems.size() > 0)
			sb.append("\n\tActive Items: ");
		for (Map.Entry<Item,Item> kvpair : activeitems.entrySet()){
			sb.append(kvpair.getKey().toString());
			sb.append(",");
		}
		return sb.toString();
	}
}
