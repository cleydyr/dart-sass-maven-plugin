package com.github.cleydyr.dart.maven.plugin.test.util;

import java.io.File;
import java.util.Random;
import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;

public class TestUtil {

    public static void installMainPlugin() throws VerificationException {
        String mainPluginDir = System.getProperty("basedir");

        Verifier verifier = new Verifier(new File(mainPluginDir).getAbsolutePath());

        verifier.addCliOption("-DskipTests");
        verifier.addCliOption("-Dmaven.javadoc.skip=true");
        verifier.addCliOption("-Dgpg.skip");
        verifier.executeGoal("install");
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }

    public static void executeGoal(File testDir, String goal) throws VerificationException {
        Verifier verifier = new Verifier(testDir.getAbsolutePath());
        verifier.executeGoal(goal);
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
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
