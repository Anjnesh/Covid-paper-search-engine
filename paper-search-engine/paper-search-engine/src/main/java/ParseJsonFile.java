import Classes.Path;
import PreProcessData.JsonCollection;
import PreProcessData.StopWordRemover;
import PreProcessData.WordNormalizer;
import PreProcessData.WordTokenizer;

import java.io.FileWriter;
import java.util.Map;


public class ParseJsonFile {

	public static void main(String[] args) throws Exception {
		
		long startTime=System.currentTimeMillis(); //star time of running code

		ParseJsonFile pj = new ParseJsonFile();
		pj.PreProcess();
		
		long endTime=System.currentTimeMillis(); //end time of running code
		System.out.println("corpus running time: "+(endTime-startTime)/60000.0+" min");
	}
	
	public void PreProcess() throws Exception {
		// Loading the collection file and initiate the DocumentCollection class

		JsonCollection corpus = new JsonCollection();

		// loading stopword list and initiate the StopWordRemover and WordNormalizer class
		StopWordRemover stopwordRemover = new StopWordRemover();
		WordNormalizer normalizer = new WordNormalizer();

		// initiate the BufferedWriter to output result
		FileWriter wr = new FileWriter(Path.Result+"all");

		// initiate a doc object, which can hold document number and document content of a document
		Map<String, Object> doc = null;

		// process the corpus, document by document, iteractively
		int count=0;
		while (count<corpus.corpusLen) {
			doc = corpus.nextDocument();
			// load document number of the document
			String docno = doc.keySet().iterator().next();

			// load document content
			char[] content = (char[]) doc.get(docno);

			// write docno into the result file
			wr.append(docno + "\n");

			// initiate the WordTokenizer class
			WordTokenizer tokenizer = new WordTokenizer(content);

			// initiate a word object, which can hold a word
			char[] word = null;

			// process the document word by word iteratively
			while ((word = tokenizer.nextWord()) != null) {
				// each word is transformed into lowercase
				word = normalizer.lowercase(word);

				// filter out stopword, and only non-stopword will be written
				// into result file
				if (!stopwordRemover.isStopword(word))
					wr.append(normalizer.stem(word) + " ");
					//stemmed format of each word is written into result file
			}
			wr.append("\n");// finish processing one document
			count++;
			if(count%1000==0)
				System.out.println("finish "+count+" docs");
		}
		System.out.println("totaly document count:  "+count);
		wr.close();
	}

}
