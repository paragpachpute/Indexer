package umass.searchengine.encoder;

import java.nio.ByteBuffer;

import umass.searchengine.indexer.InvertedIndex;
import umass.searchengine.model.LookupTable;

public interface Compressor {
	
	/**
	 * Takes inverted index as an input and compresses that to generate byte buffer output
	 * @param invertedIndex is an input containing populated inverted index
	 * @param lookup lookup table to write the position of the terms within byte buffer
	 * @param byteBuffer compressed inverted index would be stored in the byte buffer 
	 */
	void compress(InvertedIndex invertedIndex, LookupTable lookup, ByteBuffer byteBuffer);
	
	/**
	 * Takes compressed byte buffer as an input and generates inverted index
	 * @param byteBuffer is an input containing compressed representation of inverted index
	 * @param lookup lookup table to find the position of the terms within byte buffer
	 * @return invertedIndex uncompressed inverted index would be generated from the byte buffer
	 */
	InvertedIndex decompress(ByteBuffer byteBuffer, LookupTable lookup);

}
