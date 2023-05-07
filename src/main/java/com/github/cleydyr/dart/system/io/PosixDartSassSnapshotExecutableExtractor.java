package com.github.cleydyr.dart.system.io;

import com.github.cleydyr.dart.release.DartSassReleaseParameter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class PosixDartSassSnapshotExecutableExtractor extends PosixDartSassExecutableExtractor {
    public static final Collection<String> RESOURCE_NAMES =
            Collections.unmodifiableList(Arrays.asList("sass", "src/sass.snapshot", "src/dart"));

    public PosixDartSassSnapshotExecutableExtractor(
            DartSassReleaseParameter dartSassReleaseParameter,
            ExecutableResourcesProvider executableResourcesProvider) {
        super(dartSassReleaseParameter, executableResourcesProvider, RESOURCE_NAMES);
    }
}
