package umass.searchengine.ranking;

public interface Scorer {
	
	/**
	 * function that calculates score of current query term using some ranking algorithm
	 * @param ni number of documents that contains this query term
	 * @param fi number of times the query term has occurred in the document id i
	 * @param qfi number of times the query term has occurred in the given query
	 * @param cQi number of times query term q has occurred in the collection
	 * @param dl length of the current document
	 * @return returns score based on current ranking algorithm
	 */
	double score(int ni, int fi, int qfi, int cQi, int dl);
	
	/**
	 * Specifies whether the output of the score functions is log scaled
	 * @return boolean true if output of score function is log scaled
	 */
	boolean isOutputLogScaled();

}
