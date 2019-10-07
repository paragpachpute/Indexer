package umass.searchengine.query;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;

import umass.searchengine.model.CorpusStatistics;
import umass.searchengine.model.InvertedIndex;
import umass.searchengine.model.Posting;

public class DocumentAtATime implements Query {

	@Override
	public List<Integer> query(InvertedIndex invertedIndex, String[] queryTerms, CorpusStatistics stats, int k) {
		Map<String, ListIterator<Posting>> L = new HashMap<>();
		for (String term : queryTerms) {
			L.put(term, invertedIndex.get(term).getPostingsList().listIterator());
		}

		Queue<DocumentScore> priorityQueue = new PriorityQueue<>(k, scoreComparator);
		for (int d = 1; d <= stats.getNumOfDocs(); d++) {
			int docScore = 0;
			for (String term : L.keySet()) {
				if (L.get(term).hasNext()) {
					Posting posting = L.get(term).next();
					if (posting.getSceneNum() == d) {
						docScore += posting.getTermFreq();
					} else {
						L.get(term).previous();
					}
				}
			}
			DocumentScore documentScore = new DocumentScore(d, docScore);
			priorityQueue.add(documentScore);
			if (priorityQueue.size() == k + 1) {
				// Removes the element having least score
				priorityQueue.remove();
			}
		}
		
//		priorityQueue.stream().forEach(ds -> System.out.println(ds.getDocId()+ ":" + ds.getScoreId()));
		List<Integer> docIds = priorityQueue.stream().sorted(scoreComparator).map(ds -> ds.getDocId()).collect(Collectors.toList());
		Collections.reverse(docIds);
//		docIds.stream().forEach(ds -> System.out.println(ds));
		return docIds;
		
	}
	
	Comparator<DocumentScore> scoreComparator = (d1, d2) -> {
		// Ascending order
		return d1.getScore() - d2.getScore();
	};
	
	private class DocumentScore {
		private int docId;
		private int score;
		
		public DocumentScore(int docId, int scoreId) {
			super();
			this.docId = docId;
			this.score = scoreId;
		}

		public int getDocId() {
			return docId;
		}

		public int getScore() {
			return score;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "DocumentScore [docId=" + docId + ", score=" + score + "]";
		}
	}

}
