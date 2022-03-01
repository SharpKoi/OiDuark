package com.sharpkoi.oiduark.user;

import java.io.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.sharpkoi.oiduark.utils.OiDuarkUtils;

public class UserSetting {
	
	private String mediaDirPath;
	private int playerVolume;
	
	public UserSetting(String mediaDir, int volume) {
		this.mediaDirPath = mediaDir;
		this.playerVolume = volume;
	}
	
	public String getUserMediaDir() {
		return mediaDirPath;
	}
	
	public void setUserMediaDirPath(String path) {
		this.mediaDirPath = path;
	}
	
	public int getUserVolume() {
		return playerVolume;
	}
	
	public static UserSetting load(File jsonConfig) {
		try {
			if(!jsonConfig.exists()) {
				jsonConfig.createNewFile();
			}

			Gson gson = new Gson();
			JsonReader reader = new JsonReader(new FileReader(jsonConfig));
			return gson.fromJson(reader, new TypeToken<UserSetting>() {}.getType());

//			InputStream configStream = new FileInputStream(jsonConfig);
//			InputStreamReader reader = new InputStreamReader(configStream, StandardCharsets.UTF_8);
//			JSONParser parser = new JSONParser();
//			JSONObject settingData = (JSONObject) parser.parse(reader);
//			String mediaDirPath = settingData.get("media-dir").toString();
//			int volume = ((Long) settingData.get("player-volume")).intValue();
//			return new UserSetting(mediaDirPath, volume);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public void save() {
		save(UserData.getUserSettingFile());
	}
	
	public void save(File jsonConfig) {
		if(!jsonConfig.exists()) {
			OiDuarkUtils.createEmptyFile(jsonConfig);
		}

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		OiDuarkUtils.saveJson(jsonConfig, gson.toJsonTree(this));
	}
}
