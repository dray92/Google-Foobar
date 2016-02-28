package primary;

/**
 * Line up the captives
====================

As you ponder sneaky strategies for assisting with the great rabbit escape, you realize that you have an opportunity to fool Professor Booleans guards into thinking there are fewer rabbits total than there actually are.

By cleverly lining up the rabbits of different heights, you can obscure the sudden departure of some of the captives.

Beta Rabbits statisticians have asked you for some numerical analysis of how this could be done so that they can explore the best options.

Luckily, every rabbit has a slightly different height, and the guards are lazy and few in number. Only one guard is stationed at each end of the rabbit line-up as they survey their captive population. With a bit of misinformation added to the facility roster, you can make the guards think there are different numbers of rabbits in holding.

To help plan this caper you need to calculate how many ways the rabbits can be lined up such that a viewer on one end sees x rabbits, and a viewer on the other end sees y rabbits, because some taller rabbits block the view of the shorter ones.

For example, if the rabbits were arranged in line with heights 30 cm, 10 cm, 50 cm, 40 cm, and then 20 cm, a guard looking from the left side would see 2 rabbits (30 and 50 cm) while a guard looking from the right side would see 3 rabbits (20, 40 and 50 cm).

Write a method answer(x,y,n) which returns the number of possible ways to arrange n rabbits of unique heights along an east to west line, so that only x are visible from the west, and only y are visible from the east. The return value must be a string representing the number in base 10.

If there is no possible arrangement, return "0".

The number of rabbits (n) will be as small as 3 or as large as 40
The viewable rabbits from either side (x and y) will be as small as 1 and as large as the total number of rabbits (n).

Languages
=========

To provide a Python solution, edit solution.py
To provide a Java solution, edit solution.java

Test cases
==========

Inputs:
    (int) x = 2
    (int) y = 2
    (int) n = 3
Output:
    (string) "2"

Inputs:
    (int) x = 1
    (int) y = 2
    (int) n = 6
Output:
    (string) "24"
 * @author Debosmit
 *
 */

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Line_Up_The_Captives {
	
	public static String answer(int left, int right, int n) {
		// general idea for this solution is to fix the position
		// of the tallest rabbit and get all combinations of 
		// rabbits on both sides of the tallest rabbit
		
		BigInteger count = BigInteger.ZERO;
		for(int posTallestRabbit = left - 1 ; posTallestRabbit < n - right + 1 ;  posTallestRabbit++) {
			int leftNum = posTallestRabbit;
			int rightNum = n - leftNum - 1;
			
			BigInteger leftCombinations = getAllCombinations(left - 1, leftNum);
			BigInteger rightCombinations = getAllCombinations(right - 1, rightNum);
			
			// split remaining rabbits on both sides [simple]
			BigInteger remainderCombinations = getCombination(n - 1, leftNum);
			
			// count += leftCombinations * rightCombinations * remainderCombinations
			count = count.add( leftCombinations.multiply(rightCombinations).multiply(remainderCombinations) );
		}
		return count.toString();
	}
	
	// mapping between rabbitsVisible, totalRabbitsAvailable 
	// and the number of possible combinations
	private static Map<Integer, Map<Integer, BigInteger>> map =  new HashMap<Integer, Map<Integer, BigInteger>>();
	private static BigInteger getAllCombinations(int rabbitsVisible, int totalRabbitsAvailable) {
		
		// if the number of rabbits that have to be visible is
		// ever greater than the total number of rabbits
		// available, there is no combinations possible.
		if(rabbitsVisible > totalRabbitsAvailable)
			return BigInteger.ZERO;
		
		// if the number of rabbits that have to be visible is
		// ever equal to the total number of rabbits, there is
		// one possible combination since the rabbits are distinct
		if(rabbitsVisible == totalRabbitsAvailable)
			return BigInteger.ONE;
		
		// if rabbitsVisible -> (totalRabbitsAvailable, combinations) is available
		// no need to do the math again
		if(map.containsKey(rabbitsVisible)) 
			if(map.get(rabbitsVisible).containsKey(totalRabbitsAvailable))
				return map.get(rabbitsVisible).get(totalRabbitsAvailable);
		
		// now, we have more rabbits available than we require to be 
		// visible; so the tallest rabbit can be placed in a few 
		// possible spots within the constraints
		BigInteger count = BigInteger.ZERO;
				
		for(int tallestRabbitPos = 0 ; tallestRabbitPos < totalRabbitsAvailable ; tallestRabbitPos++) {
			// everything till the tallestRabbit, including
			// the tallest rabbit can be seen
			int visible = tallestRabbitPos;
			
			// everything after the tallestRabbit not visible
			// excluding the tallest rabbit
			int hidden = totalRabbitsAvailable - visible - 1;
			
			// for the hidden rabbits, order doesn't matter
			BigInteger hiddenCombinations = getFactorial(hidden);
			
			// rabbits need to be split between the visible and 
			// hidden sides of the tallest rabbit
			// number of rabbits to be divided = totalRabbitsAvailable - 1
			// number of rabbits that are visible = visible
			BigInteger combinationsOfRabbitsAroundTallest = 
					getCombination(totalRabbitsAvailable - 1, visible);
			
			// visible rabbits combinations, excluding the tallest rabbit
			// the position of which has been fixed
			BigInteger visibleCombinations = getAllCombinations(rabbitsVisible - 1, visible);
			
			// update count
			// count += hiddenCombinations * combinationsOfRabbitsAroundTallest * visibleCombinations
			count = count.add( hiddenCombinations.multiply(combinationsOfRabbitsAroundTallest).multiply(visibleCombinations) );
		}
		
		if(!map.containsKey(rabbitsVisible))
			map.put(rabbitsVisible, new HashMap<Integer, BigInteger>());
		map.get(rabbitsVisible).put(totalRabbitsAvailable, count);
		
		return count;
	}
	
	// formula source: http://www.mathwords.com/c/combination_formula.htm
	// nCr = (n!) / { (r!) (n-r)! }
	//	   = n(n-1)(n-2)...(n-r-1)(n-r!) / { (r!) (n-r)! }
	//	   = n(n-1)...(n-r-1) / (r!)
	private static BigInteger getCombination(int n, int r) {
		// not doing the last line of the formula since we 
		// already have a DP-implementation of factorial
		// available, and we would rather use that
				
		BigInteger nFactorial = getFactorial(n);
		BigInteger rFactorial = getFactorial(r);
		BigInteger nMinusRFactorial = getFactorial(n-r);
		
		BigInteger denominator = rFactorial.multiply(nMinusRFactorial);
		
		return nFactorial.divide(denominator);		
	}
	
	
	// DP table to keep track of factorials
	private static Map<Integer, BigInteger> factorialDP = new HashMap<Integer, BigInteger>();
	/*
	 * takes in an integer parameter and returns
	 * a BigInteger representation of the factorial
	 * uses a global variable factorialDP to store results
	 * to make future computations of factorial are faster.
	 * Intermediary results are not stored since that is
	 * causing a StackOverflow error.
	 */
	private static BigInteger getFactorial(int n) {
		if(n == 1)
			return BigInteger.ONE;
		
		if(factorialDP.containsKey(n)) 
			return factorialDP.get(n);
		
		BigInteger newNumber = BigInteger.ONE;
		for(int multiple = 2 ; multiple <= n ; multiple++)
			newNumber = newNumber.multiply(BigInteger.valueOf(multiple));
		
		// add to HashMap for future reference
		factorialDP.put(n, newNumber);
		
		return newNumber;	
	}
	
	public static void main(String[] args) {
//		int x = 2, y = 2, n = 3;
//		System.out.println(answer(x,y,n));
		
		int x = 1, y = 2, n = 6;
		System.out.println(answer(x,y,n));
	}
}
