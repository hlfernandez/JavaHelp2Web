package es.uvigo.ei.sing;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.codehaus.plexus.util.FileUtils;
import org.xml.sax.SAXException;

import es.uvigo.ei.sing.io.HelpWriter;
import es.uvigo.ei.sing.io.LinkToAnchorHelper;
import es.uvigo.ei.sing.io.MapParser;
import es.uvigo.ei.sing.io.TocItem;
import es.uvigo.ei.sing.io.TocParser;

public class JavaToWebHelp {

	public static void toWebHelp(File buildDirectory, InputStream helpZip,
			String tocFile, String mapFile, String htmlDir, File helpDir
	) {
		try {
			extractZipFile(buildDirectory, helpZip);
			
			File outputDirectory = new File(buildDirectory, "web-help");
			FileUtils.copyDirectoryStructure(
				new File(helpDir, htmlDir), new File(outputDirectory, htmlDir));
			
			createWebHelp(tocFile, mapFile, helpDir, outputDirectory);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void createWebHelp(String tocFile, String mapFile,
		File helpDir, File outputDirectory)
		throws ParserConfigurationException, SAXException, IOException 
	{
		File realTocFile = new File(helpDir, tocFile);
		TocItem root = TocParser.parseTOC(realTocFile);
		Map<String, String> targetToURL = 
			MapParser.parseMap(new File(helpDir, mapFile));
		
		HelpWriter.toWebHelp(root, targetToURL, new File(outputDirectory,
			"help.html"), helpDir);
		
		LinkToAnchorHelper.writeAnchors(new File(outputDirectory,
			"/js/anchors.js"), outputDirectory, root, targetToURL);
	}

	private static void extractZipFile(File buildDirectory, InputStream helpZip)
		throws IOException, ZipException 
	{
		File file = new File(buildDirectory, "web-help.zip");
		Files.copy(helpZip, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
		ZipFile zipFile = new ZipFile(file);
		zipFile.extractAll(buildDirectory.getAbsolutePath());
		file.delete();
	}
}
