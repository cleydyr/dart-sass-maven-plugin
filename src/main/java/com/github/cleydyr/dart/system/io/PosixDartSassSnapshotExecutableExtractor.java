package com.github.cleydyr.dart.system.io;

public class PosixDartSassSnapshotExecutableExtractor extends PosixDartSassExecutableExtractor {
    private static String[] _RESOURCE_NAMES = new String[] {"sass", "src/sass.snapshot", "src/dart"};

    @Override
    public String[] getResourceNames() {
        return _RESOURCE_NAMES;
    }
}
