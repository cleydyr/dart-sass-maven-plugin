package com.github.cleydyr.dart.system.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public interface DartSassExecutableExtractor {

    default Path createExecutableFolder() throws IOException {
        Path executableFolder = Paths.get(System.getProperty("java.io.tmpdir"), "dart-sass-maven-plugin");

        if (!Files.isDirectory(executableFolder)) {
            Files.createDirectory(executableFolder);
        }

        Path srcDir = executableFolder.resolve("src");

        if (!Files.isDirectory(srcDir)) {
            Files.createDirectory(srcDir);
        }

        return executableFolder;
    }

    void extract() throws IOException;
}
