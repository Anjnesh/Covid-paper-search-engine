package PreProcessData;
import Classes.Stemmer;

/**
 * This is for INFSCI 2140 in 2021
 *
 */
public class WordNormalizer {
	//you can add essential private methods or variables

	// YOU MUST IMPLEMENT THIS METHOD
	public char[] lowercase( char[] chars ) {
		//transform the uppercase characters in the word to lowercase
		int n = chars.length;
		if(n==0) {return chars;}
		char lowerChar[] = new char[n];
		//transform the char in the char to lowercase array one by one
		for(int i=0;i<chars.length;i++) {
			lowerChar[i] = Character.toLowerCase(chars[i]);;
		}
		return lowerChar;
	}

	public String stem(char[] chars)
	{
		//use the stemmer in Classes package to do the stemming on input word, and return the stemmed word
		Stemmer s = new Stemmer();
		char[] word=chars;
		s.add(word, word.length);
		s.stem();
		String str=s.toString();
		return str;
	}

}
