package umass.searchengine.model;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Posting implements Cloneable {

	private int sceneNum;
	private int termFreq;
	private List<Integer> positions;
	private ListIterator<Integer> it;
	
	/**
	 * @param sceneNum
	 * @param positions
	 */
	public Posting(int sceneId) {
		super();
		this.sceneNum = sceneId;
		this.positions = new ArrayList<>();
	}
	
	/**
	 * @param sceneNum
	 * @param termFreq
	 * @param positions
	 */
	public Posting(int sceneNum, int termFreq, List<Integer> positions) {
		super();
		this.sceneNum = sceneNum;
		this.termFreq = termFreq;
		this.positions = positions;
	}

	public void addPosition(int pos) {
		positions.add(pos);
		termFreq++;
	}
	
	public int[] getIntArray() {
		List<Integer> list = new ArrayList<>();
		list.add(sceneNum);
		list.add(termFreq);
		list.addAll(positions);
		return list.stream().mapToInt(Integer::intValue).toArray();
	}

	/**
	 * @return the sceneNum
	 */
	public int getDocumentId() {
		return sceneNum;
	}

	/**
	 * @return the positions
	 */
	public List<Integer> getPositions() {
		return positions;
	}

	/**
	 * @return the termFreq
	 */
	public int getTermFreq() {
		return termFreq;
	}

	/**
	 * @param sceneNum the sceneNum to set
	 */
	public void setSceneNum(int sceneNum) {
		this.sceneNum = sceneNum;
	}
	
	public void beginIteration() {
		it = positions.listIterator();
	}
	
	public int next() {
		return it.hasNext() ? it.next() : -1;
	}
	
	public int nextCandidate() {
		if (!it.hasNext())
			return -1;
		int next = it.next();
		it.previous();
		return next;
	}
	
	public int skipTo(int pos) {
		while (it != null && it.hasNext()) {
			int p = it.next();
			if (p >= pos) {
				return p;
			}
		}
		return -1;
	}
	
	public int currentPosition() {
		int pos = -1;
		if (it != null && it.hasPrevious()) {
			pos = it.previous();
			it.next();
		}
		return pos;
	}
	
	public void goToPrevious() {
		it.previous();
	}
	
	public boolean hasNext() {
		return it.hasNext();
	}
	
	@Override
	protected Posting clone() throws CloneNotSupportedException {
		List<Integer> clonedPositions = new ArrayList<>();
		for (Integer i : this.positions)
			clonedPositions.add(i);
		Posting newPosting = new Posting(this.sceneNum, this.termFreq, clonedPositions);
		return newPosting;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((positions == null) ? 0 : positions.hashCode());
		result = prime * result + sceneNum;
		result = prime * result + termFreq;
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
		Posting other = (Posting) obj;
		if (positions == null) {
			if (other.positions != null)
				return false;
		} else if (!positions.equals(other.positions))
			return false;
		if (sceneNum != other.sceneNum)
			return false;
		if (termFreq != other.termFreq)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Posting [sceneNum=" + sceneNum + ", termFreq=" + termFreq + ", positions=" + positions + "]";
	}

}
