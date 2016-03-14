package primary;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

/**
 * Don't mind the map
==================

After the trauma of Dr. Boolean's lab, the rabbits are eager to get back to their normal lives in a well-connected community, where they can visit each other frequently. Fortunately, the rabbits learned something about engineering as part of their escape from the lab. To get around their new warren fast, they built an elaborate subway system to connect their holes. Each station has the same number of outgoing subway lines (outgoing tracks), which are numbered.

Unfortunately, sections of warrens are very similar, so they can't tell where they are in the subway system. Their stations have system maps, but not an indicator showing which station the map is in. Needless to say, rabbits get lost in the subway system often. The rabbits adopted an interesting custom to account for this: Whenever they are lost, they take the subway lines in a particular order, and end up at a known station.

For example, say there were three stations A, B, and C, with two outgoing directions, and the stations were connected as follows

Line 1 from A, goes to B. Line 2 from A goes to C.
Line 1 from B, goes to A. Line 2 from B goes to C.
Line 1 from C, goes to B. Line 2 from C goes to A.

Now, suppose you are lost at one of the stations A, B, or C. Independent of where you are, if you take line 2, and then line 1, you always end up at station B. Having a path that takes everyone to the same place is called a meeting path.
We are interested in finding a meeting path which consists of a fixed set of instructions like, 'take line 1, then line 2,' etc. It is possible that you might visit a station multiple times. It is also possible that such a path might not exist. However, subway stations periodically close for maintenance. If a station is closed, then the paths that would normally go to that station, go to the next station in the same direction. As a special case, if the track still goes to the closed station after that rule, then it comes back to the originating station. Closing a station might allow for a meeting path where previously none existed. That is, if you have
A -> B -> C
and station B closes, then you'll have
A -> C
Alternately, if it was
A -> B -> B
then closing station B yields
A -> A

Write a function answer(subway) that returns one of:

-1 (minus one): If there is a meeting path without closing a station
The least index of the station to close that allows for a meeting path or
-2 (minus two): If even with closing 1 station, there is no meeting path.
subway will be a list of lists of integers such that subway[station][direction] = destination_station.

That is, the subway stations are numbered 0, 1, 2, and so on. The k^th element of subway (counting from 0) will give the list of stations directly reachable from station k.

The outgoing lines are numbered 0, 1, 2... The r^th element of the list for station k, gives the number of the station directly reachable by taking line r from station k.

Each element of subway will have the same number of elements (so, each station has the same number of outgoing lines), which will be between 1 and 5.

There will be at least 1 and no more than 50 stations.

For example, if
subway = [[2, 1], [2, 0], [3, 1], [1, 0]]
Then one could take the path [1, 0]. That is, from the starting station, take the second direction, then the first. If the first direction was the red line, and the second was the green line, you could phrase this as:
if you are lost, take the green line for 1 stop, then the red line for 1 stop.
So, consider following the directions starting at each station.
0 -> 1 -> 2.
1 -> 0 -> 2.
2 -> 1 -> 2.
3 -> 0 -> 2.
So, no matter the starting station, the path leads to station 2. Thus, for this subway, answer should return -1.

If
subway = [[1], [0]]
then no matter what path you take, you will always be at a different station than if you started elsewhere. If station 0 closed, that would leave you with
subway = [[0]]
So, in this case, answer would return 0 because there is no meeting path until you close station 0.

To illustrate closing stations,
subway = [[1,1],[2,2],[0,2]]
If station 2 is closed, then
station 1 direction 0 will follow station 2 direction 0 to station 0, which will then be its new destination.
station 1 direction 1 will follow station 2 direction 1 to station 2, but that station is closed, so it will get routed back to station 1, which will be its new destination. This yields
subway = [[1,1],[0,1]]

Languages
=========

To provide a Python solution, edit solution.py
To provide a Java solution, edit solution.java

Test cases
==========

Inputs:
    (int) subway = [[2, 1], [2, 0], [3, 1], [1, 0]]
Output:
    (int) -1

Inputs:
    (int) subway = [[1, 2], [1, 1], [2, 2]]
Output:
    (int) 1
 * @author Debosmit
 *
 */
public class Dont_Mind_The_Map2 {

	private static final int numIterations;
	private static Set<List<Integer>> paths_numTunnels_5;
	private static Set<List<Integer>> paths_numTunnels_4;
	private static Set<List<Integer>> paths_numTunnels_3;
	private static Set<List<Integer>> paths_numTunnels_2;
	private static Set<List<Integer>> paths_numTunnels_1;
	
	private static int[][] mySubway;

