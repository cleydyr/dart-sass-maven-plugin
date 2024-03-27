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
import com.github.cleydyr.dart.system.io.exception.DartSassExecutableExtractorException;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.maven.settings.Proxy;

@Named
@Singleton
public class DefaultDartSassExecutableExtractorFactory implements DartSassExecutableExtractorFactory {

    @Override
    public DartSassExecutableExtractor getDartSassExecutableExtractor(
            DartSassReleaseParameter dartSassReleaseParameter, File cachedFileDirectory, Proxy proxy) {
        try {
            if (OSDetector.isWindows()) {
                ExecutableResourcesProvider executableResourcesProvider;
                executableResourcesProvider = new ZipFilesystemExecutableResourcesProvider(
                        cachedFileDirectory, new ApacheFluidHttpClientReleaseDownloader(proxy));

                return new WindowsDartSassExecutableExtractor(dartSassReleaseParameter, executableResourcesProvider);
            }

            ExecutableResourcesProvider executableResourcesProvider = new TarFilesystemExecutableResourcesProvider(
                    cachedFileDirectory, new ApacheFluidHttpClientReleaseDownloader(proxy));

            return new PosixDartSassSnapshotExecutableExtractor(dartSassReleaseParameter, executableResourcesProvider);
        } catch (URISyntaxException | MalformedURLException e) {
            throw new DartSassExecutableExtractorException("Error while creating DartSassExecutableExtractor", e);
        }
    }
}
