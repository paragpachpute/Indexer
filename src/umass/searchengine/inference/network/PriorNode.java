package umass.searchengine.inference.network;

import java.io.IOException;

import umass.searchengine.indexer.InvertedIndex;
import umass.searchengine.model.ProbabiltyDistribution;

public class PriorNode implements QueryNode {
	
	InvertedIndex index;
	ProbabiltyDistribution probType;

	/**
	 * @param index
	 * @param probType
	 */
	public PriorNode(InvertedIndex index, ProbabiltyDistribution probType) {
		super();
		this.index = index;
		this.probType = probType;
	}

	@Override
	public Double score(int docId) {
		try {
			return Math.log(this.index.getPriorForDocument(docId, this.probType));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int nextCandidate() {
		return Integer.MAX_VALUE;
	}

	@Override
	public void skipTo(int docId) {
		// do nothing
	}

	@Override
	public boolean hasNext() {
		return false;
	}

}
