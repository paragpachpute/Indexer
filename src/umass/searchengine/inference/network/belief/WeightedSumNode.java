package umass.searchengine.inference.network.belief;

import java.util.List;

import umass.searchengine.inference.network.QueryNode;

public class WeightedSumNode extends BeliefNode {
	
	private List<Double> weights;

	public WeightedSumNode(List<QueryNode> children, List<Double> weights) {
		super(children);
		this.weights = weights;
	}

	@Override
	public Double score(int docId) {
		double score = 0;
		for (int i = 0; i < children.size(); i++) {
			score += weights.get(i) * Math.exp(children.get(i).score(docId));
		}
		return Math.log(score / weights.stream().mapToDouble(child -> child).sum());
	}

}
