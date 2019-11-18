package umass.searchengine.inference.network.belief;

import java.util.List;

import umass.searchengine.inference.network.QueryNode;

public class MaxNode extends BeliefNode {

	public MaxNode(List<QueryNode> children) {
		super(children);
	}

	@Override
	public Double score(int docId) {
		return children.stream().mapToDouble(child -> child.score(docId)).max().getAsDouble();
	}
}