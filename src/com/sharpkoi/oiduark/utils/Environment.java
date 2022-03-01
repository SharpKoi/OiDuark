package com.sharpkoi.oiduark.utils;

/**
 * Check the runtime OS.
 */
public class Environment {
    public static final String OS = System.getProperty("os.name").toLowerCase();

    public static boolean isLinux() {
        return OS.contains("linux");
    }

    public static boolean isWindows() {
        return OS.contains("windows");
    }

    public static boolean isWindowsXP() {
        return OS.contains("windows xp");
    }

    public static boolean isWindows7() {
        return OS.contains("windows 7");
    }

    public static boolean isMac() {
        return OS.contains("mac");
    }
}
