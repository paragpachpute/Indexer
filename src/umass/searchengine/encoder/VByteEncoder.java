package umass.searchengine.encoder;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import umass.searchengine.model.InvertedIndex;
import umass.searchengine.model.LookupTable;
import umass.searchengine.model.Posting;
import umass.searchengine.model.PostingList;

public class VByteEncoder {
	
	/**
	 * Writes the compressed inverted index into byteBuffer
	 * @param invertedIndex index to compress
	 * @param lookup to store offset information
	 * @param byteBuffer buffer to write compressed data
	 */
	public void compress(InvertedIndex invertedIndex, LookupTable lookup, ByteBuffer byteBuffer) {
		for (String term : invertedIndex.getUniqueWords()) {
			lookup.get(term).setOffset(byteBuffer.position());
			byteBuffer.put(covertToVByte(invertedIndex.get(term).getDocumentFreq()));
			for (Posting p : invertedIndex.get(term).getPostingsList()){
				int[] valIntArray = p.getIntArray();
				for (int i = 0; i < valIntArray.length; i++) {
					byte[] byteArray = covertToVByte(valIntArray[i]); 
					byteBuffer.put(byteArray);
				}
			}
		}
	}
	
	/**
	 * Generates Inverted index from byte buffer
	 * @param byteBuffer compressed representation of inverted index
	 * @param lookup containing word's offset information
	 * @return Inverted index
	 */
	public InvertedIndex decompress(ByteBuffer byteBuffer, LookupTable lookup) {
		InvertedIndex invertedIndex = new InvertedIndex();
		for (String term : lookup.keySet()) {
			byteBuffer.position(lookup.get(term).getOffset());
			PostingList postingList = new PostingList();
			
			int df = fromVByte(byteBuffer);
			for (int i = 0; i < df; i++) {
				int sceneId = fromVByte(byteBuffer);
				int docTf = fromVByte(byteBuffer);
				List<Integer> positions = new ArrayList<>();
				for (int j = 0; j < docTf; j++) {
					positions.add(fromVByte(byteBuffer));
				}
				Posting posting = new Posting(sceneId, docTf, positions);
				postingList.add(posting);
			}
			
			invertedIndex.put(term, postingList);
		}
		return invertedIndex;
	}

	public byte[] covertToVByte(int i) {
		if (i <  0) {
			throw new UnsupportedOperationException("Cannot compress negative number through V-Byte encoder");
		}
		Stack<Byte> stack = new Stack<>();
		
		// Create first byte by setting the compression termination flag
		byte firstByte = (byte) (i % 128);
		firstByte += 128;
		i /= 128;
		stack.push(firstByte);
		
		while (i > 0) {
			stack.push((byte) (i % 128));
			i /= 128;
		}
		byte[] bytes = new byte[stack.size()];
		int index = 0;
		while (!stack.empty()) {
			bytes[index++] = stack.pop();
		}
		return bytes;
	}
	
	public int fromVByte(ByteBuffer buffer) {
		int val = 0;
		byte input = buffer.get();
		while (input >= 0) {
			val += input;
			val = val << 7;
			input = buffer.get();
		} 
		// mask the 1 flag
		val += input + 128;
		
		return val;
	}
	
	public int fromVByte(byte[] input) {
		return fromVByte(ByteBuffer.wrap(input));
	}

}
