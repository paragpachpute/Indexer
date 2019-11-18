package umass.searchengine.inference.network.belief;

import java.util.List;

import umass.searchengine.inference.network.QueryNode;

public class SumNode extends BeliefNode {

	public SumNode(List<QueryNode> children) {
		super(children);
	}

	@Override
	public Double score(int docId) {
		return Math.log(children.stream().mapToDouble(child -> Math.exp(child.score(docId))).sum() / children.size());
	}
}
