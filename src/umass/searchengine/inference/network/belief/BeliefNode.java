package umass.searchengine.inference.network.belief;

import java.util.List;

import umass.searchengine.inference.network.QueryNode;

public abstract class BeliefNode implements QueryNode {
	
	List<QueryNode> children;

	/**
	 * @param children
	 * @param id
	 */
	public BeliefNode(List<QueryNode> children) {
		super();
		this.children = children;
	}

	@Override
	public int nextCandidate() {
		return children.stream().filter(child -> child.hasNext()).mapToInt(child -> child.nextCandidate()).min().getAsInt();
	}
	
	@Override
	public void skipTo(int docId) {
		children.stream().forEach(child -> child.skipTo(docId));
	}
	
	@Override
	public boolean hasNext() {
		for (QueryNode node : children)
			if (node.hasNext())
				return true;
		
		return false;
	}
}
