package umass.searchengine.inference.network.proximity;

import java.util.List;

import umass.searchengine.indexer.InvertedIndex;
import umass.searchengine.model.CorpusStatistics;
import umass.searchengine.model.Posting;
import umass.searchengine.ranking.Scorer;

public class BooleanAnd extends UnOrderedWindow {

	public BooleanAnd(List<String> terms, InvertedIndex index, CorpusStatistics stats, int windowSize, Scorer scorer) {
		super(terms, index, stats, windowSize, scorer);
	}

	@Override
	protected List<Integer> getAllWindows(int windowSize, List<Posting> documentPostings) {
		// Just pass the window size as the size of the document
		return super.getAllWindows(
				this.stats.getDocLengths().get(documentPostings.get(0).getDocumentId()), 
				documentPostings
			);
	}
	

}
