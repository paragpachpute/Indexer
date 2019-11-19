package test.umass.searchengine.encoder;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import umass.searchengine.encoder.VByteEncoder;
import umass.searchengine.indexer.AuxiliaryTableCreator;
import umass.searchengine.indexer.InvertedIndex;
import umass.searchengine.model.LookupTable;
import umass.searchengine.model.Posting;
import umass.searchengine.model.PostingList;

public class TestVByteEncoder {
	
	VByteEncoder vByteEncoder = new VByteEncoder();

	@Test
	public void testCovertToVByte() {
		int val = 20;
		byte[] vByte = new byte[] {(byte) (20 + 128)};
		byte[] result = vByteEncoder.covertToVByte(val);
		Assertions.assertArrayEquals(vByte, result);
		
		val = 127;
		vByte = new byte[] {(byte) 0xFF};
		result = vByteEncoder.covertToVByte(val);
		Assertions.assertArrayEquals(vByte, result);
		
		val = 128;
		vByte = new byte[] {(byte) 0x01, (byte) 0x80};
		result = vByteEncoder.covertToVByte(val);
		Assertions.assertArrayEquals(vByte, result);
		
		val = 130;
		vByte = new byte[] {(byte) 0x01, (byte) 0x82};
		result = vByteEncoder.covertToVByte(val);
		Assertions.assertArrayEquals(vByte, result);
		
		val = 20000;
		vByte = new byte[] {(byte) 0x01, (byte) 0x1C, (byte) 0xA0};
		result = vByteEncoder.covertToVByte(val);
		Assertions.assertArrayEquals(vByte, result);
	}
	
	@Test
	public void testfromVByte() {
		int val = 20;
		byte[] vByte = new byte[] {(byte) (20 + 128)};
		int result = vByteEncoder.fromVByte(vByte);
		Assertions.assertEquals(val, result);
		
		val = 127;
		vByte = new byte[] {(byte) 0xFF};
		result = vByteEncoder.fromVByte(vByte);
		Assertions.assertEquals(val, result);
		
		val = 128;
		vByte = new byte[] {(byte) 0x01, (byte) 0x80};
		result = vByteEncoder.fromVByte(vByte);
		Assertions.assertEquals(val, result);
		
		val = 130;
		vByte = new byte[] {(byte) 0x01, (byte) 0x82};
		result = vByteEncoder.fromVByte(vByte);
		Assertions.assertEquals(val, result);
		
		val = 20000;
		vByte = new byte[] {(byte) 0x01, (byte) 0x1C, (byte) 0xA0};
		result = vByteEncoder.fromVByte(vByte);
		Assertions.assertEquals(val, result);
	}

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
		vByteEncoder.compress(index, lookup, byteBuffer);

		Assertions.assertSame(0, lookup.get("a").getOffset());
		Assertions.assertSame(9, lookup.get("b").getOffset());

		byte compressedValue = (byte) 0x82; // df of a
		Assertions.assertEquals(compressedValue, byteBuffer.get(0));
		
		compressedValue = (byte) 0x81; // sceneNum of a
		Assertions.assertEquals(compressedValue, byteBuffer.get(1));
		
		compressedValue = (byte) 0x82; // termFreq of a
		Assertions.assertEquals(compressedValue, byteBuffer.get(2));
		
		InvertedIndex decomInvertedIndex = vByteEncoder.decompress(byteBuffer, lookup);
		System.out.println(decomInvertedIndex);
		
	}	
}
