package umass.searchengine.main;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import com.fasterxml.jackson.databind.ObjectMapper;

import umass.searchengine.encoder.Compressor;
import umass.searchengine.encoder.CompressorFactory;
import umass.searchengine.indexer.AuxiliaryTableCreator;
import umass.searchengine.indexer.DocumentVectorIndex;
import umass.searchengine.indexer.IndexCreator;
import umass.searchengine.indexer.InvertedIndex;
import umass.searchengine.loader.DatasetLoader;
import umass.searchengine.model.Corpus;
import umass.searchengine.model.LookupTable;
import umass.searchengine.utils.FileUtils;
import umass.searchengine.utils.IndexerUtils;

public class BuildAndWriteIndex {

	public static void main(String[] args) throws IOException {
		Corpus corpus = new DatasetLoader().load();
		IndexCreator indexCreator = new IndexCreator().create(corpus);
		InvertedIndex invertedIndex = indexCreator.getInvertedIndex();
		DocumentVectorIndex index = indexCreator.getDocumentIndex();
		LookupTable lookup = AuxiliaryTableCreator.createLookupTable(invertedIndex);
		System.out.println("Index created");

		String fileName = "documentVectorIndex.json";
		String json = new ObjectMapper().writeValueAsString(index);
		FileUtils.writeLines(fileName, new StringBuffer(json));
		System.out.println("Written Document Vector Index");
		
		fileName = "uncompressedIndex";
		boolean compressionReqd = false;

		ByteBuffer output = ByteBuffer.allocate(IndexerUtils.getSizeOfIndexerInBytes(invertedIndex));
		Compressor compressor = CompressorFactory.getCompressor(compressionReqd);
		compressor.compress(invertedIndex, lookup, output);
		System.out.println("Converted to bytes");

		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(new File("./src/data/lookup_" + fileName), lookup);
		FileUtils.writeFile(fileName, output);
		System.out.println("Written");
		
		fileName = "compressedIndex";
		compressionReqd = true;

		output = ByteBuffer.allocate(IndexerUtils.getSizeOfIndexerInBytes(invertedIndex));
		compressor = CompressorFactory.getCompressor(compressionReqd);
		compressor.compress(invertedIndex, lookup, output);
		System.out.println("Converted to bytes");

		mapper = new ObjectMapper();
		mapper.writeValue(new File("./src/data/lookup_" + fileName), lookup);
		FileUtils.writeFile(fileName, output);
		System.out.println("Written");
	}
}
