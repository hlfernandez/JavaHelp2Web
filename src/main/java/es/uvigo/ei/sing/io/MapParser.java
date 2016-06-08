package es.uvigo.ei.sing.io;

import static es.uvigo.ei.sing.io.XMLParser.parseXML;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MapParser {

	private static final String ATTR_URL = "url";
	private static final String ATTR_TARGET = "target";
	private static final String MAP_ID = "mapID";

	public static Map<String, String> parseMap(File file)
		throws ParserConfigurationException, SAXException, IOException 
	{
		Document dom = parseXML(file);
		NodeList mapIds = dom.getDocumentElement().getElementsByTagName(MAP_ID);
		return createTargetToUrlMap(mapIds);
	}

	private static Map<String, String> createTargetToUrlMap(NodeList mapIds) {
		Map<String, String> targetToUrl = new HashMap<String, String>();

		for (int i = 0; i < mapIds.getLength(); i++) {
			Element mapId = (Element) mapIds.item(i);
			targetToUrl.put(
				mapId.getAttribute(ATTR_TARGET), mapId.getAttribute(ATTR_URL)
			);
		}

		return targetToUrl;
	}
}
