package com.github.cleydyr.dart.system.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class WindowsDartSassExecutableExtractor implements DartSassExecutableExtractor {
    private static String[] _RESOURCE_NAMES =
            new String[] {"sass.bat", "src/sass.snapshot", "src/dart.exe"};

    @Override
    public void extract() throws IOException {
        Path executableFolder = createExecutableFolder();

        for (String resourceName : _RESOURCE_NAMES) {
            InputStream resourceInputStream =
                    getClass().getResourceAsStream("/sass-binaries/windows/x64/" + resourceName);

            Path resourcePath = executableFolder.resolve(resourceName);

            if (Files.exists(resourcePath)) {
                continue;
            }

            Files.copy(resourceInputStream, resourcePath);
        }
    }
}
