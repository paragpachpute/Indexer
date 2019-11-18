package umass.searchengine.inference.network;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;

import umass.searchengine.model.DocumentScore;

public class InferenceNetwork {
	
	public List<DocumentScore> runQuery(QueryNode query, int k) {

		Queue<DocumentScore> priorityQueue = new PriorityQueue<>(k, scoreComparator);
		while (query.hasNext()) {
			int docId = query.nextCandidate();
			Double score = query.score(docId);
			if (score != null) {
				DocumentScore documentScore = new DocumentScore(docId, score);
				priorityQueue.add(documentScore);
			}
			
			if (priorityQueue.size() == k + 1) {
				// Removes the element having least score
				priorityQueue.remove();
			}
		}
		List<DocumentScore> docIds = priorityQueue.stream().sorted(scoreComparator).collect(Collectors.toList());
		Collections.reverse(docIds);
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