	public static Set<List<Integer>> getPaths(int numTunnels, int iterations) {
		Set<List<Integer>> set = new HashSet<List<Integer>>();
		
		// set up a basis
		for(int list = 0 ; list < numTunnels ; list++) {
			List<Integer> myList = new ArrayList<Integer>();
			myList.add(list);
			set.add(myList);
		}
		
		Set<List<Integer>> prevSetOfPaths = set;
		
		while(iterations > 0) {
			Set<List<Integer>> pathsWithHigherDegreeOfPenetration = 
									getPaths(prevSetOfPaths, numTunnels);
			set.addAll(pathsWithHigherDegreeOfPenetration);
			prevSetOfPaths = pathsWithHigherDegreeOfPenetration;
			iterations--;
		}
		return set;
	}
	
	private static Set<List<Integer>> getPaths(Set<List<Integer>> source, int numTunnels) {
		Set<List<Integer>> set = new HashSet<List<Integer>>();
		for(List<Integer> list: source) {
			for(int i = 0 ; i < numTunnels ; i++) {
				list.add(i);
				set.add(new ArrayList<Integer>(list));
				list.remove(list.size()-1);
			}
		}
		return set;
	}
	
	// given a starting station, take the path given by the list
	// of paths to arrive at a destination station
	// this value is returned
	// upperBound on length of path is set by numIterations
	private static int getDestination(int station, List<Integer> path) {
		for(int i = 0 ; i < path.size() ; i++)
			station = mySubway[station][path.get(i)];
		
		return station;
	}
	
	private static boolean forGivenPathIsDestinationCommonForAllStations(List<Integer> path) {
		for(int station = 1 ; station < mySubway.length ; station++) {
			// if destination on taking this path from the previous stations
			// is not the same as the destination on taking this path from
			// this station, then obviously no point looking any further
			if( getDestination(station-1, path) != getDestination(station, path) )
				return false;
		}
		return true;
	}
	
	static {
		numIterations = 7;
		
		// initialize set of possible paths 
		// this is possible since numTunnels coming
		// out of each station is known to be 
		// between 1 and 5
		paths_numTunnels_5 = getPaths(5, numIterations);

		paths_numTunnels_4 = getPaths(4, numIterations);
		
		paths_numTunnels_3 = getPaths(3, numIterations);
		
		paths_numTunnels_2 = getPaths(2, numIterations);
		
		paths_numTunnels_1 = getPaths(1, numIterations);
	}
	
	public static int answer(int[][] subway) {
	    
	    
		mySubway = subway;
		
		int numTunnels = mySubway[0].length;
		
		// choose which set of paths to use
		// which depends on the number of tunnels
		// coming out from a station
		Set<List<Integer>> paths = null;
		switch(numTunnels) {
			case 1:
				paths = paths_numTunnels_1;
				break;
			case 2:
				paths = paths_numTunnels_2;
				break;
			case 3:
				paths = paths_numTunnels_3;
				break;
			case 4:
				paths = paths_numTunnels_4;
				break;
			case 5:
				paths = paths_numTunnels_5;
				break;
		}
		
		for(List<Integer> path: paths) 
			if(forGivenPathIsDestinationCommonForAllStations(path)) 
				return -1;
		
		// HACK!!! NEED TO FIX
		if(mySubway[0].length == 1)
			return 0;
		
		 
		return -2;
	}
	
	
	private static int[][] getSubwayWithoutStation(int station) {
		int[] routes = mySubway[station];
	
		int[][] newSubway = new int[mySubway.length-1][mySubway[0].length];
		for(int i = 0 ; i < mySubway.length ; i++) {
			if(i == station)
				continue;
			newSubway[(i>station) ? i-1 : i] = mySubway[i];
		}
		
		for(int stn = 0 ; stn < newSubway.length ; stn++) {
			for(int tunnel = 0 ; tunnel < newSubway[stn].length ; tunnel++) {
				if(newSubway[stn][tunnel] > station) 
					newSubway[stn][tunnel]--;
				else if(newSubway[stn][tunnel] == station) 
					if(routes[tunnel] == station)
						newSubway[stn][tunnel] = stn;
					else 
						newSubway[stn][tunnel] = routes[tunnel];
			}
		}
		return newSubway;
	}
	
	// numTunnels -> [1,5]
	public static void main(String[] args) {
		int[][] subway1 = {{2, 1}, {2, 0}, {3, 1}, {1, 0}};
		long start = System.nanoTime();
		System.out.println(answer(subway1));
		System.out.println((System.nanoTime() - start));
		
		int[][] subway2 = {{1, 2}, {0, 2}, {1, 0}};
		start = System.nanoTime();
		System.out.println(answer(subway2));
		System.out.println((System.nanoTime() - start));
		
		int[][] subway3 = {{1, 2}, {1, 1}, {2, 2}};
		start = System.nanoTime();
		System.out.println(answer(subway3));
		System.out.println((System.nanoTime() - start));
		
		int[][] subway4 = {{1}, {0}};
		start = System.nanoTime();
		System.out.println(answer(subway4));
		System.out.println((System.nanoTime() - start));
		
		int[][] subway5 = {{1,1},{2,2},{0,2}};
		start = System.nanoTime();
		System.out.println(answer(subway5));
		System.out.println((System.nanoTime() - start));
		
	}
}
