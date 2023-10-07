package com.github.cleydyr.dart.system.io;

import com.github.cleydyr.dart.release.DartSassReleaseParameter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class PosixDartSassExecutableExtractor extends AbstractDartSassExecutableExtractor {

    public PosixDartSassExecutableExtractor(
            DartSassReleaseParameter dartSassReleaseParameter,
            ExecutableResourcesProvider executableResourcesProvider,
            Collection<String> resourceNames) {
        super(dartSassReleaseParameter, executableResourcesProvider, resourceNames);
    }

    private static final Set<PosixFilePermission> PERMISSION_SET =
            new HashSet<>(Arrays.asList(PosixFilePermission.OWNER_EXECUTE, PosixFilePermission.OWNER_READ));

    @Override
    public void setResourcePermissions(Path resourcePath) throws IOException {
        Files.setPosixFilePermissions(resourcePath, PERMISSION_SET);
    }
}
