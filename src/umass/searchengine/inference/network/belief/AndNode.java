package umass.searchengine.inference.network.belief;

import java.util.List;

import umass.searchengine.inference.network.QueryNode;
import umass.searchengine.inference.network.belief.BeliefNode;

public class AndNode extends BeliefNode {

	public AndNode(List<QueryNode> children) {
		super(children);
	}

	@Override
	public Double score(int docId) {
		return children.stream().mapToDouble(child -> child.score(docId)).sum();
	}

}
