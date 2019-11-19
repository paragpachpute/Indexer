package test.umass.searchengine.query;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import umass.searchengine.indexer.AuxiliaryTableCreator;
import umass.searchengine.indexer.InvertedIndex;
import umass.searchengine.model.LookupTable;
import umass.searchengine.model.Posting;
import umass.searchengine.model.PostingList;
import umass.searchengine.query.DiceCoeff;

public class TestDiceCoeff {
	
	DiceCoeff dice = new DiceCoeff();
	
	@Test
	public void testGetAdjecentcyScore() {
		List<Integer> list1 = new ArrayList<>();
		list1.add(1);
		list1.add(2);
		
		List<Integer> list2 = new ArrayList<>();
		list2.add(2);
		list2.add(3);
		list2.add(4);
		
		Assertions.assertSame(2, dice.getAdjecentcyScore(list1, list2));
		Assertions.assertSame(1, list1.get(0));
		Assertions.assertSame(2, list1.get(1));

		Assertions.assertSame(2, list2.get(0));
		Assertions.assertSame(3, list2.get(1));
		Assertions.assertSame(4, list2.get(2));
	}
	
	@Test
	public void testGetMostSimilarWord() {
		InvertedIndex index = new InvertedIndex();
		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(2);
		Posting p = new Posting(1);
		list.forEach(i -> p.addPosition(i));

		List<Integer> list2 = new ArrayList<>();
		list2.add(2);
		list2.add(3);
		Posting p2 = new Posting(1);
		list2.forEach(i -> p2.addPosition(i));
		
		PostingList plist = new PostingList();
		plist.add(p);
		PostingList plist2 = new PostingList();
		plist2.add(p2);
		
		index.put("a", plist);
		index.put("b", plist2);
		index.put("c", plist);
		index.put("d", plist2);
		System.out.println(index);

		LookupTable lookup = AuxiliaryTableCreator.createLookupTable(index);
		System.out.println("word: " + dice.getMostSimilarWord(index, lookup, "a"));
	}

}
