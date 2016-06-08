package es.uvigo.ei.sing.io;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XMLParser {

	public static Document parseXML(File file) throws ParserConfigurationException,
		SAXException, IOException 
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setFeature(
			"http://apache.org/xml/features/nonvalidating/load-external-dtd",
			false);

		DocumentBuilder db = dbf.newDocumentBuilder();
		return db.parse(file);
	}
}
