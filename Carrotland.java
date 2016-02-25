package primary;


/**
 * Carrotland
==========

The rabbits are free at last, free from that horrible zombie science experiment. They need a happy, safe home, where they can recover.

You have a dream, a dream of carrots, lots of carrots, planted in neat rows and columns! But first, you need some land. And the only person who's selling land is Farmer Frida. Unfortunately, not only does she have only one plot of land, she also doesn't know how big it is - only that it is a triangle. However, she can tell you the location of the three vertices, which lie on the 2-D plane and have integer coordinates.

Of course, you want to plant as many carrots as you can. But you also want to follow these guidelines: The carrots may only be planted at points with integer coordinates on the 2-D plane. They must lie within the plot of land and not on the boundaries. For example, if the vertices were (-1,-1), (1,0) and (0,1), then you can plant only one carrot at (0,0).

Write a function answer(vertices), which, when given a list of three vertices, returns the maximum number of carrots you can plant.

The vertices list will contain exactly three elements, and each element will be a list of two integers representing the x and y coordinates of a vertex. All coordinates will have absolute value no greater than 1000000000. The three vertices will not be collinear.

Languages
=========

To provide a Python solution, edit solution.py
To provide a Java solution, edit solution.java

Test cases
==========

Inputs:
    (int) vertices = [[2, 3], [6, 9], [10, 160]]
Output:
    (int) 289

Inputs:
    (int) vertices = [[91207, 89566], [-88690, -83026], [67100, 47194]]
Output:
    (int) 1730960165
 * @author Debosmit
 *
 */
public class Carrotland {
	public static int answer(int[][] vertices) { 

        // Your code goes here.
		// leftmost bound
		int leftBound = Math.min( Math.min(vertices[0][0], vertices[1][0]), vertices[2][0] );
		
		// rightmost bound
		int rightBound = Math.max( Math.max(vertices[0][0], vertices[1][0]), vertices[2][0] );
		
		// lower-most bound
		int lowerBound = Math.min( Math.min(vertices[0][1], vertices[1][1]), vertices[2][1] );
		
		// upper-most bound
		int upperBound = Math.max( Math.max(vertices[0][1], vertices[1][1]), vertices[2][1] );
		
		int horizontalRange = Math.abs(rightBound - leftBound);
		int verticalRange = Math.abs(upperBound - lowerBound);
		
		// initialize answer to upper bound
		
		// this contains the number of points in 
		// the interior of the rectangle
		long answer =  ( horizontalRange - 1 ) * ( verticalRange - 1 );
		
		for(int pointIndex = 0 ; pointIndex < 3 ; pointIndex++) {
			// first point
			int Ax = vertices[pointIndex][0];
			int Ay = vertices[pointIndex][1];
			
			// second point
			int Bx = vertices[(pointIndex+1)%3][0];
			int By = vertices[(pointIndex+1)%3][1];
			
			// is line perpendicular or parallel
			// to the X-axis
			if(parallelOrPerpendicular(Ax, Ay, Bx, By)) 
				continue;
			
			
			long numPtsLine = getNumPointsLine(Ax, Bx, Ay, By);
			
			// exclude endpoints
			numPtsLine -= 2;
			
			long width = Math.abs(Ax - Bx);
			long height = Math.abs(Ay - By);
			
			long num = ( (width - 1) * (height - 1) ) - numPtsLine;
			
			answer -= numPtsLine + num/2;
		}
		
		/* Now, this model assumed that all the vertices of the triangle 
		 * will lie on the perimeter of the surrounding rectangle.
		 * For, indices {2, 3}, {6, 9}, {10, 160}, the point {6,9} lies
		 * inside the rectangle. So, there is a rectangle that lies outside 
		 * triangle and has not yet been accounted for.
		 */
		// finding culprit point
		for (int a = 0; a < 3; a++) {
            long x = vertices[a][0];
            long y = vertices[a][1];

            if (x == leftBound || x == rightBound || y == upperBound || y == lowerBound) {
                continue;
            }

            // If this point is inside the rectangle, it must mean that
            // the other two points are the corners of the rectangle
            int[] edge1 = vertices[(a + 1) % 3];
            int[] edge2 = vertices[(a + 2) % 3];

            // other two corners
            int[] edge3 = new int[]{ edge1[0], edge2[1] };
            int[] edge4 = new int[]{ edge2[0], edge1[1] };

            // need to find which corner this point is closer to,
            // since the contained rectangle is not a part of the triangle.
            double distance1 = distance(x, y, edge3[0], edge3[1]);
            double distance2 = distance(x, y, edge4[0], edge4[1]);

            int[] edge = distance1 < distance2 ? edge3 : edge4;

            // Subtract the double-counted area from the corner
            long width = Math.abs(edge[0] - x);
            long height = Math.abs(edge[1] - y);

            answer -= width * height;
        }
		
		return (int)answer;
    }
	
	private static double distance(long x1, long y1, long x2, long y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
	
	private static int getNumPointsLine(int Ax, int Bx, int Ay, int By) {
		int x = Math.abs(Ax - Bx);
		int y = Math.abs(Ay - By);
		return ( GCD(x,y) + 1 );
	}	
	
	private static int GCD(int x, int y) {
		return y == 0 ? x : GCD(y, x%y);
	}
	
	private static boolean parallelOrPerpendicular(int Ax, int Ay, int Bx, int By) {
		return ( (Ax == Bx) || (Ay == By) );
	}
	
	public static void main(String[] args) {
//		int[][] vertices = {{2, 3}, {6, 9}, {10, 160}};
//		System.out.println(answer(vertices));

		int[][] vertices = {{91207, 89566}, {-88690, -83026}, {67100, 47194}};
		System.out.println(answer(vertices));
		
//		int[][] vertices = {{0,4},{0,0},{4,1}};
//		System.out.println(answer(vertices));
		
//		int[][] vertices = {{2,4},{0,0},{4,1}};
//		System.out.println(answer(vertices));
		
//		int[][] vertices = {{-1,-1},{1,0},{0,1}};
//		System.out.println(answer(vertices));
	}
}
