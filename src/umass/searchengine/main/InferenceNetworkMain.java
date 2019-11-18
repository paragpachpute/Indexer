package umass.searchengine.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import umass.searchengine.indexer.AuxiliaryTableCreator;
import umass.searchengine.indexer.IndexCreator;
import umass.searchengine.inference.network.InferenceNetwork;
import umass.searchengine.inference.network.QueryNode;
import umass.searchengine.inference.network.belief.AndNode;
import umass.searchengine.inference.network.belief.MaxNode;
import umass.searchengine.inference.network.belief.OrNode;
import umass.searchengine.inference.network.belief.SumNode;
import umass.searchengine.inference.network.filter.FilterReject;
import umass.searchengine.inference.network.filter.FilterRequire;
import umass.searchengine.inference.network.proximity.OrderedWindow;
import umass.searchengine.inference.network.proximity.ProximityNode;
import umass.searchengine.inference.network.proximity.TermNode;
import umass.searchengine.inference.network.proximity.UnOrderedWindow;
import umass.searchengine.loader.DatasetLoader;
import umass.searchengine.model.Corpus;
import umass.searchengine.model.CorpusStatistics;
import umass.searchengine.model.DocumentScore;
import umass.searchengine.model.InvertedIndex;
import umass.searchengine.query.DocumentAtATime;
import umass.searchengine.query.Query;
import umass.searchengine.ranking.BM25;
import umass.searchengine.ranking.Dirichlet;
import umass.searchengine.ranking.JelinekMercer;
import umass.searchengine.ranking.Scorer;
import umass.searchengine.utils.FileUtils;

public class InferenceNetworkMain {

