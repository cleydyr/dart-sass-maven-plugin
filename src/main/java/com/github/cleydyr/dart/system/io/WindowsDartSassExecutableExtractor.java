package com.github.cleydyr.dart.system.io;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class WindowsDartSassExecutableExtractor implements DartSassExecutableExtractor {
    private static Collection<String> RESOURCE_NAMES =
            Collections.unmodifiableList(Arrays.asList("sass.bat", "src/sass.snapshot", "src/dart.exe"));

    @Override
    public Collection<String> getResourceNames() {
        return RESOURCE_NAMES;
    }
}
