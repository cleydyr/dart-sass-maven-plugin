package com.github.cleydyr.dart.command.files;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Files;
import org.apache.maven.shared.verifier.util.ResourceExtractor;
import org.junit.jupiter.api.Test;

public class DefaultFileCounterTest {
    @Test
    public void testFileExtensions() {
        FileCounter fileCounter = new DefaultFileCounter();

        assertTrue(fileCounter.hasAllowedExtensionDifferentFolders("test.css"));
        assertTrue(fileCounter.hasAllowedExtensionDifferentFolders("test.scss"));
        assertTrue(fileCounter.hasAllowedExtensionDifferentFolders("test.sass"));
        assertFalse(fileCounter.hasAllowedExtensionSameFolder("test.css"));

        assertFalse(fileCounter.hasAllowedExtensionDifferentFolders(""));
        assertThrows(IllegalArgumentException.class, () -> fileCounter.hasAllowedExtensionDifferentFolders(null));

        assertFalse(fileCounter.hasAllowedExtensionDifferentFolders("testcss"));
        assertFalse(fileCounter.hasAllowedExtensionDifferentFolders("testsass"));
        assertFalse(fileCounter.hasAllowedExtensionDifferentFolders("testscss"));

        assertFalse(fileCounter.hasAllowedExtensionDifferentFolders("test.SCSS"));
        assertFalse(fileCounter.hasAllowedExtensionDifferentFolders("test.CSS"));
        assertFalse(fileCounter.hasAllowedExtensionDifferentFolders("test.SASS"));

        assertFalse(fileCounter.hasAllowedExtensionDifferentFolders("test.Sass"));
        assertFalse(fileCounter.hasAllowedExtensionDifferentFolders("test.Scss"));
        assertFalse(fileCounter.hasAllowedExtensionDifferentFolders("test.Css"));
    }

    @Test
    public void testFileCount() throws Exception {
        File testDir = ResourceExtractor.simpleExtractResources(getClass(), "/test-project/src/main/sass");

        FileCounter fileCounter = new DefaultFileCounter();

        assertEquals(
                4,
                fileCounter.getProcessableFileCount(
                        testDir.toPath(), Files.createTempDirectory("dart-sass-maven-plugin-test")));
    }

    @Test
    public void testFileCountWithSameInputAndOutputFolder() throws Exception {
        File testDir = ResourceExtractor.simpleExtractResources(getClass(), "/test-project/src/main/sass");

        FileCounter fileCounter = new DefaultFileCounter();

        assertEquals(3, fileCounter.getProcessableFileCount(testDir.toPath(), testDir.toPath()));
    }
}
