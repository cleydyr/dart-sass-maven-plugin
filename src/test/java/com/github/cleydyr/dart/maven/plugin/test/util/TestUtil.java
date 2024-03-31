package com.github.cleydyr.dart.maven.plugin.test.util;

import java.io.File;
import java.util.Random;
import org.apache.maven.shared.verifier.VerificationException;
import org.apache.maven.shared.verifier.Verifier;

public final class TestUtil {
    private TestUtil() {
        // Utility class
    }

    public static void installMainPlugin() throws VerificationException {
        String mainPluginDir = System.getProperty("basedir");

        Verifier verifier = new Verifier(new File(mainPluginDir).getAbsolutePath());

        verifier.addCliArguments("-DskipTests", "-Dmaven.javadoc.skip=true", "-Dgpg.skip", "install");
        verifier.execute();
        verifier.verifyErrorFreeLog();
    }

    public static void executeGoal(File testDir, String goal) throws VerificationException {
        Verifier verifier = new Verifier(testDir.getAbsolutePath());
        verifier.addCliArgument(goal);
        verifier.execute();
        verifier.verifyErrorFreeLog();
    }

    public static String randomAlphaString(int size) {
        char[] chars = new char[size];

        Random rand = new Random();

        for (int i = 0; i < size; i++) {
            chars[i] = (char) ('a' + rand.nextInt(26));
        }

        return new String(chars);
    }
}