	public static void main(String[] args) throws IOException {
		Corpus corpus = new DatasetLoader().load();
		InvertedIndex invertedIndex = new IndexCreator().create(new DatasetLoader().load());
		CorpusStatistics stats = AuxiliaryTableCreator.createStatsTable(invertedIndex);
		
		InferenceNetwork network = new InferenceNetwork();
		Scorer scorer = new Dirichlet(1500, stats.getNumOfTerms());

		String tokensFileName = "./src/data/ranking_queries";
		List<String> queryTermLines = FileUtils.readLines(tokensFileName);

		StringBuffer fileContents = new StringBuffer();
		for (int i = 0; i < queryTermLines.size(); i++) {
			String line = queryTermLines.get(i);
			List<QueryNode> queryNodes = Arrays.asList(line.split("\\s")).stream()
					.map(term -> new TermNode(term, invertedIndex.getCopy(term), stats, scorer)).collect(Collectors.toList());
			QueryNode andNode = new AndNode(queryNodes);

			List<DocumentScore> scores = network.runQuery(andNode, 10);

			System.out.println("query: " + line);
			for (int output = 0; output < scores.size(); output++) {
				DocumentScore docScore = scores.get(output);
				String resultLine = String.format("Q%d %s %-35s %d %f %s", i + 1, "skip",
						corpus.getSceneNameFromNumber(docScore.getDocId()), output + 1, docScore.getScore(),
						"ppachpute-ql-dir-and-1500");
				System.out.println(resultLine);
				fileContents.append(resultLine);
				fileContents.append("\n");

			}
		}
		FileUtils.writeLines("and.trecrun", fileContents);
		
		fileContents = new StringBuffer();
		for (int i = 0; i < queryTermLines.size(); i++) {
			String line = queryTermLines.get(i);
			List<QueryNode> queryNodes = Arrays.asList(line.split("\\s")).stream()
					.map(term -> new TermNode(term, invertedIndex.getCopy(term), stats, scorer)).collect(Collectors.toList());
			QueryNode orNode = new OrNode(queryNodes);

			List<DocumentScore> scores = network.runQuery(orNode, 10);

			System.out.println("query: " + line);
			for (int output = 0; output < scores.size(); output++) {
				DocumentScore docScore = scores.get(output);
				String resultLine = String.format("Q%d %s %-35s %d %f %s", i + 1, "skip",
						corpus.getSceneNameFromNumber(docScore.getDocId()), output + 1, docScore.getScore(),
						"ppachpute-ql-dir-or-1500");
				System.out.println(resultLine);
				fileContents.append(resultLine);
				fileContents.append("\n");

			}
		}
		FileUtils.writeLines("or.trecrun", fileContents);
		
		fileContents = new StringBuffer();
		for (int i = 0; i < queryTermLines.size(); i++) {
			String line = queryTermLines.get(i);
			List<QueryNode> queryNodes = Arrays.asList(line.split("\\s")).stream()
					.map(term -> new TermNode(term, invertedIndex.getCopy(term), stats, scorer)).collect(Collectors.toList());
			QueryNode maxNode = new MaxNode(queryNodes);

			List<DocumentScore> scores = network.runQuery(maxNode, 10);

			System.out.println("query: " + line);
			for (int output = 0; output < scores.size(); output++) {
				DocumentScore docScore = scores.get(output);
				String resultLine = String.format("Q%d %s %-35s %d %f %s", i + 1, "skip",
						corpus.getSceneNameFromNumber(docScore.getDocId()), output + 1, docScore.getScore(),
						"ppachpute-ql-dir-max-1500");
				System.out.println(resultLine);
				fileContents.append(resultLine);
				fileContents.append("\n");

			}
		}
		FileUtils.writeLines("max.trecrun", fileContents);
		
		fileContents = new StringBuffer();
		for (int i = 0; i < queryTermLines.size(); i++) {
			String line = queryTermLines.get(i);
			List<QueryNode> queryNodes = Arrays.asList(line.split("\\s")).stream()
					.map(term -> new TermNode(term, invertedIndex.getCopy(term), stats, scorer)).collect(Collectors.toList());
			QueryNode sumNode = new SumNode(queryNodes);

			List<DocumentScore> scores = network.runQuery(sumNode, 10);

			System.out.println("query: " + line);
			for (int output = 0; output < scores.size(); output++) {
				DocumentScore docScore = scores.get(output);
				String resultLine = String.format("Q%d %s %-35s %d %f %s", i + 1, "skip",
						corpus.getSceneNameFromNumber(docScore.getDocId()), output + 1, docScore.getScore(),
						"ppachpute-ql-dir-sum-1500");
				System.out.println(resultLine);
				fileContents.append(resultLine);
				fileContents.append("\n");

			}
		}
		FileUtils.writeLines("sum.trecrun", fileContents);
		
		fileContents = new StringBuffer();
		for (int i = 0; i < queryTermLines.size(); i++) {
			String line = queryTermLines.get(i);
			QueryNode owNode = new OrderedWindow(Arrays.asList(line.split("\\s")), 
					invertedIndex, stats, 1, scorer); 
			
			List<DocumentScore> scores = network.runQuery(owNode, 10);

			System.out.println("query: " + line);
			for (int output = 0; output < scores.size(); output++) {
				DocumentScore docScore = scores.get(output);
				String resultLine = String.format("Q%d %s %-35s %d %f %s", i + 1, "skip",
						corpus.getSceneNameFromNumber(docScore.getDocId()), output + 1, docScore.getScore(),
						"ppachpute-ql-dir-od1-1500");
				System.out.println(resultLine);
				fileContents.append(resultLine);
				fileContents.append("\n");

			}
		}
		FileUtils.writeLines("od1.trecrun", fileContents);
		
		fileContents = new StringBuffer();
		for (int i = 0; i < queryTermLines.size(); i++) {
			String line = queryTermLines.get(i);
			List<String> terms = Arrays.asList(line.split("\\s"));
			QueryNode uowNode = new UnOrderedWindow(terms,
					invertedIndex, stats, terms.size()*3, scorer); 
			
			List<DocumentScore> scores = network.runQuery(uowNode, 10);

			System.out.println("query: " + line);
			for (int output = 0; output < scores.size(); output++) {
				DocumentScore docScore = scores.get(output);
				String resultLine = String.format("Q%d %s %-35s %d %f %s", i + 1, "skip",
						corpus.getSceneNameFromNumber(docScore.getDocId()), output + 1, docScore.getScore(),
						"ppachpute-ql-dir-uw-1500");
				System.out.println(resultLine);
				fileContents.append(resultLine);
				fileContents.append("\n");

			}
		}
		FileUtils.writeLines("uw.trecrun", fileContents);
	}

}
