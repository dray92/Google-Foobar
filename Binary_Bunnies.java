package primary;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * Binary bunnies
==============

As more and more rabbits were rescued from Professor Booleans horrid laboratory, you had to develop a system to track them, since some habitually continue to gnaw on the heads of their brethren and need extra supervision. For obvious reasons, you based your rabbit survivor tracking system on a binary search tree, but all of a sudden that decision has come back to haunt you.

To make your binary tree, the rabbits were sorted by their ages (in days) and each, luckily enough, had a distinct age. For a given group, the first rabbit became the root, and then the next one (taken in order of rescue) was added, older ages to the left and younger to the right. The order that the rabbits returned to you determined the end pattern of the tree, and herein lies the problem.

Some rabbits were rescued from multiple cages in a single rescue operation, and you need to make sure that all of the modifications or pathogens introduced by Professor Boolean are contained properly. Since the tree did not preserve the order of rescue, it falls to you to figure out how many different sequences of rabbits could have produced an identical tree to your sample sequence, so you can keep all the rescued rabbits safe.

For example, if the rabbits were processed in order from [5, 9, 8, 2, 1], it would result in a binary tree identical to one created from [5, 2, 9, 1, 8].

You must write a function answer(seq) that takes an array of up to 50 integers and returns a string representing the number (in base-10) of sequences that would result in the same tree as the given sequence.

Languages
=========

To provide a Python solution, edit solution.py
To provide a Java solution, edit solution.java

Test cases
==========

Inputs:
    (int list) seq = [5, 9, 8, 2, 1]
Output:
    (string) "6"

Inputs:
    (int list) seq = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
Output:
    (string) "1"
 * @author Debosmit
 *
 */
public class Binary_Bunnies {

	public static class BinarySearchTree {
		private Integer val;
		private BinarySearchTree left;
		private BinarySearchTree right;
		
		public BinarySearchTree(int[] values) {
			this.val = null;
			this.left = null;
			this.right = null;
			
			for(int value: values)
				this.insert(value);
		}
		
		public BinarySearchTree(int value) {
			this.val = value;
			this.left = null;
			this.right = null;
		}
		
		private void insert(int value) {
			// root is null
			if(this.getValue() == null)
				this.val = value;
			
			// root value is greater
			// insert into right subtree
			else if(this.getValue() > value) {
				// right node does not exist
				if(this.right == null) 
					this.right = new BinarySearchTree(value);
				// right subtree exists
				else 
					this.right.insert(value);
			}
			
			// root value is lesser
			// insert into left subtree
			else if(this.getValue() < value){
				// left node does not exist
				if(this.left == null) 
					this.left = new BinarySearchTree(value);
				// left subtree exists
				else 
					this.left.insert(value);
			}
		}
		
		public Integer getValue() {
			return this.val;
		}
	}
	
	// number of nodes in a binary search tree
	public static int numNodes(BinarySearchTree root) {
		if(root == null)
			return 0;
		return 1 + numNodes(root.left) + numNodes(root.right);
	}
	
	// formula: nCr = n! / [r! * (n-r)!]
	// returning BigInteger instead of Integer, since in the worst
	// case on subtree will have 36 elements, the other will have 
	// 13 elements (50 elements max). 
	// 49C13 = 97684251196698678262364590675736200670399692800000000
	// which cannot fit into an Integer object
	private static BigInteger nChooseR(int n, int r) {
		if(r > n)
			return nChooseR(r, n);
		
		BigInteger diff = getFactorial(n-r);
		
		BigInteger numerator = getFactorial(n);
		
		BigInteger denominator = getFactorial(r);
		denominator = denominator.multiply(diff);

		return numerator.divide(denominator);
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
	
	public static String answer(int[] seq) {
		BinarySearchTree root = new BinarySearchTree(seq);
		return numCombinations(root);
	}
	
	private static String numCombinations(BinarySearchTree root) {
		// value at root = null
		// => empty tree 
		// => 1 combination
		if(root == null || root.getValue() == null)
			return "1";

		// num nodes in left subtree
		int leftCount = numNodes(root.left);
		int rightCount = numNodes(root.right);

		// num ways to permute at this level given the number
		// of children in each of subtrees
		BigInteger ways = nChooseR( leftCount+rightCount, Math.min(leftCount, rightCount) );
		
		// num combinations for each of the subtrees
		BigInteger leftWays = new BigInteger(numCombinations(root.left));
		BigInteger rightWays = new BigInteger(numCombinations(root.right));
		
		
		return ways.multiply(leftWays).multiply(rightWays).toString();
	}

	public static void main(String[] args) {
//		int[] values = new int[]{5,2,9,1,8};
		int[] values = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		System.out.println(answer(values));
		return;
	}
}
