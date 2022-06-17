package com.github.cleydyr.dart.system;

public class OSDetector {
    public static final String OS_MAC_OS = "macos";

    public static final String OS_WINDOWS = "windows";

    public static final String OS_LINUX = "linux";

    private static final String _DETECTED_OS;

    private static final String _DETECTED_ARCHITECTURE;

    private static final boolean _IS_WINDOWS;

    static {
        String osName = System.getProperty("os.name");

        if (osName == null) {
            throw new Error("os.name system property is not set");
        }

        if (osName.contains("Mac OS")) {
            _DETECTED_OS = OS_MAC_OS;

            _IS_WINDOWS = false;
        } else if (osName.contains("Windows")) {
            _DETECTED_OS = OS_WINDOWS;

            _IS_WINDOWS = true;
        } else {
            _DETECTED_OS = OS_LINUX;

            _IS_WINDOWS = false;
        }

        String osArchitecture = System.getProperty("os.arch");

        if (osArchitecture == null) {
            throw new Error("os.arch system property is not set");
        }

        if (osArchitecture.equals("aarch64")) {
            _DETECTED_ARCHITECTURE = "aarch64";
        } else if (osArchitecture.contains("64")) {
            _DETECTED_ARCHITECTURE = "x64";
        } else {
            _DETECTED_ARCHITECTURE = "ia32";
        }
    }

    public static String getOSName() {
        return _DETECTED_OS;
    }

    public static String getOSArchitecture() {
        return _DETECTED_ARCHITECTURE;
    }

    public static boolean isWindows() {
        return _IS_WINDOWS;
    }
}
