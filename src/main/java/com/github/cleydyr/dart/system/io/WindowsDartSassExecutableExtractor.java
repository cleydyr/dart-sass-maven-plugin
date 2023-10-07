package com.github.cleydyr.dart.system.io;

import com.github.cleydyr.dart.release.DartSassReleaseParameter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class WindowsDartSassExecutableExtractor extends AbstractDartSassExecutableExtractor {
    public static final Collection<String> RESOURCE_NAMES =
            Collections.unmodifiableList(Arrays.asList("sass.bat", "src/sass.snapshot", "src/dart.exe"));

    public WindowsDartSassExecutableExtractor(
            DartSassReleaseParameter dartSassReleaseParameter,
            ExecutableResourcesProvider executableResourcesProvider) {
        super(dartSassReleaseParameter, executableResourcesProvider, RESOURCE_NAMES);
    }

    @Override
    void setResourcePermissions(Path resourcePath) throws IOException {
        // There's no need to set permissions on Windows.
    }
}
