package com.sharpkoi.oiduark.user;

import java.io.*;
import java.nio.file.Paths;
import java.util.Properties;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.sharpkoi.oiduark.OiDuarkApp;
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

	public static UserConfig load() {
		Properties props = OiDuarkApp.getInstance().getProperties();
		File configFile =
				Paths.get(Environment.getUserConfigHomePath(),
						props.getProperty("app.name").toLowerCase(),
						props.getProperty("user-config-file")).toFile();
		if(configFile.exists())
			return load(configFile);
		else
			return new UserConfig();
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
		Properties props = OiDuarkApp.getInstance().getProperties();
		File configFile =
				Paths.get(Environment.getUserConfigHomePath(),
						props.getProperty("app.name").toLowerCase(),
						props.getProperty("user-config-file")).toFile();
		save(configFile);
	}
	
	public void save(File jsonConfig) {
		if(!jsonConfig.exists()) {
			OiDuarkUtils.createEmptyFile(jsonConfig);
		}

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		OiDuarkUtils.saveJson(jsonConfig, gson.toJsonTree(this));
	}
}
