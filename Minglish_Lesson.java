package primary;

import java.util.HashSet;
import java.util.LinkedHashMap;
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
        Map<Character, LinkedHashSet<Character>> map = getMap(words);
        
        /***** identify characters to start dfs from *****/
        Set<Character> valueCharacters = new HashSet<Character>();
        // get all values present
        for(Character key: map.keySet()) {
        	for(Character ch: map.get(key))
        		valueCharacters.add(ch);
        }
        
        Set<Character> startingCharacters = new LinkedHashSet<Character>();
        // finding unique key values and preserving ordering
        for(Character key: map.keySet()) 
        	if(!valueCharacters.contains(key))
        		startingCharacters.add(key);
        
        /**** processing graph to obtain alphabet ****/	
        Set<Character> alphabet = new LinkedHashSet<Character>(), visitedChars = new HashSet<Character>();
        
        // do dfs starting at every 'starting character' which is essentially
        // all characters, for which we have inequalities such that this 
        // is smaller than another character.
        for(Character ch: startingCharacters) 
        	traverse(ch, alphabet, visitedChars, map);
        
        // linkedhashset to string
        // last char is at the end, so doing in reverse
        StringBuilder orderedAlpha = new StringBuilder();
        for(char ch: alphabet)
        	orderedAlpha.insert(0,ch);
        
        return orderedAlpha.toString();
    }
	
	// building map that will contain idea of ordering
	private static Map<Character, LinkedHashSet<Character>> getMap(String[] words) {
		// LinkedHashMap will preserve ordering of obtained inequalities
		Map<Character, LinkedHashSet<Character>> map = new LinkedHashMap<Character, LinkedHashSet<Character>>();
		// building map that will contain idea of ordering
        for(int i = 0 ; i < words.length - 1 ; i++) {
        	//
        	Integer breakInSequence = getPointOfInequality(words[i], words[i+1]);
        	
        	// no break found => cannot derive some inequality from these 2 words
        	// if 2 words are the same till minLength, they are useless for
        	// intention to derive an inequality
        	// eg. [yzy, yzyx,..] when looking at y and yx, we compare first 3
        	// character from each string and retrieve no useful info
        	// it gives us no useful info; this will also cover the case
        	// where both words are the same
        	if(breakInSequence == null)
        		continue;
        	
        	char key = words[i].charAt(breakInSequence);
        	char value = words[i + 1].charAt(breakInSequence);
        	
        	// previous word character will be used as key to preserve sense of ordering
        	if(!map.containsKey(key))
        		map.put(key, new LinkedHashSet<Character>());
        	
        	map.get(key).add(value);
        }
        return map;
	}
	
	private static Integer getPointOfInequality(String a, String b) {
		
		// length of smaller string for comparison purposes
		int len = Math.min(a.length(), b.length());
		for(int i = 0 ; i < len ; i++)
			if(a.charAt(i) != b.charAt(i))
				return i;
		
		return null;
	}
	
	// depth first search traversal
	private static void traverse(Character ch, Set<Character> alphabet,
			Set<Character> visitedChars, Map<Character, LinkedHashSet<Character>> map) {
		
		// add to visited list since we don't ever want to visit
		// the same letter twice
		if(!visitedChars.add(ch))
			return;
		
		// do a mild check to see if map contains ch as a key
		// if it does, do a dfs on each of the nodes in the Arraylist
		if(map.containsKey(ch)) 
			for(Character nextChar: map.get(ch)) 		
				traverse(nextChar, alphabet, visitedChars, map);
		
		// add it to the set
		alphabet.add(ch);
	}
	
	public static void main(String[] args) {
		String[] words = {"ba", "ab", "cb"};
		System.out.println(answer(words));
	}
}
