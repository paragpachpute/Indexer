package umass.searchengine.encoder;

public class CompressorFactory {
	
	/**
	 * Based on compressionRequired either returns Empty compressor or Combined compressor (Delta + V-Byte)
	 * @param compressionRequired true if compression is required
	 * @return Compressor that has implementation of compress and decompress from {@link Compressor}
	 */
	public static Compressor getCompressor(boolean compressionRequired) {
		return compressionRequired ? new CombinedCompressor() : new EmptyCompressor();
	}
}
