package umass.searchengine.main;

import java.io.IOException;
import java.util.List;

import umass.searchengine.indexer.AuxiliaryTableCreator;
import umass.searchengine.indexer.IndexCreator;
import umass.searchengine.indexer.InvertedIndex;
import umass.searchengine.loader.DatasetLoader;
import umass.searchengine.model.Corpus;
import umass.searchengine.model.CorpusStatistics;
import umass.searchengine.model.DocumentScore;
import umass.searchengine.query.DocumentAtATime;
import umass.searchengine.query.Query;
import umass.searchengine.ranking.BM25;
import umass.searchengine.ranking.Dirichlet;
import umass.searchengine.ranking.JelinekMercer;
import umass.searchengine.ranking.Scorer;
import umass.searchengine.utils.FileUtils;

public class RankingAlgorithms {

	public static void main(String[] args) throws IOException {
		Corpus corpus = new DatasetLoader().load();
		InvertedIndex invertedIndex = new IndexCreator().createInvertedIndex(corpus);
		CorpusStatistics stats = AuxiliaryTableCreator.createStatsTable(invertedIndex);

		String tokensFileName = "./src/data/ranking_queries";
		List<String> queryTermLines = FileUtils.readLines(tokensFileName);
		double avdl = stats.getDocLengths().values().stream().mapToDouble(d -> d).average().getAsDouble();

		BM25 bm25 = new BM25(1.2, 1.0, 0.75, avdl, stats.getDocLengths().size());
		JelinekMercer jm = new JelinekMercer(0.2, (int) stats.getNumOfTerms());
		Dirichlet dirichlet = new Dirichlet(1500, (int) stats.getNumOfTerms());

		Scorer[] scorers = new Scorer[] { bm25, jm, dirichlet };
		String[] scorersName = new String[] { "ppachpute-bm25-1.2-1.0-0.75", "ppachpute-ql-jm-0.2", "ppachpute-ql-dir-1500" };
		String[] fileNames = new String[] { "bm25.trecrun", "ql-jm.trecrun", "ql-dir.trecrun" };

		for (int num = 0; num < scorers.length; num++) {
			StringBuffer fileContents = new StringBuffer();
			Query query = new DocumentAtATime(scorers[num]);
			for (int i = 0; i < queryTermLines.size(); i++) {
				String line = queryTermLines.get(i);
				List<DocumentScore> scores = query.query(invertedIndex, line.split(" "), stats, 10);
				System.out.println("query: " + line);
				for (int output = 0; output < scores.size(); output++) {
					DocumentScore docScore = scores.get(output);
					String resultLine = String.format("Q%d %s %-35s %d %f %s", i + 1, "skip",
							corpus.getSceneNameFromNumber(docScore.getDocId()), output + 1, docScore.getScore(),
							scorersName[num]);
					System.out.println(resultLine);
					fileContents.append(resultLine);
					fileContents.append("\n");

				}
			}
			FileUtils.writeLines(fileNames[num], fileContents);
		}
	}

}
