package umass.searchengine.encoder;

import java.util.List;

import umass.searchengine.indexer.InvertedIndex;
import umass.searchengine.model.Posting;
import umass.searchengine.model.PostingList;

public class DeltaEncoder {

	/**
	 * It delta encodes the inverted index in place
	 * @param invertedIndex index to encode
	 */
	public void encode(InvertedIndex invertedIndex) {
		for (String term : invertedIndex.getUniqueWords()) {
			int previousSceneNum = 0;
			for (Posting p : invertedIndex.get(term).getPostingsList()) {
				encodeList(p.getPositions());
				int origSceneNum = p.getDocumentId();
				p.setSceneNum(p.getDocumentId() - previousSceneNum);
				previousSceneNum = origSceneNum;
			}
		}
	}
	
	/**
	 * It delta decodes the inverted index in place
	 * @param invertedIndex index to decode
	 */
	public void decode(InvertedIndex invertedIndex) {
		for (String term : invertedIndex.getUniqueWords()) {
			PostingList plist = invertedIndex.get(term);
			for (int i = 1; i < plist.getPostingsList().size(); i++) {
				Posting currentPosting = plist.get(i);
				Posting previousPosting = plist.get(i - 1);
				currentPosting.setSceneNum(currentPosting.getDocumentId() + previousPosting.getDocumentId());
				decodeList(currentPosting.getPositions());
			}
			//decode list would not be called for first posting hence calling here explicitly
			decodeList(plist.get(0).getPositions());
		}
	}
	
	public void encodeList(List<Integer> list) {
		int previousValue = list.get(0);
		for (int i = 1; i < list.size(); i++) {
			int origValue = list.get(i);
			list.set(i, list.get(i) - previousValue);
			previousValue = origValue;
		}
	}
	
	public void decodeList(List<Integer> list) {
		for (int i = 1; i < list.size(); i++) {
			list.set(i, list.get(i) + list.get(i - 1));
		}
	}
}
