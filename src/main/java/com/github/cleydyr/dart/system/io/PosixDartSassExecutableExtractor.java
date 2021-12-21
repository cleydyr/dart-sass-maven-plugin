package com.github.cleydyr.dart.system.io;

import com.github.cleydyr.dart.system.OSDetector;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Arrays;
import java.util.HashSet;

public class PosixDartSassExecutableExtractor implements DartSassExecutableExtractor {
    private static String[] _RESOURCE_NAMES =
            new String[] {"sass", "src/sass.snapshot", "src/dart"};

    @Override
    public void extract() throws IOException {
        Path executableFolder = createExecutableFolder();

        for (String resourceName : _RESOURCE_NAMES) {
            InputStream resourceInputStream =
                    getClass()
                            .getResourceAsStream(
                                    "/sass-binaries/"
                                            + OSDetector.getOSName()
                                            + "/"
                                            + OSDetector.getOSArchitecture()
                                            + "/"
                                            + resourceName);

            Path resourcePath = executableFolder.resolve(resourceName);

            if (Files.exists(resourcePath)) {
                continue;
            }

            Files.copy(resourceInputStream, resourcePath);

            Files.setPosixFilePermissions(
                    resourcePath,
                    new HashSet<>(
                            Arrays.asList(
                                    PosixFilePermission.OWNER_EXECUTE,
                                    PosixFilePermission.OWNER_READ)));
        }
    }
}
