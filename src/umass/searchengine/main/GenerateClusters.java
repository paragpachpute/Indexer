package umass.searchengine.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import umass.searchengine.cluster.Cluster;
import umass.searchengine.cluster.CosineSimilarity;
import umass.searchengine.cluster.LinkageType;
import umass.searchengine.indexer.DocumentVector;
import umass.searchengine.indexer.DocumentVectorIndex;
import umass.searchengine.indexer.IndexCreator;
import umass.searchengine.indexer.InvertedIndex;
import umass.searchengine.loader.DatasetLoader;
import umass.searchengine.model.Corpus;
import umass.searchengine.utils.FileUtils;

public class GenerateClusters {

	public static void main(String[] args) throws IOException {
		Corpus corpus = new DatasetLoader().load();
		IndexCreator creator = new IndexCreator().create(corpus);
		DocumentVectorIndex index = creator.getDocumentIndex();
		InvertedIndex invertedIndex = creator.getInvertedIndex();
		CosineSimilarity similarity = new CosineSimilarity(invertedIndex);
		new GenerateClusters().generateAllClusters(corpus, index, invertedIndex, similarity);
	}

	public List<Cluster> generate(List<Integer> docIds, double threshold, DocumentVectorIndex index,
			InvertedIndex invertedIndex, CosineSimilarity similarity) throws IOException {
		List<Cluster> clusters = new ArrayList<>();
		List<DocumentVector> docs = docIds.stream().map(did -> index.getDocumentVector(did))
				.collect(Collectors.toList());

		int clusterId = 1;
		for (DocumentVector d : docs) {
			double maxScore = Double.MIN_VALUE;
			Cluster bestCluster = null;
			
			for (Cluster anotherCluster : clusters) {
				double score = anotherCluster.score(d, LinkageType.CENTROID_LINKAGE);
				if (score > maxScore && score > threshold) {
					maxScore = score;
					bestCluster = anotherCluster;
				}
			}

			if (maxScore == Double.MIN_VALUE) {
				// Means that the best match not found, make a new cluster
				clusters.add(new Cluster(clusterId++, d, similarity));
			} else {
				bestCluster.addMember(d);
			}
		}
		return clusters;
	}

	public void generateAllClusters(Corpus corpus, DocumentVectorIndex index, InvertedIndex invertedIndex,
			CosineSimilarity similarity) throws IOException {
		List<Integer> docIds = corpus.getCorpus().stream().mapToInt(s -> s.getSceneNum()).boxed()
				.collect(Collectors.toList());
		for (int i = 1; i < 20; i++) {
			StringBuffer fileContents = new StringBuffer();
			double threshold = i * 0.05;
			String thresholdStr = String.format("%.2f", threshold);
			
			List<Cluster> clusters = generate(docIds, threshold, index, invertedIndex, similarity);
			System.out.print("\\multirow{" + clusters.size() + "}{*}{" + thresholdStr + "} ");
			
			for (Cluster c : clusters) {
				for (DocumentVector d : c.getMembers()) {
					String line = String.format("%d %s", c.getId(), corpus.getSceneNameFromNumber(d.getDocId()));
					fileContents.append(line + "\n");
				}
				System.out.println("& " + c.getId() + " & " + c.getMembers().size() + "\\\\");
			}
			System.out.println("\\hline");
			
			String fileName = "cluster-" + thresholdStr + ".out";
			FileUtils.writeLines(fileName, fileContents);
		}
	}

}
