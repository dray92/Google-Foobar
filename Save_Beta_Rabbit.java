package primary;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;


/**
 * Save Beta Rabbit
================
 
Oh no! The mad Professor Boolean has trapped Beta Rabbit in an NxN grid of rooms. In the center of each room (except for the top left room) is a hungry zombie. In order to be freed, and to avoid being eaten, Beta Rabbit must move through this grid and feed the zombies.
 
Beta Rabbit starts at the top left room of the grid. For each room in the grid, there is a door to the room above, below, left, and right. There is no door in cases where there is no room in that direction. However, the doors are locked in such a way that Beta Rabbit can only ever move to the room below or to the right. Once Beta Rabbit enters a room, the zombie immediately starts crawling towards him, and he must feed the zombie until it is full to ward it off. Thankfully, Beta Rabbit took a class about zombies and knows how many units of food each zombie needs be full.
 
To be freed, Beta Rabbit needs to make his way to the bottom right room (which also has a hungry zombie) and have used most of the limited food he has. He decides to take the path through the grid such that he ends up with as little food as possible at the end.
 
Write a function answer(food, grid) that returns the number of units of food Beta Rabbit will have at the end, given that he takes a route using up as much food as possible without him being eaten, and ends at the bottom right room. If there does not exist a route in which Beta Rabbit will not be eaten, then return -1.
 
food is the amount of food Beta Rabbit starts with, and will be a positive integer no larger than 200.
 
grid will be a list of N elements. Each element of grid will itself be a list of N integers each, denoting a single row of N rooms. The first element of grid will be the list denoting the top row, the second element will be the list denoting second row from the top, and so on until the last element, which is the list denoting the bottom row. In the list denoting a single row, the first element will be the amount of food the zombie in the left-most room in that row needs, the second element will be the amount the zombie in the room to its immediate right needs and so on. The top left room will always contain the integer 0, to indicate that there is no zombie there.
 
The number of rows N will not exceed 20, and the amount of food each zombie requires will be a positive integer not exceeding 10.
 
Languages
=========
 
To provide a Python solution, edit solution.py
To provide a Java solution, edit solution.java
 
Test cases
==========
 
Inputs:
    (int) food = 7
    (int) grid = [[0, 2, 5], [1, 1, 3], [2, 1, 1]]
Output:
    (int) 0
 
Inputs:
    (int) food = 12
    (int) grid = [[0, 2, 5], [1, 1, 3], [2, 1, 1]]
Output:
    (int) 1
 * @author Debosmit
 *
 */
public class Save_Beta_Rabbit {

	private static int[][] g;
	public static int answer(int food, int[][] grid) {
		int N = grid.length - 1;
		cache = new HashMap<List<Integer>, Integer>();
		g = grid;	// store local reference so as to not 
					// pass around grid a million times
		
		/*
		 * idea is to start at the end traverse both left and 
		 * up; if any traversal reaches origin, return whatever 
		 * food was left; if any traversal hit negative values 
		 * before reaching origin, returns null. null is treated
		 * as Integer.MAX_VALUE because:
		 * - we are concerned with minimizing the amount of food left
		 * - this value will never be reached since max 20 rows, 20 cols, and each 
		 * 		grid can have a maximum of 10 food. so, max = 20*20*10 = 4000
		 * 
		 * ideally, for a path starting from N-1,N-1 and ending at 0,0
		 * you would want food to be as close to 0 as possible.
		 */
		
		
		Integer foodLeft = rem(food, N, N);
		if(foodLeft == null || foodLeft == Integer.MAX_VALUE)
			return -1;
		return foodLeft < 0 ? -1 : foodLeft;
	}
	
	// map to cache (food, row, col) to integer
	// keeps track of result of path originating at (row,col)
	// with a specific food
	private static Map<List<Integer>, Integer> cache;
	private static Integer rem(int food, int row, int col) {
		// if origin is reached, return
		if(row == 0 && col == 0)
			return food;
		
		// check if stored in cache
		List<Integer> tuple = Arrays.asList(food, row, col);
		if(cache.containsKey(tuple))
			return cache.get(tuple);
		
		// unmodifiable so key cannot change hash code
		List<Integer> newKey = Collections.unmodifiableList(Arrays.asList(food, row, col));		
		
		// decrement food count
		food -= g[row][col];
		
		// if food count went negative, return null
		if(food < 0) 
			return null;
		
		// when there is space to go left and right
		if(col > 0 && row > 0) {
			Integer left = rem(food, row, col-1);
			Integer up = rem(food, row-1, col);
			Integer result = Math.min( left==null?Integer.MAX_VALUE:left , up==null?Integer.MAX_VALUE:up );
			cache.put(newKey, result);
			return result;
		} 
		// no space to go up, only left
		else if(col > 0) {
			Integer left = rem(food, row, col-1);
			cache.put(newKey, left);
			return left;
		} 
		
		// no space to go left, only up
		else {//if(row > 0) {
			Integer up = rem(food, row-1, col);
			cache.put(newKey, up);
			return up;
		}
	}
	
	public static void main(String[] args) {
//		int food = 7;
//		int[][] grid = new int[][]{{0, 2}, {1, 3}};
//		for(int row[]: grid)
//			System.out.println(Arrays.toString(row));
//		System.out.println(answer(food, grid));
		
//		int food = 7;
//		int[][] grid = new int[][]{{0, 2, 5}, {1, 1, 3}, {2, 1, 1}};
//		for(int row[]: grid)
//			System.out.println(Arrays.toString(row));
//		System.out.println(answer(food, grid));
		
		int food = 12;
		int[][] grid = new int[][]{{0, 2, 5}, {1, 1, 3}, {2, 1, 1}};
		for(int row[]: grid)
			System.out.println(Arrays.toString(row));
		System.out.println(answer(food, grid));
		
	}
}
