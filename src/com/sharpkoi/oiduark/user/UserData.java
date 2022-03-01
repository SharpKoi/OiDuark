package com.sharpkoi.oiduark.user;

import com.sharpkoi.oiduark.app.Main;
import com.sharpkoi.oiduark.utils.Environment;

import java.io.File;

public class UserData {

    private static File getUserHome() {
        return new File(System.getProperty("user.home"));
    }

    private static File getUserLocalDataDir() {
        if(Environment.isWindowsXP())
            return new File(System.getenv("APPDATA"));
        if(Environment.isWindows())
            return new File(System.getenv("LOCALAPPDATA"));
        if(Environment.isLinux())
            return new File(getUserHome(), ".local/share");

        return getUserHome();
    }

    /**
     * Get the directory where userdata is stored.
     * e.g. ~/.local/share for Linux, and C:/Users/${username}/AppData/Local/ for Windows.
     * @return the directory where userdata is stored
     */
    public static File getUserdataDir() {
        return new File(getUserLocalDataDir(), "OiDuark");
    }

    public static File getUserSettingFile() {
        return new File(getUserdataDir(), Main.getInstance().getProperties().getString("user-setting"));
    }

    public static File getTagDataFile() {
        return new File(getUserdataDir(), Main.getInstance().getProperties().getString("tag-data"));
    }

    public static File getMediaDataFile() {
        return new File(getUserdataDir(), Main.getInstance().getProperties().getString("media-data"));
    }

    /**
     * Get the default directory where musics are stored.
     * OiDuark will load musics from the directory by default.
     * @return the default directory where musics are stored
     */
    public static File getDefaultMediaDir() {
        // TODO: check windows user's music directory path.
        return new File(getUserHome(), "Music");
    }
}
