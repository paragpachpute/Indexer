package umass.searchengine.model;

public class CorpusStatistics {
	
	private long numOfDocs;
	private long numOfTerms;
	private long numOfUniqueTerms;
	
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

}
