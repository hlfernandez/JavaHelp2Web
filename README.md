JavaHelp to web maven plugin
==================================

A maven plugin to automatically convert a standard JavaHelp into a simple HTML website.

![Screenshot](https://raw.githubusercontent.com/hlfernandez/JavaHelp2Web/master/screenshots/screenshot.png)

JavaHelp structure
----

This is the recommended JavaHelp structure:
```
├── help/
│   ├── toc.xml
│   ├── map.xml
│   ├── HTML/
│   │   ├── asection.html
│   │   ├── Images/
│   │   │   ├── aimage.png
```

Plugin configuration
----
The following table shows the plugin's parameters. Although you can change any of them, default values for all are provided in order to match the recommended JavaHelp structure.

Element       | Description                                                     | Required
------------- | ----------------------------------------------------------------| -----------------------------------------
helpDir       | Project's directory containing the standard JavaHelp files      | No; default to "src/main/resources/help"
tocFile       | Name of the toc file                                            | No; defaults to "toc.xml"
mapFile       | Name of the map file                                            | No; defaults to "map.xml"
htmlDir       | Name of the directory containing the HTML files                 | No; defaults to "HTML"

Installation
----

Clone the project with `git clone https://github.com/hlfernandez/JavaHelp2Web.git` and install it using `mvn install`.

Usage
----
The plugin can be used directly from the command line with `mvn es.uvigo.ei.sing:java-help-to-web-maven-plugin:1.0:java-help-to-web`.

Alternatively, you can attach it to the build lifecycle. Here is an example:


```xml
      <build>
        <plugins>
          <plugin>
            <groupId>es.uvigo.ei.sing</groupId>
            <artifactId>java-help-to-web-maven-plugin</artifactId>
            <version>1.0</version>
            <executions>
              <execution>
                <phase>compile</phase>
                <goals>
                  <goal>java-help-to-web</goal>
                </goals>
              </execution>
            </executions>
          </plugin>
        </plugins>
      </build>
```