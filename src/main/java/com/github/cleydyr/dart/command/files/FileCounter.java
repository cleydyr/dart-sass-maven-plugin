package com.github.cleydyr.dart.command.files;

import java.nio.file.Path;

public interface FileCounter {
    long getProcessableFileCount(Path inputFolderPath) throws FileCounterException;

    boolean hasAllowedExtension(String fileName);
}
