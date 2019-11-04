package umass.searchengine.model;

import java.util.Map;

public class CorpusStatistics {
	
	private long numOfDocs;
	private long numOfTerms;
	private long numOfUniqueTerms;
	private Map<Integer, Integer> docLengths;
	
	/**
	 * @return the numOfDocs
	 */
	public long getNumOfDocs() {
		return numOfDocs;
	}
	/**
	 * @param numOfDocs the numOfDocs to set
	 */
	public void setNumOfDocs(long numOfDocs) {
		this.numOfDocs = numOfDocs;
	}
	/**
	 * @return the numOfTerms
	 */
	public long getNumOfTerms() {
		return numOfTerms;
	}
	/**
	 * @param numOfTerms the numOfTerms to set
	 */
	public void setNumOfTerms(long numOfTerms) {
		this.numOfTerms = numOfTerms;
	}
	/**
	 * @return the numOfUniqueTerms
	 */
	public long getNumOfUniqueTerms() {
		return numOfUniqueTerms;
	}
	/**
	 * @param numOfUniqueTerms the numOfUniqueTerms to set
	 */
	public void setNumOfUniqueTerms(long numOfUniqueTerms) {
		this.numOfUniqueTerms = numOfUniqueTerms;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CorpusStatistics [numOfDocs=" + numOfDocs + ", numOfTerms=" + numOfTerms + ", numOfUniqueTerms="
				+ numOfUniqueTerms + "]";
	}
	/**
	 * @return the docLengths
	 */
	public Map<Integer, Integer> getDocLengths() {
		return docLengths;
	}
	/**
	 * @param docLengths the docLengths to set
	 */
	public void setDocLengths(Map<Integer, Integer> docLengths) {
		this.docLengths = docLengths;
	}

}
