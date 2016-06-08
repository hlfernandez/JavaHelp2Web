package es.uvigo.ei.sing;

import java.io.File;
import java.io.InputStream;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo( 
	name = "java-help-to-web", 
	defaultPhase = LifecyclePhase.PROCESS_SOURCES 
)
public class JavaHelpToWebMojo extends AbstractMojo {

	private final InputStream helpZip = 
		getClass().getResourceAsStream("/web-help.zip");

	@Parameter(defaultValue = "${project.build.directory}", required = true)
	private File outputDirectory;

	@Parameter(defaultValue = "src/main/resources/help", required = true, 
		property = "jhw.helpDir")
	private File helpDir;

	@Parameter(defaultValue = "toc.xml", required = true, 
		property = "jhw.tocFile")
	private String tocFile;

	@Parameter(defaultValue = "map.xml", required = true, 
		property = "jhw.mapFile")
	private String mapFile;

	@Parameter(defaultValue = "HTML", required = true, 
		property = "jhw.htmlDir")
	private String htmlDir;

	public void execute() throws MojoExecutionException {
		JavaToWebHelp.toWebHelp(
			outputDirectory, helpZip, tocFile, mapFile,	htmlDir, helpDir);
	}
}