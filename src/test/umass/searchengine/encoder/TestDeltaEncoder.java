package test.umass.searchengine.encoder;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import umass.searchengine.encoder.DeltaEncoder;
import umass.searchengine.indexer.InvertedIndex;
import umass.searchengine.model.Posting;
import umass.searchengine.model.PostingList;

public class TestDeltaEncoder {
	
	private DeltaEncoder encoder;
	
	@Before
	public void setup() {
		encoder = new DeltaEncoder();
	}
	
	@Test
	public void testEncodeList() {
		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		
		encoder.encodeList(list);
		
		Assertions.assertSame(1, list.get(0));
		Assertions.assertSame(1, list.get(1));
		Assertions.assertSame(1, list.get(2));
		Assertions.assertSame(1, list.get(3));
	}
	
	@Test
	public void testDecodeList() {
		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(1);
		list.add(1);
		list.add(1);
		
		encoder.decodeList(list);
		
		Assertions.assertSame(1, list.get(0));
		Assertions.assertSame(2, list.get(1));
		Assertions.assertSame(3, list.get(2));
		Assertions.assertSame(4, list.get(3));
	}
	
	@Test
	public void testEncode() {
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
		
		String term = "a";
		index.put(term, plist);
		System.out.println(index);
		
		Assertions.assertSame(2, index.get(term).size());
		Assertions.assertSame(1, index.get(term).get(0).getDocumentId());
		Assertions.assertSame(3, index.get(term).get(1).getDocumentId());
		Assertions.assertSame(1, index.get(term).get(0).getPositions().get(0));
		Assertions.assertSame(2, index.get(term).get(0).getPositions().get(1));
		Assertions.assertSame(3, index.get(term).get(1).getPositions().get(0));
		Assertions.assertSame(5, index.get(term).get(1).getPositions().get(1));
		
		encoder.encode(index);
		
		Assertions.assertSame(2, index.get(term).size());
		Assertions.assertSame(1, index.get(term).get(0).getDocumentId());
		Assertions.assertSame(2, index.get(term).get(1).getDocumentId());
		Assertions.assertSame(1, index.get(term).get(0).getPositions().get(0));
		Assertions.assertSame(1, index.get(term).get(0).getPositions().get(1));
		Assertions.assertSame(3, index.get(term).get(1).getPositions().get(0));
		Assertions.assertSame(2, index.get(term).get(1).getPositions().get(1));
		
	}
	
	@Test
	public void testDecode() {
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
		
		String term = "a";
		index.put(term, plist);
		System.out.println(index);
		
		encoder.encode(index);
		
		Assertions.assertSame(2, index.get(term).size());
		Assertions.assertSame(1, index.get(term).get(0).getDocumentId());
		Assertions.assertSame(2, index.get(term).get(1).getDocumentId());
		Assertions.assertSame(1, index.get(term).get(0).getPositions().get(0));
		Assertions.assertSame(1, index.get(term).get(0).getPositions().get(1));
		Assertions.assertSame(3, index.get(term).get(1).getPositions().get(0));
		Assertions.assertSame(2, index.get(term).get(1).getPositions().get(1));
		
		encoder.decode(index);
		
		Assertions.assertSame(2, index.get(term).size());
		Assertions.assertSame(1, index.get(term).get(0).getDocumentId());
		Assertions.assertSame(3, index.get(term).get(1).getDocumentId());
		Assertions.assertSame(1, index.get(term).get(0).getPositions().get(0));
		Assertions.assertSame(2, index.get(term).get(0).getPositions().get(1));
		Assertions.assertSame(3, index.get(term).get(1).getPositions().get(0));
		Assertions.assertSame(5, index.get(term).get(1).getPositions().get(1));
		
	}

}
