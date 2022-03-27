package com.sharpkoi.oiduark.user;

import com.sharpkoi.oiduark.app.Main;
import lombok.AllArgsConstructor;

import java.io.File;
import java.util.ResourceBundle;

@AllArgsConstructor
public class UserData {
    private UserConfig userConfig;
    private ResourceBundle props;

    public File getTagDataFile() {
        return new File(userConfig.getUserdataDir(), props.getString("tag-data-file"));
    }

    public File getMediaDataFile() {
        return new File(userConfig.getUserdataDir(), props.getString("media-data-file"));
    }
}
