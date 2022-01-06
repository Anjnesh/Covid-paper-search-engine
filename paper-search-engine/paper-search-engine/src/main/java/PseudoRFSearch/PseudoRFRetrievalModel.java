package PseudoRFSearch;

import Classes.Document;
import Classes.Query;
import IndexingLucene.MyIndexReader;
import SearchLucene.QueryRetrievalModel;

import java.util.*;
//import Search.*;

public class PseudoRFRetrievalModel {

	class DocScore{
		private int docId;
		private double docScore;

		DocScore(int docId,double docScore){
			this.docId=docId;
			this.docScore=docScore;
		}
		public int getId(){
			return docId;
		}
		public  double getScore(){
			return docScore;
		}
	}
	// comparator for sorting the doc based on score descending order
	private class DocScoreComparator implements Comparator<DocScore> {
		public int compare(DocScore arg0, DocScore arg1) {
			if (arg0.docScore != arg1.docScore)
				return arg0.docScore < arg1.docScore ? 1 : -1;
			else
				return 0;
		}
	}

	MyIndexReader ixreader;

	// map for storing the pair of <docId, <term, term_frequency>>
	HashMap<Integer,HashMap<String,Integer>> queryDoc;
	// map for storing the pair of <term, collection_frequency>
	HashMap<String, Long>	termFreq;

	//set the parameter  mu of Dirichlet Smoothing
	private double miu=2000.0;
	private long colLen;

	public PseudoRFRetrievalModel(MyIndexReader ixreader)
	{

		this.ixreader=ixreader;
		colLen=this.ixreader.CollectionLength();
	}
	
	/**
	 * Search for the topic with pseudo relevance feedback in 2017 spring assignment 4. 
	 * The returned results (retrieved documents) should be ranked by the score (from the most relevant to the least).
	 * 
	 * @param aQuery The query to be searched for.
	 * @param TopN The maximum number of returned document
	 * @param TopK The count of feedback documents
	 * @param alpha parameter of relevance feedback model
	 * @return TopN most relevant document, in List structure
	 */
	public List<Document> RetrieveQuery( Query aQuery, int TopN, int TopK, double alpha) throws Exception {	
		// this method will return the retrieval result of the given Query, and this result is enhanced with pseudo relevance feedback
		// (1) you should first use the original retrieval model to get TopK documents, which will be regarded as feedback documents
		// (2) implement GetTokenRFScore to get each query token's P(token|feedback model) in feedback documents
		// (3) implement the relevance feedback model for each token: combine the each query token's original retrieval score P(token|document) with its score in feedback documents P(token|feedback model)
		// (4) for each document, use the query likelihood language model to get the whole query's new score, P(Q|document)=P(token_1|document')*P(token_2|document')*...*P(token_n|document')
		
		
		//get P(token|feedback documents)
		HashMap<String,Double> TokenRFScore=GetTokenRFScore(aQuery,TopK);

		// sort all retrieved documents from most relevant to least, and return TopN
		List<Document> results = new ArrayList<Document>();

		String[] tokens = aQuery.GetQueryContent().split(" ");
		List<DocScore> scoreRes = new ArrayList<>();
		queryDoc.forEach((docid, tf) -> {
			int doclen = 0;
			double score = 1.0;
			try {
				doclen = this.ixreader.docLength(docid);
			} catch (Exception e) {
			} ;
			// Dirichlet piror smoothing
			// P(qi|D)=(docLen/(docLen+miu))*(dtf/docLen)+(miu/(docLen+miu))*(ctf/colLen)
			// P_final=alpha * P(qi|D) + (1-alpha) * P(qi|feedback Documents)
			double part1 = doclen / (doclen + miu);
			double part2 = miu / (doclen + miu);
			for (String token : tokens) {
				long cf = termFreq.get(token);

				if (cf == 0)
					continue;
				int dtf = tf.getOrDefault(token, 0);
				double p_doc = (double) dtf / doclen; // c(w, D)
				double p_ref = (double) cf / colLen; // p(w|REF)
				score *= (alpha*(part1 * p_doc + part2 * p_ref)+(1-alpha)*TokenRFScore.get(token));
			}

			scoreRes.add(new DocScore(docid,score));
		});

		// sort by score decreasing
		Collections.sort(scoreRes, new DocScoreComparator());

		for (int i = 0; i < TopN; i++) {
			DocScore ds = scoreRes.get(i);
			Document doc = null;
			try {
				int id = ds.getId();
				doc = new Document(Integer.toString(id), ixreader.getDocno(id), ds.getScore());
			} catch (Exception e) {
			} ;
			results.add(doc);
		}
		return results;
	}
	
	public HashMap<String,Double> GetTokenRFScore(Query aQuery,  int TopK) throws Exception
	{
		// for each token in the query, you should calculate token's score in feedback documents: P(token|feedback documents)
		// use Dirichlet smoothing
		// save <token, score> in HashMap TokenRFScore, and return it
		HashMap<String,Double> TokenRFScore=new HashMap<String,Double>();

		String[] tokens = aQuery.GetQueryContent().split(" ");
		List<Document> feedbackDocs = new QueryRetrievalModel(ixreader).retrieveQuery(aQuery, TopK);

		queryDoc=new HashMap<>();
		termFreq=new HashMap<>();

		for(String tok:tokens){
			long ctf = ixreader.CollectionFreq(tok);
			termFreq.put(tok,ctf);
			if(ctf==0)	continue;
			int[][] postingList = ixreader.getPostingList(tok);
			for(int[] posting: postingList){
				if(!queryDoc.containsKey(posting[0])){
					HashMap<String, Integer>tf = new HashMap<>();
					tf.put(tok,posting[1]);
					queryDoc.put(posting[0],tf);
				}else{
					queryDoc.get(posting[0]).put(tok,posting[1]);
				}
			}
		}

		int psedoLen=0;
		// map for storing the pair of <term, term frequency in feedbackDoc>
		Map<String,Integer> pseudo = new HashMap<>();
		for(Document doc : feedbackDocs){
			queryDoc.get(Integer.parseInt(doc.docid())).forEach((term,tf)->{
				if(pseudo.containsKey(term)){
					pseudo.put(term,tf+pseudo.get(term));
				}else{
					pseudo.put(term,tf);
				}

			});
			psedoLen+=ixreader.docLength(Integer.parseInt(doc.docid()));
		}

		// Dirichlet piror smoothing
		// P=(pseudoLen/(pseudoLen+miu))*(tf/pseudoLen)+(miu/(pseudoLen+miu))*(ctf/colLen)
		final int pseudoLength=psedoLen;
		double part1 = psedoLen / (psedoLen + miu);
		double part2 = miu / (psedoLen + miu);
		pseudo.forEach((token,tf)->{
			long ctf=termFreq.get(token);
			double p_doc = (double) tf/pseudoLength; // c(W, D)
			double p_ref = (double) ctf/colLen; // p(w|REF)
			double score = part1*p_doc + part2 * p_ref;
			TokenRFScore.put(token,score);
		});

		return TokenRFScore;
	}

	
	
}