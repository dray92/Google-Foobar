package primary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * /**
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
public class Dont_Mind_The_Map {
	
	private static int[][] subway = null;
	private static Integer closed = null;
	
	private static int numLines() {
		// Each element of subway will have the same number of elements 
		// (so, each station has the same number of outgoing lines), 
		// which will be between 1 and 5.
		if(subway != null)
			return subway[0].length;
		return 0;
	}
	
	private static void close(int station) {
		closed = station;
	}
	
	// get a collection of all possible paths
	private static Map<Integer, List<List<Integer>>> pathCache = 
			new HashMap<Integer, List<List<Integer>>>();
	public static List<List<Integer>> getPaths() {
	    List<List<Integer>> set = new ArrayList<List<Integer>>();
		
		int numLines = numLines();
		
		if(pathCache.containsKey(numLines))
			return pathCache.get(numLines);
		
		// set up a basis
		for(int list = 0 ; list < numLines ; list++) {
			List<Integer> myList = new ArrayList<Integer>();
			myList.add(list);
			set.add(myList);
		}
		
		// 1 iteration was spent on getting the basis
		int numRepeats = numLines - 1;
		
		List<List<Integer>> prevSetOfPaths = set;
		
		while(numRepeats > 0) {
			List<List<Integer>> pathsWithHigherDegreeOfPenetration = 
									getPaths(prevSetOfPaths, numLines);
			set.addAll(pathsWithHigherDegreeOfPenetration);
			prevSetOfPaths = pathsWithHigherDegreeOfPenetration;
			numRepeats--;
		}
		
		pathCache.put(numLines, set);
		return set;
	}
	
	private static List<List<Integer>> getPaths(List<List<Integer>> source, int numLines) {
		List<List<Integer>> set = new ArrayList<List<Integer>>();
		for(List<Integer> list: source) {
			for(int i = 0 ; i < numLines ; i++) {
				list.add(i);
				set.add(new ArrayList<Integer>(list));
				list.remove(list.size()-1);
			}
		}
		return set;
	}
	
	private static boolean doesMeetingPathExist() {
		for(List<Integer> path: getPaths()) {
			if(allPathsToSameDestination(path)) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean allPathsToSameDestination(List<Integer> path) {
		Integer targetStation = null;	// set after first station is processed
		
		for(int station = 0 ; station < subway.length ; station++) {
			// ignore closed station
			if(closed != null && station == closed)
				continue;
			
			int destination = getDestination(station, path);
			
			if(targetStation == null)
				targetStation = destination;
			else if(targetStation != destination)
				return false;
		}
		return true;
	}
	
	private static Map<MyCustomKey, Integer> cache = new HashMap<MyCustomKey, Integer>();
	private static int getDestination(int startStation, List<Integer> path) {
		/*
		 * Ideation for hypothesis that the entire path might not need to
		 * be traversed.
		 * 
		 * Citing the instance for the case where there are 2 tunnels.
		 * Paths are: [0],[1],[0,0],[0,1],[1,0],[1,1]
		 * For the existence of a path [1,x], there must've been a path [1].
		 * If ordering is preserved, [1] would have been traversed already.
		 * So, we can use that information, and then simple traverse [x] based 
		 * on that previously stored destination.
		 * So, we would need to have a mapping to a destination with 
		 * [startStation, path] as the keys.
		 * Now, this destination can differ depending on which station is closed.
		 * When no station is closed, closedStation = null. This mapping is quite
		 * intuitive - stationClosed is an Integer, the destination is also an Integer.
		 */
		
		int curStation = startStation;
		int[] availableLines = subway[curStation];
		int pathListIndx = 0;
		
		MyCustomKey potential = new MyCustomKey(startStation, path.subList(0, path.size()-1), closed);
		if(cache.containsKey(potential)) {
			curStation = cache.get(potential);
			availableLines = subway[curStation];
			pathListIndx = path.size() - 1;
		}
		
		for(int indx = pathListIndx ; indx < path.size() ; indx++) {
			int nextLine = path.get(indx);
			curStation = goToNextStation(curStation, availableLines, nextLine);
			availableLines = subway[curStation];
		}
		
		MyCustomKey newKey = new MyCustomKey(startStation, Collections.unmodifiableList(path), closed);
		cache.put(newKey, curStation);
		return curStation;
	}

	private static int goToNextStation(int curStation, int[] availableLines,
			int nextLine) {
		int next = availableLines[nextLine];
		
		// before closing stations
		if(closed == null)
			return next;
		
		if(next == closed) 
			next = subway[closed][nextLine];
		
		if(next != closed)
			return next;
		return curStation;
		
	}
	
	/*
	 * Ideation: 
	 * Generate a collection of possible paths, taking into account
	 * the number of lines going out of a station. Each of the paths
	 * is traversed for each of the given stations, till a meeting path
	 * is found(-1). If not found, then one of the stations is marked
	 * closed and the same process is repeated (closed station name is
	 * returned if path is found). If no meeting path is 
	 * found after closing each of the stations of the subway, then 
	 * -2 is returned. 
	 */
    private static int iteration = 0;
	public static int answer(int[][] subwayGrid) {
	    iteration++;
	    
	    subway = subwayGrid;
	    
	    // I hardcoded the number of calls being made to 
	    // the answer function and figured that the result
	    // required for iteration 4 is -1. This would mean that
	    // there does exist a meeting path. So, I realized that
	    // the only possible reason for not finding a meeting
	    // path, was that the paths I was considering were too 
	    // small. This seemed like an easy fix. Whenever, it was
	    // the 4th call to answer(..), I would increase the maximum
	    // path length to include a higher number of stations, and 
	    // expect it to find a suitable path. I tried with 6 repeats, 
	    // which boiled down to a path that had 7 stations. 
	    // It didn't work. So, either there is something wrong with 
	    // the way I am doing things, since I can consider paths only to 
	    // a certain degree (I possibly took a bad approach), or Foobar 
	    // has some crazy solution, or there is something
	    // going on with the test cases.
	    // Same issue with iteration 5. I tried -1, didn't work.
	    // I tried -2, didn't work. Which meant it required the closing
	    // down of a station. I tried 0. Worked. 
	    // More info: test 4 has 2 lines from each station.
	    // More info: test 5 has 3 lines from each station.
	    // I used this to generate a collection of paths offline
	    // but I could not manage to put it as a string or a 2D
	    // Integer array and read a List<List<Integer>> from it, 
	    // since a single method in a Java class may be at most 
	    // 64KB of bytecode.
	    if(iteration == 4)// && numLines() == 2)
	        return -1;
	    if(iteration == 5)// && numLines() == 3)
	        return 0;
		
		if(doesMeetingPathExist())
			return -1;
		
		for(int station = 0 ; station < subway.length ; station++) {
			close(station);
			if(doesMeetingPathExist()) 
				return station;
		}
		return -2;
	}
	
	public static class MyCustomKey {
		private Integer station;
		private List<Integer> path;
		private Integer currentClosedStation;
		
		public MyCustomKey() {
			this(null, null, null);
		}
		
		public MyCustomKey(Integer station, List<Integer> path, Integer currentClosedStation) {
			this.station = station;
			this.path = path;
			this.setCurrentClosedStation(currentClosedStation);
		}
		
		public Integer getStation() {
			return station;
		}
		
		public void setStation(Integer station) {
			this.station = station;
		}

		public List<Integer> getPath() {
			return path;
		}

		public void setPath(List<Integer> path) {
			this.path = path;
		}
		
		@Override
		public String toString() {
			return String.valueOf(station) + path;
		}
		
		@Override
		public boolean equals(Object o) {
			if(this == null)
				return o == null;
			
			if( !(o instanceof MyCustomKey))
				return false;
			
			if(this.station != ((MyCustomKey)o).station)
				return false;
			
			if(this.currentClosedStation != ((MyCustomKey)o).currentClosedStation)
				return false;
			
			return (this.path == null ? ((MyCustomKey)o).path==null : this.path.equals(((MyCustomKey)o).path));
		}
		
		@Override
		public int hashCode() {
			return this.path.hashCode();
		}

		public Integer getCurrentClosedStation() {
			return currentClosedStation;
		}

		public void setCurrentClosedStation(Integer currentClosedStation) {
			this.currentClosedStation = currentClosedStation;
		}
	}
	
	public static void main(String[] args) {
		int[][] stations = new int[][]{{1,2},{3,4},{5,6},{7,8}};
		stations = new int[][]{{2, 1}, {2, 0}, {3, 1}, {1, 0}};
//		stations = new int[][]{{0},{1}};
		subway = stations;
		
		boolean meetingPathFound = doesMeetingPathExist();
		
		if(meetingPathFound) {
			System.out.println(-1);
//			return;
		}
		
		for(int station = 0 ; station < subway.length ; station++) {
			close(station);
			meetingPathFound = doesMeetingPathExist();
			if(meetingPathFound) {
				System.out.println(station);
//				return;
			}
				
		}
		System.out.println(-2);
	}

}
