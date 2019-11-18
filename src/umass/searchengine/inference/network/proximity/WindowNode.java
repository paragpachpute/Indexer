package umass.searchengine.inference.network.proximity;

import java.util.ArrayList;
import java.util.List;

import umass.searchengine.model.CorpusStatistics;
import umass.searchengine.model.InvertedIndex;
import umass.searchengine.model.Posting;
import umass.searchengine.model.PostingList;
import umass.searchengine.ranking.Scorer;

public abstract class WindowNode extends ProximityNode {

	public WindowNode(List<String> terms, InvertedIndex index, 
			CorpusStatistics stats, int windowSize, Scorer scorer) {
		super(stats, scorer);
		this.proximityNodePostingList = createPostingList(terms, index, windowSize);
	}

	public PostingList createPostingList(List<String> terms, InvertedIndex index, int windowSize) {
		List<PostingList> termsPostingList = new ArrayList<>();
		PostingList orderedWindowPostingList = new PostingList();

		// Get posting list for each term
		for (String term : terms) {
			PostingList plist = index.getCopy(term);
			plist.beginIteration();
			termsPostingList.add(plist);
		}

		int maxDocId = -1;
		while (!isAnyPostingListConsumed(termsPostingList, maxDocId)) {
			maxDocId = getMostLikelyDocument(termsPostingList, terms, maxDocId);
			List<Posting> documentPostings = new ArrayList<>();
			boolean isACandidate = doesDocumentContainAllterms(termsPostingList, terms, maxDocId, documentPostings);

			// Check if the candidate document contains the window
			if (isACandidate) {
				List<Integer> windowPositions = getAllWindows(windowSize, documentPostings);

				if (windowPositions.size() > 0) {
					System.out.println("Window found: " + maxDocId + ", " + windowPositions.size());
					Posting p = new Posting(maxDocId, windowPositions.size(), windowPositions);
					orderedWindowPostingList.add(p);
				}
			}
		}

		orderedWindowPostingList.beginIteration();
		return orderedWindowPostingList;
	}

	protected abstract List<Integer> getAllWindows(int windowSize, List<Posting> documentPostings);

	private boolean doesDocumentContainAllterms(List<PostingList> termsPostingList, List<String> terms, int docId,
			List<Posting> documentPostings) {
		for (int i = 0; i < terms.size(); i++) {
			PostingList list = termsPostingList.get(i);
			Posting p = list.currentDocument();

			if (p == null || p.getDocumentId() < docId) {
				p = list.skipTo(docId);
				if (p == null) {
					return false;
				}
			}

			p.beginIteration();
			documentPostings.add(p);
			if (p.getDocumentId() != docId) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Gets the most likely document. Likely here means the document that is more
	 * probable This is calculated by finding max document from all the next
	 * candidates of posting lists This is an optimization step. Same concept is
	 * used in conjunctive doc at a time algorithm
	 * 
	 * @param termsPostingList
	 * @param terms
	 * @return most likely document
	 */
	private int getMostLikelyDocument(List<PostingList> termsPostingList, List<String> terms, int previousMax) {
		int maxDocId = previousMax;
		for (int i = 0; i < terms.size(); i++) {
			PostingList list = termsPostingList.get(i);
			Posting p = list.currentDocument();

			if (p == null || maxDocId == -1 || i == 0 && p.getDocumentId() <= maxDocId) {
				p = list.nextCandidate();
				if (p == null)
					break;
			}
			int docId = p.getDocumentId();
			// check with max
			if (docId > maxDocId)
				maxDocId = docId;
		}
		return maxDocId;
	}

	private boolean isAnyPostingListConsumed(List<PostingList> lists, int prevMaxDocId) {
		for (PostingList list : lists) {
			if (list.currentDocument() != null && list.currentDocument().getDocumentId() <= prevMaxDocId
					&& !list.hasNext())
				return true;
		}
		return false;
	}

	protected boolean isAnyPositionListConsumed(List<Posting> lists, int previousTermPosition) {
		for (Posting list : lists) {
			if (list.currentPosition() <= previousTermPosition && !list.hasNext())
				return true;
		}
		return false;
	}

}
