package PreProcessData;

import java.util.Scanner;

/**
 * This is for INFSCI 2140 in 2021
 *
 * TextTokenizer can split a sequence of text into individual word tokens.
 */
public class WordTokenizer {
	//you can add essential private methods or variables

	private Scanner myReader = null;

	// YOU MUST IMPLEMENT THIS METHOD
	public WordTokenizer( char[] texts ) {
		// this constructor will tokenize the input texts (usually it is a char array for a whole document)
		String doc =new String(texts);
		//use scanner to scan the string
		myReader = new Scanner(doc);

	}

	// YOU MUST IMPLEMENT THIS METHOD
	public char[] nextWord() {
		// read and return the next word of the document
		// or return null if it is the end of the document
		if(myReader.hasNext()) {
			
			String data = myReader.next().replaceAll("([[^0-9]&&[^a-z]&&\\S]+)", "");
			return data.toCharArray();
		}
		else {
			return null;
		}
		

		
	}
	
//	public static void main(String[] args) {
//		String s = "Violence seems on the rise inEgypt as a latest sign that militant attacks may be intensified inthe most populous Arab";
//		char t[] = s.toCharArray();
//		WordTokenizer w = new WordTokenizer(t);
//		for(int i=0; i<10;i++) {
//		System.out.println(w.nextWord());}
//	}

}
