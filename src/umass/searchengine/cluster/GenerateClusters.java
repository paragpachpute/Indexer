package umass.searchengine.cluster;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import umass.searchengine.indexer.DocumentVectorIndex;
import umass.searchengine.indexer.IndexCreator;
import umass.searchengine.loader.DatasetLoader;
import umass.searchengine.model.Corpus;

public class GenerateClusters {
	
	public Set<Cluster> generate(List<Integer> docIds, double threshold) throws IOException {
		Set<Cluster> clusters = new HashSet<>();
		Corpus corpus = new DatasetLoader().load();
		DocumentVectorIndex index = new IndexCreator().createIndex(corpus);
		docIds.stream().forEach(docId -> {
			clusters.add(new Cluster(index.getDocumentVector(docId)));
		});
		
		double minScore = Double.MIN_VALUE;
		while (true) {
			// Using this as a pair object
			AbstractMap.SimpleEntry<Cluster, Cluster> bestClusterPair = new AbstractMap.SimpleEntry<>(null, null);
			for (Cluster currentCluster : clusters) {
				for (Cluster anotherCluster : clusters) {
					double score = currentCluster.score(anotherCluster, LinkageType.SINGLE_LINKAGE);
					if (score < minScore && score > threshold) {
						minScore = score;
						bestClusterPair = new AbstractMap.SimpleEntry<>(currentCluster, anotherCluster);
					}
				}
			}
			
			if (minScore == Double.MIN_VALUE) {
				// Means the no more clusters are possible
				break;
			}
			
			// removing 2nd cluster from the original clusters set
			clusters.remove(bestClusterPair.getValue());
			// adding 2nd cluster to the 1st cluster as its member
			bestClusterPair.getKey().addMember(bestClusterPair.getValue());
		}
		

		return clusters;
	}
	
	public void generateAllClusters(List<Integer> docIds) throws IOException {
		for (int i = 1; i < 20; i++) {
			double threshold = i * 0.05;
			Set<Cluster> clusters = generate(docIds, threshold);
			// TODO print them in proper format
		}
	}

}
