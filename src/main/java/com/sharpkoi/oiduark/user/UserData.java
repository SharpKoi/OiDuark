package com.sharpkoi.oiduark.user;

import lombok.AllArgsConstructor;

import java.io.File;
import java.nio.file.Paths;
import java.util.Properties;

@AllArgsConstructor
public class UserData {
    private UserConfig userConfig;
    private Properties props;

    public File getTagDataFile() {
        return Paths.get(
                userConfig.getUserdataDirPath(),
                props.getProperty("app.name").toLowerCase(),
                props.getProperty("tag-data-file")).toFile();
    }

    public File getMediaDataFile() {
        return Paths.get(
                userConfig.getUserdataDirPath(),
                props.getProperty("app.name").toLowerCase(),
                props.getProperty("media-data-file")).toFile();
    }
}
