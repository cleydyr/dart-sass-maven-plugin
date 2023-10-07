package com.github.cleydyr.dart.command.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Stream;
import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class DefaultFileCounter implements FileCounter {
    private static final String[] ALLOWED_EXTENSIONS_DIFFERENT_FOLDERS = {".css", ".sass", ".scss"};
    private static final String[] ALLOWED_EXTENSIONS_SAME_FOLDER = {".sass", ".scss"};

    @Override
    public long getProcessableFileCount(Path inputFolderPath, Path outputFolderPath) throws FileCounterException {
        try (Stream<Path> walk = Files.walk(inputFolderPath)) {
            return walk.parallel()
                    .filter(p -> !Files.isDirectory(p)) // files only
                    .map(p -> p.toString()) // convert path to string
                    .filter(
                            inputFolderPath.equals(outputFolderPath)
                                    ? this::hasAllowedExtensionSameFolder
                                    : this::hasAllowedExtensionDifferentFolders) // check file extension
                    .count();
        } catch (IOException e) {
            throw new FileCounterException("Can't list folder " + inputFolderPath, e);
        }
    }

    @Override
    public boolean hasAllowedExtensionSameFolder(String fileName) {
        return hasAllowedExtension(fileName, ALLOWED_EXTENSIONS_SAME_FOLDER);
    }

    @Override
    public boolean hasAllowedExtensionDifferentFolders(String fileName) {
        return hasAllowedExtension(fileName, ALLOWED_EXTENSIONS_DIFFERENT_FOLDERS);
    }

    public boolean hasAllowedExtension(String fileName, String... allowedExtensions) {
        if (fileName == null) {
            throw new IllegalArgumentException("fileName can't be null");
        }

        return Arrays.stream(allowedExtensions).anyMatch(fileName::endsWith);
    }
}
