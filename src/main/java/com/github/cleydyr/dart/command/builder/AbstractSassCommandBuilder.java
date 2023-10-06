package com.github.cleydyr.dart.command.builder;

import com.github.cleydyr.dart.command.AbstractSassCommand;
import com.github.cleydyr.dart.command.SassCommand;
import com.github.cleydyr.dart.command.enums.SourceMapURLs;
import com.github.cleydyr.dart.command.enums.Style;
import com.github.cleydyr.dart.command.exception.SassCommandException;
import com.github.cleydyr.dart.command.parameter.ParameterPair;
import com.github.cleydyr.dart.release.DartSassReleaseParameter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractSassCommandBuilder implements SassCommandBuilder {
    private final List<Path> loadPaths = new ArrayList<>();

    private Style style = Style.EXPANDED;

    private boolean noCharsetEnabled;

    private boolean errorCSSEnabled = true;

    private boolean updateEnabled;

    private boolean noSourceMapEnabled;

    private SourceMapURLs sourceMapURLs = SourceMapURLs.RELATIVE;

    private boolean embedSourcesEnabled;

    private boolean embedSourceMapEnabled;

    private boolean stopOnErrorEnabled;

    private boolean colorEnabled = true;

    private boolean noUnicodeEnabled;

    private boolean quietEnabled;

    private boolean quietDepsEnabled;

    private boolean traceEnabled;

    private boolean watchEnabled;

    private boolean pollEnabled;

    private final Collection<ParameterPair> parameterPairs = new ArrayList<>();

    @Override
    public SassCommandBuilder withLoadPath(Path loadPath) {
        this.loadPaths.add(loadPath);

        return this;
    }

    @Override
    public SassCommandBuilder withStyle(Style style) {
        this.style = style;

        return this;
    }

    @Override
    public SassCommandBuilder withNoCharset(boolean noCharsetEnabled) {
        this.noCharsetEnabled = noCharsetEnabled;

        return this;
    }

    @Override
    public SassCommandBuilder withErrorCSS(boolean errorCSSEnabled) {
        this.errorCSSEnabled = errorCSSEnabled;

        return this;
    }

    @Override
    public SassCommandBuilder withUpdate(boolean updateEnabled) {
        this.updateEnabled = updateEnabled;

        return this;
    }

    @Override
    public SassCommandBuilder withNoSourceMap(boolean noSourceMapEnabled) {
        this.noSourceMapEnabled = noSourceMapEnabled;

        return this;
    }

    @Override
    public SassCommandBuilder withSourceMapURLs(SourceMapURLs sourceMapURLs) {
        this.sourceMapURLs = sourceMapURLs;

        return this;
    }

    @Override
    public SassCommandBuilder withEmbedSources(boolean embedSourcesEnabled) {
        this.embedSourcesEnabled = embedSourcesEnabled;

        return this;
    }

    @Override
    public SassCommandBuilder withEmbedSourceMap(boolean embedSourceMapEnabled) {
        this.embedSourceMapEnabled = embedSourceMapEnabled;

        return this;
    }

    @Override
    public SassCommandBuilder withStopOnError(boolean stopOnErrorEnabled) {
        this.stopOnErrorEnabled = stopOnErrorEnabled;

        return this;
    }

    @Override
    public SassCommandBuilder withColor(boolean colorEnabled) {
        this.colorEnabled = colorEnabled;

        return this;
    }

    @Override
    public SassCommandBuilder withNoUnicode(boolean noUnicodeEnabled) {
        this.noUnicodeEnabled = noUnicodeEnabled;

        return this;
    }

    @Override
    public SassCommandBuilder withQuiet(boolean quietEnabled) {
        this.quietEnabled = quietEnabled;

        return this;
    }

    @Override
    public SassCommandBuilder withQuietDeps(boolean quietDepsEnabled) {
        this.quietDepsEnabled = quietDepsEnabled;

        return this;
    }

    @Override
    public SassCommandBuilder withTrace(boolean traceEnabled) {
        this.traceEnabled = traceEnabled;

        return this;
    }

    @Override
    public SassCommandBuilder withPaths(Path inputFilePath, Path outputFilePath) {
        ParameterPair parameterPair = new ParameterPair(inputFilePath, outputFilePath);

        parameterPairs.add(parameterPair);

        return this;
    }

    @Override
    public SassCommandBuilder withWatch(boolean watchEnabled) {
        this.watchEnabled = watchEnabled;

        return this;
    }

    @Override
    public SassCommandBuilder withPoll(boolean pollEnabled) {
        this.pollEnabled = pollEnabled;

        return this;
    }

    @Override
    public SassCommand build(DartSassReleaseParameter dartSassReleaseParameter) throws SassCommandException {
        AbstractSassCommand sassCommand = getSassCommandInstance(dartSassReleaseParameter);

        sassCommand.setNoCharsetEnabled(noCharsetEnabled);
        sassCommand.setColorEnabled(colorEnabled);
        sassCommand.setEmbedSourceMapEnabled(embedSourceMapEnabled);
        sassCommand.setEmbedSourcesEnabled(embedSourcesEnabled);
        sassCommand.setErrorCSSEnabled(errorCSSEnabled);
        sassCommand.setLoadPaths(loadPaths);
        sassCommand.setNoSourceMapEnabled(noSourceMapEnabled);
        sassCommand.setNoUnicodeEnabled(noUnicodeEnabled);
        sassCommand.setParameterPairs(parameterPairs);
        sassCommand.setQuietDepsEnabled(quietDepsEnabled);
        sassCommand.setQuietEnabled(quietEnabled);
        sassCommand.setSourceMapURLs(sourceMapURLs);
        sassCommand.setStopOnErrorEnabled(stopOnErrorEnabled);
        sassCommand.setStyle(style);
        sassCommand.setTraceEnabled(traceEnabled);
        sassCommand.setUpdateEnabled(updateEnabled);
        sassCommand.setWatchEnabled(watchEnabled);
        sassCommand.setPollEnabled(pollEnabled);

        return sassCommand;
    }

    protected abstract AbstractSassCommand getSassCommandInstance(DartSassReleaseParameter dartSassReleaseParameter)
            throws SassCommandException;
}
