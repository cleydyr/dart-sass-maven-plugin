package com.github.cleydyr.dart.system.io;

import com.github.cleydyr.dart.release.DartSassReleaseParameter;
import com.github.cleydyr.dart.system.OSDetector;
import java.io.InputStream;

public class PluginExecutableResourcesProvider implements ExecutableResourcesProvider {

    @Override
    public InputStream getInputStream(DartSassReleaseParameter dartSassReleaseParameter, String resourceName) {
        return getClass()
                .getResourceAsStream("/sass-binaries/"
                        + OSDetector.getOSName()
                        + "/"
                        + OSDetector.getOSArchitecture()
                        + "/dart-sass/"
                        + resourceName);
    }
}
