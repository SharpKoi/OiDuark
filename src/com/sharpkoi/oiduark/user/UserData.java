package com.sharpkoi.oiduark.user;

import lombok.AllArgsConstructor;

import java.io.File;
import java.nio.file.Paths;
import java.util.ResourceBundle;

@AllArgsConstructor
public class UserData {
    private UserConfig userConfig;
    private ResourceBundle props;

    public File getTagDataFile() {
        return Paths.get(
                userConfig.getUserdataDirPath(),
                props.getString("app-name").toLowerCase(),
                props.getString("tag-data-file")).toFile();
    }

    public File getMediaDataFile() {
        return Paths.get(
                userConfig.getUserdataDirPath(),
                props.getString("app-name").toLowerCase(),
                props.getString("media-data-file")).toFile();
    }
}
