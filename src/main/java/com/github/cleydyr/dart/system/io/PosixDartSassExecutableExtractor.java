package com.github.cleydyr.dart.system.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Collections;

import com.github.cleydyr.dart.system.OSDetector;

public class PosixDartSassExecutableExtractor implements DartSassExecutableExtractor {
	@Override
	public void extract() throws IOException {
		InputStream inputStream = getClass().getResourceAsStream("/sass-binaries/" + OSDetector.getOSName() + "/" + OSDetector.getOSArchitecture() +  "/sass");

		Path executableFolder = createExecutableFolder();

		Path executablePath = executableFolder.resolve("sass");

		if (Files.exists(executablePath)) {
			return;
		}

		Files.copy(inputStream, executablePath);

		Files.setPosixFilePermissions(executablePath, Collections.singleton(PosixFilePermission.OWNER_EXECUTE));
	}
}
