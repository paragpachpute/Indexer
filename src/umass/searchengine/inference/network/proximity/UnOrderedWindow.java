package umass.searchengine.inference.network.proximity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import umass.searchengine.indexer.InvertedIndex;
import umass.searchengine.model.CorpusStatistics;
import umass.searchengine.model.Posting;
import umass.searchengine.ranking.Scorer;

public class UnOrderedWindow extends WindowNode {

	public UnOrderedWindow(List<String> terms, InvertedIndex index, CorpusStatistics stats, int windowSize, Scorer scorer) {
		super(terms, index, stats, windowSize, scorer);
	}

	@Override
	protected List<Integer> getAllWindows(int windowSize, List<Posting> documentPostings) {
		List<Integer> windowPositions = new ArrayList<>();
		int minPosId = 0;
		for (Posting p: documentPostings)
			p.skipTo(minPosId);
		
		Set<Integer> positions = new HashSet<>();
		for (Posting p: documentPostings) {
			// To handle the cases like "to be or not to be" where both the "to"s might point to same position
			while (positions.contains(p.currentPosition())){
				if (p.next() == -1)
					break;
			}
			positions.add(p.currentPosition());
		}
		
		while (!isAnyPositionListConsumed(documentPostings, minPosId) && positions.size() == documentPostings.size()) {
			minPosId = positions.stream().mapToInt(pos -> pos).min().getAsInt();
			int maxPosId = positions.stream().mapToInt(pos -> pos).max().getAsInt();
			if (maxPosId - minPosId < windowSize)
				windowPositions.add(minPosId);

			for (Posting p: documentPostings) {
				if (p.currentPosition() <= minPosId)
					p.skipTo(minPosId);
			}
			
			positions = new HashSet<>();
			for (Posting p: documentPostings) {
				// To handle the cases like "to be or not to be" where both the "to"s might point to same position
				while (positions.contains(p.currentPosition())) {
					if (p.next() == -1)
						break;
				}
				positions.add(p.currentPosition());
			}
		}
		return windowPositions;
	}

}
