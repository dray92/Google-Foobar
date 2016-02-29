package primary;

import java.util.Arrays;

/**
 * Peculiar balance
================
Can we save them? Beta Rabbit is trying to break into a lab that contains the only known zombie cure - but there's an
obstacle. The  door will only open if a challenge is solved correctly. The future of the zombified rabbit population is
at stake, so Beta reads the  challenge: There is a scale with an object on the left-hand side, whose mass is given in
some number of units. Predictably, the task is to  balance the two sides. But there is a catch: You only have this
peculiar weight set, having masses 1, 3, 9, 27, ... units. That is, one  for each power of 3. Being a brilliant
mathematician, Beta Rabbit quickly discovers that any number of units of mass can be balanced  exactly using this set.
To help Beta get into the room, write a method called answer(x), which outputs a list of strings representing where the
weights should be  placed, in order for the two sides to be balanced, assuming that weight on the left has mass x units.
The first element of the output list should correspond to the 1-unit weight, the second element to the 3-unit weight,
and so on. Each  string is one of:
"L" : put weight on left-hand side
"R" : put weight on right-hand side
"-" : do not use weight
To ensure that the output is the smallest possible, the last element of the list must not be "-".
x will always be a positive integer, no larger than 1000000000.
 * @author Debosmit
 *
 */
public class Peculiar_Balance {
	
	private static final int MAX_ALLOWED_VALUE = 1000000000;
	
	public static String[] answer(int x) {
		
		int nextHigherPow = getNextHigherPower(x); 
		
		/** 
		 * Ideation:
		 * Say, x is 25. This means I have 25 on the left
		 * side. The nextHigherPower of 3 from 25 is 27.
		 * Hence, our result set will have a size of 1 + log(27)[base 3],
		 * which is 1 + 3 = 4, since the 3^3 = 27.
		 * The result set will look like [" ", " ", " ", " "], where the first
		 * index shows position of 1(3^0), second shows position of 3(3^1),
		 * third shows position of 9(3^2), and fourth shows position of 27(3^3).
		 * Our result set has a 0-based index.
		 * 
		 * Current power being considered is 3.
		 * Current difference in sides = +25	(+ => left is more) (- => right is more)
		 * First weight being considered is 27. Now, will adding 27 to the left decrease
		 * the absolute value of the difference? (|+25 + 27| < |+25|)? No. Will adding 27 to 
		 * the right decrease the absolute value of the difference? (|+25 - 27| < |+25|). Yes.
		 * Lets do it!
		 * [" ", " ", " ", "R"]
		 * 
		 * Then, consider the next power. Power 2.
		 * Current difference in sides = -2.
		 * Weight being considered is 9. Now, will adding 9 to the left decrease
		 * the absolute value of the difference? (|-2 + 9| < |-2|)? No. Will adding 9 to 
		 * the right decrease the absolute value of the difference? (|+25 - 27| < |+2|). No.
		 * Then, the weight of 9 is useless to our purposes.
		 * [" ", " ", "-", "R"]
		 * 
		 * Then, consider the next power. Power 1.
		 * Current difference in sides = -2.
		 * Weight being considered is 3. Now, will adding 3 to the left decrease
		 * the absolute value of the difference? (|-2 + 3| < |-2|)? Yes. Lets do it!
		 * [" ", "L", "-", "R"]
		 * 
		 * Then, consider the next power. Power 0.
		 * Current difference in sides = +1.
		 * Weight being considered is 1. Now, will adding 1 to the left decrease
		 * the absolute value of the difference? (|+1 + 1| < |+1|)? No. Will adding 1 to 
		 * the right decrease the absolute value of the difference? (|+1 - 1| < |+1|). Yes.
		 * Lets do it!
		 * ["R", "L", "-", "R"]
		 */
		
		
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
		// if the last character is "-", then return array that
		// doesn't contain the "-". There can't be another "-"
		// before this since that would then imply that both
		// ceiling( log10(x) / log10(3) ) and floor( log10(x) / log10(3) ) 
		// are not being considered, which is plain absurd
		if(result[result.length - 1].compareTo("-") == 0)
			return Arrays.copyOf(result, result.length - 1);
		
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
			pow3 = new int[2 + (int)(Math.log10(MAX_ALLOWED_VALUE)/Math.log10(3))];
			pow3[0] = 1;
			for(int i = 1 ; i < pow3.length ; i++)
				pow3[i] = 3 * pow3[i-1];
		}
	}
	
	public static void main(String[] args) {
		
		final boolean TESTER = false;
		
		if(TESTER) {
			long start = System.currentTimeMillis();
			for(int i = 1 ; i <= 1000000000 ; i++) {
				String[] ans = answer(i);
				long left = i;
				long right = 0;
				for(int index = 0 ; index < ans.length ; index++) {
					if(ans[index].compareTo("L") == 0) 
						left += Math.pow(3,index);
					else if(ans[index].compareTo("R") == 0)
						right += Math.pow(3,index);
				}
				
				// if left and right aren't equal, we failed
				if(left != right) {
					System.out.println("Failed at x = " + i);
					break;
				}
				if(i%10000 == 0)
					System.out.println("Testing x = " + i);
			}
			System.out.println( "Total time: " + (System.currentTimeMillis() - start) );
		}
		System.out.println(Arrays.toString(answer(999999999)));
		System.out.println(Arrays.toString(answer(25)));
	}
}
