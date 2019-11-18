package umass.searchengine.indexer;

import umass.searchengine.model.Corpus;
import umass.searchengine.model.InvertedIndex;
import umass.searchengine.model.Posting;
import umass.searchengine.model.PostingList;
import umass.searchengine.model.Scene;

public class IndexCreator {
	
	/**
	 * Creates inverted index from the corpus.
	 * @param corpus
	 * @return
	 */
	public InvertedIndex create(Corpus corpus) {
		InvertedIndex invertedIndex = new InvertedIndex();
		
		for (Scene scene : corpus.getCorpus()) {
			String[] text = scene.getText().split("\\s+");
			
			for (int i = 0; i < text.length; i++) {
				String word = text[i];
				if (!word.strip().equals("")) {
					
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
		}
		return invertedIndex;
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
