package com.sharpkoi.oiduark.audio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sharpkoi.oiduark.utils.MetaData;

public class Audio {
	// for operation
	private String filename;
	private double duration;
	private List<Integer> tags;
	
	//for display
	private String title;
	private String author;
	private String coverPath;
	private LinkedHashMap<Integer, String> timeLyrics;
	
	public Audio(String filename) {
		this(filename, "Unknown", "Unknown", "Unknown", 0.0);
	}
	
	public Audio(String filename, String title, String author, String coverPath, double duration) {
		this.filename = filename;
		this.title = title;
		this.author = author;
		this.coverPath = coverPath;
		this.duration = duration;
	}
	
	@SuppressWarnings("unchecked")
	public static Audio loadFromJson(String path, String filename) {
		Audio audio = null;
		//TODO: set audio info
		JSONParser parser = new JSONParser();
		try(InputStreamReader reader = new InputStreamReader(new FileInputStream(MetaData.AUDIO_DATA_PATH), Charset.forName("utf-8"))) {
			JSONObject audioDatabase = (JSONObject) parser.parse(reader);
			if(!audioDatabase.containsKey(filename)) {
				System.out.printf("[Warning] Can not find audio file: %s\n", filename);
				return null;
			}
			JSONObject audioData = (JSONObject) audioDatabase.get(filename);
			
			audio = new Audio(
						filename,
						audioData.getOrDefault("title", "Unknown").toString(),
						audioData.getOrDefault("author", "Unknown").toString(),
						audioData.getOrDefault("cover", "Unknown").toString(),
						(Double) audioData.getOrDefault("duration", 0.0)
					);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return audio;
	}
	
	public String getGlobalPath() {
		return new File(MetaData.MEDIA_DIR + filename).getAbsolutePath();
	}

	public String getFilename() {
		return filename;
	}

	public double getDuration() {
		return duration;
	}
	
	public void setDuration(double d) {
		this.duration = d;
	}

	public List<Integer> getTags() {
		return tags;
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCoverPath() {
		return coverPath;
	}
	
	public void setCoverPath(String path) {
		this.coverPath = path;
	}

	public LinkedHashMap<Integer, String> getTimeLyrics() {
		return timeLyrics;
	}
}
