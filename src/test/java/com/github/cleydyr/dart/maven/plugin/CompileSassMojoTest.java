package com.github.cleydyr.dart.maven.plugin;

import com.github.cleydyr.dart.command.enums.SourceMapURLs;
import com.github.cleydyr.maven.plugin.CompileSassMojo;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import junit.framework.TestCase;
import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;

public class CompileSassMojoTest extends TestCase {
    public void testNoSourceMapFlag() throws Exception {
        CompileSassMojo compileSassMojo = new CompileSassMojo();

        compileSassMojo.setSourceMapURLs(SourceMapURLs.RELATIVE);
        compileSassMojo.setNoSourceMap(true);

        compileSassMojo.unsetIncompatibleOptions();

        assertNull(compileSassMojo.getSourceMapURLs());
        assertFalse(compileSassMojo.isEmbedSourceMap());
        assertFalse(compileSassMojo.isEmbedSources());
    }

    public void testCompileSubfolders() throws Exception {
        _installMainPlugin();

        File testDir = ResourceExtractor.simpleExtractResources(getClass(), "/test-project");

        _executeGoal(testDir, "clean");

        _executeGoal(testDir, "compile");

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
        };

        assertEquals(
                "There should be " + expectedFilesSub.length + " files in the sub-folder",
                expectedFilesSub.length,
                Files.list(subfolder).count());
    }

    private void _installMainPlugin() throws VerificationException {
        String mainPluginDir = System.getProperty("basedir");

        Verifier verifier = new Verifier(new File(mainPluginDir).getAbsolutePath());

        verifier.addCliOption("-DskipTests");
        verifier.addCliOption("-Dmaven.javadoc.skip=true");
        verifier.addCliOption("-Dgpg.skip");
        verifier.executeGoal("install");
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }

    private void _executeGoal(File testDir, String goal) throws VerificationException {
        Verifier verifier = new Verifier(testDir.getAbsolutePath());

        verifier.executeGoal(goal);
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }
}
