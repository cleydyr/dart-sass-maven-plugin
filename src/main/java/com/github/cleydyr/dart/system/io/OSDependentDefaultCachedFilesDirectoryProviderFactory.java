package com.github.cleydyr.dart.system.io;

import com.github.cleydyr.dart.system.OSDetector;
import java.io.File;
import java.util.function.Supplier;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Named
public class OSDependentDefaultCachedFilesDirectoryProviderFactory
        implements DefaultCachedFilesDirectoryProviderFactory {

    private static final String WINDOWS_APP_DATA_BASE_DIR = System.getenv("LOCALAPPDATA");

    private static final String POSIX_APP_DATA_BASE_DIR = System.getProperty("user.home");

    private static final File BASE_DIR = OSDetector.isWindows()
            ? new File(WINDOWS_APP_DATA_BASE_DIR, "dart-sass-maven-plugin")
            : new File(POSIX_APP_DATA_BASE_DIR, "." + "dart-sass-maven-plugin");

    @Override
    public Supplier<File> get() {
        return () -> BASE_DIR;
    }
}
