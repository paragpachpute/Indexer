package umass.searchengine.query;

import java.util.List;

import umass.searchengine.model.InvertedIndex;
import umass.searchengine.model.LookupTable;
import umass.searchengine.model.Posting;

public class DiceCoeff {

	public String getMostSimilarWord(InvertedIndex invertedIndex, LookupTable lookup, String a) {
		double maxDice = -1;
		String maxDiceWord = "";
		for (String b : invertedIndex.getUniqueWords()) {
			int Nab = 0;
			for (Posting aPostings : invertedIndex.get(a).getPostingsList()) {
				for (Posting bPostings : invertedIndex.get(b).getPostingsList()) {
					if (aPostings.getSceneNum() == bPostings.getSceneNum())
						Nab += getAdjecentcyScore(aPostings.getPositions(), bPostings.getPositions());
				}
			}
			double diceCoeff = 1.0 * Nab / (lookup.get(a).getCollectionFreq() + lookup.get(b).getCollectionFreq());
			if (diceCoeff > maxDice) {
//				System.out.println("Better Word: " + b + ", score: " + Nab);
				maxDice = diceCoeff;
				maxDiceWord = b;
			}
		}
		return maxDiceWord;
	}
	
	public int getAdjecentcyScore(List<Integer> aPositions, List<Integer> bPositions) {
		return (int) aPositions.stream().map(i -> i + 1).filter(bPositions :: contains).count();
	}
}
