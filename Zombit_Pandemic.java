package primary;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Zombit pandemic
===============

The nefarious Professor Boolean is up to his usual tricks. This time he is using social engineering to achieve his twisted goal of infecting all the rabbits and turning them into zombits! Having studied rabbits at length, he found that rabbits have a strange quirk: when placed in a group, each rabbit nudges exactly one rabbit other than itself. This other rabbit is chosen with uniform probability. We consider two rabbits to have socialized if either or both of them nudged the other. (Thus many rabbits could have nudged the same rabbit, and two rabbits may have socialized twice.) We consider two rabbits A and B to belong to the same rabbit warren if they have socialized, or if A has socialized with a rabbit belonging to the same warren as B.

For example, suppose there were 7 rabbits in Professor Boolean's nefarious lab. We denote each rabbit using a number. The nudges may be as follows:

1 nudges 2
2 nudges 1
3 nudges 7
4 nudges 5
5 nudges 1
6 nudges 5
7 nudges 3

This results in the rabbit warrens {1, 2, 4, 5, 6} and {3, 7}.

Professor Boolean realized that by infecting one rabbit, eventually it would infect the rest of the rabbits in the same warren! Unfortunately, due to budget constraints he can only infect one rabbit, thus infecting only the rabbits in one warren. He ponders, what is the expected maximum number of rabbits he could infect?

Write a function answer(n), which returns the expected maximum number of rabbits Professor Boolean can infect given n, the number of rabbits. n will be an integer between 2 and 50 inclusive. Give the answer as a string representing a fraction in lowest terms, in the form "numerator/denominator". Note that the numbers may be large.

