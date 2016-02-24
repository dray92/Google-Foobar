package primary;

import java.util.HashSet;
import java.util.Set;

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
		
		// document words array
		String[] words = document.split(" ");

		// using HashSet since comparison will be the fastest
		Set<String> searchWords = new HashSet<String>();
		for(String word: searchTerms)
			searchWords.add(word);
		
		int numSearchWords = searchTerms.length;
		
		int startIndx = -1, curIndx = 0;
		
		// global comparison metrics
		int minSequenceSize = Integer.MAX_VALUE, minSequenceStart = 0; 
		
		// keep track of words found, HashSet since ordering not required
		// using set since searchTerms can have a frequency > 1 in a sequence
		// don't want that to affect result
		Set<String> foundWords = new HashSet<String>();
		
		while(curIndx != words.length) {
			
			// current word
			String word = words[curIndx];
			
			// only concerned if this is a searchTerm
			if(searchWords.contains(word)) {
				// no words found
				// => start of sequence
				if(foundWords.size() == 0)
					startIndx = curIndx;
				
				// add word
				foundWords.add(word);
				
				if(foundWords.size() == numSearchWords) {
					// do comparisons
					int curSequenceSize = curIndx - startIndx + 1;
					if(curSequenceSize < minSequenceSize) {
						// update global comparison metrics
						minSequenceStart = startIndx;
						minSequenceSize = curSequenceSize;
					}
					
					// reset curIndx to make sequence search start 
					// from the next index
					curIndx = startIndx;
					
					// clear foundWords
					// old Set will be deleted by garbage collector
					// new Set being initialized since old size might 
					// have been unnecessarily big
					foundWords = new HashSet<String>();
				}
			}
			curIndx++;
		}
		
		StringBuilder sequence = new StringBuilder();
		
		// adding first word since remaining words will need spaces
		sequence.append(words[minSequenceStart]);
		for(int i = minSequenceStart + 1; i < minSequenceStart + minSequenceSize ; i++)
			sequence.append(" " + words[i]);
		
		return sequence.toString();
    }
	
	public static void main(String[] args) {
//		String doc = "many google employees can program";
//		String[] search = {"google", "program"};
		
//		String doc = "world there hello hello where world";
//		String[] search = {"hello", "world"};
		
//		String doc = "a b c d a";
//		String[] search = {"a", "c", "d"};
		
		String doc = "many google employees can program can google employees because google is a technology company that writes programs";
		String[] search = {"google", "program", "can"};
		
//		String doc = "a b d a c a c c d a";
//		String[] search = {"a", "c", "d"};
		
		System.out.println(answer(doc, search));
	}
}
