package primary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Peculiar_Balance {

	public static String[] answer(int x) {
		// will keep track of added weights
		// this is being done instead of directly initializing the result
		// ArrayList since there will be a problem with orderings and instead
		// of wasting a bunch of memory, this seems like a better approach
		// Integer's that are negative imply they are on the left
		List<Integer> weightList = new ArrayList<Integer>();
		
		// presently, more weight on the left
		int imbalance = -x;
		
		// continue till both sides aren't balanced
		while(imbalance != 0) {
			int nearestPow3 = higherPower3(Math.abs(imbalance));
			if( weightList.contains(new Integer(nearestPow3)) || weightList.contains(new Integer(-nearestPow3)) )
				nearestPow3 /= 3;
			if(imbalance < 0) {
				// add to right
				weightList.add(nearestPow3);
				imbalance += nearestPow3;
			} else {
				// add to left
				weightList.add(-nearestPow3);
				imbalance -= nearestPow3;
			}
		}
		
		// first number in the weightList will give an indication of the 
		// max weight inserted into the balance
		int size = Math.abs(weightList.get(0));
		
		// get corresponding power of 3
		// don't really care that much about float to int conversion here
		size = (int) (Math.log10(size) / Math.log10(3));
		
		String[] ans = new String[size+1];
		
		// initialize ans list with "-"
		for(int i = 0 ; i <= size ; i++) 
			ans[i] = "-";
		
		// replace "-" with appropriate weight wherever necessary
		for(int i = 0 ; i < weightList.size() ; i++) {
			int val = weightList.get(i);
			
			int index = (int) (Math.log10(Math.abs(val)) / Math.log10(3));
			ans[index] = (val<0 ? "L" : "R");
		}
		
		return ans;
	}
	
	private static Map<Integer, Integer> higherPower3 = new HashMap<Integer, Integer>();
	private static int higherPower3(int n) {
		if(isPowerOf3(n))
			return n;
		
		if(higherPower3.containsKey(n))
			return higherPower3.get(n);
		
		int count = 0;
		while(n > 0) {
			n /= 3;
			count++;
		}
		
		int retVal = (int) Math.pow(3, count);
		higherPower3.put(n, retVal);
		return retVal;
	}
	
	private static Map<Integer, Boolean> isPowerOf3 = new HashMap<Integer, Boolean>();
	private static boolean isPowerOf3(int n) {
		if(isPowerOf3.containsKey(n))
			return isPowerOf3.get(n);
		
		while (n % 3 == 0) {
		    n /= 3;
		}
		
		boolean retVal = n == 1;
		isPowerOf3.put(n, retVal);
		return retVal;
	}
	
	public static String[] answer2(int x) {
		
		getPow3Array();
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
	private static void getPow3Array() {
		if(pow3 == null) {
			// log(1000000000)/log(3) = 18.8631294686
			pow3 = new int[2 + (int)(Math.log10(1000000000)/Math.log10(3))];
			pow3[0] = 1;
			for(int i = 1 ; i < pow3.length ; i++)
				pow3[i] = 3 * pow3[i-1];
		}
	}
	
	public static void main(String[] args) {
		int NUM_ITERS = 500000;
		String[] ans = null;
		
		long start = System.currentTimeMillis();
		
//		while(NUM_ITERS-- > 0) {
//			ans = answer(1000000000 - 1);
//		}
		System.out.println( "Time: " + (System.currentTimeMillis() - start) + " msecs");
		
		System.out.println(Arrays.toString(ans));
		
		int left = 0;
		int right = 0;
//		for(int i = 0 ; i < ans.length ; i++) {
//			if(ans[i].compareTo("L") == 0)
//				left += Math.pow(3, i);
//			else if(ans[i].compareTo("R") == 0)
//				right += Math.pow(3, i);
//		}
		System.out.println("left =  " + left);
		System.out.println("right = " + right);
		
		
		ans = null;
		start = System.currentTimeMillis();
		
		while(NUM_ITERS-- > 0) {
			ans = answer2(1000000000 - 1);
		}
		System.out.println( "Time: " + (System.currentTimeMillis() - start) + " msecs");
		
		System.out.println(Arrays.toString(ans));
		
		
		
		left = 0;
		right = 0;
		for(int i = 0 ; i < ans.length ; i++) {
			if(ans[i].compareTo("L") == 0)
				left += Math.pow(3, i);
			else if(ans[i].compareTo("R") == 0)
				right += Math.pow(3, i);
		}
		System.out.println("left =  " + left);
		System.out.println("right = " + right);
	}
}
