package com.github.cleydyr.dart.maven.plugin;

import com.github.cleydyr.dart.command.SassCommand;
import com.github.cleydyr.dart.command.builder.SassCommandBuilder;
import com.github.cleydyr.dart.command.exception.SassCommandException;
import com.github.cleydyr.dart.command.files.DefaultFileCounter;
import com.github.cleydyr.dart.net.DummyGithubLatestVersionProvider;
import com.github.cleydyr.dart.system.io.DartSassExecutableExtractor;
import com.github.cleydyr.dart.system.io.OSDependentDefaultCachedFilesDirectoryProviderFactory;
import com.github.cleydyr.dart.system.io.factory.DartSassExecutableExtractorFactory;
import com.github.cleydyr.maven.plugin.WatchSassMojo;
import java.io.File;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class WatchSassMojoTest {
    private final String tmpDirPrefix = getClass().getName();

    @Test
    public void testWatchWithoutPoll() throws Exception {
        SassCommandBuilder mockSassCommandBuilder = _mockSassCommandBuilder();

        File stubFile = Files.createTempDirectory(tmpDirPrefix).toFile();

        WatchSassMojo watchSassMojo = new WatchSassMojo(
                new DefaultFileCounter(),
                () -> mockSassCommandBuilder,
                _mockDartSassExecutableExtractorFactory(),
                new DummyGithubLatestVersionProvider(),
                new OSDependentDefaultCachedFilesDirectoryProviderFactory(),
                null);

        watchSassMojo.setInputFolder(stubFile);
        watchSassMojo.setOutputFolder(stubFile);

        watchSassMojo.execute();

        Mockito.verify(mockSassCommandBuilder, Mockito.times(1)).withWatch(true);
        Mockito.verify(mockSassCommandBuilder, Mockito.times(0)).withPoll(true);
    }

    @Test
    public void testWatchWithPoll() throws Exception {
        SassCommandBuilder mockSassCommandBuilder = _mockSassCommandBuilder();

        File stubFile = Files.createTempDirectory(tmpDirPrefix).toFile();

        WatchSassMojo watchSassMojo = new WatchSassMojo(
                new DefaultFileCounter(),
                () -> mockSassCommandBuilder,
                _mockDartSassExecutableExtractorFactory(),
                new DummyGithubLatestVersionProvider(),
                new OSDependentDefaultCachedFilesDirectoryProviderFactory(),
                null);

        watchSassMojo.setInputFolder(stubFile);
        watchSassMojo.setOutputFolder(stubFile);
        watchSassMojo.setPoll(true);

        watchSassMojo.execute();

        Mockito.verify(mockSassCommandBuilder, Mockito.times(1)).withWatch(true);
        Mockito.verify(mockSassCommandBuilder, Mockito.times(1)).withPoll(true);
    }

    private SassCommandBuilder _mockSassCommandBuilder() throws SassCommandException {
        SassCommandBuilder mockSassCommandBuilder = Mockito.mock(SassCommandBuilder.class);

        SassCommand mockSassCommand = Mockito.mock(SassCommand.class);

        Mockito.when(mockSassCommandBuilder.build(Mockito.any())).thenReturn(mockSassCommand);

        return mockSassCommandBuilder;
    }

    private DartSassExecutableExtractorFactory _mockDartSassExecutableExtractorFactory() {
        return (any0, any1, any2) -> Mockito.mock(DartSassExecutableExtractor.class);
    }
}
