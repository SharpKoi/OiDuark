package com.sharpkoi.oiduark.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sharpkoi.oiduark.app.Main;

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
	
	public void setUserMediaDir(String path) {
		this._mediaDir = path;
	}
	
	public int getUserVolume() {
		return _volume;
	}
	
	public static UserSetting load(File jsonConfig) {
		try {
			if(!jsonConfig.exists()) {
				jsonConfig.createNewFile();
			}
			
			InputStream configStream = new FileInputStream(jsonConfig);
			InputStreamReader reader = new InputStreamReader(configStream, Charset.forName("utf-8"));
			JSONParser parser = new JSONParser();
			JSONObject settingData = (JSONObject) parser.parse(reader);
			String mediaDir = settingData.get("media-dir").toString();
			int volume = ((Long) settingData.get("player-volume")).intValue();
			return new UserSetting(mediaDir, volume);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void save() {
		save(new File(Main.getInstance().getProperties().getString("user-setting")));
	}
	
	@SuppressWarnings("unchecked")
	public void save(File jsonConfig) {
		try {
			if(!jsonConfig.exists()) {
				OiDuarkUtils.createEmptyFile(jsonConfig);
			}
			JSONObject json = new JSONObject();
			json.put("media-dir", _mediaDir);
			json.put("player-volume", _volume);
			
			FileWriterWithEncoding writer = new FileWriterWithEncoding(jsonConfig, Charset.forName("utf-8"));
            writer.write(json.toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
