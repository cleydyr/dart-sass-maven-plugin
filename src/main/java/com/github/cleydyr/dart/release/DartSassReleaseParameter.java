package com.github.cleydyr.dart.release;

public class DartSassReleaseParameter {
    private final String os;

    private final String arch;

    private final String version;

    public DartSassReleaseParameter(String os, String arch, String version) {
        if (os == null) {
            throw new IllegalArgumentException("os can't be null");
        }

        if (arch == null) {
            throw new IllegalArgumentException("arch can't be null");
        }

        if (version == null) {
            throw new IllegalArgumentException("version can't be null");
        }

        this.os = os;
        this.arch = arch;
        this.version = version;
    }

    public String getOS() {
        return os;
    }

    public String getArch() {
        return arch;
    }

    public String getVersion() {
        return version;
    }
}
