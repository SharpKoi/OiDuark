package com.sharpkoi.oiduark.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sharpkoi.oiduark.audio.Audio;

/**
 * Utilities for json parsing and writing.
 */
public class OiDuarkUtils {
	public static void saveJson(File file, JsonElement json) {
		try {
			if(!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8);
			gson.toJson(json, writer);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void createEmptyJsonFile(File file) {
		try {
			file.getParentFile().mkdirs();
			file.createNewFile();
			
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8);
			gson.toJson(new JsonObject(), writer);
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
