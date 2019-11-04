package test.umass.searchengine.query;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import umass.searchengine.indexer.AuxiliaryTableCreator;
import umass.searchengine.model.DocumentScore;
import umass.searchengine.model.InvertedIndex;
import umass.searchengine.model.Posting;
import umass.searchengine.model.PostingList;
import umass.searchengine.query.DocumentAtATime;
import umass.searchengine.ranking.CountScores;

public class TestDocumentAtATime {
	
	DocumentAtATime documentAtATime = new DocumentAtATime(new CountScores());
	
	@Test
	public void testQuery() {
		InvertedIndex index = new InvertedIndex();
		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(2);
		Posting p = new Posting(1);
		list.forEach(i -> p.addPosition(i));

		List<Integer> list2 = new ArrayList<>();
		list2.add(3);
		list2.add(5);
		Posting p2 = new Posting(2);
		list2.forEach(i -> p2.addPosition(i));
		
		PostingList plist = new PostingList();
		plist.add(p);
		plist.add(p2);
		PostingList plist2 = new PostingList();
		plist2.add(p2);
		
		index.put("a", plist);
		index.put("b", plist2);
		System.out.println(index);
		
		String[] terms = new String[] {"a", "b"};
		List<DocumentScore> docIds = documentAtATime.query(index, terms, AuxiliaryTableCreator.createStatsTable(index), 1);
		
		Assertions.assertEquals(1, docIds.size());
		Assertions.assertSame(2, docIds.get(0).getDocId());
	}

}
