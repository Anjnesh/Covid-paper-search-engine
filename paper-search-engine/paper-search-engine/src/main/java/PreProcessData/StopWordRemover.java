package PreProcessData;

import Classes.Path;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

public class StopWordRemover {
	//you can add essential private methods or variables.
	private HashSet<String> stopwordSet = new HashSet<String>();
	
	public StopWordRemover( ) throws IOException {
		// load and store the stop words from the fileinputstream with appropriate data structure
		// that you believe is suitable for matching stop words.
		// address of stopword.txt should be Path.StopwordDir
			
			FileInputStream fis = new FileInputStream(Path.StopwordDir);
			BufferedReader myReader = new BufferedReader(new InputStreamReader(fis));
			//store the stops words in a HashSet
			while (myReader.ready()) {
		        String data = myReader.readLine();
		        stopwordSet.add(data);
		        }
			myReader.close();
			
	}
	
	// YOU MUST IMPLEMENT THIS METHOD
	public boolean isStopword( char[] word ) {
		// return true if the input word is a stopword, or false if not
		String s = new String(word).toLowerCase();
		if(stopwordSet.contains(s)) {
			return true;
		}
		return false;
	}
	

}
