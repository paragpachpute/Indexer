package test.umass.searchengine.encoder;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import umass.searchengine.encoder.EmptyCompressor;
import umass.searchengine.indexer.AuxiliaryTableCreator;
import umass.searchengine.model.InvertedIndex;
import umass.searchengine.model.LookupTable;
import umass.searchengine.model.Posting;
import umass.searchengine.model.PostingList;

public class TestEmptyCompressor {
	
	EmptyCompressor emptyCompressor = new EmptyCompressor();

	@Test
	public void testCompress() {
		InvertedIndex index = new InvertedIndex();
		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(2);
		Posting p = new Posting(1);
		list.forEach(i -> p.addPosition(i));

		List<Integer> list2 = new ArrayList<>();
		list2.add(3);
		list2.add(5);
		Posting p2 = new Posting(3);
		list2.forEach(i -> p2.addPosition(i));
		
		PostingList plist = new PostingList();
		plist.add(p);
		plist.add(p2);
		PostingList plist2 = new PostingList();
		plist2.add(p2);
		
		index.put("a", plist);
		index.put("b", plist2);
		System.out.println(index);
		LookupTable lookup = AuxiliaryTableCreator.createLookupTable(index);
		
		ByteBuffer byteBuffer = ByteBuffer.allocate(10000);
		emptyCompressor.compress(index, lookup, byteBuffer);
		
		Assertions.assertSame(0, lookup.get("a").getOffset());
		Assertions.assertSame(36, lookup.get("b").getOffset());

		int sizeOfInt = 4;
		// df of a
		Assertions.assertEquals(2, byteBuffer.getInt(0)); 
		
		// sceneNum of a
		Assertions.assertEquals(1, byteBuffer.getInt(1 * sizeOfInt));
		
		// termFreq of a
		Assertions.assertEquals(2, byteBuffer.getInt(2 * sizeOfInt));
		
		InvertedIndex decomInvertedIndex = emptyCompressor.decompress(byteBuffer, lookup);
		System.out.println(decomInvertedIndex);
		
	}	
}
