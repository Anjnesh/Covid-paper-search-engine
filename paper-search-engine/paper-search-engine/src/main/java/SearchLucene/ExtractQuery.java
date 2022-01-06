package SearchLucene;

import Classes.Query;
import PreProcessData.WordNormalizer;
import PreProcessData.WordTokenizer;

import java.util.ArrayList;

public class ExtractQuery {

	ArrayList<Query> queries;

	int idx = 0;

	public ExtractQuery(String query){
		queries = new ArrayList<>();
		Query aQuery=new Query();
		aQuery.SetQueryContent(query);
		aQuery.SetQueryContent(queryNormalizer(aQuery.GetQueryContent()));
		queries.add(aQuery);
	}
	public ExtractQuery() {
		// you should extract the 4 queries from the Path.TopicDir
		// NT: the query content of each topic should be 1) tokenized, 2) to
		// lowercase, 3) remove stop words, 4) stemming
		// NT: you can simply pick up title only for query, or you can also use
		// title + description + narrative for the query content.
		queries = new ArrayList<>();
		Query aQuery = new Query();
		aQuery.SetTopicId("1");
		aQuery.SetQueryContent("covid 19");
		aQuery.SetQueryContent(queryNormalizer(aQuery.GetQueryContent()));
		queries.add(aQuery);
		aQuery = new Query();
		aQuery.SetTopicId("2");
		aQuery.SetQueryContent("cancer surgery");
		aQuery.SetQueryContent(queryNormalizer(aQuery.GetQueryContent()));
		queries.add(aQuery);
		aQuery = new Query();
		aQuery.SetTopicId("3");
		aQuery.SetQueryContent("academy 19 20");
		aQuery.SetQueryContent(queryNormalizer(aQuery.GetQueryContent()));
		queries.add(aQuery);
		aQuery = new Query();
		aQuery.SetTopicId("4");
		aQuery.SetQueryContent("progress dysphagia");
		aQuery.SetQueryContent(queryNormalizer(aQuery.GetQueryContent()));
		queries.add(aQuery);
	}
	public String queryNormalizer(String content){
		WordTokenizer tokenizer = new WordTokenizer(content.toCharArray());
		WordNormalizer normalizer = new WordNormalizer();
		char[] word=null;
		StringBuilder result=new StringBuilder();
		while ((word = tokenizer.nextWord()) != null) {
			// each word is transformed into lowercase
			word = normalizer.lowercase(word);
			result.append(normalizer.stem(word) + " ");
			//stemmed format of each word is written into result file

		}
		return result.toString();
	}

	public boolean hasNext() {
		if (idx == queries.size()) {
			return false;
		} else {
			return true;
		}
	}

	public Query next() {
		return queries.get(idx++);
	}

}
