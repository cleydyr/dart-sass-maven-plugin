package com.github.cleydyr.dart.system.io;

import com.github.cleydyr.dart.release.DartSassReleaseParameter;
import com.github.cleydyr.dart.system.OSDetector;
import com.github.cleydyr.dart.system.io.exception.DartSassExecutableExtractorException;
import com.github.cleydyr.dart.system.io.utils.SystemUtils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

public abstract class AbstractDartSassExecutableExtractor implements DartSassExecutableExtractor {
    protected Collection<String> resourceNames;

    protected DartSassReleaseParameter dartSassReleaseParameter;

    protected ExecutableResourcesProvider executableResourcesProvider;

    public AbstractDartSassExecutableExtractor(
            DartSassReleaseParameter dartSassReleaseParameter,
            ExecutableResourcesProvider executableResourcesProvider,
            Collection<String> resourceNames) {
        this.dartSassReleaseParameter = dartSassReleaseParameter;
        this.executableResourcesProvider = executableResourcesProvider;
        this.resourceNames = resourceNames;
    }

    @Override
    public void extract() {
        extractResourcesToFolder(createExecutableFolder());
    }

    private Path createExecutableFolder() {
        Path executableFolder = SystemUtils.getExecutableTempFolder(dartSassReleaseParameter);

        try {
            if (!Files.isDirectory(executableFolder)) {
                Files.createDirectories(executableFolder);
            }

            Path srcDir = executableFolder.resolve("src");

            if (!Files.isDirectory(srcDir)) {
                Files.createDirectory(srcDir);
            }

            return executableFolder;
        } catch (IOException e) {
            throw new DartSassExecutableExtractorException("Can't create executable folder", e);
        }
    }

    private void extractResourcesToFolder(Path executableFolder) {
        for (String resourceName : resourceNames) {
            try (InputStream resourceInputStream = getResourceInputStream("dart-sass/" + resourceName)) {

                if (resourceInputStream == null) {
                    throw new IOException(String.format(
                            "Can't extract file for system %s and architecture %s",
                            OSDetector.getOSName(), OSDetector.getOSArchitecture()));
                }

                Path resourcePath = executableFolder.resolve(resourceName);

                if (Files.exists(resourcePath)) {
                    continue;
                }

                Files.copy(resourceInputStream, resourcePath);

                setResourcePermissions(resourcePath);
            } catch (IOException e) {
                throw new DartSassExecutableExtractorException("Can't extract dart sass executable", e);
            }
        }
    }

    private InputStream getResourceInputStream(String resourceName) throws IOException {
        return executableResourcesProvider.getInputStream(dartSassReleaseParameter, resourceName);
    }

    abstract void setResourcePermissions(Path resourcePath) throws IOException;
}
