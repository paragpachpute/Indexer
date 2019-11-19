package umass.searchengine.cluster;

import java.util.ArrayList;
import java.util.List;

import umass.searchengine.indexer.DocumentVector;

public class Cluster {
	
	private List<DocumentVector> members;

	public Cluster(DocumentVector vector) {
		this.members = new ArrayList<>();
		this.members.add(vector);
	}
	
	public List<DocumentVector> getMembers() {
		return this.members;
	}
	
	public void addMember(Cluster cluster) {
		cluster.getMembers().forEach(member -> this.members.add(member));
	}
	
	public Double score(Cluster cluster, LinkageType ltype) {
		switch (ltype) {
		case SINGLE_LINKAGE:
			return scoreSingleLinkage(cluster);
		case COMPLETE_LINKAGE:
			return scoreCompleteLinkage(cluster);
		case AVERAGE_LINKAGE:
			return scoreAverageLinkage(cluster);
		case CENTROID_LINKAGE:
			return scoreCentroidLinkage(cluster);
		default:
			return null;
		}
	}

	private Double scoreCentroidLinkage(Cluster cluster) {
		// TODO implement method
		return null;
	}

	private Double scoreCompleteLinkage(Cluster cluster) {
		double maxDist = Double.MIN_VALUE;
		for (DocumentVector anotherClusterDoc : cluster.getMembers()) {
			for (DocumentVector currentClusterDoc : this.getMembers()) {
				double dist = currentClusterDoc.calculateDistance(anotherClusterDoc);
				if (dist > maxDist) {
					maxDist = dist;
				}
			}
		}
		return maxDist;
	}

	private Double scoreSingleLinkage(Cluster cluster) {
		double minDist = Double.MAX_VALUE;
		for (DocumentVector anotherClusterDoc : cluster.getMembers()) {
			for (DocumentVector currentClusterDoc : this.getMembers()) {
				double dist = currentClusterDoc.calculateDistance(anotherClusterDoc);
				if (dist < minDist) {
					minDist = dist;
				}
			}
		}
		return minDist;
	}

	private Double scoreAverageLinkage(Cluster cluster) {
		double distSum = 0;
		for (DocumentVector anotherClusterDoc : cluster.getMembers()) {
			for (DocumentVector currentClusterDoc : this.getMembers()) {
				distSum += currentClusterDoc.calculateDistance(anotherClusterDoc);
			}
		}
		return distSum / (cluster.getMembers().size() * this.getMembers().size());
	}
}
