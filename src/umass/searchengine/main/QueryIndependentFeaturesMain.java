package umass.searchengine.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import umass.searchengine.indexer.AuxiliaryTableCreator;
import umass.searchengine.indexer.IndexCreator;
import umass.searchengine.indexer.InvertedIndex;
import umass.searchengine.inference.network.InferenceNetwork;
import umass.searchengine.inference.network.PriorNode;
import umass.searchengine.inference.network.QueryNode;
import umass.searchengine.inference.network.belief.AndNode;
import umass.searchengine.inference.network.proximity.TermNode;
import umass.searchengine.loader.DatasetLoader;
import umass.searchengine.model.Corpus;
import umass.searchengine.model.CorpusStatistics;
import umass.searchengine.model.DocumentScore;
import umass.searchengine.model.ProbabiltyDistribution;
import umass.searchengine.ranking.Dirichlet;
import umass.searchengine.ranking.Scorer;
import umass.searchengine.utils.FileUtils;

public class QueryIndependentFeaturesMain {
	
	public static void main(String[] args) throws IOException {
		Corpus corpus = new DatasetLoader().load();
		InvertedIndex invertedIndex = new IndexCreator().create(new DatasetLoader().load()).getInvertedIndex();
		CorpusStatistics stats = AuxiliaryTableCreator.createStatsTable(invertedIndex);
		Scorer scorer = new Dirichlet(1500, stats.getNumOfTerms());
		InferenceNetwork network = new InferenceNetwork();
		
		int numOfDocs = corpus.getCorpus().size();
		double[] probs = new double[numOfDocs];
		Random rand = new Random(1024);
		StringBuffer fileContentsRandom = new StringBuffer();
		StringBuffer fileContentsUniform = new StringBuffer();
		double sum = 0;
		for (int i = 0; i < numOfDocs; i++) {
			probs[i] = rand.nextDouble() / numOfDocs;
			sum += probs[i];
			fileContentsUniform.append(1.0 / numOfDocs);
			fileContentsUniform.append("\n");
		}
		
		for (int i = 0; i < numOfDocs; i++) {
			System.out.println(probs[i] / sum);
			fileContentsRandom.append(probs[i] / sum);
			fileContentsRandom.append("\n");
		}

		FileUtils.writeLines("random.prior", fileContentsRandom);
		FileUtils.writeLines("uniform.prior", fileContentsUniform);
		

//		#combine(#prior(uniform) the king queen royalty)
//		#combine(#prior(random) the king queen royalty)
		
		PriorNode priorNode = new PriorNode(invertedIndex, ProbabiltyDistribution.RANDOM);
		TermNode theNode = new TermNode("the", invertedIndex.getCopy("the"), stats, scorer);
		TermNode kingNode = new TermNode("king", invertedIndex.getCopy("king"), stats, scorer);
		TermNode queenNode = new TermNode("queen", invertedIndex.getCopy("queen"), stats, scorer);
		TermNode royaltyNode = new TermNode("royalty", invertedIndex.getCopy("royalty"), stats, scorer);
		
		List<QueryNode> children = new ArrayList<>();
		children.add(priorNode);
		children.add(theNode);
		children.add(kingNode);
		children.add(queenNode);
		children.add(royaltyNode);
		
		QueryNode andNode = new AndNode(children);
		List<DocumentScore> scores = network.runQuery(andNode, 10);
		StringBuffer fileContents = new StringBuffer();
		
		for (int output = 0; output < scores.size(); output++) {
			DocumentScore docScore = scores.get(output);
			String resultLine = String.format("Q%d %s %-35s %d %f %s", 1, "skip",
					corpus.getSceneNameFromNumber(docScore.getDocId()), output + 1, docScore.getScore(),
					"ppachpute-ql-dir-and-1500");
			System.out.println(resultLine);
			fileContents.append(resultLine);
			fileContents.append("\n");

		}
		FileUtils.writeLines("random.trecrun", fileContents);
		

		
		priorNode = new PriorNode(invertedIndex, ProbabiltyDistribution.UNIFORM);
		theNode = new TermNode("the", invertedIndex.getCopy("the"), stats, scorer);
		kingNode = new TermNode("king", invertedIndex.getCopy("king"), stats, scorer);
		queenNode = new TermNode("queen", invertedIndex.getCopy("queen"), stats, scorer);
		royaltyNode = new TermNode("royalty", invertedIndex.getCopy("royalty"), stats, scorer);
		
		children = new ArrayList<>();
		children.add(priorNode);
		children.add(theNode);
		children.add(kingNode);
		children.add(queenNode);
		children.add(royaltyNode);
		
		andNode = new AndNode(children);
		scores = network.runQuery(andNode, 10);
		fileContents = new StringBuffer();
		
		for (int output = 0; output < scores.size(); output++) {
			DocumentScore docScore = scores.get(output);
			String resultLine = String.format("Q%d %s %-35s %d %f %s", 1, "skip",
					corpus.getSceneNameFromNumber(docScore.getDocId()), output + 1, docScore.getScore(),
					"ppachpute-ql-dir-and-1500");
			System.out.println(resultLine);
			fileContents.append(resultLine);
			fileContents.append("\n");

		}
		FileUtils.writeLines("uniform.trecrun", fileContents);
		
	}

}
