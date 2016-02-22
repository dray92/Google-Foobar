package primary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Minglish lesson
===============

Welcome to the lab, minion. Henceforth you shall do the bidding of Professor Boolean. Some say he's mad, trying to develop a zombie serum and all... but we think he's brilliant!

First things first - Minions don't speak English, we speak Minglish. Use the Minglish dictionary to learn! The first thing you'll learn is how to use the dictionary.

Open the dictionary. Read the page numbers, figure out which pages come before others. You recognize the same letters used in English, but the order of letters is completely different in Minglish than English (a < b < c < ...).

Given a sorted list of dictionary words (you know they are sorted because you can read the page numbers), can you find the alphabetical order of the Minglish alphabet? For example, if the words were ["z", "yx", "yz"] the alphabetical order would be "xzy," which means x < z < y. The first two words tell you that z < y, and the last two words tell you that x < z.

Write a function answer(words) which, given a list of words sorted alphabetically in the Minglish alphabet, outputs a string that contains each letter present in the list of words exactly once; the order of the letters in the output must follow the order of letters in the Minglish alphabet.

The list will contain at least 1 and no more than 50 words, and each word will consist of at least 1 and no more than 50 lowercase letters [a-z]. It is guaranteed that a total ordering can be developed from the input provided (i.e. given any two distinct letters, you can tell which is greater), and so the answer will exist and be unique.

Languages
=========

To provide a Python solution, edit solution.py
To provide a Java solution, edit solution.java

Test cases
==========

Inputs:
    (string list) words = ["y", "z", "xy"]
Output:
    (string) "yzx"

Inputs:
    (string list) words = ["ba", "ab", "cb"]
Output:
    (string) "bac"
 * @author Debosmit
 *
 */
public class Minglish_Lesson {
	
	public static String answer(String[] words) { 

        // Your code goes here.
        // provided words is sorted
        // now, to understand what can help us order letters
        // at least two words will be required for this ordering
        // ordering can be inferred only when two character of the word are
        // different; this does not necessarily need to be the first character
        // eg -> ['ba', 'bb']; first char is equal, but second gives ordering
        // here, order is {a,b}
        
        // building map that will contain idea of ordering
        Map<Character, ArrayList<Character>> map = getMap(words);
        
        /***** identify characters to start dfs from *****/
        Set<Character> startingCharacters = getClone(map.keySet());	// cloned to avoid concurrent modification exception
        
        // parsing all character in arraylists
        for(Character ch: map.keySet()) 
        	for(Character connectedChar: map.get(ch)) 
        		// remove char if present
        		// omitted check to see if char present
        		// since that would incur an additional 
        		// traversal
        		startingCharacters.remove(connectedChar);
        	
        Set<Character> alphabet = new LinkedHashSet<Character>(), visitedChars = new HashSet<Character>();
        
        // do dfs starting at every 'starting character' which is essentially
        // all characters, for which we have inequalities such that this 
        // is larger than another character.
        for(Character ch: startingCharacters) 
        	traverse(ch, alphabet, visitedChars, map);
        
        // linkedhashset to string
        StringBuilder orderedAlpha = new StringBuilder();
        for(char ch: alphabet)
        	orderedAlpha.append(ch);
        
        return orderedAlpha.toString();
    }
	
	// building map that will contain idea of ordering
	private static Map<Character, ArrayList<Character>> getMap(String[] words) {
		Map<Character, ArrayList<Character>> map = new HashMap<Character, ArrayList<Character>>();
		// building map that will contain idea of ordering
        for(int i = 0 ; i < words.length - 1 ; i++) {
        	// try to establish a relationship between 
        	// this and the previous word
        	int minSimilarity = Math.min(words[i].length(), words[i+1].length());
        	
        	// find point where first break in equality occurs
        	Integer chars;
        	boolean breakBeforeEnd = false;
        	for(chars = 0 ; chars < minSimilarity ; chars++)
        		if(words[i].charAt(chars) != words[i+1].charAt(chars)) {
        			breakBeforeEnd = true;
        			break;
        		}
        	// chars points to first point in these 2 words that 
        	// can help us determine some ordering
        	
        	if(!breakBeforeEnd)
        		chars--;	// prevent index out of bounds
        	
        	// previous word will be used as key to preserve sense of ordering
        	if(!map.containsKey(words[i].charAt(chars))) 
        		map.put(words[i].charAt(chars), new ArrayList<Character>());
        	
        	map.get(words[i].charAt(chars)).add(words[i+1].charAt(chars));
        }
        return map;
	}
	
	// depth first search traversal
	private static void traverse(Character ch, Set<Character> alphabet,
			Set<Character> visitedChars, Map<Character, ArrayList<Character>> map) {
		
		// add to visited list since we don't ever want to visit
		// the same letter twice
		if(!visitedChars.add(ch))
			return;
		
		// add it to the set before traversing to preserve order
		alphabet.add(ch);
		
		// do a mild check to see if map contains ch as a key
		// if it does, do a dfs on each of the nodes in the Arraylist
		if(map.containsKey(ch)) 
			for(Character nextChar: map.get(ch)) 		
				traverse(nextChar, alphabet, visitedChars, map);
			
	}

	// a cloned copy of a set
	private static Set<Character> getClone(Set<Character> keySet) {
		Set<Character> newSet = new HashSet<Character>();
		for(Character c: keySet)
			newSet.add(c);
		return newSet;
	}

	public static void main(String[] args) {
		String[] words = {"z", "yx", "yz"};
		System.out.println(answer(words));
	}
}
