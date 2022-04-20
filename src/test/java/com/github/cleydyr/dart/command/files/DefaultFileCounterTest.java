package com.github.cleydyr.dart.command.files;

import static org.junit.Assert.assertThrows;

import java.io.File;
import junit.framework.TestCase;
import org.apache.maven.it.util.ResourceExtractor;

public class DefaultFileCounterTest extends TestCase {
    public void testFileExtensions() {
        FileCounter fileCounter = new DefaultFileCounter();

        assertTrue(fileCounter.hasAllowedExtension("test.css"));
        assertTrue(fileCounter.hasAllowedExtension("test.scss"));
        assertTrue(fileCounter.hasAllowedExtension("test.sass"));

        assertFalse(fileCounter.hasAllowedExtension(""));
        assertThrows(IllegalArgumentException.class, () -> fileCounter.hasAllowedExtension(null));

        assertFalse(fileCounter.hasAllowedExtension("testcss"));
        assertFalse(fileCounter.hasAllowedExtension("testsass"));
        assertFalse(fileCounter.hasAllowedExtension("testscss"));

        assertFalse(fileCounter.hasAllowedExtension("test.SCSS"));
        assertFalse(fileCounter.hasAllowedExtension("test.CSS"));
        assertFalse(fileCounter.hasAllowedExtension("test.SASS"));

        assertFalse(fileCounter.hasAllowedExtension("test.Sass"));
        assertFalse(fileCounter.hasAllowedExtension("test.Scss"));
        assertFalse(fileCounter.hasAllowedExtension("test.Css"));
    }

    public void testFileCount() throws Exception {
        File testDir = ResourceExtractor.simpleExtractResources(getClass(), "/test-project/src/main/sass");

        FileCounter fileCounter = new DefaultFileCounter();

        assertEquals(3, fileCounter.getProcessableFileCount(testDir.toPath()));
    }
}
