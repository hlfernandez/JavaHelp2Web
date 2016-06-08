package es.uvigo.ei.sing.io;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.codehaus.plexus.util.FileUtils;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;

public class HelpWriter {

	public static void toWebHelp(TocItem root, Map<String, String> targetToURL,
		File destFile, File helpDir) throws IOException 
	{
		StringBuilder sb = new StringBuilder();
		sb
			.append("<html>")
			.append("<head>")
			.append("<meta charset=\"UTF-8\"/>")
			.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/help.css\"/>")
			.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/jquery-ui-1.10.3.custom.min.css\"/>")
			.append("<script type=\"text/javascript\" src=\"js/jquery-1.10.2.min.js\"></script>")
			.append("<script type=\"text/javascript\" src=\"js/jquery-ui-1.10.3.custom.min.js\"></script>")
			.append("<script type=\"text/javascript\" src=\"js/help.js\"></script>")
			.append("<script type=\"text/javascript\" src=\"js/anchors.js\"></script>")
			.append("<script type=\"text/javascript\">")
			.append("$(document).ready(function() {")
			.append("initManual();")
			.append("});")
			.append("</script>")
			.append("<title>")
			.append(root.getText())
			.append("</title>")
			.append("</head>")
			.append("<body>")
			.append("<div id=\"main_container\">")
			.append("<div id=\"div-body\" class=\"container_16\">")
			.append("<div class=\"grid_16\">")
			.append("<div id=\"tabs\">")
			.append(tabsList(root.getItems()))
			.append(indexTab(root.getItems()))
			.append(tabs(root.getItems(), targetToURL, helpDir))
			.append("</div>")
			.append("</div>")
			.append("</div>")
			.append("</div>")
			.append("</body>")
			.append("</html>")
			.append("");
		prettyPrintHTML(sb.toString(), destFile);
	}

	private static String tabsList(List<TocItem> items) {
		StringBuilder sb = new StringBuilder();
		sb.append("<ul class=\"tablist\">");
		sb
			.append("<li><a href=\"#")
			.append("index")
			.append("Tab\">")
			.append("Index")
			.append("</a></li>\n");
		
		for(TocItem item : items) {
			sb
				.append("<li><a href=\"#")
				.append(item.getAnchor())
				.append("Tab\">")
				.append(item.getText())
				.append("</a></li>\n");
		}
		sb.append("</ul>");
		return sb.toString();
	}

	private static String indexTab(List<TocItem> items) {
		StringBuilder sb = new StringBuilder();
		sb
			.append("<div id=\"indexTab\">")
			.append("<p>Table of contents:</p>")
			.append("<ul class=\"contentlist\">");
		
		for (TocItem item : items) {
			sb
				.append("<li><a href=\"#")
				.append(item.getAnchor())
				.append("\" data-tabindex=\"")
				.append(items.indexOf(item) + 1)
				.append("\">")
				.append(item.getText())
				.append("</a>");
			appendSubitemsList(sb, item.getItems(), items.indexOf(item) + 1);
			sb.append("</li>");
		}
		
		sb
			.append("</ul>")
			.append("</div>");
		
		return sb.toString();
	}
	
	private static void appendSubitemsList(StringBuilder sb,
		List<TocItem> items, int index) 
	{
		if(!items.isEmpty()) {
			sb.append("<ul>");
			for (TocItem item : items) {
				sb
					.append("<li><a href=\"#")
					.append(item.getAnchor())
					.append("\" data-tabindex=\"")
					.append(index)
					.append("\">")
					.append(item.getText())
					.append("</a>");
				appendSubitemsList(sb, item.getItems(), index);
				sb.append("</li>");
			}
			sb.append("</ul>");
		}
	}

	private static String tabs(List<TocItem> items, Map<String, String> targetToURL, File helpDir) {
		StringBuilder sb = new StringBuilder();
		for(TocItem item : items) {
			sb.append(tab(item, targetToURL, helpDir));
		}
		return sb.toString();
	}
	
	private static String tab(TocItem item, Map<String, String> targetToURL, File helpDir) {
		StringBuilder sb = new StringBuilder();
		sb
			.append("<div id=\"")
			.append(item.getAnchor() + "Tab")
			.append("\">");
		
		sb.append(includeItem(item, targetToURL, helpDir));
		
		sb.append("</div>");
		return sb.toString();
	}

	private static String includeItem(TocItem item,
			Map<String, String> targetToURL, File helpDir) {
		StringBuilder sb = new StringBuilder();
		if(targetToURL.containsKey(item.getTarget())) {
			sb
				.append("<div id=\"")
				.append(item.getAnchor())
				.append("\">")
				.append(loadFile(helpDir, targetToURL.get(item.getTarget())))
				.append("</div>");
		}
		for(TocItem subItem : item.getItems()) {
			sb.append(includeItem(subItem, targetToURL, helpDir));
		}
		return sb.toString();
	}
	
	private static String loadFile(File helpDir, String file) {
		StringBuilder sb = new StringBuilder();
		try {
			for (String line : FileUtils.loadFile(new File(helpDir, file))) {
				sb.append(line);
			}
		} catch (IOException e) {
		}
		return sb.toString();
	}
	
	public static void prettyPrintHTML(String rawHTML, File destFile) throws IOException
	{    
	    Tidy tidy = new Tidy();
	    tidy.setQuiet(true);
	    tidy.setShowWarnings(false);
	    tidy.setXHTML(true);
	    tidy.setIndentContent(true);
	    tidy.setPrintBodyOnly(true);
	    tidy.setTidyMark(false);
	    tidy.setOnlyErrors(false);

	    Document htmlDOM = tidy.parseDOM(new ByteArrayInputStream(rawHTML.getBytes()), null);

	    OutputStream out = new FileOutputStream(destFile);
	    tidy.pprint(htmlDOM, out);
	}
}
