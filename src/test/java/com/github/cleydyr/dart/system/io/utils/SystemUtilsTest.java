package com.github.cleydyr.dart.system.io.utils;

import com.github.cleydyr.dart.maven.plugin.test.util.TestUtil;
import com.github.cleydyr.dart.release.DartSassReleaseParameter;
import java.nio.file.Path;
import java.nio.file.Paths;
import junit.framework.TestCase;

public class SystemUtilsTest extends TestCase {

    public void testGetExecutableTempFolder() {
        String os = TestUtil.randomAlphaString(7);
        String arch = TestUtil.randomAlphaString(7);
        String version = TestUtil.randomAlphaString(7);

        DartSassReleaseParameter parameter = new DartSassReleaseParameter(os, arch, version);

        Path path = SystemUtils.getExecutableTempFolder(parameter);

        Path expected = Paths.get(os, arch, version);

        assertTrue(path.endsWith(expected));
    }
}
