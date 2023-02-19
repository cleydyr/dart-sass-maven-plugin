package com.github.cleydyr.dart.command.files;

import java.nio.file.Path;

public interface FileCounter {
    long getProcessableFileCount(Path inputFolderPath, Path outputFolderPath) throws FileCounterException;

    boolean hasAllowedExtensionSameFolder(String fileName);

    boolean hasAllowedExtensionDifferentFolders(String fileName);
}
