package com.github.cleydyr.maven.plugin;

import com.github.cleydyr.dart.command.SassCommand;
import com.github.cleydyr.dart.command.builder.SassCommandBuilder;
import com.github.cleydyr.dart.command.enums.SourceMapURLs;
import com.github.cleydyr.dart.command.enums.Style;
import com.github.cleydyr.dart.command.exception.SassCommandException;
import com.github.cleydyr.dart.command.factory.SassCommandBuilderFactory;
import com.github.cleydyr.dart.command.files.FileCounter;
import com.github.cleydyr.dart.command.files.FileCounterException;
import com.github.cleydyr.dart.system.io.DartSassExecutableExtractor;
import com.github.cleydyr.dart.system.io.factory.DartSassExecutableExtractorFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import javax.inject.Inject;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Goal that compiles a set of sass/scss files from an input directory to an output directory.
 */
@Mojo(name = "compile-sass", defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
public class CompileSassMojo extends AbstractMojo {
    private FileCounter fileCounter;

    protected SassCommandBuilder sassCommandBuilder;

    protected DartSassExecutableExtractor dartSassExecutableExtractor;

    @Inject
    public CompileSassMojo(
            FileCounter fileCounter,
            SassCommandBuilderFactory sassCommandBuilderFactory,
            DartSassExecutableExtractorFactory dartSassExecutableExtractorFactory) {
        this.fileCounter = fileCounter;
        this.sassCommandBuilder = sassCommandBuilderFactory.getCommanderBuilder();
        this.dartSassExecutableExtractor = dartSassExecutableExtractorFactory.getDartSassExecutableExtractor();
    }

    /**
     * Path to the folder where the sass/scss are located.
     */
    @Parameter(defaultValue = "src/main/sass")
    private File inputFolder;

    /**
     * Path to the folder where the css and source map files will be created.
     */
    @Parameter(property = "project.build.directory")
    private File outputFolder;

    /**
     * Paths from where additional load path for Sass to look for stylesheets will be loaded. It can
     * be passed multiple times to provide multiple load paths. Earlier load paths will take
     * precedence over later ones.
     */
    @Parameter
    private List<File> loadPaths;

    /**
     * This option controls the output style of the resulting CSS. Dart Sass supports two output
     * styles:<br>
     * <ul>
     * <li>expanded (the default), which writes each selector and declaration on its own line; and
     * </li>
     * <li>compressed, which removes as many extra characters as possible, and writes the entire
     * stylesheet on a single line.</li>
     * </ul>
     * Use either <code>EXPANDED</code> or <code>COMPRESSED</code> in the plugin configuration.
     */
    @Parameter(defaultValue = "EXPANDED")
    private Style style;

    /**
     * This flag tells Sass never to emit a <code>@charset</code> declaration or a UTF-8 byte-order
     * mark. By default, or if the <code>charset</code> flag is activated, Sass will insert either a
     * <code>@charset</code> declaration (in expanded output mode) or a byte-order mark (in
     * compressed output mode) if the stylesheet contains any non-ASCII characters.
     */
    @Parameter(defaultValue = "false")
    private boolean noCharset;

    /**
     * This flag tells Sass whether to emit a CSS file when an error occurs during compilation.
     * This CSS file describes the error in a comment and in the content property of <code>
     * body::before,</code> so that you can see the error message in the browser without needing to
     * switch back to the terminal.<br>
     * By default, error CSS is enabled if you’re compiling to at least one file on disk (as opposed
     * to standard output). You can activate <code>errorCSS</code> explicitly to enable it even when
     * you’re compiling to standard out (not supported by this Maven plugin), or set it explicitly
     * to <code>false</code> to disable it everywhere. When it’s disabled, the <code>update</code>
     * flag and <code>watch</code> flag (the latter being not yet supported by this Maven plugin)
     * will delete CSS files instead when an error occurs.
     */
    @Parameter(defaultValue = "true")
    private boolean errorCSS;

    /**
     * If the this flag is set to <code>true</code>, Sass will only compile stylesheets whose
     * dependencies have been modified more recently than the corresponding CSS file was generated.
     * It will also print status messages when updating stylesheets.
     */
    @Parameter(defaultValue = "false")
    private boolean update;

    /**
     * If the <code>noSourceMap</code> flag is set to <code>true</code>, Sass won’t generate any
     * source maps. It cannot be passed along with other source map options (namely <code>
     * sourceMapURLs</code>, <code>embedSources</code> and <code>embedSourceMap</code>
     */
    @Parameter(defaultValue = "false")
    private boolean noSourceMap;

    /**
     * This option controls how the source maps that Sass generates link back to the Sass files that
     * contributed to the generated CSS. Dart Sass supports two types of URLs:
     * <ul>
     * <li> relative (the default) uses relative URLs from the location of the source map file to
     * the locations of the Sass source file;</li> and
     * <li>absolute uses the absolute file: URLs of the Sass source files. Note that absolute URLs
     * will only work on the same computer that the CSS was compiled.</li>
     * </ul>
     * Use either <code>RELATIVE</code> or <code>ABSOLUTE</code> in the plugin configuration.
     */
    @Parameter(defaultValue = "RELATIVE")
    private SourceMapURLs sourceMapURLs;

    /**
     * This flag tells Sass to embed the entire contents of the Sass files that contributed to the
     * generated CSS in the source map. This may produce very large source maps, but it guarantees
     * that the source will be available on any computer no matter how the CSS is served.
     */
    @Parameter(defaultValue = "false")
    private boolean embedSources;

    /**
     * This flag tells Sass to embed the contents of the source map file in the generated CSS,
     * rather than creating a separate file and linking to it from the CSS.
     */
    @Parameter(defaultValue = "false")
    private boolean embedSourceMap;

    /**
     * This flag tells Sass to stop compiling immediately when an error is detected, rather than
     * trying to compile other Sass files that may not contain errors. It’s mostly useful in
     * many-to-many mode (which is the mode currently supported by this Maven plugin).
     */
    @Parameter(defaultValue = "false")
    private boolean stopOnError;

    /**
     * This flag tells Sass to emit terminal colors. By default, it will emit colors if it looks
     * like it’s being run on a terminal that supports them. Set it to <code>false</code> to tell
     * Sass to not emit colors.
     */
    @Parameter(defaultValue = "true")
    private boolean color;

    /**
     * This flag tells Sass only to emit ASCII characters to the terminal as part of error messages.
     * By default, Sass will emit non-ASCII characters for these messages. This flag does not affect
     * the CSS output.
     */
    @Parameter(defaultValue = "false")
    private boolean noUnicode;

    /**
     * This flag tells Sass not to emit any warnings when compiling. By default, Sass emits warnings
     * when deprecated features are used or when the <code>@warn</code> rule is encountered. It also
     * silences the <code>@debug</code> rule.
     */
    @Parameter(defaultValue = "false")
    private boolean quiet;

    /**
     * This flag tells Sass not to emit deprecation warnings that come from dependencies. It
     * considers any file that’s transitively imported through a load path to be a “dependency”.
     * This flag doesn’t affect the <code>@warn</code> rule or the <code>@debug</code> rule.
     */
    @Parameter(defaultValue = "false")
    private boolean quietDeps;

    /**
     * This flag tells Sass to print the full Dart stack trace when an error is encountered. It’s
     * used by the Sass team for debugging errors.
     */
    @Parameter(defaultValue = "false")
    private boolean trace;

    public void execute() throws MojoExecutionException {
        extractExecutable();

        unsetIncompatibleOptions();

        SassCommand sassCommand = buildSassCommand();

        try {
            Instant start = Instant.now();

            sassCommand.execute();

            Instant finish = Instant.now();

            long elapsedTime = Duration.between(start, finish).toMillis();

            try {
                long fileCount = fileCounter.getProcessableFileCount(inputFolder.toPath());

                getLog().info("Compiled " + fileCount + " files in " + elapsedTime + " ms");
            } catch (FileCounterException fileCounterException) {
                throw new MojoExecutionException("Error while obtaining file count: ", fileCounterException);
            }
        } catch (SassCommandException sassCommandException) {
            throw new MojoExecutionException("Can't execute SASS command", sassCommandException);
        }
    }

    public void unsetIncompatibleOptions() {
        if (noSourceMap) {
            sourceMapURLs = null;
            embedSourceMap = false;
            embedSources = false;
        }
    }

    public void extractExecutable() throws MojoExecutionException {

        try {
            dartSassExecutableExtractor.extract();
        } catch (IOException ioException) {
            throw new MojoExecutionException("Unable to extract sass executable", ioException);
        }
    }

    protected SassCommand buildSassCommand() throws MojoExecutionException {
        if (loadPaths != null) {
            for (File loadPath : loadPaths) {
                sassCommandBuilder.withLoadPath(loadPath.toPath());
            }
        }

        setOptions();

        Path inputFolderPath = inputFolder.toPath();

        sassCommandBuilder.withPaths(inputFolderPath, outputFolder.toPath());

        try {
            return sassCommandBuilder.build();
        } catch (SassCommandException e) {
            throw new MojoExecutionException(e);
        }
    }

    protected void setOptions() {
        sassCommandBuilder.withStyle(style);
        sassCommandBuilder.withNoCharset(noCharset);
        sassCommandBuilder.withErrorCSS(errorCSS);
        sassCommandBuilder.withUpdate(update);
        sassCommandBuilder.withNoSourceMap(noSourceMap);
        sassCommandBuilder.withSourceMapURLs(sourceMapURLs);
        sassCommandBuilder.withEmbedSources(embedSources);
        sassCommandBuilder.withEmbedSourceMap(embedSourceMap);
        sassCommandBuilder.withStopOnError(stopOnError);
        sassCommandBuilder.withColor(color);
        sassCommandBuilder.withNoUnicode(noUnicode);
        sassCommandBuilder.withQuiet(quiet);
        sassCommandBuilder.withQuietDeps(quietDeps);
        sassCommandBuilder.withTrace(trace);
    }

    public File getInputFolder() {
        return inputFolder;
    }

    public void setInputFolder(File inputFolder) {
        this.inputFolder = inputFolder;
    }

    public File getOutputFolder() {
        return outputFolder;
    }

    public void setOutputFolder(File outputFolder) {
        this.outputFolder = outputFolder;
    }

    public List<File> getLoadPaths() {
        return loadPaths;
    }

    public void setLoadPaths(List<File> loadPaths) {
        this.loadPaths = loadPaths;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public boolean isNoCharset() {
        return noCharset;
    }

    public void setNoCharset(boolean noCharset) {
        this.noCharset = noCharset;
    }

    public boolean isErrorCSS() {
        return errorCSS;
    }

    public void setErrorCSS(boolean errorCSS) {
        this.errorCSS = errorCSS;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public boolean isNoSourceMap() {
        return noSourceMap;
    }

    public void setNoSourceMap(boolean noSourceMap) {
        this.noSourceMap = noSourceMap;
    }

    public SourceMapURLs getSourceMapURLs() {
        return sourceMapURLs;
    }

    public void setSourceMapURLs(SourceMapURLs sourceMapURLs) {
        this.sourceMapURLs = sourceMapURLs;
    }

    public boolean isEmbedSources() {
        return embedSources;
    }

    public void setEmbedSources(boolean embedSources) {
        this.embedSources = embedSources;
    }

    public boolean isEmbedSourceMap() {
        return embedSourceMap;
    }

    public void setEmbedSourceMap(boolean embedSourceMap) {
        this.embedSourceMap = embedSourceMap;
    }

    public boolean isStopOnError() {
        return stopOnError;
    }

    public void setStopOnError(boolean stopOnError) {
        this.stopOnError = stopOnError;
    }

    public boolean isColor() {
        return color;
    }

    public void setColor(boolean color) {
        this.color = color;
    }

    public boolean isNoUnicode() {
        return noUnicode;
    }

    public void setNoUnicode(boolean noUnicode) {
        this.noUnicode = noUnicode;
    }

    public boolean isQuiet() {
        return quiet;
    }

    public void setQuiet(boolean quiet) {
        this.quiet = quiet;
    }

    public boolean isQuietDeps() {
        return quietDeps;
    }

    public void setQuietDeps(boolean quietDeps) {
        this.quietDeps = quietDeps;
    }

    public boolean isTrace() {
        return trace;
    }

    public void setTrace(boolean trace) {
        this.trace = trace;
    }
}
