package com.github.cleydyr.dart.release;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.cleydyr.dart.maven.plugin.test.util.TestUtil;
import com.github.cleydyr.dart.system.OSDetector;
import org.junit.jupiter.api.Test;

public class DartSassReleaseParameterTest {
    @Test
    public void testGetters() {
        String os = TestUtil.randomAlphaString(7);
        String arch = TestUtil.randomAlphaString(7);
        String version = TestUtil.randomAlphaString(7);

        DartSassReleaseParameter parameter = new DartSassReleaseParameter(os, arch, version);

        assertEquals(parameter.getArch(), arch);
        assertEquals(parameter.getOS(), os);
        assertEquals(parameter.getVersion(), version);
    }

    @Test
    public void testConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new DartSassReleaseParameter(null, null, null));

        assertThrows(
                IllegalArgumentException.class,
                () -> new DartSassReleaseParameter(TestUtil.randomAlphaString(7), null, null));

        assertThrows(
                IllegalArgumentException.class,
                () -> new DartSassReleaseParameter(TestUtil.randomAlphaString(7), TestUtil.randomAlphaString(7), null));

        assertThrows(
                IllegalArgumentException.class,
                () -> new DartSassReleaseParameter(TestUtil.randomAlphaString(7), null, TestUtil.randomAlphaString(7)));

        assertThrows(
                IllegalArgumentException.class,
                () -> new DartSassReleaseParameter(null, TestUtil.randomAlphaString(7), null));

        assertThrows(
                IllegalArgumentException.class,
                () -> new DartSassReleaseParameter(null, TestUtil.randomAlphaString(7), TestUtil.randomAlphaString(7)));

        assertThrows(
                IllegalArgumentException.class,
                () -> new DartSassReleaseParameter(null, null, TestUtil.randomAlphaString(7)));
    }

    @Test
    public void testGetArtifactNameMacOsX64() {
        String os = OSDetector.OS_MAC_OS;
        String arch = OSDetector.ARCH_X64;
        String version = "1.71.1";

        DartSassReleaseParameter parameter = new DartSassReleaseParameter(os, arch, version);

        String artifactName = parameter.getArtifactName();

        assertEquals("dart-sass-1.71.1-macos-x64.tar.gz", artifactName);
    }

    @Test
    public void testGetArtifactNameLinuxArm() {
        String os = OSDetector.OS_LINUX;
        String arch = OSDetector.ARCH_ARM;
        String version = "1.71.1";

        DartSassReleaseParameter parameter = new DartSassReleaseParameter(os, arch, version);

        String artifactName = parameter.getArtifactName();

        assertEquals("dart-sass-1.71.1-linux-arm.tar.gz", artifactName);
    }

    @Test
    public void testGetArtifactNameWindowsX64() {
        String os = OSDetector.OS_WINDOWS;
        String arch = OSDetector.ARCH_X64;
        String version = "1.71.1";

        DartSassReleaseParameter parameter = new DartSassReleaseParameter(os, arch, version);

        String artifactName = parameter.getArtifactName();

        assertEquals("dart-sass-1.71.1-windows-x64.zip", artifactName);
    }

    @Test
    public void testGetArtifactNameLinuxMuslArm64() {
        String os = OSDetector.OS_LINUX_WITH_MUSL;
        String arch = OSDetector.ARCH_ARM64;
        String version = "1.71.1";

        DartSassReleaseParameter parameter = new DartSassReleaseParameter(os, arch, version);

        String artifactName = parameter.getArtifactName();

        assertEquals("dart-sass-1.71.1-linux-arm64-musl.tar.gz", artifactName);
    }

    @Test
    public void testGetArtifactNameWindowsArm64() {
        String os = OSDetector.OS_WINDOWS;
        String arch = OSDetector.ARCH_ARM64;
        String version = "1.85.1";

        DartSassReleaseParameter parameter = new DartSassReleaseParameter(os, arch, version);

        String artifactName = parameter.getArtifactName();

        assertEquals("dart-sass-1.85.1-windows-arm64.zip", artifactName);
    }
}
