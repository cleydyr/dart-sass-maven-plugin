package com.github.cleydyr.dart.system.io;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class PosixDartSassSnapshotExecutableExtractor extends PosixDartSassExecutableExtractor {
    private static Collection<String> RESOURCE_NAMES =
            Collections.unmodifiableList(Arrays.asList("sass", "src/sass.snapshot", "src/dart"));

    @Override
    public Collection<String> getResourceNames() {
        return RESOURCE_NAMES;
    }
}
