package com.github.cleydyr.dart.system.io.factory;

import com.github.cleydyr.dart.release.DartSassReleaseParameter;
import com.github.cleydyr.dart.system.OSDetector;
import com.github.cleydyr.dart.system.io.ApacheFluidHttpClientReleaseDownloader;
import com.github.cleydyr.dart.system.io.DartSassExecutableExtractor;
import com.github.cleydyr.dart.system.io.ExecutableResourcesProvider;
import com.github.cleydyr.dart.system.io.PosixDartSassSnapshotExecutableExtractor;
import com.github.cleydyr.dart.system.io.TarFilesystemExecutableResourcesProvider;
import com.github.cleydyr.dart.system.io.WindowsDartSassExecutableExtractor;
import com.github.cleydyr.dart.system.io.ZipFilesystemExecutableResourcesProvider;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class DefaultDartSassExecutableExtractorFactory implements DartSassExecutableExtractorFactory {
    @Override
    public DartSassExecutableExtractor getDartSassExecutableExtractor(
            DartSassReleaseParameter dartSassReleaseParameter, File cachedFileDirectory, URL proxyHost) {
        try {
            if (OSDetector.isWindows()) {
                ExecutableResourcesProvider executableResourcesProvider;
                executableResourcesProvider = new ZipFilesystemExecutableResourcesProvider(
                        cachedFileDirectory, new ApacheFluidHttpClientReleaseDownloader(proxyHost));

                return new WindowsDartSassExecutableExtractor(dartSassReleaseParameter, executableResourcesProvider);
            }

            ExecutableResourcesProvider executableResourcesProvider = new TarFilesystemExecutableResourcesProvider(
                    cachedFileDirectory, new ApacheFluidHttpClientReleaseDownloader(proxyHost));

            return new PosixDartSassSnapshotExecutableExtractor(dartSassReleaseParameter, executableResourcesProvider);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
