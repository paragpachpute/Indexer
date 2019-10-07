package umass.searchengine.model;

import java.util.ArrayList;
import java.util.List;

public class PostingList { 
	
	private List<Posting> list;
	private int documentFreq;
	
	/**
	 * @param documentFreq
	 */
	public PostingList() {
		list = new ArrayList<>();
	}
	
	public void add(Posting p) {
		list.add(p);
		documentFreq++;
	}
	
	public Posting get(int index) {
		return list.get(index);
	}

	/**
	 * @return the documentFreq
	 */
	public int size() {
		return documentFreq;
	}

	/**
	 * @return the list
	 */
	public List<Posting> getPostingsList() {
		return list;
	}

	/**
	 * @return the documentFreq
	 */
	public int getDocumentFreq() {
		return documentFreq;
	}

	/**
	 * @return the documentFreq
	 */
	public int getCollectionTermFreq() {
		return list.stream().map(posting -> posting.getTermFreq()).reduce(0, Integer::sum);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + documentFreq;
		result = prime * result + ((list == null) ? 0 : list.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PostingList other = (PostingList) obj;
		if (documentFreq != other.documentFreq)
			return false;
		if (list == null) {
			if (other.list != null)
				return false;
		} else if (!list.equals(other.list))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PostingList [list=" + list + ", documentFreq=" + documentFreq + "]";
	}
}
