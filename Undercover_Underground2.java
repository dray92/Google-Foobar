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

public class Undercover_Underground2 extends Undercover_Underground_Interface {

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

	/**
	 * Ideation: 
	 *  We know that f(n,n-1) = n^{n-2} is the counting function of the number of 
	 *  labeled rooted trees [Cayley’s formula]
	 *  
	 *	Now, let f(n, k) be the total number of connected graphs with n nodes and k edges,
	 *  we have a characterization of how to add a new edge:
	 *		1) Take any graph in F[n,k], and you can add an edge between any 
	 *			of the {n \choose 2} - k pairs of unmatched nodes.
	 *		2) If you have two connected graphs g_1 and g_2, say in 
	 *			F[s, t] and F[n-s, k-t] respectively (that is, a 
	 *			connected graph with s nodes and t edges and a connected 
	 *			graph with n-s nodes and k-t edges), then you can surgically 
	 *			construct a new graph by connecting these two subgraphs together.
	 *
	 *	You have s * (n-s) pairs of vertices to choose from, and you can choose 
	 *	the s point in {n \choose s} ways. You can then sum over the choice of 
	 *	s and t respectively from 1 to n-1, and in doing so, you will have 
	 *	double-counted each graph twice. Let's call this construction g(n, k).
	 *	
	 *	Then g(n,k) = (\sum_s,t {n \choose s} s (n-s) * f(s,t) * f(n-s, k-t))/2
	 *	
	 *	Now, there are no other ways to add in an extra edge (without reducing to 
	 *	the two constructions above), so the additive term 
	 *	h(n,k+1) = (N - k)f(n,k) + g(n,k) 
	 *	gives a characterization of the multiset of graphs that we've 
	 *	constructed. Why is this a multiset?
	 *	
	 *	Well, let's look at a case analysis on the two subcases 
	 *	(induction on the construction). Take a random graph g in h(n, k+1) graphs 
	 *	constructed this way. The induction hypothesis is that there are 
	 *	k + 1 copies of g within the multiset h(n, k+1)
	 *	
	 *	Let's just look at the inductive case
	 *	If you break an edge within a connected graph, then it either remains a connected 
	 *	graph or it breaks into two connected graphs.
	 *	Now, fixate on an edge e, if you break any other edges, then e will still be 
	 *	within (k+1) - 1 distinct constructions. If you break e, you will have yet 
	 *	another unique construction.
	 *	This means that there are k + 1 possible distinct classes of graphs 
	 *	(either single component of two components) from which we can construct the same 
	 *	final graph g.
	 *	
	 *	Therefore, h(n,k+1) counts each graph a total of k+1 times, and so
	 *	f(n, k+1) = h(n, k+1)/(k+1) = ((N-k)f(n,k) + g(n,k))/(k+1)
	 *	
	 *	Given a fixed n and k, this recurrence will compute the correct result in O((nk)^2) time, 
	 *	so complexity wise, it's equivalent to the previous algorithm.
	 *	The nice thing about this construction is that it easily yields an analytic 
	 *	generating function so you can do analysis on it.	
	 *	In this case, suppose you have a complex-valued function f_k(x,y), 
	 *	then 2 dy f_{k+1} = (x^2 dx^2 f_k - 2 y dy f_k) + \sum_s z^2 dz f_s dz f_{k-s} 
	 *	You'll need a lot of complex analysis machinery to solve this recurrence PDE
	 */
	
	static Map<List<Integer>, String> resultMap = new HashMap<List<Integer>, String>();
	public String answer(int N, int K) {
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
		if(K == N-1) {
			if(N < 2)
				return BigInteger.valueOf((long)Math.pow(N, N-2)).toString();
			
			// multiply N to itself N-2 times
			BigInteger val = BigInteger.ONE;
			int count = 0;
			while(count++ != N-2)
				val = val.multiply( BigInteger.valueOf( (long)N ) );
			
			return val.toString();
		}
		
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
		
		// k copies of g within the multiset
		result = result.divide(BigInteger.valueOf(K));
		
		// add to cache
		resultMap.put(Collections.unmodifiableList(Arrays.asList(N, K)), result.toString());
		return resultMap.get(tuple);
	}
	
	private BigInteger answerHelper(int N, int K) {
		
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
		Undercover_Underground2 unit = new Undercover_Underground2();
		
		for(int n = 2 ; n <= 20 ; n++) {
			for(int k = n-1 ; k <= n*(n-1)/2 ; k++) {
				String ans = unit.answer(n,k);
				System.out.println("N = " + n + " , K = " + k + " , num = " + ans);
			}
		}
	}
}
