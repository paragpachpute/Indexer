package umass.searchengine.main;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;

import umass.searchengine.model.InvertedIndex;
import umass.searchengine.model.LookupTable;
import umass.searchengine.utils.FileUtils;
import umass.searchengine.utils.IndexerUtils;

public class CompareTwoIndexes {

	public static void main(String[] args) throws IOException {
		boolean isCompressed = true;
		String indexFileName = isCompressed ? "compressedIndex" : "uncompressedIndex";
		LookupTable compressedLookupTable = FileUtils.readLookupTable(isCompressed);
		InvertedIndex compressedInvertedIndex = FileUtils.readIndex(isCompressed, indexFileName, compressedLookupTable);
		
		isCompressed = false;
		indexFileName = isCompressed ? "compressedIndex" : "uncompressedIndex";
		LookupTable unCompressedLookupTable = FileUtils.readLookupTable(isCompressed);
		InvertedIndex unCompressedInvertedIndex = FileUtils.readIndex(isCompressed, indexFileName, unCompressedLookupTable);
		
		Assertions.assertTrue(IndexerUtils.compare(unCompressedInvertedIndex, compressedInvertedIndex));
		System.out.println("Two indexes are identical");

	}

}
