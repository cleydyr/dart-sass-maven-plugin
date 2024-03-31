package com.github.cleydyr.dart.system;

import com.github.cleydyr.dart.system.exception.OSDetectionException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public final class OSDetector {
    private OSDetector() {
        // Utility class
    }

    public static final String ARCH_IA32 = "ia32";

    public static final String ARCH_X64 = "x64";

    public static final String ARCH_ARM = "arm";

    public static final String AARCH32 = "aarch32";

    public static final String AARCH64 = "aarch64";

    public static final String ARCH_ARM64 = "arm64";

    public static final String OS_MAC_OS = "macos";

    public static final String OS_WINDOWS = "windows";

    public static final String OS_LINUX = "linux";

    public static final String OS_LINUX_WITH_MUSL = "linux-musl";

    public static final Collection<String> ACCEPTED_OSES = new ArrayList<>();

    public static final Collection<String> ACCEPTED_ARCHITECTURES = new ArrayList<>();

    private static final String DETECTED_OS;

    private static final String DETECTED_ARCHITECTURE;

    private static final boolean IS_WINDOWS;

    static {
        Collections.addAll(ACCEPTED_OSES, OS_LINUX, OS_MAC_OS, OS_WINDOWS, OS_LINUX_WITH_MUSL);

        Collections.addAll(ACCEPTED_ARCHITECTURES, ARCH_ARM64, ARCH_ARM, ARCH_IA32, ARCH_X64);

        String osName = System.getProperty("os.name");

        if (osName == null) {
            throw new OSDetectionException("os.name system property is not set");
        }

        if (osName.contains("Mac OS")) {
            DETECTED_OS = OS_MAC_OS;

            IS_WINDOWS = false;
        } else if (osName.contains("Windows")) {
            DETECTED_OS = OS_WINDOWS;

            IS_WINDOWS = true;
        } else {
            DETECTED_OS = OS_LINUX;

            IS_WINDOWS = false;
        }

        String osArchitecture = System.getProperty("os.arch");

        if (osArchitecture == null) {
            throw new OSDetectionException("os.arch system property is not set");
        }

        if (AARCH32.equals(osArchitecture)) {
            DETECTED_ARCHITECTURE = ARCH_ARM;
        } else if (AARCH64.equals(osArchitecture)) {
            DETECTED_ARCHITECTURE = ARCH_ARM64;
        } else if (osArchitecture.contains("64")) {
            DETECTED_ARCHITECTURE = ARCH_X64;
        } else {
            DETECTED_ARCHITECTURE = ARCH_IA32;
        }
    }

    public static String getOSName() {
        return DETECTED_OS;
    }

    public static String getOSArchitecture() {
        return DETECTED_ARCHITECTURE;
    }

    public static boolean isWindows() {
        return IS_WINDOWS;
    }

    public static boolean isAcceptedOSName(String osName) {
        return ACCEPTED_OSES.stream().anyMatch(osName::equals);
    }

    public static boolean isAcceptedArchitecture(String architecture) {
        return ACCEPTED_ARCHITECTURES.stream().anyMatch(architecture::equals);
    }
}
