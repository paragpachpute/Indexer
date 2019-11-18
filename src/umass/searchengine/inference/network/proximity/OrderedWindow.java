package umass.searchengine.inference.network.proximity;

import java.util.ArrayList;
import java.util.List;

import umass.searchengine.model.CorpusStatistics;
import umass.searchengine.model.InvertedIndex;
import umass.searchengine.model.Posting;
import umass.searchengine.ranking.Scorer;

public class OrderedWindow extends WindowNode {
	
	public OrderedWindow(List<String> terms, InvertedIndex index, CorpusStatistics stats, int windowSize, Scorer scorer) {
		super(terms, index, stats, windowSize, scorer);
	}

	@Override
	protected List<Integer> getAllWindows(int windowSize, List<Posting> documentPostings) {
		List<Integer> windowPositions = new ArrayList<>();
		int previousTermPosition = documentPostings.get(0).nextCandidate();
		
		while (!isAnyPositionListConsumed(documentPostings, previousTermPosition)) {
			boolean validWindow = true;
			previousTermPosition = documentPostings.get(0).nextCandidate();
			
			// Check if the window exists pair wise
			for (Posting p : documentPostings) {
				int currentTermPosition = p.currentPosition();
				
				if (currentTermPosition < previousTermPosition)
					currentTermPosition = p.skipTo(previousTermPosition);
				
				if (currentTermPosition == -1 || Math.abs(currentTermPosition - previousTermPosition) > windowSize) {
					validWindow = false;
					break;
				}
				previousTermPosition = currentTermPosition;
			}
			
			if (validWindow) {
				windowPositions.add(previousTermPosition);
			}
		}
		return windowPositions;
	}

}
