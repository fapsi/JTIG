package tools;

import java.util.List;

public class GeneralTools {

	public static int[] ListToIntArray(List<Integer> l) {
		int[] r = new int[l.size()];
		int i = 0;
		for (Integer p : l){
			r[i] = p.intValue();
			i++;
		}

		return r;
	}
}
