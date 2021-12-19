package com.github.cleydyr.dart.command.parameter;

import java.nio.file.Path;

public class ParameterPair {
	Path inputPath;
	Path outputPath;

	public ParameterPair(Path inputPath, Path outputPath) {
		super();

		this.inputPath = inputPath;
		this.outputPath = outputPath;
	}

	public Path getInputPath() {
		return inputPath;
	}

	public Path getOutputPath() {
		return outputPath;
	}
}