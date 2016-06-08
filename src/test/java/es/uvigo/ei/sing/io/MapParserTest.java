package es.uvigo.ei.sing.io;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

public class MapParserTest {
	private static final File MAP_FILE = new File("src/test/resources/map.xml");
	
	@Test
	public void mapParserTest() throws ParserConfigurationException, SAXException, IOException {
		Map<String, String> targetToURL = MapParser.parseMap(MAP_FILE);
		assertEquals(2, targetToURL.size());
		assertEquals("HTML/introduction.html", targetToURL.get("introduction"));
		assertEquals("HTML/firstsection.html", targetToURL.get("firstsection"));
	}
}
