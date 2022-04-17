[![Maven](https://img.shields.io/maven-central/v/io.github.cleydyr/dart-sass-maven-plugin.svg)](https://repo1.maven.org/maven2/io/github/cleydyr/dart-sass-maven-plugin/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

# Dart Sass Maven Plugin

A Maven plugin that allows to compile SASS using Dart Sass

<img height="48" src="https://sass-lang.com/assets/img/logos/logo-b6e1ef6e.svg">

## Usage
```xml
<build>
...
        <plugins>
            <plugin>
                <groupId>io.github.cleydyr</groupId>
                <artifactId>dart-sass-maven-plugin</artifactId>
                <version>0.1.1</version>
                <executions>
                    <execution>
                        <id>generate-css-using-sass</id>
                        <phase>generate-resources</phase>
                        <goals>
                            <goal>compile-sass</goal>
                        </goals>
                        <configuration>
                            <inputFolder>${basedir}/src/main/webapp/resources/css</inputFolder>
                            <outputFolder>${basedir}/src/main/webapp/resources/css</outputFolder>
                            <update>true</update>
                            <noSourceMap>true</noSourceMap>
                            <style>COMPRESSED</style>
                            <quiet>false</quiet>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
...
  </plugins>
</build>
```

## Goals

### compile-sass
Goal that compiles a set of sass/scss files from an input directory to an output directory.

Full name: `io.github.cleydyr:dart-sass-maven-plugin:0.1.1:compile-sass`

Binds by default to the lifecycle phase: `process-resources`.

#### Parameter details
`<color>`

This flag tells Sass to emit terminal colors. By default, it will emit colors if it looks like it's being run on a terminal that supports them. Set it to false to tell Sass to not emit colors.   
**Type**: boolean  
**Required**: No  
**Default**: true

`<embedSourceMap>`

This flag tells Sass to embed the contents of the source map file in the generated CSS, rather than creating a separate file and linking to it from the CSS.   
**Type**: boolean  
**Required**: No  
**Default**: false

`<embedSources>`

This flag tells Sass to embed the entire contents of the Sass files that contributed to the generated CSS in the source map. This may produce very large source maps, but it guarantees that the source will be available on any computer no matter how the CSS is served.   
**Type**: boolean  
**Required**: No  
**Default**: false

`<errorCSS>`

This flag tells Sass whether to emit a CSS file when an error occurs during compilation. This CSS file describes the error in a comment and in the content property of body::before, so that you can see the error message in the browser without needing to switch back to the terminal.
By default, error CSS is enabled if you're compiling to at least one file on disk (as opposed to standard output). You can activate errorCSS explicitly to enable it even when you're compiling to standard out (not supported by this Maven plugin), or set it explicitly to false to disable it everywhere. When it's disabled, the update flag and watch flag (the latter being not yet supported by this Maven plugin) will delete CSS files instead when an error occurs.   
**Type**: boolean  
**Required**: No  
**Default**: true

`<inputFolder>`

Path to the folder where the sass/scss are located.   
**Type**: java.io.File  
**Required**: No  
**Default**: src/main/sass

`<loadPaths>`

Paths from where additional load path for Sass to look for stylesheets will be loaded. It can be passed multiple times to provide multiple load paths. Earlier load paths will take precedence over later ones.   
**Type**: java.util.List  
**Required**: No  

`<noCharset>`

This flag tells Sass never to emit a @charset declaration or a UTF-8 byte-order mark. By default, or if the charset flag is activated, Sass will insert either a @charset declaration (in expanded output mode) or a byte-order mark (in compressed output mode) if the stylesheet contains any non-ASCII characters.   
**Type**: boolean  
**Required**: No  
**Default**: false

`<noSourceMap>`

If the noSourceMap flag is set to true, Sass won't generate any source maps. It will void other source map options (namely sourceMapURLs, embedSources and embedSourceMap) if explicitly set.
**Type**: boolean  
**Required**: No  
**Default**: false

`<noUnicode>`

This flag tells Sass only to emit ASCII characters to the terminal as part of error messages. By default, Sass will emit non-ASCII characters for these messages. This flag does not affect the CSS output.   
**Type**: boolean  
**Required**: No  
**Default**: false

`<outputFolder>`

Path to the folder where the css and source map files will be created.   
**Type**: java.io.File  
**Required**: No  
User Property: project.build.directory

`<quiet>`

This flag tells Sass not to emit any warnings when compiling. By default, Sass emits warnings when deprecated features are used or when the @warn rule is encountered. It also silences the @debug rule.   
**Type**: boolean  
**Required**: No  
**Default**: true

`<quietDeps>`

This flag tells Sass not to emit deprecation warnings that come from dependencies. It considers any file that's transitively imported through a load path to be a "dependency". This flag doesn't affect the @warn rule or the @debug rule.   
**Type**: boolean  
**Required**: No  
**Default**: false

`<sourceMapURLs>`

This option controls how the source maps that Sass generates link back to the Sass files that contributed to the generated CSS. Dart Sass supports two types of URLs:
relative (the default) uses relative URLs from the location of the source map file to the locations of the Sass source file;
and
absolute uses the absolute file: URLs of the Sass source files. Note that absolute URLs will only work on the same computer that the CSS was compiled.
Use either RELATIVE or ABSOLUTE in the plugin configuration.   
**Type**: com.github.cleydyr.dart.command.enums.SourceMapURLs  
**Required**: No  
**Default**: RELATIVE

`<stopOnError>`

This flag tells Sass to stop compiling immediately when an error is detected, rather than trying to compile other Sass files that may not contain errors. It's mostly useful in many-to-many mode (which is the mode currently supported by this Maven plugin).   
**Type**: boolean  
**Required**: No  
**Default**: false

`<style>`

This option controls the output style of the resulting CSS. Dart Sass supports two output styles:
expanded (the default), which writes each selector and declaration on its own line; and
compressed, which removes as many extra characters as possible, and writes the entire stylesheet on a single line.
Use either EXPANDED or COMPRESSED in the plugin configuration.   
**Type**: com.github.cleydyr.dart.command.enums.Style  
**Required**: No  
**Default**: EXPANDED

`<trace>`

This flag tells Sass to print the full Dart stack trace when an error is encountered. It's used by the Sass team for debugging errors.   
**Type**: boolean  
**Required**: No  
**Default**: false

`<update>`

If the this flag is set to true, Sass will only compile stylesheets whose dependencies have been modified more recently than the corresponding CSS file was generated. It will also print status messages when updating stylesheets.   
**Type**: boolean  
**Required**: No  
**Default**: false
