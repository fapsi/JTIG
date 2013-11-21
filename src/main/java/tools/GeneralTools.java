package tools;

import java.util.Arrays;
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
	
	public static int[] AppendToIntArray(int[] toextend,int value){
		int[] newarray = Arrays.copyOf(toextend,toextend.length+1);
		newarray[toextend.length] = value;
		return newarray;
	}
}
