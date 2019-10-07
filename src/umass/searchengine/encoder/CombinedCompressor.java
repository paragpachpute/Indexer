package umass.searchengine.encoder;

import java.nio.ByteBuffer;

import umass.searchengine.model.InvertedIndex;
import umass.searchengine.model.LookupTable;

public class CombinedCompressor implements Compressor {

	@Override
	public void compress(InvertedIndex invertedIndex, LookupTable lookup, ByteBuffer byteBuffer) {
		new DeltaEncoder().encode(invertedIndex);
		new VByteEncoder().compress(invertedIndex, lookup, byteBuffer);
	}

	@Override
	public InvertedIndex decompress(ByteBuffer byteBuffer, LookupTable lookup) {
		InvertedIndex invertedIndex = new VByteEncoder().decompress(byteBuffer, lookup);
		new DeltaEncoder().decode(invertedIndex);
		return invertedIndex;
	}

}
