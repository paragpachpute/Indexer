package umass.searchengine.model;

public class Lookup {
	
	private int offset; // position from which it starts in the file
	private int docFreq;
	private int collectionFreq;
	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}
	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}
	/**
	 * @return the docFreq
	 */
	public int getDocFreq() {
		return docFreq;
	}
	/**
	 * @param docFreq the docFreq to set
	 */
	public void setDocFreq(int docFreq) {
		this.docFreq = docFreq;
	}
	/**
	 * @return the collectionFreq
	 */
	public int getCollectionFreq() {
		return collectionFreq;
	}
	/**
	 * @param collectionFreq the collectionFreq to set
	 */
	public void setCollectionFreq(int collectionFreq) {
		this.collectionFreq = collectionFreq;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Lookup [offset=" + offset + ", docFreq=" + docFreq + ", collectionFreq=" + collectionFreq + "]";
	}
}
