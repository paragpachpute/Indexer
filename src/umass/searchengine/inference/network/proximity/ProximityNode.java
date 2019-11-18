package umass.searchengine.inference.network.proximity;

import umass.searchengine.inference.network.QueryNode;
import umass.searchengine.model.CorpusStatistics;
import umass.searchengine.model.Posting;
import umass.searchengine.model.PostingList;
import umass.searchengine.ranking.Dirichlet;
import umass.searchengine.ranking.Scorer;

public abstract class ProximityNode implements QueryNode {

	protected PostingList proximityNodePostingList;
	
	protected CorpusStatistics stats;
	
	protected Scorer scorer;
	
	/**
	 * Constructor
	 * @param CorpusStatistics corpus statistics
	 */
	public ProximityNode(CorpusStatistics stats, Scorer scorer) {
		super();
		this.stats = stats;
		this.scorer = scorer;
	}

	/**
	 * Constructor for proximity node
	 * @param PostingList posting list of the node
	 * @param CorpusStatistics corpus statistics
	 */
	public ProximityNode(PostingList plist, CorpusStatistics stats, Scorer scorer) {
		super();
		this.proximityNodePostingList = plist;
		this.proximityNodePostingList.beginIteration();
		this.stats = stats;
		this.scorer = scorer;
	}
	
	@Override
	public Double score(int docId) {
		int ni = 0;
		int cqi = proximityNodePostingList.getCollectionTermFreq();
		int fi = 0;
		int dl = stats.getDocLengths().get(docId);

		Posting posting = proximityNodePostingList.skipTo(docId);
		if (posting != null) {
			if (posting.getDocumentId() == docId) {
				ni = proximityNodePostingList.getDocumentFreq();
				fi = posting.getTermFreq();
			} else {
				proximityNodePostingList.goToPrevious();
			}
		}
		return scorer.score(ni, fi, 0, cqi, dl);
	}

	@Override
	public int nextCandidate() {
		return proximityNodePostingList.nextCandidate().getDocumentId();
	}

	@Override
	public void skipTo(int docId) {
		proximityNodePostingList.skipTo(docId);
	}

	@Override
	public boolean hasNext() {
		return proximityNodePostingList.hasNext();
	}

	public int getCurrentDocId() {
		return proximityNodePostingList.currentDocument().getDocumentId();
	}

}
