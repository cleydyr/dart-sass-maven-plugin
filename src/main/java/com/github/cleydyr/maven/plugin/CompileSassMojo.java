package com.github.cleydyr.maven.plugin;

import com.github.cleydyr.dart.command.SassCommand;
import com.github.cleydyr.dart.command.builder.SassCommandBuilder;
import com.github.cleydyr.dart.command.enums.SourceMapURLs;
import com.github.cleydyr.dart.command.enums.Style;
import com.github.cleydyr.dart.command.exception.SassCommandException;
import com.github.cleydyr.dart.command.factory.SassCommandBuilderFactory;
import com.github.cleydyr.dart.system.io.DartSassExecutableExtractor;
import com.github.cleydyr.dart.system.io.factory.DartSassExecutableExtractorFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/** Goal that compiles a set of sass/scss files from an input directory to an output directory */
@Mojo(name = "compile-sass", defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
public class CompileSassMojo extends AbstractMojo {

    private static final String[] ALLOWED_EXTENSIONS = new String[] {".scss", ".sass"};

    @Parameter(defaultValue = "src/main/sass")
    private File inputFolder;

    @Parameter(property = "project.build.directory")
    private File outputFolder;

    @Parameter private List<File> loadPaths;

    @Parameter(defaultValue = "EXPANDED")
    private Style style;

    @Parameter(defaultValue = "false")
    private boolean noCharset;

    @Parameter(defaultValue = "true")
    private boolean errorCSS;

    @Parameter(defaultValue = "false")
    private boolean update;

    @Parameter(defaultValue = "false")
    private boolean noSourceMap;

    @Parameter(defaultValue = "RELATIVE")
    private SourceMapURLs sourceMapURLs;

    @Parameter(defaultValue = "false")
    private boolean embedSources;

    @Parameter(defaultValue = "false")
    private boolean embedSourceMap;

    @Parameter(defaultValue = "false")
    private boolean stopOnError;

    @Parameter(defaultValue = "true")
    private boolean color;

    @Parameter(defaultValue = "false")
    private boolean noUnicode;

    @Parameter(defaultValue = "true")
    private boolean quiet;

    @Parameter(defaultValue = "false")
    private boolean quietDeps;

    @Parameter(defaultValue = "false")
    private boolean trace;

    private long fileCount;

    public void execute() throws MojoExecutionException {
        DartSassExecutableExtractor dartSassExecutableExtractor =
                DartSassExecutableExtractorFactory.getDartSassExecutableExtractor();

        try {
            dartSassExecutableExtractor.extract();
        } catch (IOException ioException) {
            throw new MojoExecutionException("Unable to extract sass executable", ioException);
        }

        SassCommand sassCommand = buildSassCommand();

        try {
            Instant start = Instant.now();

            String output = sassCommand.execute();

            Instant finish = Instant.now();

            long elapsedTime = Duration.between(start, finish).toMillis();

            getLog().info("Compiled " + fileCount + " files in " + elapsedTime + " ms");

            if (!output.isEmpty()) {
                getLog().info("sass command output:");

                getLog().info(output);
            }
        } catch (SassCommandException sassCommandException) {
            throw new MojoExecutionException("Can't execute SASS command", sassCommandException);
        }
    }

    private SassCommand buildSassCommand() throws MojoExecutionException {
        SassCommandBuilder sassCommandBuilder = SassCommandBuilderFactory.getCommanderBuilder();

        if (loadPaths != null) {
            for (File loadPath : loadPaths) {
                sassCommandBuilder.withLoadPath(loadPath.toPath());
            }
        }

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

        Path inputFolderPath = inputFolder.toPath();

        try {
            fileCount = Files.list(inputFolderPath).count();

            Files.list(inputFolderPath)
                    .forEach(
                            inputFilePath -> {
                                Arrays.stream(ALLOWED_EXTENSIONS)
                                        .filter(inputFilePath.toString()::endsWith)
                                        .forEach(
                                                extension -> {
                                                    Path outputFilePath =
                                                            _createHomonymousFile(
                                                                    inputFilePath, extension);

                                                    sassCommandBuilder.withPaths(
                                                            inputFilePath, outputFilePath);
                                                });
                            });
        } catch (IOException e) {
            throw new MojoExecutionException("Can't list folder " + inputFolderPath, e);
        }

        return sassCommandBuilder.build();
    }

    private Path _createHomonymousFile(Path inputFilePath, String extension) {
        Path fileNamePath = inputFilePath.getName(inputFilePath.getNameCount() - 1);

        String fileName = fileNamePath.toString();

        String newFileName = fileName.replace(extension, ".css");

        return outputFolder.toPath().resolve(newFileName);
    }
}
