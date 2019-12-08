package umass.searchengine.indexer;

import java.util.HashMap;
import java.util.Map;

public class DocumentVectorIndex {
	
	private Map<Integer, DocumentVector> index;
	
	public DocumentVectorIndex() {
		this.index = new HashMap<>();
	}

	public DocumentVector getDocumentVector(int docId) {
		return this.index.get(docId);
	}

	public void addDocumentVector(int docId, DocumentVector vector) {
		this.index.put(docId, vector);
	}

	public Map<Integer, DocumentVector> getIndex() {
		return index;
	}
}
