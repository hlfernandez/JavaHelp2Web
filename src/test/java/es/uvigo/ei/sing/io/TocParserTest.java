package es.uvigo.ei.sing.io;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.xml.sax.SAXException;

@RunWith(Parameterized.class)
public class TocParserTest {
	private static final File TOC_SIMPLE_FILE = new File("src/test/resources/toc-simple.xml");
	private static final TocItem TOC_SIMPLE = new TocItem("AIBench Help", 
		Arrays.asList(new TocItem[] {
			new TocItem("Introduction", "introduction"),
			new TocItem("First section", "firstsection")
	}));
	
	private static final File TOC_COMPLEX_FILE = new File("src/test/resources/toc-complex.xml");
	private static final TocItem TOC_COMPLEX = new TocItem("AIBench Help", 
		Arrays.asList(new TocItem[] {
			new TocItem("Introduction", "introduction"),
			new TocItem("First section", "firstsection", Arrays.asList(new TocItem[] {
					new TocItem("First subsection", "firstsection.firstsubsection")
			})),
			new TocItem("Second section", "secondsection", Arrays.asList(new TocItem[] {
					new TocItem("First subsection", "secondsection.firstsubsection")
			}))
	}));
	

	 @Parameterized.Parameters
	 public static Collection<Object[]> getParameters() {
		 return Arrays.asList(new Object[][] {
				 { TOC_SIMPLE_FILE, TOC_SIMPLE },
				 { TOC_COMPLEX_FILE, TOC_COMPLEX }
		 });
	 }
	 
	private File file;
	private TocItem tocItem;
	
	public TocParserTest(File file, TocItem tocItem) {
		this.file = file;
		this.tocItem = tocItem;
	}

	@Test
	public void parseTOC() throws ParserConfigurationException, SAXException, IOException {
		Assert.assertEquals(tocItem, TocParser.parseTOC(file));
	}
}
