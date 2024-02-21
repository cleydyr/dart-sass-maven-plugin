package com.github.cleydyr.dart.maven.plugin;

import com.github.cleydyr.dart.command.enums.SourceMapURLs;
import com.github.cleydyr.dart.command.files.DefaultFileCounter;
import com.github.cleydyr.dart.maven.plugin.test.util.TestUtil;
import com.github.cleydyr.dart.net.DummyGithubLatestVersionProvider;
import com.github.cleydyr.dart.system.io.OSDependentDefaultCachedFilesDirectoryProviderFactory;
import com.github.cleydyr.maven.plugin.CompileSassMojo;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import junit.framework.TestCase;
import org.apache.maven.shared.verifier.util.ResourceExtractor;

public class CompileSassMojoTest extends TestCase {
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

    public void testCompileSubfolders() throws Exception {
        TestUtil.installMainPlugin();

        File testDir = ResourceExtractor.simpleExtractResources(getClass(), "/test-project");

        TestUtil.executeGoal(testDir, "clean");

        TestUtil.executeGoal(testDir, "compile");

        Path outputDirPath = Paths.get(testDir.getAbsolutePath(), "target", "static", "styles");

        assertTrue("Output folder was not created", Files.exists(outputDirPath));

        String[] expectedFilesTop = {
            "test-top.css", "test-top.css.map", "subfolder/",
        };

        assertEquals(
                "There should be " + expectedFilesTop.length + " files in the top folder",
                expectedFilesTop.length,
                Files.list(outputDirPath).count());

        Path subfolder = Paths.get(outputDirPath.toString(), "subfolder");

        String[] expectedFilesSub = {
            "subfolder/test-sub1.css",
            "subfolder/test-sub1.css.map",
            "subfolder/test-sub2.css",
            "subfolder/test-sub2.css.map",
            "subfolder/test-sub3.css",
            "subfolder/test-sub3.css.map"
        };

        assertEquals(
                "There should be " + expectedFilesSub.length + " files in the sub-folder",
                expectedFilesSub.length,
                Files.list(subfolder).count());

        TestUtil.executeGoal(testDir, "clean");
    }
}
