package umass.searchengine.main;

import java.io.IOException;
import java.util.List;

import umass.searchengine.indexer.AuxiliaryTableCreator;
import umass.searchengine.model.CorpusStatistics;
import umass.searchengine.model.InvertedIndex;
import umass.searchengine.model.LookupTable;
import umass.searchengine.query.DocumentAtATime;
import umass.searchengine.query.Query;
import umass.searchengine.utils.FileUtils;

public class CompressionHypothesisValidation {

	public static void main(String[] args) throws IOException {
		boolean compressed = false;
		for (int i = 1; i <= 2; i++) {
			System.out.println("Experiment with " + i * 7 + " tokens");
			for (int j = 0; j < 2; j ++) {
				System.out.println("Compressed: " + compressed);
				compressed = !compressed;
				String indexFileName = compressed ? "compressedIndex" : "uncompressedIndex";
				performExperiment(compressed, indexFileName, i * 7);
			}
		}
	}
	
	private static void performExperiment(boolean isCompressed, String indexFileName, int tokens) throws IOException {
		LookupTable lookupTable = FileUtils.readLookupTable(isCompressed);
		
		for (int i = 0; i < 2; i ++) {
			// Start timer for the experiment
			long start = System.nanoTime();
			
			InvertedIndex invertedIndex = FileUtils.readIndex(isCompressed, indexFileName, lookupTable);
			CorpusStatistics stats = AuxiliaryTableCreator.createStatsTable(invertedIndex);
			
			String tokensFileName = "./src/data/" + tokens + "_tokens";
			List<String> queryTermLines = FileUtils.readLines(tokensFileName);
			
			Query query = new DocumentAtATime();
			for (String line : queryTermLines) {
				query.query(invertedIndex, line.split(" "), stats, 5);
			}
			
			// Calculate total time taken in milli seconds
			long diff = System.nanoTime() - start;
			diff /= 1e6;
			
			System.out.println("Time taken: " + diff);
		}
	}

}
