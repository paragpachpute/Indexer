package umass.searchengine.indexer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import umass.searchengine.model.CorpusStatistics;
import umass.searchengine.model.InvertedIndex;
import umass.searchengine.model.Lookup;
import umass.searchengine.model.LookupTable;

public class AuxiliaryTableCreator {

	/**
	 * Creates lookup table containing information like document frequency and
	 * collection frequency for each term. Offset values is not set here.
	 * 
	 * @param invertedIndex
	 * @return LookupTable
	 */
	public static LookupTable createLookupTable(InvertedIndex invertedIndex) {
		LookupTable lookupTable = new LookupTable();
		for (String term : invertedIndex.getUniqueWords()) {
			int docFreq = invertedIndex.get(term).size();
			int collectionFreq = invertedIndex.get(term).getPostingsList().stream().map(p -> p.getTermFreq()).reduce(0,
					Integer::sum);

			Lookup l = new Lookup();
			l.setCollectionFreq(collectionFreq);
			l.setDocFreq(docFreq);
			lookupTable.put(term, l);
		}
		return lookupTable;
	}

	public static CorpusStatistics createStatsTable(InvertedIndex invertedIndex) {
		CorpusStatistics stats = new CorpusStatistics();
		stats.setNumOfUniqueTerms(invertedIndex.size());

		Map<Integer, Integer> docLengths = new HashMap<>();
		int totalTerms = 0;
		Set<Integer> sceneNums = new HashSet<>();
		for (String term : invertedIndex.getUniqueWords()) {
			totalTerms += invertedIndex.get(term).getCollectionTermFreq();
			invertedIndex.get(term).getPostingsList().stream().forEach(p -> {
				sceneNums.add(p.getSceneNum());
				if (docLengths.containsKey(p.getSceneNum()))
					docLengths.put(p.getSceneNum(), docLengths.get(p.getSceneNum()) + p.getPositions().size());
				else
					docLengths.put(p.getSceneNum(), p.getPositions().size());
			});
		}
		stats.setNumOfTerms(totalTerms);
		stats.setNumOfDocs(sceneNums.size());
		stats.setDocLengths(docLengths);
		return stats;
	}
}
