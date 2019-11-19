package umass.searchengine.model;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class PostingList implements Cloneable { 
	
	private List<Posting> list;

	private int documentFreq;
	private ListIterator<Posting> it;
	
	/**
	 * @param documentFreq
	 */
	public PostingList() {
		list = new ArrayList<>();
		it = list.listIterator();
	}
	
	public void add(Posting p) {
		list.add(p);
		documentFreq++;
	}
	
	public Posting get(int index) {
		return list.get(index);
	}
	
	public Posting skipTo(int docId) {
		while (it != null && it.hasNext()) {
			Posting p = it.next();
			if (p.getDocumentId() >= docId) {
				return p;
			}
		}
		return null;
	}
	
	public void beginIteration() {
		it = list.listIterator();
	}
	
	public Posting nextCandidate() {
		if (!it.hasNext())
			return null;
		Posting next = it.next();
		it.previous();
		return next;
	}
	
	public Posting currentDocument() {
		if (!it.hasPrevious())
			return null;
		Posting prev = it.previous();
		it.next();
		return prev;
	}
	
	public void goToPrevious() {
		it.previous();
	}
	
	public boolean hasNext() {
		return it.hasNext();
	}
	
	public boolean isFinished() {
		try {
			it.next();
			it.previous();
			return false;
		} catch (NoSuchElementException e) {
			return true;
		}
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
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		PostingList newList = new PostingList();
		for (Posting p : list) {
			newList.add(p.clone());
		}
		return newList;
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
