package umass.searchengine.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class InvertedIndex {
	Map<String, PostingList> map;

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

	public Map<Integer, Integer> getLengthOfScenes() {
		Map<Integer, Integer> sceneLength = new HashMap<Integer, Integer>();
		for (String word : getUniqueWords()) {
			map.get(word).getPostingsList().stream()
					.forEach(posting -> sceneLength.merge(posting.getDocumentId(), posting.getTermFreq(), Integer::sum));
		}
		return sceneLength;
	}
}
