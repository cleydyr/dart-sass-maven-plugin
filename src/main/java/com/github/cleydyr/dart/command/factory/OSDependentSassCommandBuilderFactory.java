package com.github.cleydyr.dart.command.factory;

import java.nio.file.Paths;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import com.github.cleydyr.dart.command.AbstractSassCommand;
import com.github.cleydyr.dart.command.builder.AbstractSassCommandBuilder;
import com.github.cleydyr.dart.command.builder.SassCommandBuilder;
import com.github.cleydyr.dart.system.OSDetector;

@Named
@Singleton
public class OSDependentSassCommandBuilderFactory implements SassCommandBuilderFactory {
    @Override
	public SassCommandBuilder getCommanderBuilder() {
        return new AbstractSassCommandBuilder() {
            @Override
            protected AbstractSassCommand getSassCommandInstance() {
                if (OSDetector.isWindows()) {
                    return new AbstractSassCommand() {
                        @Override
                        protected void setExecutable(List<String> commands) {
                            commands.add(
                                    Paths.get(
                                                    System.getProperty("java.io.tmpdir"),
                                                    "dart-sass-maven-plugin",
                                                    "sass.bat")
                                            .toString());
                        }
                    };
                }

                return new AbstractSassCommand() {
                    @Override
                    protected void setExecutable(List<String> commands) {
                        commands.add(
                                Paths.get(
                                                System.getProperty("java.io.tmpdir"),
                                                "dart-sass-maven-plugin",
                                                "sass")
                                        .toString());
                    }
                };
            }
        };
    }
}
