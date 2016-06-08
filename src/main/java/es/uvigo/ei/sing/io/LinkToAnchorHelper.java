package es.uvigo.ei.sing.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LinkToAnchorHelper {

	public static Set<String> extractLinks(File dir) throws IOException {
		List<File> htmlFiles = findHtmlFiles(dir);
		return extractLinks(htmlFiles);
	}
	
	private static Set<String> extractLinks(List<File> htmlFiles)
		throws IOException 
	{
		Set<String> toret = new HashSet<String>();
		for (File f : htmlFiles) {
			Document doc = Jsoup.parse(f, "UTF-8");
			Elements links = doc.select("a");
			for (Element link : links) {
				toret.add(link.attr("href"));
			}
		}
		return toret;
	}

	private static List<File> findHtmlFiles(File dir) {
		return addHtmlFiles(new ArrayList<File>(), dir);
	}

	private static List<File> addHtmlFiles(List<File> files, File dir) {
		if (!dir.isDirectory()) {
			if (dir.getName().endsWith(".html"))
				files.add(dir);
			return files;
		}

		for (File file : dir.listFiles()) {
			addHtmlFiles(files, file);
		}
		return files;
	}

	private static Map<String, String> generateAnchors(File helpDir,
		Map<String, String> targetToURL, TocItem rootItem
	) throws IOException {
		return generateAnchors(extractLinks(helpDir), targetToURL, rootItem);
	}
	
	public static Map<String, String> generateAnchors(Set<String> links,
		Map<String, String> targetToURL, TocItem rootItem
	) {
		Map<String, String> toret = new HashMap<String, String>();
		for (String link : links) {
			for (Entry<String, String> entry : targetToURL.entrySet()) {
				if (entry.getValue().contains(link)) {
					String target = entry.getKey();
					
					List<TocItem> rootItems = rootItem.getItems();
					for (TocItem item : rootItems) {
						Optional<TocItem> container = 
							tocItemContains(item, target);
						if(container.isPresent()) {
							toret.put(
								link, 
								jsAnchor(rootItems, item, container));
							break;
						}
					}
				}
			}
		}
		return toret;
	}

	private static String jsAnchor(List<TocItem> rootItems, TocItem item,
			Optional<TocItem> container) {
		return 
			String.valueOf(rootItems.indexOf(item)+1) + 
			"#" + container.get().getAnchor();
	}

	private static Optional<TocItem> tocItemContains(TocItem item, String target) {
		if (item.getTarget().equals(target)) {
			return Optional.of(item);
		} else if (!item.getItems().isEmpty()) {
			for (TocItem subitem : item.getItems()) {
				Optional<TocItem> container = tocItemContains(subitem, target);
				if (container.isPresent()) {
					return container;
				}
			}
		}
		return Optional.empty(); 
	}

	public static void writeAnchors(File anchorsFile, File helpDir, TocItem root,
		Map<String, String> targetToURL) throws IOException 
	{
		Map<String, String> anchors = generateAnchors(helpDir, targetToURL, root);
		BufferedWriter out = new BufferedWriter(new FileWriter(anchorsFile));
		out.append("var linkToAnchor = new Array();\n");
		for(Entry<String, String> entry : anchors.entrySet()) {
			appendAnchor(out, entry);
		}
		out.flush();
		out.close();
	}
	
	private static final void appendAnchor(BufferedWriter out,
		Entry<String, String> entry) throws IOException 
	{
		out
			.append("linkToAnchor[\"")
			.append(entry.getKey())
			.append("\"] = \"")
			.append(entry.getValue())
			.append("\";\n");
	}
}
