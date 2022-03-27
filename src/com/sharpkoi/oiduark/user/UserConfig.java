package com.sharpkoi.oiduark.user;

import java.io.*;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.sharpkoi.oiduark.app.Main;
import com.sharpkoi.oiduark.utils.Environment;
import com.sharpkoi.oiduark.utils.OiDuarkUtils;
import lombok.Data;

@Data
public class UserConfig {
	private String mediaDirPath;
	private String userdataDirPath;
	private boolean darkMode = true;
	private boolean lyricsDisplay = true;
	private int playerVolume = 100;

	private UserConfig() {
		this.mediaDirPath = getDefaultMediaDir().getAbsolutePath();
		this.userdataDirPath = Environment.getUserDataHomePath();
	}

	public static UserConfig defaultConfig() {
		return new UserConfig();
	}

	/**
	 * Get the default directory where musics are stored.
	 * OiDuark will load musics from the directory by default.
	 * @return the default directory where musics are stored
	 */
	public static File getDefaultMediaDir() {
		return new File(Environment.getUserHome(), "Music");
	}

	public File getMediaDir() {
		return Paths.get(mediaDirPath).toFile();
	}

	public File getUserdataDir() {
		return Paths.get(userdataDirPath).toFile();
	}

	public static UserConfig load(File jsonConfig) {
		try {
			assert jsonConfig.exists(): "Could not find the user config file.";

			Gson gson = new Gson();
			JsonReader reader = new JsonReader(new FileReader(jsonConfig));
			return gson.fromJson(reader, new TypeToken<UserConfig>() {}.getType());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public void save() {
		save(new File(Environment.getUserConfigHome(), Main.getInstance().getProperties().getString("user-config-file")));
	}
	
	public void save(File jsonConfig) {
		if(!jsonConfig.exists()) {
			OiDuarkUtils.createEmptyFile(jsonConfig);
		}

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		OiDuarkUtils.saveJson(jsonConfig, gson.toJsonTree(this));
	}
}
