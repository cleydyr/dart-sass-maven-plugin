package com.github.cleydyr.dart.system.io.factory;

import com.github.cleydyr.dart.system.OSDetector;
import com.github.cleydyr.dart.system.io.DartSassExecutableExtractor;
import com.github.cleydyr.dart.system.io.PosixDartSassSnapshotExecutableExtractor;
import com.github.cleydyr.dart.system.io.WindowsDartSassExecutableExtractor;
import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class DefaultDartSassExecutableExtractorFactory implements DartSassExecutableExtractorFactory {
    @Override
    public DartSassExecutableExtractor getDartSassExecutableExtractor() {
        if (OSDetector.isWindows()) {
            return new WindowsDartSassExecutableExtractor();
        }

        return new PosixDartSassSnapshotExecutableExtractor();
    }
}
