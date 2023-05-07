package com.github.cleydyr.dart.release;

import static org.junit.Assert.assertThrows;

import com.github.cleydyr.dart.maven.plugin.test.util.TestUtil;
import junit.framework.TestCase;

public class DartSassReleaseParameterTest extends TestCase {
    public void testGetters() {
        String os = TestUtil.randomAlphaString(7);
        String arch = TestUtil.randomAlphaString(7);
        String version = TestUtil.randomAlphaString(7);

        DartSassReleaseParameter parameter = new DartSassReleaseParameter(os, arch, version);

        assertEquals(parameter.getArch(), arch);
        assertEquals(parameter.getOS(), os);
        assertEquals(parameter.getVersion(), version);
    }

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
}
