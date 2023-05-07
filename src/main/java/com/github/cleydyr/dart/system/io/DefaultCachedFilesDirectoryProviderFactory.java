package com.github.cleydyr.dart.system.io;

import java.io.File;
import java.util.function.Supplier;

public interface DefaultCachedFilesDirectoryProviderFactory {
    Supplier<File> get();
}
