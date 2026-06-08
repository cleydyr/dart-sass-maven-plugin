package com.github.cleydyr.dart.command;

import static com.github.cleydyr.dart.command.enums.Style.COMPRESSED;

import com.github.cleydyr.dart.command.enums.SourceMapURLs;
import com.github.cleydyr.dart.command.enums.Style;
import com.github.cleydyr.dart.command.exception.SassCommandException;
import com.github.cleydyr.dart.command.parameter.ParameterPair;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    public void setWatchEnabled(boolean watchEnabled) {
        this.watchEnabled = watchEnabled;
    }

    public void setPollEnabled(boolean pollEnabled) {
        this.pollEnabled = pollEnabled;
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

    private boolean watchEnabled;

    private Collection<ParameterPair> parameterPairs = new ArrayList<>();

    private boolean pollEnabled;

    @Override
    public void execute() throws SassCommandException {
        List<String> commands = new ArrayList<>();

        setExecutable(commands);

        setOptions(commands);

        setArguments(commands);

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(commands).inheritIO();

            processBuilder.redirectErrorStream(true);

            processBuilder.inheritIO();

            Process process = processBuilder.start();

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new SassCommandException(
                        "Process [" + processBuilder.command() + "] exited with code " + exitCode + '\n');
            }
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

        if (watchEnabled) {
            commands.add("--watch");

            if (pollEnabled) {
                commands.add("--poll");
            }
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
        if (style == COMPRESSED) {
            commands.add("--style=compressed");
        } else {
            commands.add("--style=expanded");
        }
    }

    private void _setLoadPaths(List<String> commands) {
        for (Path loadPath : loadPaths) {
            commands.add("--load-path");

            commands.add(loadPath.toString());
        }
    }
}
