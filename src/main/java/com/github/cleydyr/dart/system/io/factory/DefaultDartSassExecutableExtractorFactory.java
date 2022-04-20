package com.github.cleydyr.dart.system.io.factory;

import javax.inject.Named;
import javax.inject.Singleton;

import com.github.cleydyr.dart.system.OSDetector;
import com.github.cleydyr.dart.system.io.DartSassExecutableExtractor;
import com.github.cleydyr.dart.system.io.PosixDartSassExecutableExtractor;
import com.github.cleydyr.dart.system.io.WindowsDartSassExecutableExtractor;

@Named
@Singleton
public class DefaultDartSassExecutableExtractorFactory implements DartSassExecutableExtractorFactory {
    @Override
	public DartSassExecutableExtractor getDartSassExecutableExtractor() {
        if (OSDetector.isWindows()) {
            return new WindowsDartSassExecutableExtractor();
        }

        return new PosixDartSassExecutableExtractor();
    }
}
