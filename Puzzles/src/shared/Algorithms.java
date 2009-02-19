package shared;

import java.util.ArrayList;
import java.util.Scanner;


//Edited some typo's in the comments -Kyle
/**
 * Algorithms used in various places throughout the program
 * @author Sad Panda Software
 * @version 1.0
 */
public class Algorithms {
	
	/**
	 * Converts an array to a string.  Primarily used for loading, saving, and output
	 * @param a a list of words
	 * @return a string of words, one on each line
	 */
	public static String arrayToString (ArrayList<String> a) {
		String s = "";
		for (String word: a) {
			s += word + "\n";
		}
		return (s);
	}
	
	/**
	 * Converts a string to an array.  Primarily used for loading, saving, and output
	 * @param s a string of words, one on each line
	 * @return a list of words
	 */
	public static ArrayList<String> stringToArray (String s) {
		ArrayList<String> list = new ArrayList<String>();
		Scanner parse = new Scanner (s);
		parse.useDelimiter("\n");
		while (parse.hasNext()) {
			String next = parse.next();
			System.out.println (next);
			if (next.length() > 0){ 
				list.add(next);
			}
		}
		return (list);
	}
	
	/**
	 * This is the Comparator class used to sort words by length
	 * @author Sad Panda Software
	 * @version 1.0
	 */
	public static class SortByLineLength implements java.util.Comparator<String> {

		public int compare(String one, String two) {
			return (two.length() - one.length());
		}
	}
}
