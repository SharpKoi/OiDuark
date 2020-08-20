package com.sharpkoi.oiduark.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class UserSetting {
	
	private String _mediaDir;
	private int _volume;
	
	public UserSetting(String mediaDir, int volume) {
		this._mediaDir = mediaDir;
		this._volume = volume;
	}
	
	public String getUserMediaDir() {
		return _mediaDir;
	}
	
	public int getUserVolume() {
		return _volume;
	}
	
	public static UserSetting load(File jsonConfig) throws FileNotFoundException {
		 InputStream configStream = new FileInputStream(jsonConfig);
		
		try(InputStreamReader reader = new InputStreamReader(configStream, Charset.forName("utf-8"))) {
			JSONParser parser = new JSONParser();
			JSONObject settingData = (JSONObject) parser.parse(reader);
			String mediaDir = settingData.get("media-dir").toString();
			int volume = (Integer) settingData.get("player-volume");
			return new UserSetting(mediaDir, volume);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public void save(File jsonConfig) {
		JSONObject json = new JSONObject();
		json.put("media-dir", _mediaDir);
		json.put("player-volume", _volume);
		
		try (FileWriter writer = new FileWriter(jsonConfig)) {
            writer.write(json.toJSONString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
