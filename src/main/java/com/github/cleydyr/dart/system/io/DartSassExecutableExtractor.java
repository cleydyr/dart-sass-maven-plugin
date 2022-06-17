package com.github.cleydyr.dart.system.io;

import com.github.cleydyr.dart.command.exception.SassCommandException;
import com.github.cleydyr.dart.system.OSDetector;
import java.io.IOException;
import java.io.InputStream;
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

    default void extract() throws Exception {
        Path executableFolder = createExecutableFolder();

        for (String resourceName : getResourceNames()) {
            InputStream resourceInputStream = getClass()
                    .getResourceAsStream("/sass-binaries/"
                            + OSDetector.getOSName()
                            + "/"
                            + OSDetector.getOSArchitecture()
                            + "/dart-sass/"
                            + resourceName);

            if (resourceInputStream == null) {
                throw new SassCommandException(String.format(
                        "Can't extract file for system %s and architecture %s",
                        OSDetector.getOSName(), OSDetector.getOSArchitecture()));
            }

            Path resourcePath = executableFolder.resolve(resourceName);

            if (Files.exists(resourcePath)) {
                continue;
            }

            Files.copy(resourceInputStream, resourcePath);

            setResourcePermissions(resourcePath);
        }
    }

    String[] getResourceNames();

    default void setResourcePermissions(Path resourcePath) throws IOException {}
}
