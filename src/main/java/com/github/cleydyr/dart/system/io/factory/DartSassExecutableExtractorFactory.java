package com.github.cleydyr.dart.system.io.factory;

import com.github.cleydyr.dart.release.DartSassReleaseParameter;
import com.github.cleydyr.dart.system.io.DartSassExecutableExtractor;
import java.io.File;
import java.net.URI;

import org.apache.maven.settings.Proxy;

public interface DartSassExecutableExtractorFactory {

    DartSassExecutableExtractor getDartSassExecutableExtractor(
            URI baseURI,
            DartSassReleaseParameter dartSassReleaseParameter, File cachedFileDirectory, Proxy proxy);
}
