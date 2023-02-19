package com.github.cleydyr.dart.system.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Arrays;
import java.util.HashSet;

public abstract class PosixDartSassExecutableExtractor implements DartSassExecutableExtractor {

    private static final HashSet<PosixFilePermission> PERMISSION_SET =
            new HashSet<>(Arrays.asList(PosixFilePermission.OWNER_EXECUTE, PosixFilePermission.OWNER_READ));

    @Override
    public void setResourcePermissions(Path resourcePath) throws IOException {
        Files.setPosixFilePermissions(resourcePath, PERMISSION_SET);
    }
}
