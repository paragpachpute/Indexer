package umass.searchengine.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import umass.searchengine.encoder.Compressor;
import umass.searchengine.encoder.CompressorFactory;
import umass.searchengine.model.InvertedIndex;
import umass.searchengine.model.LookupTable;

public class FileUtils {

	public static void writeFile(String fileName, ByteBuffer output) throws IOException {
		String filePath = "./src/data/";
		File origFile = new File(filePath + fileName);
		FileOutputStream fos = new FileOutputStream(origFile);
		fos.write(output.array());
		fos.close();
	}

	public static byte[] readFile(String fileName) throws IOException {
		try (FileInputStream fis = new FileInputStream(fileName)){
			return fis.readAllBytes();
		}
	}

	public static List<String> readLines(String fileName) throws IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			return reader.lines().collect(Collectors.toList());
		}
	}

	public static void writeLines(String fileName, StringBuffer output) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
		writer.append(output);
		writer.close();
	}

	public static InvertedIndex readIndex(boolean isCompressed, String indexFileName, LookupTable lookupTable)
			throws IOException {
		int vocabSize = 15635;
		String filePath = "./src/data/";
		byte[] input = FileUtils.readFile(filePath + indexFileName);
		
		Compressor compressor = CompressorFactory.getCompressor(isCompressed);
		InvertedIndex invertedIndex = compressor.decompress(ByteBuffer.wrap(input), lookupTable);
		Assertions.assertEquals(vocabSize, invertedIndex.size(), "Inverted index retreived is not correct");
		
		return invertedIndex;
	}

	public static LookupTable readLookupTable(boolean isCompressed) throws IOException {
		int vocabSize = 15635;
		String filePath = "./src/data/";
		String indexFileName = isCompressed ? "compressedIndex" : "uncompressedIndex";
		String lookupFileName = "lookup_" + indexFileName;

		ObjectMapper mapper = new ObjectMapper();
		LookupTable lookupTable = mapper.readValue(new File(filePath + lookupFileName),
				new TypeReference<LookupTable>() {
				});
		Assertions.assertEquals(vocabSize, lookupTable.size(), "Lookup table retreived is not correct");
		return lookupTable;
	}

}
