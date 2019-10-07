package umass.searchengine.main;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;

import umass.searchengine.model.InvertedIndex;
import umass.searchengine.model.LookupTable;
import umass.searchengine.utils.FileUtils;

public class Random7TermsAndStatistics {

	public static void main(String[] args) throws IOException {
		Assertions.assertEquals(2, args.length);
		String indexFileName = args[0];
		boolean isCompressed = Boolean.parseBoolean(args[1]);
		
		LookupTable lookupTable = FileUtils.readLookupTable(isCompressed);
		InvertedIndex invertedIndex = FileUtils.readIndex(isCompressed, indexFileName, lookupTable);
		
		List<String> termsList = invertedIndex.getUniqueWords().stream().collect(Collectors.toList());
		int totalWords = termsList.size();
		Random random = new Random();
		
		for (int i = 0; i < 100; i++) {
			Set<String> uniqueTerms = new HashSet<>();
			while (uniqueTerms.size() != 7) {
				uniqueTerms.add(termsList.get(random.nextInt(totalWords)));
			}
			for (String term : uniqueTerms) {
				int df = invertedIndex.get(term).getDocumentFreq();
				int tf = invertedIndex.get(term).getCollectionTermFreq();
				System.out.println(String.format("Word: %s, docFreq: %s, collectionTermFreq: %s", term, df, tf));
			}
		}

	}

}
