package com.github.cleydyr.dart.system.io.utils;

import com.github.cleydyr.dart.release.DartSassReleaseParameter;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class SystemUtils {
    private SystemUtils() {
        // Utility class
    }

    public static Path getExecutableTempFolder(DartSassReleaseParameter dartSassReleaseParameter) {
        String os = dartSassReleaseParameter.getOS();

        String arch = dartSassReleaseParameter.getArch();

        String version = dartSassReleaseParameter.getVersion();

        return Paths.get(System.getProperty("java.io.tmpdir"), "dart-sass-maven-plugin", os, arch, version);
    }
}
