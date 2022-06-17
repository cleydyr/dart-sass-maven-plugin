package com.github.cleydyr.dart.system.io;

public class WindowsDartSassExecutableExtractor implements DartSassExecutableExtractor {
    private static String[] _RESOURCE_NAMES = new String[] {"sass.bat", "src/sass.snapshot", "src/dart.exe"};

    @Override
    public String[] getResourceNames() {
        return _RESOURCE_NAMES;
    }
}
