package com.github.cleydyr.dart.command;

import com.github.cleydyr.dart.command.enums.SourceMapURLs;
import com.github.cleydyr.dart.command.enums.Style;
import com.github.cleydyr.dart.command.exception.SassCommandException;
import com.github.cleydyr.dart.command.parameter.ParameterPair;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractSassCommand implements SassCommand {
    private List<Path> loadPaths;

    public void setLoadPaths(List<Path> loadPaths) {
        this.loadPaths = loadPaths;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public void setNoCharsetEnabled(boolean noCharsetEnabled) {
        this.noCharsetEnabled = noCharsetEnabled;
    }

    public void setErrorCSSEnabled(boolean errorCSSEnabled) {
        this.errorCSSEnabled = errorCSSEnabled;
    }

    public void setUpdateEnabled(boolean updateEnabled) {
        this.updateEnabled = updateEnabled;
    }

    public void setNoSourceMapEnabled(boolean noSourceMapEnabled) {
        this.noSourceMapEnabled = noSourceMapEnabled;
    }

    public void setSourceMapURLs(SourceMapURLs sourceMapURLs) {
        this.sourceMapURLs = sourceMapURLs;
    }

    public void setEmbedSourcesEnabled(boolean embedSourcesEnabled) {
        this.embedSourcesEnabled = embedSourcesEnabled;
    }

    public void setEmbedSourceMapEnabled(boolean embedSourceMapEnabled) {
        this.embedSourceMapEnabled = embedSourceMapEnabled;
    }

    public void setStopOnErrorEnabled(boolean stopOnErrorEnabled) {
        this.stopOnErrorEnabled = stopOnErrorEnabled;
    }

    public void setColorEnabled(boolean colorEnabled) {
        this.colorEnabled = colorEnabled;
    }

    public void setNoUnicodeEnabled(boolean noUnicodeEnabled) {
        this.noUnicodeEnabled = noUnicodeEnabled;
    }

    public void setQuietEnabled(boolean quietEnabled) {
        this.quietEnabled = quietEnabled;
    }

    public void setQuietDepsEnabled(boolean quietDepsEnabled) {
        this.quietDepsEnabled = quietDepsEnabled;
    }

    public void setTraceEnabled(boolean traceEnabled) {
        this.traceEnabled = traceEnabled;
    }

    public void setParameterPairs(Collection<ParameterPair> parameterPairs) {
        this.parameterPairs = parameterPairs;
    }

    private Style style;

    private boolean noCharsetEnabled;

    private boolean errorCSSEnabled;

    private boolean updateEnabled;

    private boolean noSourceMapEnabled;

    private SourceMapURLs sourceMapURLs;

    private boolean embedSourcesEnabled;

    private boolean embedSourceMapEnabled;

    private boolean stopOnErrorEnabled;

    private boolean colorEnabled;

    private boolean noUnicodeEnabled;

    private boolean quietEnabled;

    private boolean quietDepsEnabled;

    private boolean traceEnabled;

    private Collection<ParameterPair> parameterPairs = new ArrayList<>();

    @Override
    public String execute() throws SassCommandException {
        List<String> commands = new ArrayList<>();

        setExecutable(commands);

        setOptions(commands);

        setArguments(commands);

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(commands);

            Process process = processBuilder.start();

            InputStream inputStream = process.getInputStream();

            InputStream errorInputStream = process.getErrorStream();

            String processOutput =
                    new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                            .lines()
                            .collect(Collectors.joining("\n"));

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                String errorOutput =
                                new BufferedReader(new InputStreamReader(errorInputStream, StandardCharsets.UTF_8))
                                        .lines()
                                        .collect(Collectors.joining("\n"));

                StringBuilder sb = new StringBuilder( 4 );

                sb.append( "Process exited with code " );
                sb.append( exitCode );
                sb.append( "\n" );
                sb.append( errorOutput );
                sb.append( "\n" );
                sb.append( processOutput );

                throw new SassCommandException(sb.toString());
            }

            return processOutput;
        } catch (InterruptedException interruptedException) {
            throw new SassCommandException(interruptedException);
        } catch (IOException ioException) {
            throw new SassCommandException("Can't execute sass command", ioException);
        }
    }

    protected void setArguments(List<String> commands) {
        for (ParameterPair parameterPair : parameterPairs) {
            commands.add(parameterPair.getInputPath() + ":" + parameterPair.getOutputPath());
        }
    }

    protected abstract void setExecutable(List<String> commands);

    protected void setOptions(List<String> commands) {
        _setLoadPaths(commands);

        _setStyle(commands);

        _setSourceMapURLs(commands);

        _setFlags(commands);
    }

    private void _setFlags(List<String> commands) {
        if (noCharsetEnabled) {
            commands.add("--no-charset");
        }

        if (errorCSSEnabled) {
            commands.add("--error-css");
        }

        if (updateEnabled) {
            commands.add("--update");
        }

        if (noSourceMapEnabled) {
            commands.add("--no-source-map");
        }

        if (embedSourcesEnabled) {
            commands.add("--embed-sources");
        }

        if (embedSourceMapEnabled) {
            commands.add("--embed-source-map");
        }

        if (stopOnErrorEnabled) {
            commands.add("--stop-on-error");
        }

        if (!colorEnabled) {
            commands.add("--no-color");
        }

        if (noUnicodeEnabled) {
            commands.add("--no-unicode");
        }

        if (quietEnabled) {
            commands.add("--quiet");
        }

        if (quietDepsEnabled) {
            commands.add("--quiet-deps");
        }

        if (traceEnabled) {
            commands.add("--trace");
        }
    }

    private void _setSourceMapURLs(List<String> commands) {
        if (sourceMapURLs == null) {
            return;
        }

        switch (sourceMapURLs) {
            case ABSOLUTE:
                commands.add("--source-map-urls=absolute");
                break;
            case RELATIVE:
                commands.add("--source-map-urls=relative");
                break;
            default:
                break;
        }
    }

    private void _setStyle(List<String> commands) {
        switch (style) {
            case COMPRESSED:
                commands.add("--style=compressed");
                break;
            case EXPANDED:
                commands.add("--style=expanded");
                break;
        }
    }

    private void _setLoadPaths(List<String> commands) {
        for (Path loadPath : loadPaths) {
            commands.add("--load-path");

            commands.add(loadPath.toString());
        }
    }
}
