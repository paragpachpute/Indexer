package umass.searchengine.encoder;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import umass.searchengine.indexer.InvertedIndex;
import umass.searchengine.model.LookupTable;
import umass.searchengine.model.Posting;
import umass.searchengine.model.PostingList;

public class EmptyCompressor implements Compressor {

	@Override
	public void compress(InvertedIndex invertedIndex, LookupTable lookup, ByteBuffer byteBuffer) {
		for (String term : invertedIndex.getUniqueWords()) {
			lookup.get(term).setOffset(byteBuffer.position());
			byteBuffer.putInt(invertedIndex.get(term).getDocumentFreq());
			for (Posting p : invertedIndex.get(term).getPostingsList()){
				int[] valIntArray = p.getIntArray();
				for (int i = 0; i < valIntArray.length; i++) {
					byteBuffer.putInt(valIntArray[i]);
				}
			}
		}
	}

	@Override
	public InvertedIndex decompress(ByteBuffer byteBuffer, LookupTable lookup) {
		InvertedIndex invertedIndex = new InvertedIndex();
		for (String term : lookup.keySet()) {
			byteBuffer.position(lookup.get(term).getOffset());
			PostingList postingList = new PostingList();
			
			int df = byteBuffer.getInt();
			for (int i = 0; i < df; i++) {
				int sceneId = byteBuffer.getInt();
				int docTf = byteBuffer.getInt();
				List<Integer> positions = new ArrayList<>();
				for (int j = 0; j < docTf; j++) {
					positions.add(byteBuffer.getInt());
				}
				Posting posting = new Posting(sceneId, docTf, positions);
				postingList.add(posting);
			}
			
			invertedIndex.put(term, postingList);
		}
		return invertedIndex;
		
	}
}
