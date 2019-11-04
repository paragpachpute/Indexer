package umass.searchengine.query;

import java.util.List;

import umass.searchengine.model.CorpusStatistics;
import umass.searchengine.model.DocumentScore;
import umass.searchengine.model.InvertedIndex;

public interface Query {
	
	/**
	 * Calculate score of the query terms with respect to document collection
	 * @param invertedIndex data structure efficiently representing document collection
	 * @param queryTerms array of query terms to evaluate for
	 * @param lookup table it contains some stats about the collection
	 * @param k number of relevant documents to return
	 * @return list of scores for top k documents along with their document ids
	 */
	List<DocumentScore> query(InvertedIndex invertedIndex, String[] queryTerms, CorpusStatistics stats, int k);

}
