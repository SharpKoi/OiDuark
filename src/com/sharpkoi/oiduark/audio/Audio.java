package com.sharpkoi.oiduark.audio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sharpkoi.oiduark.app.Main;
import com.sharpkoi.oiduark.utils.OiDuarkUtils;

public class Audio {
	// for operation
	private String filepath;
	private double duration;
	private LinkedList<Integer> tags;
	
	//for display
	private String title;
	private String author;
	private String coverPath;
	private LinkedHashMap<Integer, String> timeLyrics;
	
	public Audio(String filepath) {
		this(filepath, "Unknown", "Unknown", "Unknown", 0.0);
	}
	
	public Audio(String filepath, String title, String author, String coverPath, double duration) {
		this.filepath = filepath;
		this.title = title;
		this.author = author;
		this.coverPath = coverPath;
		this.duration = duration;
		
		tags = new LinkedList<>();
		timeLyrics = new LinkedHashMap<>();
	}
	
	@SuppressWarnings("unchecked")
	public static Audio loadFor(String audioFilePath) throws FileNotFoundException {
		Audio audio = null;
		
		JSONParser parser = new JSONParser();
		try {
			File mediaDataFile = new File(Main.getInstance().getMediaDataPath());
			if(!mediaDataFile.exists()) {
				OiDuarkUtils.createEmptyJsonFile(mediaDataFile);
			}
			
			InputStream mediaDataStream = new FileInputStream(mediaDataFile);
			InputStreamReader reader = new InputStreamReader(mediaDataStream, Charset.forName("utf-8"));
			JSONObject audioDatabase = (JSONObject) parser.parse(reader);
			if(!audioDatabase.containsKey(audioFilePath)) {
				System.out.printf("[Warning] Audio %s has not been set.\n", audioFilePath);
				return new Audio(
							audioFilePath,
							audioFilePath.substring(audioFilePath.lastIndexOf("\\")+1, audioFilePath.lastIndexOf(".")),
							"Unknown",
							"Unknown",
							0.0
						);
			}
			JSONObject audioData = (JSONObject) audioDatabase.get(audioFilePath);
			
			audio = new Audio(
						audioFilePath,
						audioData.getOrDefault("title", "Unknown").toString(),
						audioData.getOrDefault("author", "Unknown").toString(),
						audioData.getOrDefault("cover", "Unknown").toString(),
						(Double) audioData.getOrDefault("duration", 0.0)
					);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return audio;
	}
	
	public String getGlobalPath() {
		Path gpath = Paths.get(Main.getInstance().getMediaDir(), filepath);
		return gpath.toFile().getAbsolutePath();
	}

	public String getFilePath() {
		return filepath;
	}

	public double getDuration() {
		return duration;
	}
	
	public void setDuration(double d) {
		this.duration = d;
	}

	public LinkedList<Integer> getTags() {
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
	
	public void loadLyrics(File txt) throws FileNotFoundException {
		InputStream in = new FileInputStream(txt);
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("utf-8")))) {
			String line = "";
			while((line = reader.readLine()) != null) {
				int segIndex = line.indexOf(" ");
				timeLyrics.put(Integer.valueOf(line.substring(0, segIndex)), line.substring(segIndex).trim());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
