
/**
 * @author parag
 *
 */
module search_engine {
	requires com.fasterxml.jackson.databind;
	requires com.fasterxml.jackson.core;
	requires junit;
	requires org.junit.jupiter.api;
	requires java.base;
	
	exports  umass.searchengine.model;
	exports  test.umass.searchengine.encoder;
	exports	 test.umass.searchengine.query;
	exports  test.umass.searchengine.inference.network.proximity;
}