package umass.searchengine.inference.network.proximity;

import umass.searchengine.model.CorpusStatistics;
import umass.searchengine.model.PostingList;
import umass.searchengine.ranking.Scorer;

public class TermNode extends ProximityNode {
	
	private String term;

	public TermNode(String term, PostingList plist, CorpusStatistics stats, Scorer scorer) {
		super(plist, stats, scorer);
		this.term = term;
	}

}
