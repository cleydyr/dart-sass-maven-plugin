package com.github.cleydyr.dart.maven.plugin;

import static org.junit.jupiter.api.Assertions.*;

import com.github.cleydyr.dart.command.enums.SourceMapURLs;
import com.github.cleydyr.dart.command.files.DefaultFileCounter;
import com.github.cleydyr.dart.maven.plugin.test.util.TestUtil;
import com.github.cleydyr.dart.net.DummyGithubLatestVersionProvider;
import com.github.cleydyr.dart.system.io.OSDependentDefaultCachedFilesDirectoryProviderFactory;
import com.github.cleydyr.maven.plugin.CompileSassMojo;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.apache.maven.shared.verifier.util.ResourceExtractor;
import org.junit.jupiter.api.Test;

public class CompileSassMojoTest {
    @Test
    public void testNoSourceMapFlag() throws Exception {
        CompileSassMojo compileSassMojo = new CompileSassMojo(
                new DefaultFileCounter(),
                () -> null,
                (any0, any1, any2) -> null,
                new DummyGithubLatestVersionProvider(),
                new OSDependentDefaultCachedFilesDirectoryProviderFactory(),
                null);

        compileSassMojo.setSourceMapURLs(SourceMapURLs.RELATIVE);
        compileSassMojo.setNoSourceMap(true);

        compileSassMojo.unsetIncompatibleOptions();

        assertNull(compileSassMojo.getSourceMapURLs());
        assertFalse(compileSassMojo.isEmbedSourceMap());
        assertFalse(compileSassMojo.isEmbedSources());
    }

    @Test
    public void testCompileSubfolders() throws Exception {
        TestUtil.installMainPlugin();

        File testDir = ResourceExtractor.simpleExtractResources(getClass(), "/test-project");

        TestUtil.executeGoal(testDir, "clean");

        TestUtil.executeGoal(testDir, "compile");

        Path outputDirPath = Paths.get(testDir.getAbsolutePath(), "target", "static", "styles");

        assertTrue(Files.exists(outputDirPath));

        String[] expectedFilesTop = {
            "test-top.css", "test-top.css.map", "subfolder/",
        };

        assertEquals(expectedFilesTop.length, getFileCount(outputDirPath));

        Path subfolder = Paths.get(outputDirPath.toString(), "subfolder");

        String[] expectedFilesSub = {
            "subfolder/test-sub1.css",
            "subfolder/test-sub1.css.map",
            "subfolder/test-sub2.css",
            "subfolder/test-sub2.css.map",
            "subfolder/test-sub3.css",
            "subfolder/test-sub3.css.map"
        };

        assertEquals(expectedFilesSub.length, getFileCount(subfolder));

        TestUtil.executeGoal(testDir, "clean");
    }

    private static long getFileCount(Path outputDirPath) throws IOException {
        long fileCount;

        try (Stream<Path> fileList = Files.list(outputDirPath)) {
            fileCount = fileList.count();
        }

        return fileCount;
    }
}
