package com.github.cleydyr.maven.plugin;

import javax.inject.Inject;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.github.cleydyr.dart.command.factory.SassCommandBuilderFactory;
import com.github.cleydyr.dart.command.files.FileCounter;
import com.github.cleydyr.dart.system.io.factory.DartSassExecutableExtractorFactory;

/**
 * Goal that compiles a set of sass/scss files from an input directory to an output directory keeps the process opened watching for changes in the source files.
 */
@Mojo(name = "watch-sass")
public class WatchSassMojo extends CompileSassMojo {
	/**
     * This flag tells Sass to manually check for changes to the source files every so often
     * instead of relying on the operating system to notify it when something changes. This may
     * be necessary if you’re editing Sass on a remote drive where the operating system’s
     * notification system doesn’t work.
     */
    @Parameter(defaultValue = "false")
    private boolean poll;

	 @Inject
	public WatchSassMojo(FileCounter fileCounter, SassCommandBuilderFactory sassCommandBuilderFactory,
			DartSassExecutableExtractorFactory dartSassExecutableExtractorFactory) {
		super(fileCounter, sassCommandBuilderFactory, dartSassExecutableExtractorFactory);
	}

	 @Override
	protected void setOptions() {
		super.setOptions();

		sassCommandBuilder.withWatch(true);
		sassCommandBuilder.withPoll(poll);
	}

	public boolean isPoll() {
		return poll;
	}

	public void setPoll(boolean poll) {
		this.poll = poll;
	}
}
