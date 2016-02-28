package primary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Hash it out
===========
 
Something horrible must have gone wrong in that last mission. As you wake in a holding cell, you realize that youre in the clutches of Professor Booleans numerous but relatively incompetent minions! Time to plan another escape.
 
Lucky for you nobody is around (do these security minions just sleep all the time?), so you have a chance to examine your cell. Looking around, you see no signs of surveillance (ha, they must underestimate you already) and the only thing keeping you contained is an electronic door lock. Should be easy enough.
 
You and Beta Rabbit worked together to exfiltrate some of Professor Booleans security information in anticipation of a moment just like this one. Time to put it to the test.
 
If memory serves, this locking mechanism relies on a horribly bad cryptographic hash, and you should be able to break it with some rudimentary calculations.
 
To open these doors, you will need to reverse engineer the hash function it is using. You already managed to steal the details of the algorithm used, and with some quiet observation of the guards you find out the results of the hash (the digest). Now to break it.
 
The function takes a 16 byte input and gives a 16 byte output. It uses multiplication (*), bit-wise exclusive OR (XOR) and modulo (%) to calculate an element of the digest based on elements of the input message:
 
digest [i] = ( (129 * message[i]) XOR message[i-1]) % 256
 
For the first element, the value of message[-1] is 0.
 
For example, if message[0] = 1 and message[1] = 129, then:
For digest[0]:
129*message[0] = 129
129 XOR message[-1] = 129
129 % 256 = 129
Thus digest[0] = 129.
 
For digest[1]:
129*message[1] = 16641
16641 XOR message[0] = 16640
16640 % 256 = 0
Thus digest[1] = 0.
 
Write a function answer(digest) that takes an array of 16 integers and returns another array of 16 that correspond to the unique message that created this digest. Since each value is a single byte, the values are 0 to 255 for both message and digest.
 
Languages
=========
 
To provide a Python solution, edit solution.py
To provide a Java solution, edit solution.java
 
Test cases
==========
 
Inputs:
    (int list) digest = [0, 129, 3, 129, 7, 129, 3, 129, 15, 129, 3, 129, 7, 129, 3, 129]
Output:
    (int list) [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]
 
Inputs:
    (int list) digest = [0, 129, 5, 141, 25, 137, 61, 149, 113, 145, 53, 157, 233, 185, 109, 165]
Output:
    (int list) [0, 1, 4, 9, 16, 25, 36, 49, 64, 81, 100, 121, 144, 169, 196, 225]
 * @author Debosmit
 *
 */
public class Hash_It_Out {

	/**
	 * Ideation:
	 * digest[i] = ( ( 129 * message[i] ) XOR ( message[i-1] ) ) % 256
	 * 
	 * Lets break this down into parts.
	 * 
	 * First, the part with the XOR operator. Can we establish bounds?
	 * We know each index of the message array holds the value of a byte. 
	 * Values of bytes range from [0, 255]. So, no negative values 
	 * are possible (? sic). Hence, lowest value possible is 0. The maximum 
	 * value, hence, would be 129*255. We have established bounds 
	 * for ( ( 129 * message[i] ) XOR ( message[i-1] ) ).
	 * Values will be in [0, 129*255].
	 * 
	 * Now, to think about the modulus operator. In this instance, if some value
	 * can be represented as 256*x + y, where x,y ∈ [0,1,2...), it is 
	 * this 'y' that we care about, since modulo 256 would return 'y', and 
	 * that is what digest[i] is.
	 * So, it would be sufficient to look at only the multiples of 256 
	 * in the range [0, 129*256].
	 * 
	 * So, result = 256*x + y, such that 256*x ∈ [0, ..., 129*256],
	 * or, x ∈ [0, 1, ..., 129].
	 * 
	 * Now, to break down the part with the XOR operation. 
	 * [Note the same variable. Surprise, surprise. They are the same thing]
	 * result = ( 129 * message[i] ) XOR ( message[i-1] )
	 * result XOR ( message[i-1] ) = 129 * message[i]
	 * message[i] = ( result XOR message[i-1] ) / 129.
	 * 
	 * Here, we can comment that, for some a ∈ [0,1,2...),
	 * result XOR'ed with message[i-1] = 129*a. 
	 * This follows trivially from the fact that
	 * message[i] is a whole number and the XOR operator returns whole numbers,
	 * and that we are only working with non-negative numbers here.
	 * So, as soon as we get ( result XOR message[i-1] ) modulo 129
	 * to be 0, we are good. :)
	 * 
	 * Now, (I think?) we have sufficient information about this problem to 
	 * start off.
	 * 
	 * First, for some i ∈ [0, 1, 2, .., digits.length-1], we add
	 * digits[i] to every multiple of 256 in the range [0, 129*255].
	 * 
	 * NOTE: message[-1] = 0. So, for index 0, we consider the previous character 
	 * message to be 0.
	 */
	
	public static int[] answer(int[] digest) {
		int[] curDigestSum = new int[multiples256.size()];
		int[] message = new int[digest.length];
		for(int i = 0 ; i < digest.length ; i++) {
			
			// 256*x + y
			// or, 256*x + digest[i]
			for(int mutiple = 0 ; mutiple < multiples256.size() ; mutiple++) 
				curDigestSum[mutiple] = multiples256.get(mutiple) + digest[i];
			
			// message[-1] = 0
			int prev = (i != 0) ? message[i-1] : 0;
			
			for(int indx = 0; indx < curDigestSum.length ; indx++) {
				int result = curDigestSum[indx]; // note same variable name as ideation
				
				// message[i] = ( result XOR message[i-1] ) / 129.
				if( ( (result ^ prev) % 129 ) == 0 ) {
					// message is a byte => we are concerned with modulo 256 value
					message[i] = ( (result ^ prev) / 129 ) % 256;
					break;
				}
			}
		}
		return message;
	}
	
	private static List<Integer> multiples256 = null;
	private static final int _LOWER_BOUND = 0;
	private static final int _UPPER_BOUND = 129*255;
	static {
		if(multiples256 == null) {
			multiples256 = new ArrayList<Integer>();
			int coef = 0;
			while( (_LOWER_BOUND <= coef*256) && (coef*256 <= _UPPER_BOUND) ) 
				multiples256.add(256 * coef++);
			
		}
	}
	
	public static void main(String[] args) {
//		int[] digest = new int[]{0, 129, 3, 129, 7, 129, 3, 129, 15, 129, 3, 129, 7, 129, 3, 129};
//		System.out.println(Arrays.toString(answer(digest)));
		
		int[] digest = new int[]{0, 129, 5, 141, 25, 137, 61, 149, 113, 145, 53, 157, 233, 185, 109, 165};
		System.out.println(Arrays.toString(answer(digest)));
	}
}
