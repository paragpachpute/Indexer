package umass.searchengine.inference.network.belief;

import java.util.List;

import umass.searchengine.inference.network.QueryNode;

public class NotNode extends BeliefNode {

	public NotNode(List<QueryNode> children) {
		super(children);
	}

	@Override
	public Double score(int docId) {
		return Math.log(1 - Math.exp(children.get(0).score(docId)));
	}
}