For example, if there were 4 rabbits, he could infect a maximum of 2 (when they pair up) or 4 (when they're all socialized), but the expected value is 106 / 27. Therefore the answer would be "106/27".

Languages
=========

To provide a Python solution, edit solution.py
To provide a Java solution, edit solution.java

Test cases
==========

Inputs:
    (int) n = 4
Output:
    (string) "106/27"

Inputs:
    (int) n = 2
Output:
    (string) "2/1"
 * @author Debosmit
 *
 */
public class Zombit_Pandemic {
	
	// returns all partitions that don't have a single node tree
	private static Map<Integer, Set<ArrayList<Integer>>> partitionMap 
							= new HashMap<Integer, Set<ArrayList<Integer>>>();
	private static Set<ArrayList<Integer>> partition(int n) {
		if(partitionMap.containsKey(n))
			return partitionMap.get(n);
		
		Set<ArrayList<Integer>> hashSet = new HashSet<ArrayList<Integer>>();
        partition(n, n, new ArrayList<Integer>(), hashSet);
        
        partitionMap.put(n, hashSet);
        return hashSet;
    }
    private static void partition(int n, int max, ArrayList<Integer> arrayList, Set<ArrayList<Integer>> hashSet) {
        if (n == 0) {
        	// single nodes are not allowed
        	if(!(arrayList.contains(1)))
        		hashSet.add(new ArrayList<Integer>(arrayList));
        } else {
  
	        for (int i = Math.min(max, n); i >= 1; i--) {
	        	arrayList.add(i);
	            partition(n-i, i, arrayList, hashSet);
	            arrayList.remove(arrayList.size() - 1);
	        }
        }
    }

    // returns number of pseudoforests with N nodes containing only one connected tree
    private static Map<Integer, BigInteger> singleCCMap = new HashMap<Integer, BigInteger>();
    private static BigInteger getNumPseudoforestsSingleCC(int n) {
    	if(singleCCMap.containsKey(n))
    		return singleCCMap.get(n);
    	
    	BigInteger sum = BigInteger.ZERO;
    	
    	for(int nodes = 1 ; nodes < n ; nodes++) {
    		BigInteger choices = choose(n, nodes);
    		choices = choices.multiply( power(n - nodes, n - nodes) );
    		choices = choices.multiply( power(nodes, nodes) );
    		
    		sum = sum.add(choices);
    	}
    	
    	sum = sum.divide( BigInteger.valueOf(n) );
    	
    	singleCCMap.put(n, sum);
    	return sum;
    }
    
    // returns number of ways to split N labeled items into 
    // connected components of sizes based on the partition provided
    private static BigInteger numPossibleSplits(int n, ArrayList<Integer> partition) {
    	
    	// at least one split
    	BigInteger numerator = BigInteger.ONE;
    	int s = 0;
    	
    	for(int i = 0 ; i < partition.size() ; i++) {
    		BigInteger choices = choose(n - s, partition.get(i));
    		numerator = numerator.multiply(choices);
    		s += partition.get(i);
    	}
    	
    	// multiplicities of every integer in the partition for division
    	Map<Integer, Integer> map = new HashMap<Integer, Integer>();
    	for(int num: partition) {
    		if( !(map.containsKey(num)) ) 
    			map.put(num, 0);
    		
    		map.put(num, map.get(num) + 1);
    	}
    	
    	// ways of connecting labeled nodes inside each connected component
    	BigInteger denominator = BigInteger.ONE;
    	for(Integer key: map.keySet()) 
    		denominator = denominator.multiply( factorial(map.get(key)) );
    	
    	return numerator.divide(denominator);
    }
    
    // ref: http://math.stackexchange.com/questions/1090498/how-to-calculate-the-expected-maximum-tree-size-in-a-pseudoforest
    private static Map<Integer, BigInteger> numeratorMap
    						= new HashMap<Integer, BigInteger>();
    private static BigInteger getNumerator(int n) {
    	if(numeratorMap.containsKey(n))
    		numeratorMap.get(n);
    	
    	Set<ArrayList<Integer>> partitions = partition(n);
    	BigInteger sum = BigInteger.ZERO;
    	for(ArrayList<Integer> partition: partitions) {
    		int max = Integer.MIN_VALUE;
    		
    		BigInteger mul = BigInteger.ONE;
    		for(Integer p: partition) {
    			max = Math.max(max, p);
    			BigInteger num = getNumPseudoforestsSingleCC(p);
    			mul = mul.multiply(num);
    		}
    		BigInteger numPossible = numPossibleSplits(n, partition);
    		
    		mul = mul.multiply(numPossible).multiply(BigInteger.valueOf((long)max));
    		
    		sum = sum.add(mul);
    	}
    	
    	numeratorMap.put(n, sum);
    	return sum;
    }

    // full ideation can be found at: 
    public static String answer(int n) {
    	BigInteger numerator = getNumerator(n);
    	
    	// denominator is the total number of forests for N
        BigInteger denominator = power(n-1, n);
        
        // get gcd to reduce fraction
        BigInteger gcd = numerator.gcd(denominator);
        
        
        numerator = numerator.divide(gcd);
        denominator = denominator.divide(gcd);
        
        return numerator.toString() + "/" + denominator.toString();
    }
    
    
    /* Utility stuff */
    private static Map<Integer, BigInteger> factorialMap = new HashMap<Integer, BigInteger>();
	private static boolean nostart;
    private static long S, K, A;
    public static BigInteger factorial(int n) {
		if (n < 2)
	        return BigInteger.ONE;

	    // if factorial was calculated before
	    if(factorialMap.containsKey(n))
	    	return factorialMap.get(n);

        nostart = false;
        int h = n / 2;
        S = h + 1;
        K = S + h;
        A = (n & 1) == 1 ? K : 1;
        if ((h & 1) == 1) {
            A = -A;
        }
        K += 4;

        BigInteger result = hyperFact(h + 1).shiftLeft(h);
        factorialMap.put(n, result);
        return result;
    }
    private static BigInteger hyperFact(int l) {
        if (l > 1) {
            int m = l / 2;
            return hyperFact(m).multiply(hyperFact(l - m));
        }

        if (nostart) {
            S -= K -= 4;
            return BigInteger.valueOf(S);
        }

        nostart = true;
        return BigInteger.valueOf(A);
    }
    
    // reference: https://en.wikipedia.org/wiki/Binomial_coefficient#Multiplicative_formula
	private static Map<List<Integer>, BigInteger> nCrMap = new HashMap<List<Integer>, BigInteger>();
	private static BigInteger choose(int n, int r) {
		if(r > n || r < 0)
			throw new IllegalArgumentException("Need n,r: n >= r && r >= 0");
		
		// check if key is present
		List<Integer> tuple = Arrays.asList(n, r);
		if( nCrMap.containsKey(tuple) )
			return nCrMap.get(tuple);
		
		if(r == 0 || r == n)
			return BigInteger.ONE;
		
		if(n == r-1)
			return BigInteger.valueOf(n);
		
		// tuple is not present
		// generate a key
		List<Integer> key = Collections.unmodifiableList(Arrays.asList(n, r));
		
		// r must be in [0,n]
		BigInteger ntok = BigInteger.ONE;			
		BigInteger ktok = BigInteger.ONE;
		int upperBound = Math.min(r, n-r);
		for(int t = 1 ; t <= upperBound ; t++) {
			ntok = ntok.multiply(BigInteger.valueOf(n));
			ktok = ktok.multiply(BigInteger.valueOf(t));
			n--;
		}
		nCrMap.put(key, ntok.divide(ktok));
		
		// tuple is key
		return nCrMap.get(tuple);
	}
    
	// Math.pow does an approximation (uses doubles and rounding
	// causes approximation for larger values that results in 
	// loss of accuracy)
    private static BigInteger power(int base, int power) {
    	BigInteger result = BigInteger.ONE;
    	BigInteger pow = BigInteger.valueOf(base);
    	for(int p = 1 ; p <= power ; p++) 
    		result = result.multiply(pow);
    	
    	return result;
    }
    
    static {
    	// populate partitionMap
    	for(int i = 2 ; i <= 50 ; i++) 
    		partition(i);
    	
    	// populate numerators
    	for(int i = 2 ; i <= 50 ; i++) 
    		getNumerator(i);
    }
    
    public static void main(String[] args) { 
        for(int N = 2 ; N <= 50 ; N++)
        	System.out.println(N + " -> " + answer(N));
    }
}
