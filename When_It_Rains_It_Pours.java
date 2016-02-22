package primary;

/**
 * When it rains it pours
======================

It's raining, it's pouring. You and your agents are nearing the building where the captive rabbits are being held, but a sudden storm puts your escape plans at risk. The structural integrity of the rabbit hutches you've built to house the fugitive rabbits is at risk because they can buckle when wet. Before the rabbits can be rescued from Professor Boolean's lab, you must compute how much standing water has accumulated on the rabbit hutches.

Specifically, suppose there is a line of hutches, stacked to various heights and water is poured from the top (and allowed to run off the sides). We'll assume all the hutches are square, have side length 1, and for the purposes of this problem we'll pretend that the hutch arrangement is two-dimensional.

For example, suppose the heights of the stacked hutches are [1,4,2,5,1,2,3] (the hutches are shown below):

. . . I . . .
. I . I . . .
. I . I . . I
. I I I . I I
I I I I I I I
1 4 2 5 1 2 3

When water is poured over the top at all places and allowed to runoff, it will remain trapped at the 'O' locations:

. . . I . . .
. I X I . . .
. I X I X X I
. I I I X I I
I I I I I I I
1 4 2 5 1 2 3

The amount of water that has accumulated is the number of Xs, which, in this instance, is 5.

Write a function called answer(heights) which, given the heights of the stacked hutches from left-to-right as a list, computes the total area of standing water accumulated when water is poured from the top and allowed to run off the sides.

The heights array will have at least 1 element and at most 9000 elements. Each element will have a value of at least 1, and at most 100000.

Languages
=========

To provide a Python solution, edit solution.py
To provide a Java solution, edit solution.java

Test cases
==========

Inputs:
    (int list) heights = [1, 4, 2, 5, 1, 2, 3]
Output:
    (int) 5

Inputs:
    (int list) heights = [1, 2, 3, 2, 1]
Output:
    (int) 0
 * @author Debosmit
 *
 */
public class When_It_Rains_It_Pours {
	
	public static int answer(int[] heights) { 

        // Your code goes here.
        // essentially, a 2D array will have to be modeled
        // while standing at a particular index, 
        // look at the next indices to find a higher index
        int cur = 0;    // start from index 0
        int sum = 0;    // sum
        while(cur < heights.length-1) {
            int nextCol = cur + 1; // next column
            
            // get next column that has greater than
            // or equal to present height
            for(int i = nextCol ; i < heights.length ; i++) {
                // next column to visit is found
                if(heights[i] >= heights[cur]) {
                    nextCol = i;
                    break;
                }
                
                // next higher column is found
                if(heights[i] > heights[nextCol]) {
                    // mark column
                    nextCol = i;
                    // but keep searching since there may
                    // be higher columns after this
                    // eg arr = [4,2,3,..]
                }
            }
            
            if(nextCol == cur)
                // currently at highest column
                // done searching
                return sum;
                
            // there is a difference in heights
            // between present column and the next 
            // column that will be visited
            // there must be water here
            int diff = Math.min(heights[cur], heights[nextCol]);
            
            // add water on path from current
            // column to the next higher column
            for(int i = cur+1 ; i < nextCol ; i++)
                sum += (diff - heights[i]);
                
            cur = nextCol;
        }
        return sum;
    } 
	
	public static void main(String[] args) {
		int[] h = {1, 4, 2, 5, 1, 2, 3};
		System.out.println(answer(h));
	}
}
