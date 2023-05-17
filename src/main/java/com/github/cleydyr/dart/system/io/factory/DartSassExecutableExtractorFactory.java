package com.github.cleydyr.dart.system.io.factory;

import com.github.cleydyr.dart.release.DartSassReleaseParameter;
import com.github.cleydyr.dart.system.io.DartSassExecutableExtractor;
import java.io.File;
import java.net.URL;

public interface DartSassExecutableExtractorFactory {
    DartSassExecutableExtractor getDartSassExecutableExtractor(
            DartSassReleaseParameter dartSassReleaseParameter, File cachedFileDirectory, URL proxyHost);
}
