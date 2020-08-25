package com.sharpkoi.oiduark.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

public class OiDuarkUtils {
	
	public static void saveJson(File store, JSONAware json) {
		try {
			if(!store.exists()) {
				store.getParentFile().mkdirs();
				store.createNewFile();
			}
			
			FileWriter writer = new FileWriter(store);
			writer.write(json.toJSONString());
	        writer.flush();
	        writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void createEmptyJsonFile(File jsonFile) {
		try {
			jsonFile.getParentFile().mkdirs();
			jsonFile.createNewFile();
			
			FileWriter writer = new FileWriter(jsonFile);
			writer.write(new JSONObject().toJSONString());
	        writer.flush();
	        writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void createEmptyFile(File file) {
		try {
			file.getParentFile().mkdirs();
			file.createNewFile();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
