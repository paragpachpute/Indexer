package umass.searchengine.cluster;

import java.util.HashSet;
import java.util.Set;

import umass.searchengine.indexer.DocumentVector;
import umass.searchengine.indexer.InvertedIndex;

public class CosineSimilarity {
	
	private InvertedIndex index;

	/**
	 * @param index
	 */
	public CosineSimilarity(InvertedIndex index) {
		super();
		this.index = index;
	}

	/**
	 * Calculates Cosine similarity between passed document first and second
	 * @param DocumentVector first representing a document
	 * @param DocumentVector second representing another document
	 * @param InvertedIndex index needed to get IDF score of a term
	 * @return cosine similarity
	 */
	public double calculate(DocumentVector first, DocumentVector second) {
		Set<String> termsPresentInBothDocs = new HashSet<>(first.getTerms());
		termsPresentInBothDocs.retainAll(second.getTerms());
		double sum = 0;
		double squaredSum1 = 0;
		double squaredSum2 = 0;
		
		for (String term : termsPresentInBothDocs) {
//			if (term.equals("sure")) {
//				System.out.println("yo");
//			}
//			if (term.equals("the")) {
//				System.out.println("yo");
//			}
			double tfIdfCurrentDoc = first.getTermCount(term) * index.getIdfOfTerm(term);
			double tfIdfVector = second.getTermCount(term) * index.getIdfOfTerm(term);
			sum +=  tfIdfCurrentDoc * tfIdfVector;
		}
		
		for (String term : first.getTerms()) {
			double tfIdfCurrentDoc = first.getTermCount(term) * index.getIdfOfTerm(term);
			squaredSum1 += tfIdfCurrentDoc * tfIdfCurrentDoc;
		}
		
		for (String term : second.getTerms()) {
			double tfIdfCurrentDoc = second.getTermCount(term) * index.getIdfOfTerm(term);
			squaredSum2 += tfIdfCurrentDoc * tfIdfCurrentDoc;
		}
		
		double ans = sum / Math.sqrt(squaredSum1 * squaredSum2);
		return ans;
	}
}
