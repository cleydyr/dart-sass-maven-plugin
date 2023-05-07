package com.github.cleydyr.dart.system.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;

public class ZipFilesystemExecutableResourcesProvider extends FilesystemExecutableResourcesProvider {

    public ZipFilesystemExecutableResourcesProvider(File repository, ReleaseDownloader downloader) {
        super(repository, downloader);
    }

    @Override
    protected InputStream getResourceFromReleaseArchive(String resourceName, File release) throws IOException {
        try (ZipFile zipFile = new ZipFile(release)) {
            InputStream inputStream = zipFile.getInputStream(zipFile.getEntry(resourceName));

            if (inputStream == null) {
                throw new IOException("Can't find resource " + resourceName + " inside release archive at "
                        + release.getAbsolutePath());
            }

            return inputStream;
        }
    }
}
