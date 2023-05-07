package com.github.cleydyr.dart.system.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

public class TarFilesystemExecutableResourcesProvider extends FilesystemExecutableResourcesProvider {
    public TarFilesystemExecutableResourcesProvider(File repository, ReleaseDownloader downloader) {
        super(repository, downloader);
    }

    @Override
    protected InputStream getResourceFromReleaseArchive(String resourceName, File release) throws IOException {
        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(
                new GzipCompressorInputStream(new BufferedInputStream(new FileInputStream(release))));

        TarArchiveEntry entry;

        while ((entry = tarArchiveInputStream.getNextTarEntry()) != null) {
            if (entry.getName().equals(resourceName)) {
                return tarArchiveInputStream;
            }
        }

        throw new IOException(
                "Can't find resource " + resourceName + " inside release archive at " + release.getAbsolutePath());
    }
}
