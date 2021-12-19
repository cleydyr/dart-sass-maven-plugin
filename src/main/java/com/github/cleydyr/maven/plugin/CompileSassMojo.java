package com.github.cleydyr.maven.plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.github.cleydyr.dart.command.SassCommand;
import com.github.cleydyr.dart.command.builder.SassCommandBuilder;
import com.github.cleydyr.dart.command.exception.SassCommandException;
import com.github.cleydyr.dart.command.factory.SassCommandBuilderFactory;
import com.github.cleydyr.dart.system.io.DartSassExecutableExtractor;
import com.github.cleydyr.dart.system.io.factory.DartSassExecutableExtractorFactory;

/**
 * Goal that compiles a set of sass/scss files from an input directory to an
 * output directory
 *
 */
@Mojo(name = "compile-sass", defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
public class CompileSassMojo extends AbstractMojo {

	private static final String[] ALLOWED_EXTENSIONS = new String[]{".scss", ".sass"};

	@Parameter(required = true)
	private File inputFolder;

	@Parameter(required = true)
	private File outputFolder;

	public void execute() throws MojoExecutionException {
		DartSassExecutableExtractor dartSassExecutableExtractor = DartSassExecutableExtractorFactory.getDartSassExecutableExtractor();

		try {
			dartSassExecutableExtractor.extract();
		} catch (IOException ioException) {
			throw new MojoExecutionException("Unable to extract sass executable", ioException);
		}

		SassCommand sassCommand = buildSassCommand();

		try {
			sassCommand.execute();
		} catch (SassCommandException sassCommandException) {
			throw new MojoExecutionException("Can't execute SASS command", sassCommandException);
		}
	}

	private SassCommand buildSassCommand() throws MojoExecutionException {
		SassCommandBuilder sassCommandBuilder = SassCommandBuilderFactory.getCommanderBuilder();

		Path inputFolderPath = inputFolder.toPath();

		try {
			Files.list(inputFolderPath).forEach(inputFilePath -> {
				Arrays.stream(ALLOWED_EXTENSIONS)
					.filter(inputFilePath.toString()::endsWith)
					.forEach(extension -> {
						Path outputFilePath = _createHomonymousFile(inputFilePath, extension);

						sassCommandBuilder.withPaths(inputFilePath, outputFilePath);
					});
			});
		} catch (IOException e) {
			throw new MojoExecutionException("Can't list folder " + inputFolderPath, e);
		}

		return sassCommandBuilder.build();
	}

	private Path _createHomonymousFile(Path inputFilePath, String extension) {
		Path fileNamePath = inputFilePath.getName(inputFilePath.getNameCount() - 1);

		String fileName = fileNamePath.toString();

		String newFileName = fileName.replace(extension, ".css");

		return outputFolder.toPath().resolve(newFileName);
	}
}
