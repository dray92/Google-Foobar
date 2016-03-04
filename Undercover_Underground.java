package primary;

/**
 * Undercover underground
======================

As you help the rabbits establish more and more resistance groups to fight against Professor Boolean, you need a way to pass messages back and forth.

Luckily there are abandoned tunnels between the warrens of the rabbits, and you need to find the best way to use them. In some cases, Beta Rabbit wants a high level of interconnectedness, especially when the groups show their loyalty and worthiness. In other scenarios the groups should be less intertwined, in case any are compromised by enemy agents or zombits.

Every warren must be connected to every other warren somehow, and no two warrens should ever have more than one tunnel between them. Your assignment: count the number of ways to connect the resistance warrens.

For example, with 3 warrens (denoted A, B, C) and 2 tunnels, there are three distinct ways to connect them:

A-B-C
A-C-B
C-A-B

With 4 warrens and 6 tunnels, the only way to connect them is to connect each warren to every other warren.

Write a function answer(N, K) which returns the number of ways to connect N distinctly labelled warrens with exactly K tunnels, so that there is a path between any two warrens.

The return value must be a string representation of the total number of ways to do so, in base 10.
N will be at least 2 and at most 20.
K will be at least one less than N and at most (N * (N - 1)) / 2

Languages
=========

To provide a Python solution, edit solution.py
To provide a Java solution, edit solution.java

Test cases
==========

Inputs:
    (int) N = 2
    (int) K = 1
Output:
    (string) "1"

Inputs:
    (int) N = 4
    (int) K = 3
Output:
    (string) "16"
 * @author Debosmit
 *
 */
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Undercover_Underground {

	static Map<List<Integer>, BigInteger> nCrMap = new HashMap<List<Integer>, BigInteger>();
	// formula: nCr = n! / [r! * (n-r)!]
	private static BigInteger nChooseR(int n, int r) {
		// check if key is present
		List<Integer> tuple = Arrays.asList(n, r);
		if( nCrMap.containsKey(tuple) )
			return nCrMap.get(tuple);

		// covering some basic cases using
		// if statements to prevent unnecessary
		// calculations and memory wastage
		
		// given 5 objects, there are 0 ways to choose 6
		if(r > n)
			return BigInteger.valueOf(0);
		
		// given 5 objects, there are 5 ways of choosing 1
		// given 5 objects, there are 5 ways of choosing 4
		if( (r == 1) || ( (n-r) == 1 ) )
			return BigInteger.valueOf(n);
		
		// given 5 objects, there is 1 way of choosing 5 objects
		// given 5 objects, there is 1 way of choosing 0 objects
		if( (r == 0) || ( (n-r) == 0 ) )
			return BigInteger.valueOf(1);
		
		BigInteger diff = getFactorial(n-r);
		
		BigInteger numerator = getFactorial(n);
		
		BigInteger denominator = getFactorial(r);
		denominator = denominator.multiply(diff);

		// unmodifiable so key cannot change hash code
		nCrMap.put(Collections.unmodifiableList(Arrays.asList(n, r)), numerator.divide(denominator));
		
		return nCrMap.get(tuple);
	}
	
	private static Map<Integer, BigInteger> factorials = new HashMap<Integer, BigInteger>();
	private static BigInteger getFactorial(int n) {
		if(factorials.containsKey(n))
			return factorials.get(n);
		
		BigInteger fact = BigInteger.ONE;
		for(int i = 2 ; i <= n ; i++)
			fact = fact.multiply(BigInteger.valueOf(i));
		
		factorials.put(n, fact);
		
		return fact;
	}

	static Map<List<Integer>, String> resultMap = new HashMap<List<Integer>, String>();
	// N -> number of nodes
	// K -> number of edges
	// N will be at least 2 and at most 20.
	// K will be at least one less than n and at most (n * (n - 1)) / 2
	public static String answer(int N, int K) {
		/* for the case where K < N-1 */
		if(K < N-1)
			return BigInteger.ZERO.toString();
		
		/* for the case where K = N-1 */
		// Cayley's formula applies [https://en.wikipedia.org/wiki/Cayley's_formula].
		// number of trees on n labeled vertices is n^{n-2}.
		if(K == N-1)
			return BigInteger.valueOf((long)Math.pow(N, N-2)).toString();
		
		/* for the case where K > N-1 */
		// check if key is present in the map
		List<Integer> tuple = Arrays.asList(N, K);
		if( resultMap.containsKey(tuple) )
			return resultMap.get(tuple);
						
		// maximum number of edges in a simply 
		// connected undirected unweighted graph 
		// with n nodes = |N| * |N-1| / 2
		int maxEdges = N * (N-1) / 2;
		
		/* for the case where K = N(N-1)/2 */
		// if K is the maximum possible 
		// number of edges for the number of 
		// nodes, then there is only one way is 
		// to make a graph (connect each node
		// to all other nodes)
		if(K == maxEdges)
			return BigInteger.ONE.toString();
		
		/* for the case where K > N(N-1)/2 */
		if(K > maxEdges)
			return BigInteger.ZERO.toString();
		
		BigInteger count = BigInteger.ZERO;
		
		for(int k = 1; k <= N-1 ; k++) {
			BigInteger combinations = nChooseR(N-1, k);
			combinations = combinations.multiply(new BigInteger(answer(N-1, K-k)));
			count = count.add(combinations);
		}
		
		// unmodifiable so key cannot change hash code
		resultMap.put(Collections.unmodifiableList(Arrays.asList(N, K)), count.toString());
		
		return count.toString();
	}
	
	static Map<List<Integer>, String> resultMap2 = new HashMap<List<Integer>, String>();
	// reference: http://math.stackexchange.com/questions/689526/how-many-connected-graphs-over-v-vertices-and-e-edges
	public static String answer2(int N, int K) {
		/* for the case where K < N-1 */
		if(K < N-1)
			return BigInteger.ZERO.toString();
		
		/* for the case where K = N-1 */
		// Cayley's formula applies [https://en.wikipedia.org/wiki/Cayley's_formula].
		// number of trees on n labeled vertices is n^{n-2}.
		if(K == N-1)
			return BigInteger.valueOf((long)Math.pow(N, N-2)).toString();
		
		/* for the case where K > N-1 */
		// check if key is present in the map
		List<Integer> tuple = Arrays.asList(N, K);
		if( resultMap2.containsKey(tuple) )
			return resultMap2.get(tuple);
						
		// maximum number of edges in a simply 
		// connected undirected unweighted graph 
		// with n nodes = |N| * |N-1| / 2
		int maxEdges = N * (N-1) / 2;
		
		/* for the case where K = N(N-1)/2 */
		// if K is the maximum possible 
		// number of edges for the number of 
		// nodes, then there is only one way is 
		// to make a graph (connect each node
		// to all other nodes)
		if(K == maxEdges)
			return BigInteger.ONE.toString();
		
		/* for the case where K > N(N-1)/2 */
		if(K > maxEdges)
			return BigInteger.ZERO.toString();
		
		// get the universal set
		BigInteger allPossible = nChooseR(maxEdges, K);
		
		BigInteger repeats = BigInteger.ZERO;
		// now, to remove duplicates, or incomplete graphs
		// when can these cases occur?
		for(int n = 0 ; n <= N-2 ; n++) {
			
			BigInteger choose_n_from_rem_nodes = nChooseR(N-1, n);
			
			// this is essentially choosing N-3-n nodes from N-1-n
			// which is (N-1-n) choose (N-3-n)
			// = {N-1-n}! / { {N-3-n}! {(N-1-n)-(N-3-n)}!
			// = {N-1-n}{N-2-n}{N-3-n}! / { {N-3-n}! * 2}!
			// = {N-1-n}{N-2-n} / 2
			int chooseN = (N - 1 - n) * (N - 2 - n) / 2;
			
			BigInteger repeatedEdges = BigInteger.ZERO;
			for(int k = 0 ; k <= K ; k++) {
				BigInteger combinations = nChooseR(chooseN, k);
				
				BigInteger recurse = new BigInteger(answer2(n+1, K-k));
				
				repeatedEdges = repeatedEdges.add(combinations.multiply(recurse));
			}
			
			repeats = repeats.add(choose_n_from_rem_nodes.multiply(repeatedEdges));
		}
		
		// remove repeats
		allPossible = allPossible.subtract(repeats);
		
		// add to cache
		resultMap2.put(Collections.unmodifiableList(Arrays.asList(N, K)), allPossible.toString());
		return resultMap2.get(tuple);
	}
	
	public static void main(String[] args) {
		int fail = 0;
		int total = 0;
		for(int n = 2 ; n <= 20 ; n++) {
			for(int k = n-1 ; k <= n*(n-1)/2 ; k++) {
				total++;
				String ans = answer(n,k);
				String ans2 = answer2(n,k);
				if(ans.compareTo(ans2) != 0) {
					fail++;
					System.out.println("N = " + n + " , K = " + k + " , num = " + ans + " ||| " + ans2);
				}
			}
		}
		System.out.println("Approach 1 fails " + ((100*fail)/total) + "% of the test");
	}
}