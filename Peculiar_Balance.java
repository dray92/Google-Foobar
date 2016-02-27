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
	
	public static void main(String[] args) {
		
		String[] ans = answer(1000000000 - 1);
		System.out.println(Arrays.toString(ans));
	}
}
