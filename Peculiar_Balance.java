package primary;

import java.util.Arrays;

public class Peculiar_Balance {
	
	public static String[] answer(int x) {
		
		int nextHigherPow = getNextHigherPower(x);
		
		String[] result = new String[nextHigherPow+1];
		// this loop ensures that the same weight isn't encountered more than once
		for(int i = nextHigherPow ; i >= 0 ; i--) {
			int weight = pow3[i];
			// now, where to add to add this weight
			// our goal is that after adding this weight, 
			// the net difference between the left and
			// right sides must go down
			
			// add to left side
			if( Math.abs(x+weight) < Math.abs(x) ) {
				x += weight;
				result[i] = "L";
			} 
			
			// add to right
			else if( Math.abs(x-weight) < Math.abs(x) ) {
				x -= weight;
				result[i] = "R";
			} 
			
			// if weight is not affecting anything, don't need to add
			else {
				result[i] = "-";
			}
		}
			
		return result;
	}
	
	private static int getNextHigherPower(int x) {
		for(int i = pow3.length-1 ; i>= 0 ; i--) {
			if(pow3[i] == x)
				return i;
			if(pow3[i] < x)
				return i+1;
		}
		return pow3.length;
	}
	
	private static int[] pow3;
	// initialize pow3
	static {
		if(pow3 == null) {
			// log(1000000000)/log(3) = 18.8631294686
			pow3 = new int[2 + (int)(Math.log10(1000000000)/Math.log10(3))];
			pow3[0] = 1;
			for(int i = 1 ; i < pow3.length ; i++)
				pow3[i] = 3 * pow3[i-1];
		}
	}
	
	public static void main(String[] args) {
		System.out.println(Arrays.toString(answer(999999999)));
	}
}
