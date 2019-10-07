package umass.searchengine.loader;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import umass.searchengine.model.Corpus;

public class DatasetLoader {
	
	/**
	 * Uses jackson to load dataset
	 * @return corpus
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public Corpus load() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		Corpus corpus = mapper.readValue(new File("src/data/shakespeare-scenes.json"), Corpus.class);
		return corpus;
	}

}
