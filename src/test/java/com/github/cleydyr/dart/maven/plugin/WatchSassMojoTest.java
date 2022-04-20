package com.github.cleydyr.dart.maven.plugin;

import java.io.File;

import org.mockito.Mockito;

import com.github.cleydyr.dart.command.SassCommand;
import com.github.cleydyr.dart.command.builder.SassCommandBuilder;
import com.github.cleydyr.dart.command.files.DefaultFileCounter;
import com.github.cleydyr.dart.system.io.DartSassExecutableExtractor;
import com.github.cleydyr.dart.system.io.factory.DartSassExecutableExtractorFactory;
import com.github.cleydyr.maven.plugin.WatchSassMojo;
import com.google.common.io.Files;

import junit.framework.TestCase;

public class WatchSassMojoTest extends TestCase {
    public void testWatchWithoutPoll() throws Exception {
    	SassCommandBuilder mockSassCommandBuilder = _mockSassCommandBuilder();

        File stubFile = Files.createTempDir(); 

        WatchSassMojo watchSassMojo = new WatchSassMojo(new DefaultFileCounter(), () -> mockSassCommandBuilder, _mockDartSassExecutableExtractorFactory());

        watchSassMojo.setInputFolder(stubFile);
        watchSassMojo.setOutputFolder(stubFile);

        watchSassMojo.execute();

        Mockito.verify(mockSassCommandBuilder, Mockito.times(1)).withWatch(true);
        Mockito.verify(mockSassCommandBuilder, Mockito.times(0)).withPoll(true);
    }

    public void testWatchWithPoll() throws Exception {
    	SassCommandBuilder mockSassCommandBuilder = _mockSassCommandBuilder();

        File stubFile = Files.createTempDir(); 

        WatchSassMojo watchSassMojo = new WatchSassMojo(new DefaultFileCounter(), () -> mockSassCommandBuilder, _mockDartSassExecutableExtractorFactory());

        watchSassMojo.setInputFolder(stubFile);
        watchSassMojo.setOutputFolder(stubFile);
        watchSassMojo.setPoll(true);

        watchSassMojo.execute();

        Mockito.verify(mockSassCommandBuilder, Mockito.times(1)).withWatch(true);
        Mockito.verify(mockSassCommandBuilder, Mockito.times(1)).withPoll(true);
    }

	private SassCommandBuilder _mockSassCommandBuilder() {
		SassCommandBuilder mockSassCommandBuilder =
    			Mockito.mock(SassCommandBuilder.class);

    	SassCommand mockSassCommand = Mockito.mock(SassCommand.class);

    	Mockito.when(mockSassCommandBuilder.build()).thenReturn(mockSassCommand);
		return mockSassCommandBuilder;
	}

	private DartSassExecutableExtractorFactory _mockDartSassExecutableExtractorFactory() {
		DartSassExecutableExtractor mockDartSassExecutableExtractor =
    			Mockito.mock(DartSassExecutableExtractor.class);

		return () -> mockDartSassExecutableExtractor;
	}
}
