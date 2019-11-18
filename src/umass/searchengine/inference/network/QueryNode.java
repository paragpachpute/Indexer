package umass.searchengine.inference.network;

public interface QueryNode {

	/**
	 * Returns score for the given document. Returned score is in natural log scale
	 * @param docId document of which score is to be calculated
	 * @return score
	 */
	public Double score(int docId);
	
	/**
	 * Advances to the next document
	 * @return next candidate id
	 */
	public int nextCandidate();
	
	/**
	 * skips each child to the specified document
	 * @param docId document id to skip to
	 */
	public void skipTo(int docId);
	
	/**
	 * checks if there are any documents which are yet to be scored
	 * @return true if there are documents left else false
	 */
	public boolean hasNext();
}
