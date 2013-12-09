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
	
	public static Integer[] IntArrayToIntegerArray(int[] old){
		Integer[] result = new Integer[old.length];
		for (int i = 0; i < old.length; i++){
			result[i] = old[i];
		}
		return result;
	}
	
	public static int[] AppendToIntArray(int[] toextend,int value){
		int[] newarray = Arrays.copyOf(toextend,toextend.length+1);
		newarray[toextend.length] = value;
		return newarray;
	}
	
	public static String getEndOfPath(String path){
		String[] tmp = path.split("/");
		if (tmp.length <= 0)
			return "";
		return tmp[tmp.length-1];
	}
}
