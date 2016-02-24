package primary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Spy snippets
============

You've been recruited by the team building Spy4Rabbits, a highly advanced search engine used to help fellow agents discover files and intel needed to continue the operations against Dr. Boolean's evil experiments. The team is known for recruiting only the brightest rabbit engineers, so there's no surprise they brought you on board. While you're elbow deep in some important encryption algorithm, a high-ranking rabbit official requests a nice aesthetic feature for the tool called "Snippet Search." While you really wanted to tell him how such a feature is a waste of time in this intense, fast-paced spy organization, you also wouldn't mind getting kudos from a leader. How hard could it be, anyway?

When someone makes a search, Spy4Rabbits shows the title of the page. Your commander would also like it to show a short snippet of the page containing the terms that were searched for.

Write a function called answer(document, searchTerms) which returns the shortest snippet of the document, containing all of the given search terms. The search terms can appear in any order.

The length of a snippet is the number of words in the snippet. For example, the length of the snippet "tastiest color of carrot" is 4. (Who doesn't like a delicious snack!)

The document will be a string consisting only of lower-case letters [a-z] and spaces. Words in the string will be separated by a single space. A word could appear multiple times in the document.
searchTerms will be a list of words, each word comprised only of lower-case letters [a-z]. All the search terms will be distinct.

Search terms must match words exactly, so "hop" does not match "hopping".

Return the first sub-string if multiple sub-strings are shortest. For example, if the document is "world there hello hello where world" and the search terms are ["hello", "world"], you must return "world there hello".

The document will be guaranteed to contain all the search terms.

The number of words in the document will be at least one, will not exceed 500, and each word will be 1 to 10 letters long. Repeat words in the document are considered distinct for counting purposes.
The number of words in searchTerms will be at least one, will not exceed 100, and each word will not be more than 10 letters long.

Languages
=========

To provide a Python solution, edit solution.py
To provide a Java solution, edit solution.java

Test cases
==========

Inputs:
    (string) document = "many google employees can program"
    (string list) searchTerms = ["google", "program"]
Output:
    (string) "google employees can program"

Inputs:
    (string) document = "a b c d a"
    (string list) searchTerms = ["a", "c", "d"]
Output:
    (string) "c d a"
 * @author Debosmit
 *
 */
public class Spy_Snippets {

	public static String answer(String document, String[] searchTerms) { 

        // Your code goes here.
		
		// look for spaces and add word
		ArrayList<String> words = new ArrayList<String>();
		int start = 0;
		// single character, single word case
		if(document.length() == 1)
			words.add(document.substring(start,1));
		
		for(int i = 1 ; i < document.length() ; i++) {
			// last word
			if(i == document.length() - 1)
				words.add(document.substring(start, i+1));
			
			// all other words
			if(document.charAt(i) == ' ') {
				words.add(document.substring(start, i));
				start = i+1;
			}
			
		}

		Map<String, ArrayList<Integer>> map = new HashMap<String, ArrayList<Integer>>();
		
		for(String searchTerm: searchTerms) {
			if(!map.containsKey(searchTerm))
				map.put(searchTerm, new ArrayList<Integer>());
			
			// not using hash approach to compare strings since
			// total neither document or list of search terms is not
			// considered to be very long
			for(int i = 0 ; i < words.size() ; i++) {
				// word found
				if(words.get(i).compareTo(searchTerm) == 0)
					map.get(searchTerm).add(i);
			}
		}
		
		// set up initial score
		int minScore = Integer.MAX_VALUE;
		
		// set up snippet bounds
		int overallLower = 0, overallUpper = words.size() - 1;
		
		// goal is to compare distances from current instance of searchWord
		// to other searchWords; we have to choose a subset that has 
		// the smallest distance
		// one way to think would be to have a circle with the currentWord 
		// at the center, and other words surrounding it, with the most
		// distant words on the circumference
		for(String searchTerm: map.keySet()) {
			// iterate over the indices of the word in document
			for(int index: map.get(searchTerm)) {
				
				// keeps track of distance of each element 
				// in subset around current searchTerm
				// minimum subset is the one that contains result
				ArrayList<Integer> thisToOtherDistances = new ArrayList<Integer>();
				
				// distance from this index to this index is 0
				thisToOtherDistances.add(0);
				
				// trying to form new snippet around this word index
				int lower = index, upper = index;
				
				// iterate over the other search words
				for(String otherSearchTerm: map.keySet()) {
					
					// skip if current searchTerm shows up
					if(searchTerm.equals(otherSearchTerm))
						continue;
					
					int minDist = Integer.MAX_VALUE;
					
					// iterate over indices of the other word
					for(int otherIndex: map.get(otherSearchTerm)) {
						int dist = (int)Math.abs(index - otherIndex);
						
						// this index is of interest
						if(dist < minDist) {
							minDist = dist;
							
							// update snippet bounds
							lower = Math.min(lower, otherIndex);
							upper = Math.max(upper, otherIndex);
						}
					}
					
					// add least possible distance from other term to current term
					thisToOtherDistances.add(minDist);
				}
				
				int score = Collections.max(thisToOtherDistances) - Collections.min(thisToOtherDistances) + 1;
				if(score < minScore) {
					minScore = score;
					
					// update global snippet bounds only if new bounds are tighter
					if((upper - lower) < (overallUpper-overallLower)) {
						overallLower = lower;
						overallUpper = upper;
					}
				}
			}
		}
		
		StringBuilder string = new StringBuilder();
		string.append(words.get(overallLower));
		for(int i = overallLower+1 ; i <= overallUpper ; i++)
			string.append(" " + words.get(i));
		
		return string.toString();
    }
	
	public static void main(String[] args) {
//		String doc = "many google employees can program";
//		String[] search = {"google", "program"};
		
//		String doc = "world there hello hello where world";
//		String[] search = {"hello", "world"};
		
//		String doc = "a b c d a";
//		String[] search = {"a", "c", "d"};
		
//		String doc = "many google employees can program can google employees because google is a technology company that writes programs";
//		String[] search = {"google", "program", "can"};
		
		String doc = "a b d a c a c c d a";
		String[] search = {"a", "c", "d"};
		
		System.out.println(answer(doc, search));
	}
}
