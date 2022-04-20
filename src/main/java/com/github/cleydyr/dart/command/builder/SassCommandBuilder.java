package com.github.cleydyr.dart.command.builder;

import com.github.cleydyr.dart.command.SassCommand;
import com.github.cleydyr.dart.command.enums.SourceMapURLs;
import com.github.cleydyr.dart.command.enums.Style;
import java.nio.file.Path;

public interface SassCommandBuilder {
    SassCommandBuilder withLoadPath(Path loadPath);

    SassCommandBuilder withStyle(Style style);

    SassCommandBuilder withNoCharset(boolean enabled);

    SassCommandBuilder withErrorCSS(boolean enabled);

    SassCommandBuilder withUpdate(boolean updateEnabled);

    SassCommandBuilder withNoSourceMap(boolean noSourceMapEnabled);

    SassCommandBuilder withSourceMapURLs(SourceMapURLs sourceMapUrls);

    SassCommandBuilder withEmbedSources(boolean embedSourcesEnabled);

    SassCommandBuilder withEmbedSourceMap(boolean embedSourceMapEnabled);

    SassCommandBuilder withStopOnError(boolean stopOnErrorEnabled);

    SassCommandBuilder withColor(boolean colorEnabled);

    SassCommandBuilder withNoUnicode(boolean noUnicodeEnabled);

    SassCommandBuilder withQuiet(boolean quietEnabled);

    SassCommandBuilder withQuietDeps(boolean quietDepsEnabled);

    SassCommandBuilder withTrace(boolean traceEnabled);

    SassCommandBuilder withPaths(Path inputFilePath, Path outputFilePath);

    SassCommandBuilder withWatch(boolean watchEnabled);

    SassCommandBuilder withPoll(boolean pollEnabled);

    SassCommand build();
}
