package umass.searchengine.main;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import umass.searchengine.indexer.AuxiliaryTableCreator;
import umass.searchengine.indexer.IndexCreator;
import umass.searchengine.indexer.InvertedIndex;
import umass.searchengine.loader.DatasetLoader;
import umass.searchengine.model.LookupTable;
import umass.searchengine.query.DiceCoeff;
import umass.searchengine.utils.FileUtils;

public class CalculateDice {

	public static void main(String[] args) throws IOException {
		InvertedIndex invertedIndex = new IndexCreator().createInvertedIndex(new DatasetLoader().load());
		LookupTable lookup = AuxiliaryTableCreator.createLookupTable(invertedIndex);
		List<String> termsList = invertedIndex.getUniqueWords().stream().collect(Collectors.toList());
		int totalWords = termsList.size();
		Random random = new Random();

		StringBuffer output7Tokens = new StringBuffer();
		StringBuffer output14Tokens = new StringBuffer();
		DiceCoeff diceCoeff = new DiceCoeff();
		
		for (int i = 0; i < 100; i++) {
			Set<String> uniqueTerms = new HashSet<>();
			while (uniqueTerms.size() != 7) {
				uniqueTerms.add(termsList.get(random.nextInt(totalWords)));
			}
			for (String term : uniqueTerms) {
				output7Tokens.append(term + " ");
				output14Tokens.append(term + " ");
				String nearestWord = diceCoeff.getMostSimilarWord(invertedIndex, lookup, term);
				output14Tokens.append(nearestWord + " ");
				System.out.println(String.format("Nearest word of: %s is: %s", term, nearestWord));
			}
			output7Tokens.append("\n");
			output14Tokens.append("\n");
		}
		
		String fileName = "7_tokens";
		FileUtils.writeLines(fileName, output7Tokens);
		fileName = "14_tokens";
		FileUtils.writeLines(fileName, output14Tokens);

	}

}
