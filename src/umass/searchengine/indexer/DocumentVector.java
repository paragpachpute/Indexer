package umass.searchengine.indexer;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import umass.searchengine.model.CorpusStatistics;

public class DocumentVector {
	
	private Map<String, Double> termCounts;
	private int docId;
	
	/**
	 * @param docId
	 */
	public DocumentVector(int docId) {
		super();
		this.termCounts = new HashMap<>();
		this.docId = docId;
	}

	/**
	 * @param termCounts
	 * @param docId
	 */
	public DocumentVector(Map<String, Double> termCounts, int docId) {
		super();
		this.termCounts = termCounts;
		this.docId = docId;
	}

	public void addTerm(String termName, int termCount) {
		this.termCounts.put(termName, termCounts.getOrDefault(termName, 0.0) + termCount);
	}
	
	public double getTermCount(String termName) {
		return this.termCounts.getOrDefault(termName, 0.0);
	}
	
	public Collection<Double> getAllTermCount() {
		return this.termCounts.values();
	}
	
	public int getDocId() {
		return this.docId;
	}
	
	public Set<String> getTerms() {
		return termCounts.keySet();
	}
	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + docId;
		result = prime * result + ((termCounts == null) ? 0 : termCounts.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocumentVector other = (DocumentVector) obj;
		if (docId != other.docId)
			return false;
		if (termCounts == null) {
			if (other.termCounts != null)
				return false;
		} else if (!termCounts.equals(other.termCounts))
			return false;
		return true;
	}

}
