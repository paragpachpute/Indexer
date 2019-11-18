package umass.searchengine.inference.network.filter;

import umass.searchengine.inference.network.QueryNode;
import umass.searchengine.inference.network.proximity.ProximityNode;

public class FilterRequire extends FilterOperator {

	public FilterRequire(ProximityNode filter, QueryNode query) {
		super(filter, query);
	}

	@Override
	public Double score(int docId) {
		skipTo(docId);
		if (this.filter.getCurrentDocId() == docId)
			return this.query.score(docId);
		return null;
	}

}
