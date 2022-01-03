package com.github.cleydyr.dart.maven.plugin;

import com.github.cleydyr.dart.command.enums.SourceMapURLs;
import com.github.cleydyr.maven.plugin.CompileSassMojo;
import junit.framework.TestCase;

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
}
