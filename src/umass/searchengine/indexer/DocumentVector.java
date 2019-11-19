package umass.searchengine.indexer;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DocumentVector {
	
	private Map<String, Integer> termCounts;
	private int docId;
	
	/**
	 * @param docId
	 */
	public DocumentVector(int docId) {
		super();
		this.docId = docId;
		this.termCounts = new HashMap<>();
	}

	public void addTerm(String termName, int termCount) {
		this.termCounts.put(termName, termCount);
	}
	
	public int getTermCount(String termName) {
		return this.termCounts.getOrDefault(termName, 0);
	}
	
	public Collection<Integer> getAllTermCount() {
		return this.termCounts.values();
	}
	
	public int getDocId() {
		return this.docId;
	}
	
	public Set<String> getTerms() {
		return termCounts.keySet();
	}
	
	/**
	 * Calculates Cosine distance between current document and passed document
	 * @param vector another document
	 * @return cosine distance
	 */
	public double calculateDistance(DocumentVector vector) {
		Set<String> termsPresentInBothDocs = new HashSet<>(getTerms());
		termsPresentInBothDocs.retainAll(vector.getTerms());
		int sum = 0;
		for (String term : termsPresentInBothDocs) {
			sum += getTermCount(term) * vector.getTermCount(term);
		}
		int squaredSum1 = this.getAllTermCount().stream().mapToInt(count -> count*count).sum();
		int squaredSum2 = vector.getAllTermCount().stream().mapToInt(count -> count*count).sum();
		return sum / Math.sqrt(squaredSum1 * squaredSum2);
	}

}
