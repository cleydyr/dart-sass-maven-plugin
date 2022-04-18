package com.github.cleydyr.dart.command.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Stream;

import javax.inject.Named;
import javax.inject.Singleton;


@Named
@Singleton
public class DefaultFileCounter implements FileCounter {
	private static String[] ALLOWED_EXTENSIONS = {".css", ".sass", ".scss"};

	@Override
	public long getProcessableFileCount(Path inputFolderPath) throws FileCounterException {
		try (Stream<Path> walk = Files.walk(inputFolderPath)) {
			return walk.parallel()
					.filter(p -> !Files.isDirectory(p)) // files only
					.map(p -> p.toString()) // convert path to string
					.filter(this::hasAllowedExtension) // check file extension
					.count();
		} catch (IOException e) {
			throw new FileCounterException("Can't list folder " + inputFolderPath, e);
		}
	}

	@Override
	public boolean hasAllowedExtension(String fileName) {
		if (fileName == null) {
			throw new IllegalArgumentException("fileName can't be null");
		}

		return Arrays.stream(ALLOWED_EXTENSIONS).anyMatch(fileName::endsWith);
	}
}
