package com.github.cleydyr.dart.system.io;

public class PosixDartSassNativeExecutableExtractor extends PosixDartSassExecutableExtractor {
    private static String[] _RESOURCE_NAMES = new String[] {"sass"};

    @Override
    public String[] getResourceNames() {
        return _RESOURCE_NAMES;
    }
}
