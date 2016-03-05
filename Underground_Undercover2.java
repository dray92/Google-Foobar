package primary;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Python code by Lee Gao
 * Some modification by Debosmit Ray
def choose(n, k):
    """
    A fast way to calculate binomial coefficients by Andrew Dalke (contrib).
    """
    if 0 <= k <= n:
        ntok = 1
        ktok = 1
        for t in xrange(1, min(k, n - k) + 1):
            ntok *= n
            ktok *= t
            n -= 1
        return ntok // ktok
    else:
        return 0

def g(n, k):
    return sum(
        choose(n, s)*s*(n - s) * sum(
            f(s, t) * f(n - s, k - t) for t in range(0, k + 1)) for s in range(1, n))/2

def h(n,k):
    sum = 0
    
    for s in range(1,n):
        subSum = 0
        for t in range(0,k+1):
            subSum += f(s, t) * f(n - s, k - t)
        
        subSum = choose(n, s)*s*(n - s) * subSum
        sum+= subSum
    
    return sum/2

F = {}
def f(n, k):
    if (n, k) in F:
        return F[n, k]
    
    N = n * (n - 1)/2
    
    if k is n - 1:
        return int(n ** (n-2))
    if k < n or k > N:
        return 0
    if k == N:
        return 1
    
    result = ((N - k + 1) * f(n, k - 1) + h(n, k - 1)) / k
    F[n, k] = result
    return result
    
def test():
    for N in range(2, 21):
        for K in range(N-1, N*(N-1)/2 + 1):
            print "N = " + str(N) + " , K = " + str(K) + " , num = " + str(f(N, K))
            
            
test()
 * @author Debosmit
 *
 */

public class Underground_Undercover2 {

	private static Map<List<Integer>, BigInteger> nCrMap = new HashMap<List<Integer>, BigInteger>();
	// reference: https://en.wikipedia.org/wiki/Binomial_coefficient#Multiplicative_formula
	// reference: Python code written by Lee Gao
	private static BigInteger choose(int n, int r) {
		if(r > n || r < 0)
			throw new IllegalArgumentException("Need n,r: n >= r && r >= 0");
		
		// check if key is present
		List<Integer> tuple = Arrays.asList(n, r);
		if( nCrMap.containsKey(tuple) )
			return nCrMap.get(tuple);
		
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

	static Map<List<Integer>, String> resultMap = new HashMap<List<Integer>, String>();
	public static String answer(int N, int K) {
		/* for the case where K > N-1 */
		// check if key is present in the map
		List<Integer> tuple = Arrays.asList(N, K);
		if( resultMap.containsKey(tuple) )
			return resultMap.get(tuple);
		
		// maximum number of edges in a simply 
		// connected undirected unweighted graph 
		// with n nodes = |N| * |N-1| / 2
		int maxEdges = N * (N-1) / 2;	
		
		/* for the case where K < N-1 or K > N(N-1)/2 */
		if(K < N-1 || K > maxEdges)
			return BigInteger.ZERO.toString();
		
		/* for the case where K = N-1 */
		// Cayley's formula applies [https://en.wikipedia.org/wiki/Cayley's_formula].
		// number of trees on n labeled vertices is n^{n-2}.
		if(K == N-1)
			return BigInteger.valueOf((long)Math.pow(N, N-2)).toString();
		
		/* for the case where K = N(N-1)/2 */
		// if K is the maximum possible 
		// number of edges for the number of 
		// nodes, then there is only one way is 
		// to make a graph (connect each node
		// to all other nodes)
		if(K == maxEdges)
			return BigInteger.ONE.toString();
		
		// number of edges left from maxEdges if I take away K-1 edges
		BigInteger numWays = BigInteger.valueOf(maxEdges - K + 1);
		
		// number of graphs possible for each of the numWays edges for a graph that has 1 less edge
		BigInteger numGraphsWithOneLessEdge = new BigInteger( answer(N,  K-1) );
		
		// number of all possible subgraphs with K-1 edges
		BigInteger subGraphs = answerHelper(N, K-1);
		
		// numWays*numGraphsWithOneLessEdge + subGraphs
		BigInteger result = subGraphs.add(numWays.multiply(numGraphsWithOneLessEdge));
		
		// this contains repeats for each of the K edges
		result = result.divide(BigInteger.valueOf(K));
		
		// add to cache
		resultMap.put(Collections.unmodifiableList(Arrays.asList(N, K)), result.toString());
		return resultMap.get(tuple);
	}
	
	private static BigInteger answerHelper(int N, int K) {
		
		BigInteger totalGraphs = BigInteger.ZERO;
		
		for(int n = 1 ; n < N ; n++) {
			BigInteger graphs = BigInteger.ZERO;
			for(int k = 0 ; k <= K ; k++) {
				// number of graphs with n nodes and k edges
				BigInteger num = new BigInteger( answer(n, k) );
				
				// number of graphs with N-n nodes and K-k edges
				BigInteger num2 = new BigInteger( answer(N-n, K-k) );
				
				graphs = graphs.add( num.multiply(num2) );
			}
			
			// number of ways to choose n nodes from N nodes
			BigInteger choose = choose(N, n);
			
			// this is repeated for each of the n chosen nodes
			// and the N-n unchosen nodes
			choose = choose.multiply(BigInteger.valueOf(n)).multiply(BigInteger.valueOf(N-n));
			
			totalGraphs = totalGraphs.add( choose.multiply(graphs) );
			
		}
		
		// now, consider the case where N = 20
		// when n = 2, we compute for N-n = 18
		// when n = 18, we do the same thing again
		// hence, contents of totalGraphs is 2 times
		// of what it should be
		return totalGraphs.divide(BigInteger.valueOf(2));
	}
	
	public static void main(String[] args) {	
		for(int n = 2 ; n <= 20 ; n++) {
			for(int k = n-1 ; k <= n*(n-1)/2 ; k++) {
				String ans = answer(n,k);
				System.out.println("N = " + n + " , K = " + k + " , num = " + ans);
			}
		}
	}
}
