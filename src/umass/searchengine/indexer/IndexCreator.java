package umass.searchengine.indexer;

import java.util.HashMap;
import java.util.Map;

import umass.searchengine.model.Corpus;
import umass.searchengine.model.Posting;
import umass.searchengine.model.PostingList;
import umass.searchengine.model.Scene;

public class IndexCreator {
	
	private DocumentVectorIndex documentIndex;
	private InvertedIndex invertedIndex;
	
	/**
	 * Creates the indexes from the corpus.
	 * @param corpus
	 * @return IndexCreator so that it can be used in a chain 
	 */
	public IndexCreator create(Corpus corpus) {
		this.invertedIndex = new InvertedIndex();
		this.documentIndex = new DocumentVectorIndex();
		
		for (Scene scene : corpus.getCorpus()) {
			String[] text = scene.getText().split("\\s+");
			Map<String, Double> termCounts = new HashMap<>();
			
			for (int i = 0; i < text.length; i++) {
				String word = text[i];
				if (!word.strip().equals("")) {
					
					termCounts.put(word, termCounts.getOrDefault(word, 0.0) + 1);
					// Check if word is present in inverted index or not
					if (invertedIndex.get(word) == null) {
						PostingList wordPostingList = createNewPostingListAndAddPosting(scene.getSceneNum(), i+1);
						invertedIndex.put(word, wordPostingList);
						
					} else {
						// Word is present in invertedIndex check if the there are any positions available
						// for current scene id
						PostingList wordPostingList = invertedIndex.get(word);
						Posting latestPosting = wordPostingList.get(wordPostingList.size() - 1);
						if (latestPosting.getDocumentId() == scene.getSceneNum()) {
							latestPosting.addPosition(i+1);
						} else {
							Posting posting = new Posting(scene.getSceneNum());
							// Indexing is 0 based to make positions 1 based adding 1 below
							posting.addPosition(i+1);
							invertedIndex.get(word).add(posting);
						}
					}
				}
			}
			DocumentVector vector = new DocumentVector(termCounts, scene.getSceneNum());
			documentIndex.addDocumentVector(scene.getSceneNum(), vector);
		}
		return this;
	}
	
	public InvertedIndex getInvertedIndex() {
		return this.invertedIndex;
	}
	
	public DocumentVectorIndex getDocumentIndex() {
		return this.documentIndex;
	}
	
	private static PostingList createNewPostingListAndAddPosting(int sceneNum, int position) {
		PostingList wordPostingList = new PostingList();
		Posting posting = new Posting(sceneNum);
		// Indexing is 0 based to make positions 1 based adding 1 below
		posting.addPosition(position);
		wordPostingList.add(posting);
		return wordPostingList;
	}

}
