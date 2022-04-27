package com.github.cleydyr.dart.maven.plugin;

import com.github.cleydyr.dart.command.SassCommand;
import com.github.cleydyr.dart.command.builder.SassCommandBuilder;
import com.github.cleydyr.dart.command.exception.SassCommandException;
import com.github.cleydyr.dart.command.files.DefaultFileCounter;
import com.github.cleydyr.dart.system.io.DartSassExecutableExtractor;
import com.github.cleydyr.dart.system.io.factory.DartSassExecutableExtractorFactory;
import com.github.cleydyr.maven.plugin.WatchSassMojo;
import java.io.File;
import java.nio.file.Files;
import junit.framework.TestCase;
import org.mockito.Mockito;

public class WatchSassMojoTest extends TestCase {
    private String _tmpDirPrefix = getClass().getName();

    public void testWatchWithoutPoll() throws Exception {
        SassCommandBuilder mockSassCommandBuilder = _mockSassCommandBuilder();

        File stubFile = Files.createTempDirectory(_tmpDirPrefix).toFile();

        WatchSassMojo watchSassMojo = new WatchSassMojo(
                new DefaultFileCounter(), () -> mockSassCommandBuilder, _mockDartSassExecutableExtractorFactory());

        watchSassMojo.setInputFolder(stubFile);
        watchSassMojo.setOutputFolder(stubFile);

        watchSassMojo.execute();

        Mockito.verify(mockSassCommandBuilder, Mockito.times(1)).withWatch(true);
        Mockito.verify(mockSassCommandBuilder, Mockito.times(0)).withPoll(true);
    }

    public void testWatchWithPoll() throws Exception {
        SassCommandBuilder mockSassCommandBuilder = _mockSassCommandBuilder();

        File stubFile = Files.createTempDirectory(_tmpDirPrefix).toFile();

        WatchSassMojo watchSassMojo = new WatchSassMojo(
                new DefaultFileCounter(), () -> mockSassCommandBuilder, _mockDartSassExecutableExtractorFactory());

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

        Mockito.when(mockSassCommandBuilder.build()).thenReturn(mockSassCommand);

        return mockSassCommandBuilder;
    }

    private DartSassExecutableExtractorFactory _mockDartSassExecutableExtractorFactory() {
        DartSassExecutableExtractor mockDartSassExecutableExtractor = Mockito.mock(DartSassExecutableExtractor.class);

        return () -> mockDartSassExecutableExtractor;
    }
}
