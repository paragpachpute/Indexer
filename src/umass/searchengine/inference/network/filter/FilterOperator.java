package umass.searchengine.inference.network.filter;

import umass.searchengine.inference.network.QueryNode;
import umass.searchengine.inference.network.proximity.ProximityNode;

public abstract class FilterOperator implements QueryNode {
	
	protected ProximityNode filter;
	protected QueryNode query;
	
	/**
	 * @param filter
	 * @param query
	 */
	public FilterOperator(ProximityNode filter, QueryNode query) {
		super();
		this.filter = filter;
		this.query = query;
	}
	
	@Override
	public int nextCandidate() {
		return query.nextCandidate();
	}

	@Override
	public void skipTo(int docId) {
		query.skipTo(docId);
		filter.skipTo(docId);
	}

	@Override
	public boolean hasNext() {
		return query.hasNext();
	}
	

}
