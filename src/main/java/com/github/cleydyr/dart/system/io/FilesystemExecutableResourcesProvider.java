package com.github.cleydyr.dart.system.io;

import com.github.cleydyr.dart.release.DartSassReleaseParameter;
import com.github.cleydyr.dart.system.io.exception.ReleaseDownloadException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public abstract class FilesystemExecutableResourcesProvider implements ExecutableResourcesProvider {
    private final File repository;

    private final ReleaseDownloader downloader;

    public FilesystemExecutableResourcesProvider(File repository, ReleaseDownloader downloader) {
        this.repository = repository;
        this.downloader = downloader;
    }

    @Override
    public InputStream getInputStream(DartSassReleaseParameter dartSassReleaseParameter, String resourceName)
            throws IOException {
        File releaseDirectory = getReleaseDirectory(repository, dartSassReleaseParameter);

        if (!releaseDirectory.exists()) {
            Files.createDirectories(releaseDirectory.toPath());
        }

        File release = getReleaseFile(releaseDirectory);

        if (!release.exists()) {
            downloadRelease(dartSassReleaseParameter, release);
        }

        return getResourceFromReleaseArchive(resourceName, release);
    }

    protected abstract InputStream getResourceFromReleaseArchive(String resourceName, File release) throws IOException;

    protected File getReleaseFile(File releaseDirectory) {
        return new File(releaseDirectory, "release");
    }

    private void downloadRelease(DartSassReleaseParameter dartSassReleaseParameter, File destination)
            throws ReleaseDownloadException {
        downloader.download(dartSassReleaseParameter, destination);
    }

    private File getReleaseDirectory(File repository, DartSassReleaseParameter dartSassReleaseParameter) {
        return new File(
                new File(new File(repository, dartSassReleaseParameter.getOS()), dartSassReleaseParameter.getArch()),
                dartSassReleaseParameter.getVersion());
    }
}
