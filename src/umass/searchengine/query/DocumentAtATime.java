package umass.searchengine.query;

import java.util.Arrays;
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
import umass.searchengine.model.DocumentScore;
import umass.searchengine.model.InvertedIndex;
import umass.searchengine.model.Posting;
import umass.searchengine.ranking.Scorer;

public class DocumentAtATime implements Query {
	
	private Scorer scorer;

	/**
	 * @param scorer
	 * @param scoreComparator
	 */
	public DocumentAtATime(Scorer scorer) {
		super();
		this.scorer = scorer;
	}

	@Override
	public List<DocumentScore> query(InvertedIndex invertedIndex, String[] queryTerms, CorpusStatistics stats, int k) {
		Map<String, ListIterator<Posting>> L = new HashMap<>();
		for (String term : queryTerms) {
			L.put(term, invertedIndex.get(term).getPostingsList().listIterator());
		}
		
		Queue<DocumentScore> priorityQueue = new PriorityQueue<>(k, scoreComparator);
		for (int d = 0; d < stats.getNumOfDocs(); d++) {
			double docScore = 0;
			boolean scored = false;
			for (String term : L.keySet()) {
				int ni = invertedIndex.get(term).getDocumentFreq();
				int qfi = Collections.frequency(Arrays.asList(queryTerms), term);
				int cqi = invertedIndex.get(term).getCollectionTermFreq();
				int fi = 0;
				int dl = stats.getDocLengths().get(d);
				
				if (L.get(term).hasNext()) {
					Posting posting = L.get(term).next();
					if (posting.getSceneNum() == d) {
						scored = true;
						fi = posting.getTermFreq();
					} else {
						L.get(term).previous();
					}
				}
				docScore += scorer.score(ni, fi, qfi, cqi, dl);
			}
			DocumentScore documentScore = new DocumentScore(d, docScore);
			
			if (scored)
				priorityQueue.add(documentScore);
			
			if (priorityQueue.size() == k + 1) {
				// Removes the element having least score
				priorityQueue.remove();
			}
		}
		
//		priorityQueue.stream().forEach(ds -> System.out.println(ds.getDocId()+ ":" + ds.getScoreId()));
		List<DocumentScore> docIds = priorityQueue.stream().sorted(scoreComparator).collect(Collectors.toList());
		Collections.reverse(docIds);
//		docIds.stream().forEach(ds -> System.out.println(ds));
		return docIds;
		
	}
	
	Comparator<DocumentScore> scoreComparator = (d1, d2) -> {
		// Ascending order
		if (d1.getScore() > d2.getScore())
			return 1;
		else if (d1.getScore() < d2.getScore())
			return -1;
		else
			return 0;
	};

}
