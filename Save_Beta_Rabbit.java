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
		g = grid;
		Integer foodLeft = rem(food, N, N);
		if(foodLeft == null || foodLeft == Integer.MAX_VALUE)
			return -1;
		return foodLeft < 0 ? -1 : foodLeft;
	}
	
	// Collections.unmodifiableList(Arrays.asList(n, r)
	private static Map<List<Integer>, Integer> cache;
	private static int getRemainder(int foodLeft, int x, int y) {
		
		if(x == 0 && y == 0)
			return foodLeft;
		
		List<Integer> tuple = Arrays.asList(foodLeft, x, y);
		if(cache.containsKey(tuple))
			return cache.get(tuple);
		
		// unmodifiable so key cannot change hash code
		List<Integer> newKey = Collections.unmodifiableList(Arrays.asList(foodLeft, x, y));
		
		foodLeft -= g[x][y];
		
		if(foodLeft < 0 || x < 0 || y < 0) {
			cache.put(newKey, foodLeft + 1);
			return cache.get(tuple);
		}
		
		else {
			// both x and y can be non-zero
			if(x > 0 && y > 0) {
				int up = getRemainder(foodLeft, x-1, y);
				int left = getRemainder(foodLeft, x, y-1);
				int cur = Math.min( left , up );
				cache.put(newKey, cur);
				return cache.get(tuple);
			}
			// x can be non-zero, y can be 0
			else if(x > 0) {
				int up = getRemainder(foodLeft, x-1, y);
				cache.put(newKey, up);
				return cache.get(tuple);
			}
			
			// x can be 0, y non-zero
			else {
				int left = getRemainder(foodLeft, x, y-1);
				cache.put(newKey, left);
				return cache.get(tuple);
			}
		}	
	}
	
	private static Integer rem(int food, int row, int col) {
		if(row == 0 && col == 0)
			return food;
		
		food -= g[row][col];
		
		if(food < 0) 
			return null;
		
		if(col > 0 && row > 0) {
			Integer left = rem(food, row, col-1);
			Integer up = rem(food, row-1, col);
			return Math.min( left==null?Integer.MAX_VALUE:left , up==null?Integer.MAX_VALUE:up );
		} else if(col > 0) {
			Integer left = rem(food, row, col-1);
			return left;
		} else {//if(row > 0) {
			Integer up = rem(food, row-1, col);
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
//		
		int food = 12;
		int[][] grid = new int[][]{{0, 2, 5}, {1, 1, 3}, {2, 1, 1}};
		for(int row[]: grid)
			System.out.println(Arrays.toString(row));
		System.out.println(answer(food, grid));
		
	}
}
