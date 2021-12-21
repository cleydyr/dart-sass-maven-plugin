package com.github.cleydyr.dart.system.io.factory;

import com.github.cleydyr.dart.system.OSDetector;
import com.github.cleydyr.dart.system.io.DartSassExecutableExtractor;
import com.github.cleydyr.dart.system.io.PosixDartSassExecutableExtractor;
import com.github.cleydyr.dart.system.io.WindowsDartSassExecutableExtractor;

public class DartSassExecutableExtractorFactory {
    public static DartSassExecutableExtractor getDartSassExecutableExtractor() {
        if (OSDetector.isWindows()) {
            return new WindowsDartSassExecutableExtractor();
        }

        return new PosixDartSassExecutableExtractor();
    }
}
