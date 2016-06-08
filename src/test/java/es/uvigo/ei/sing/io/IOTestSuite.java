package es.uvigo.ei.sing.io;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	LinkToAnchorHelperTest.class,
	MapParserTest.class,
	TocParserTest.class
})
public class IOTestSuite {

}
