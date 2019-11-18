package umass.searchengine.inference.network.belief;

import java.util.List;
import java.util.stream.Collectors;

import umass.searchengine.inference.network.QueryNode;

public class OrNode extends BeliefNode {

	public OrNode(List<QueryNode> children) {
		super(children);
	}

	@Override
	public Double score(int docId) {
		double multipliedScore = 1;
		for (QueryNode node : children) {
			multipliedScore *= (1 - Math.exp(node.score(docId)));
		}
		return Math.log(1 - multipliedScore);
	}
}
