package umass.searchengine.indexer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import umass.searchengine.model.PostingList;

public class InvertedIndex {
	Map<String, PostingList> map;
	Map<Integer, Integer> sceneLength;

	/**
	 * @param map
	 */
	public InvertedIndex() {
		this.map = new HashMap<>();
	}

	public PostingList get(String key) {
		return map.get(key);
	}

	public PostingList getCopy(String key) {
		try {
			return (PostingList) map.get(key).clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Set<String> getUniqueWords() {
		return map.keySet();
	}

	public void put(String term, PostingList postings) {
		map.put(term, postings);
	}

	public int size() {
		return map.size();
	}
	
	public void genereteSceneLength() {
		this.sceneLength = new HashMap<Integer, Integer>();
		for (String word : getUniqueWords()) {
			map.get(word).getPostingsList().stream()
					.forEach(posting -> sceneLength.merge(posting.getDocumentId(), posting.getTermFreq(), Integer::sum));
		}
	}

	public Map<Integer, Integer> getLengthOfScenes() {
		if (this.sceneLength == null)
			genereteSceneLength();
		return this.sceneLength;
	}
	
	public int getLengthOfScene(int sceneNum) {
		return this.sceneLength.get(sceneNum);
	}
	 
	public Double getIdfOfTerm(String term) {
		if (this.sceneLength == null)
			genereteSceneLength();
		
		return Math.log((this.sceneLength.size() + 1) / (map.get(term).size()+ 0.5));
	}
} 
