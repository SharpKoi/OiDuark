package com.sharpkoi.oiduark.utils;

import java.io.File;
import java.nio.file.Paths;

/**
 * runtime OS checker and the default storage provider.
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

    public static String getUserHomePath() {
        return System.getProperty("user.home");
    }

    public static File getUserHome() {
        return new File(getUserHomePath());
    }

    public static String getUserDataHomePath() {
        if(isWindowsXP())
            return System.getenv("APPDATA");
        if(isWindows())
            return System.getenv("LOCALAPPDATA");
        if(isLinux())
            return Paths.get(getUserHomePath(), ".local/share").toString();

        return getUserHomePath();
    }

    public static File getUserDataHome() {
        return Paths.get(getUserDataHomePath()).toFile();
    }

    public static String getUserConfigHomePath() {
        if(isLinux())
            return Paths.get(getUserHomePath(), ".config").toString();

        return getUserHomePath();
    }

    public static File getUserConfigHome() {
        return Paths.get(getUserConfigHomePath()).toFile();
    }
}
