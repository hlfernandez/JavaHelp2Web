package es.uvigo.ei.sing.io;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

public class LinkToAnchorHelperTest {
	public static final File HELP_DIR = new File("src/test/resources/help");
	public static final File ANCHORS = new File("src/test/resources/anchors.js");
	private static final String[] LINKS = new String[]{
		"firstsection.html","introduction.html", "secondsection.html"};
	
	private static final Map<String, String> LINK_TO_ANCHOR = new HashMap<String, String>();
	
	static {
		LINK_TO_ANCHOR.put("firstsection.html","2#firstsection");
		LINK_TO_ANCHOR.put("introduction.html","1#introduction");
		LINK_TO_ANCHOR.put("secondsection.html","3#secondsection");
	}
	
	@Test
	public void extractLinksTest() throws IOException {
		Set<String> links = LinkToAnchorHelper.extractLinks(HELP_DIR);
		assertEquals(new HashSet<String>(Arrays.asList(LINKS)), links);
	}
	
	@Test
	public void generateAnchorsTest() throws IOException,
		ParserConfigurationException, SAXException 
	{
		Set<String> links = LinkToAnchorHelper.extractLinks(HELP_DIR);
		Map<String, String> targetToURL = 
			MapParser.parseMap(new File(HELP_DIR, "map.xml"));
		TocItem item = TocParser.parseTOC(new File(HELP_DIR, "toc.xml"));

		Map<String, String> linkToAnchor = LinkToAnchorHelper.generateAnchors(
			links, targetToURL, item);
		assertEquals(LINK_TO_ANCHOR, linkToAnchor);
	}
	
	@Test
	public void writeAnchorsTest() throws IOException,
		ParserConfigurationException, SAXException 
	{
		File tempFile = getTempFile();
		TocItem root = TocParser.parseTOC(new File(HELP_DIR, "toc.xml"));
		Map<String, String> targetToURL = 
			MapParser.parseMap(new File(HELP_DIR, "map.xml"));
		LinkToAnchorHelper.writeAnchors(tempFile, HELP_DIR, root, targetToURL);
		
		assertEquals(
			Files.readAllLines(ANCHORS.toPath()), 
			Files.readAllLines(tempFile.toPath())
		);
	}

	private File getTempFile() throws IOException {
		File toret = File.createTempFile("anchors", "js");
		toret.deleteOnExit();
		return toret;
	}
}
