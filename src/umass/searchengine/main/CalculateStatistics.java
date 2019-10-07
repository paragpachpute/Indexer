package umass.searchengine.main;

import java.io.IOException;
import java.util.Map;

import umass.searchengine.indexer.AuxiliaryTableCreator;
import umass.searchengine.indexer.IndexCreator;
import umass.searchengine.loader.DatasetLoader;
import umass.searchengine.model.Corpus;
import umass.searchengine.model.CorpusStatistics;
import umass.searchengine.model.InvertedIndex;
import umass.searchengine.utils.IndexerUtils;

public class CalculateStatistics {

	public static void main(String[] args) throws IOException {
		Corpus corpus = new DatasetLoader().load();
		InvertedIndex invertedIndex = IndexCreator.create(corpus);

		CorpusStatistics stats = AuxiliaryTableCreator.createStatsTable(invertedIndex);

		System.out.println("Total words in the collection: " + stats.getNumOfTerms());
		System.out.println("Unique words in the collection: " + stats.getNumOfUniqueTerms());
		System.out.println("Scenes in the collection: " + stats.getNumOfDocs());

		System.out.println("Average length of scene: " + (1.0 * stats.getNumOfTerms() / stats.getNumOfDocs()));
		Map.Entry<Integer, Integer> shortestScene = invertedIndex.getLengthOfScenes().entrySet().stream()
				.min((entry1, entry2) -> entry1.getValue() - entry2.getValue()).get();
		System.out.println("shortest scene: " + corpus.getSceneNameFromNumber(shortestScene.getKey()) + "="
				+ shortestScene.getValue());

		Map<String, Integer> playWordCountMap = IndexerUtils.getPlayWordCountMap(corpus, invertedIndex);
		System.out.println("shortest play: " + playWordCountMap.entrySet().stream()
				.min((entry1, entry2) -> entry1.getValue() - entry2.getValue()).get());
		System.out.println("longest play: " + playWordCountMap.entrySet().stream()
				.max((entry1, entry2) -> entry1.getValue() - entry2.getValue()).get());
	}

}
