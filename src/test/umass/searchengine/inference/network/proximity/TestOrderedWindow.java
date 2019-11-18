package test.umass.searchengine.inference.network.proximity;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import umass.searchengine.indexer.AuxiliaryTableCreator;
import umass.searchengine.indexer.IndexCreator;
import umass.searchengine.inference.network.proximity.OrderedWindow;
import umass.searchengine.inference.network.proximity.WindowNode;
import umass.searchengine.loader.DatasetLoader;
import umass.searchengine.model.CorpusStatistics;
import umass.searchengine.model.InvertedIndex;
import umass.searchengine.model.PostingList;
import umass.searchengine.ranking.Dirichlet;
import umass.searchengine.ranking.Scorer;

public class TestOrderedWindow {
	
	private WindowNode ow;
	private InvertedIndex index;
	private CorpusStatistics stats;
	private Scorer scorer;
	
	@Before
	public void setup() throws IOException {
		index = new IndexCreator().create(new DatasetLoader().load());
		stats = AuxiliaryTableCreator.createStatsTable(index);
		scorer = new Dirichlet(1500, stats.getNumOfTerms());
	}
	
	@Test
	public void testCreatePostingList() {
		List<String> terms = Arrays.asList("palace enter celia and rosalind celia".split("\\s"));
		ow = new OrderedWindow(terms, index, stats, 1, scorer);
		PostingList list = ow.createPostingList(terms, index, 1);
		Assertions.assertSame(2, list.size());
		Assertions.assertEquals(647, list.get(0).getDocumentId());
		Assertions.assertEquals(648, list.get(1).getDocumentId());
		
		terms = Arrays.asList("venice a street".split("\\s"));
		ow = new OrderedWindow(terms, index, stats, 1, scorer);
		list = ow.createPostingList(terms, index, 1);
		Assertions.assertSame(6, list.size());
		Assertions.assertEquals(0, list.get(0).getDocumentId());
		Assertions.assertEquals(600, list.get(1).getDocumentId());
		
		terms = Arrays.asList("to be or not to be".split("\\s"));
		ow = new OrderedWindow(terms, index, stats, 1, scorer);
		list = ow.createPostingList(terms, index, 1);
		Assertions.assertSame(1, list.size());
		Assertions.assertEquals(132, list.get(0).getDocumentId());
		
		terms = Arrays.asList("athens the palace of theseu".split("\\s"));
		ow = new OrderedWindow(terms, index, stats, 1, scorer);
		list = ow.createPostingList(terms, index, 1);
		Assertions.assertSame(2, list.size());
		Assertions.assertEquals(637, list.get(0).getDocumentId());
		Assertions.assertEquals(645, list.get(1).getDocumentId());
		
		terms = Arrays.asList("alas poor".split("\\s"));
		ow = new OrderedWindow(terms, index, stats, 1, scorer);
		list = ow.createPostingList(terms, index, 1);
		Assertions.assertSame(30, list.size());
		Assertions.assertEquals(1, list.getPostingsList().stream().filter(posting -> posting.getDocumentId() == 669).count());
		
		
	}

}
