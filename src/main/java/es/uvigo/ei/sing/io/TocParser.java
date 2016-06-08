package es.uvigo.ei.sing.io;

import static es.uvigo.ei.sing.io.XMLParser.parseXML;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TocParser {

	private static final String ATTR_TARGET = "target";
	private static final String ATTR_TEXT = "text";
	private static final String TOCITEM = "tocitem";

	public static TocItem parseTOC(File file)
		throws ParserConfigurationException, SAXException, IOException 
	{
		Document dom = parseXML(file);
		Element rootTOC = (Element) dom.getDocumentElement()
			.getElementsByTagName(TOCITEM).item(0);
		
		return new TocItem(
			rootTOC.getAttribute(ATTR_TEXT).toString(), 
			getTOCItems(rootTOC.getChildNodes())
		);
	}

	private static List<TocItem> getTOCItems(NodeList items) {
		List<TocItem> toret = new ArrayList<TocItem>();
		for (int i = 0; i < items.getLength(); i++) {
			if (items.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element tocItem = (Element) items.item(i);
				toret.add(
					new TocItem(
						tocItem.getAttribute(ATTR_TEXT).toString(), 
						tocItem.getAttribute(ATTR_TARGET).toString(), 
						getTOCItems(tocItem.getChildNodes())
					)
				);
			}
		}
		return toret;
	}
}