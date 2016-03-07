package primary;

import java.lang.reflect.InvocationTargetException;

/**
 * Sample output:
 *    Undercover_Underground took: 112236 msecs for 1000000 iterations.
 *    Undercover_Underground2 took: 124902 msecs for 1000000 iterations.
 *    
 * Both are pretty much the same :)
 * @author Debosmit
 *
 */

public class Benchmark_Underground_Undercover {

	public static void main(String[] args) {
		final int N = 1000000;
		
		
		// Undercover_Undergrounds
		Undercover_Underground_Interface unit = new Undercover_Underground();
		
		try {
			long time = tester(unit, N);
			System.out.println("Undercover_Underground took: " + time + " msecs for " + N + " iterations.");
		} 
		// provision for reflections
		catch (ClassNotFoundException | NoSuchMethodException
				| SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//  Undercover_Underground2
		unit = new Undercover_Underground2();
		
		try {
			long time = tester(unit, N);
			System.out.println("Undercover_Underground2 took: " + time + " msecs for " + N + " iterations.");
		} 
		// provision for reflections
		catch (ClassNotFoundException | NoSuchMethodException
				| SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static long tester(Undercover_Underground_Interface o, int numIters) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if( !( (o instanceof Undercover_Underground) || (o instanceof Undercover_Underground2) )  )
			return Long.MIN_VALUE;
		
		long start = System.currentTimeMillis();
		
		for(int i = 0 ; i < numIters ; i++) 
			for(int n = 2 ; n <= 20 ; n++) 
				for(int k = n-1 ; k <= n*(n-1)/2 ; k++) 
					o.answer(n,k);
				
		return (System.currentTimeMillis() - start);
		
	}
}
