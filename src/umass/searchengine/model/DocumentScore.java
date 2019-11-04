package umass.searchengine.model;

public class DocumentScore {
	private int docId;
	private double score;
	
	public DocumentScore(int docId, double scoreId) {
		super();
		this.docId = docId;
		this.score = scoreId;
	}

	public int getDocId() {
		return docId;
	}

	public double getScore() {
		return score;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DocumentScore [docId=" + docId + ", score=" + score + "]";
	}
}