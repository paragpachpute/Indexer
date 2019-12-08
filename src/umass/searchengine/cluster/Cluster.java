package umass.searchengine.cluster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import umass.searchengine.indexer.DocumentVector;
import umass.searchengine.indexer.InvertedIndex;

public class Cluster {

	private int id;
	private List<DocumentVector> members;
	private CosineSimilarity similarity;

	public Cluster(int id, DocumentVector vector, CosineSimilarity similarity) {
		this.id = id;
		this.members = new ArrayList<>();
		this.members.add(vector);
		this.similarity = similarity;
	}

	public int getId() {
		return id;
	}

	public List<DocumentVector> getMembers() {
		return this.members;
	}

	public void addMember(DocumentVector vector) {
		this.members.add(vector);
	}

	public Double score(DocumentVector d, LinkageType ltype) {
		switch (ltype) {
		case SINGLE_LINKAGE:
			return scoreSingleLinkage(d);
		case COMPLETE_LINKAGE:
			return scoreCompleteLinkage(d);
		case AVERAGE_LINKAGE:
			return scoreAverageLinkage(d);
		case CENTROID_LINKAGE:
			return scoreCentroidLinkage(d);
		default:
			return null;
		}
	}

	private Double scoreCentroidLinkage(DocumentVector d) {
		Map<String, Double> centroid = new HashMap<>();
		for (DocumentVector currentDoc : this.getMembers()) {
			for (String term : currentDoc.getTerms()) {
				centroid.put(term, centroid.getOrDefault(term, 0.0) + currentDoc.getTermCount(term));
			}
		}
		
		for (String term : centroid.keySet()) {
			centroid.put(term, centroid.get(term) / this.getMembers().size());
		}
		DocumentVector centroidDoc = new DocumentVector(centroid, -1);
		return this.similarity.calculate(centroidDoc, d);
	}

	/**
	 * Calculate Minimum distance i.e. Maximum similarity
	 * @param d document vector 
	 * @return similarity score
	 */
	private Double scoreSingleLinkage(DocumentVector d) {
		double maxSimilarityScore = Double.MIN_VALUE;
		for (DocumentVector currentClusterDoc : this.getMembers()) {
			double dist = this.similarity.calculate(currentClusterDoc, d);
			if (dist > maxSimilarityScore) {
				maxSimilarityScore = dist;
			}
		}
		return maxSimilarityScore;
	}

	/**
	 * Calculate maximum distance i.e. minimum similarity
	 * @param d document vector 
	 * @return similarity score
	 */
	private Double scoreCompleteLinkage(DocumentVector d) {
		double minSimilarityScore = Double.MAX_VALUE;
		for (DocumentVector currentClusterDoc : this.getMembers()) {
			double dist = this.similarity.calculate(currentClusterDoc, d);
			if (dist < minSimilarityScore) {
				minSimilarityScore = dist;
			}
		}
		return minSimilarityScore;
	}

	private Double scoreAverageLinkage(DocumentVector d) {
		double similarityScoreSum = 0;
		for (DocumentVector currentClusterDoc : this.getMembers()) {
			similarityScoreSum += this.similarity.calculate(currentClusterDoc, d);
		}
		return similarityScoreSum / (this.getMembers().size());
	}

	@Override
	public String toString() {
		StringBuffer docIds = new StringBuffer();
		members.stream().forEach(m -> docIds.append(" " + m.getDocId()));
		return "Cluster [members=" + docIds + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((members == null) ? 0 : members.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cluster other = (Cluster) obj;
		if (members == null) {
			if (other.members != null)
				return false;
		} else if (!members.equals(other.members))
			return false;
		return true;
	}
}
