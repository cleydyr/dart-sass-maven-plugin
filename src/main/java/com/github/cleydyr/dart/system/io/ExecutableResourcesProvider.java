package com.github.cleydyr.dart.system.io;

import com.github.cleydyr.dart.release.DartSassReleaseParameter;
import java.io.IOException;
import java.io.InputStream;

public interface ExecutableResourcesProvider {
    InputStream getInputStream(DartSassReleaseParameter dartSassReleaseParameter, String resourceName)
            throws IOException;
}
