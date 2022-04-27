package com.github.cleydyr.dart.command.factory;

import com.github.cleydyr.dart.command.AbstractSassCommand;
import com.github.cleydyr.dart.command.builder.AbstractSassCommandBuilder;
import com.github.cleydyr.dart.command.builder.SassCommandBuilder;
import com.github.cleydyr.dart.command.exception.SassCommandException;
import com.github.cleydyr.dart.system.OSDetector;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class OSDependentSassCommandBuilderFactory implements SassCommandBuilderFactory {
    @Override
    public SassCommandBuilder getCommanderBuilder() {
        return new AbstractSassCommandBuilder() {
            @Override
            protected AbstractSassCommand getSassCommandInstance() throws SassCommandException {
                String tmpDir = System.getProperty("java.io.tmpdir");

                if (tmpDir == null) {
                    throw new SassCommandException("java.io.tmpdir variable must be set");
                }

                Path tmpDirPath = Paths.get(tmpDir);

                if (!tmpDirPath.toFile().isDirectory()) {
                    throw new SassCommandException("java.io.tmpdir is not a valid directory");
                }

                if (OSDetector.isWindows()) {
                    return new AbstractSassCommand() {
                        @Override
                        protected void setExecutable(List<String> commands) {
                            commands.add(tmpDirPath
                                    .resolve("dart-sass-maven-plugin/sass.bat")
                                    .toString());
                        }
                    };
                }

                return new AbstractSassCommand() {
                    @Override
                    protected void setExecutable(List<String> commands) {
                        commands.add(tmpDirPath
                                .resolve("dart-sass-maven-plugin/sass")
                                .toString());
                    }
                };
            }
        };
    }
}
