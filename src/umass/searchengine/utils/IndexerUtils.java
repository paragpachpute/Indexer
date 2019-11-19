package umass.searchengine.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import umass.searchengine.indexer.InvertedIndex;
import umass.searchengine.model.Corpus;

public class IndexerUtils {

	public static boolean compare(InvertedIndex original, InvertedIndex uncompressed) {
		if (original.size() != uncompressed.size())
			return false;
		
		for (String term : original.getUniqueWords()) {
			if (!original.get(term).equals(uncompressed.get(term))) {
				return false;
			}
		}
		return true;
	}
	
	public static Map<String, Integer> getPlayWordCountMap(Corpus corpus, InvertedIndex invertedIndex) {
		Map<String, List<Integer>> playSceneMap = corpus.getScenesForPlay();
		Map<Integer, Integer> sceneLengths = invertedIndex.getLengthOfScenes();
		
		Map<String, Integer> playWordCountMap = new HashMap<>();
		for (String play : playSceneMap.keySet()) {
			playSceneMap.get(play).forEach(sceneNum -> playWordCountMap.merge(play, sceneLengths.get(sceneNum), Integer::sum));
		}
		return playWordCountMap;
		
	}
	
	public static int getSizeOfIndexerInBytes(InvertedIndex invertedIndex) {
		int size = 0;
		int sizeOfInt = 4;
		for (String word : invertedIndex.getUniqueWords()) {
			// size of all posting lists
			size += invertedIndex.get(word).getCollectionTermFreq() * sizeOfInt;
			// size of docId and TF for given doc
			size += invertedIndex.get(word).getDocumentFreq() * 2 * sizeOfInt;
			// size of DF of term
			size += sizeOfInt;
		}
		return size;
		
	}
}
