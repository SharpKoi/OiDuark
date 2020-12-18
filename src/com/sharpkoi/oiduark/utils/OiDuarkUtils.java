package com.sharpkoi.oiduark.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.output.FileWriterWithEncoding;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sharpkoi.oiduark.audio.Audio;

public class OiDuarkUtils {
	
	public static JsonObject parseAudioToJson(Audio audio) {
		JsonObject obj = new JsonObject();
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonArray tagsJsonArray = (JsonArray) gson.toJsonTree(audio.getTagIDList());
		
		obj.addProperty("title", audio.getTitle());
		obj.addProperty("author", audio.getAuthor());
		obj.addProperty("cover", audio.getCoverPath());
		obj.addProperty("duration", audio.getDuration());
		obj.addProperty("favorite", audio.isFavorite());
		obj.add("tags", tagsJsonArray);
		obj.addProperty("lyrics_file", audio.getLyricsFilePath());
		
		return obj;
	}
	
	public static void saveJson(File store, JsonElement json) {
		try {
			if(!store.exists()) {
				store.getParentFile().mkdirs();
				store.createNewFile();
			}
			
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			FileWriterWithEncoding writer = new FileWriterWithEncoding(store, Charset.forName("utf-8"));
			gson.toJson(json, writer);
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
			
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			FileWriterWithEncoding writer = new FileWriterWithEncoding(jsonFile, Charset.forName("utf-8"));
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
