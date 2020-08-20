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
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sharpkoi.oiduark.app.Main;

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
		
		timeLyrics = new LinkedHashMap<>();
	}
	
	@SuppressWarnings("unchecked")
	public static Audio loadFromJson(String audioFileName) throws FileNotFoundException {
		Audio audio = null;
		
		JSONParser parser = new JSONParser();
		InputStream mediaDataStream = new FileInputStream(Main.getInstance().getMediaDataPath());
		try(InputStreamReader reader = new InputStreamReader(mediaDataStream, Charset.forName("utf-8"))) {
			JSONObject audioDatabase = (JSONObject) parser.parse(reader);
			if(!audioDatabase.containsKey(audioFileName)) {
				System.out.printf("[Warning] Audio %s has not been set.\n", audioFileName);
				return new Audio(
							audioFileName,
							audioFileName.substring(0, audioFileName.lastIndexOf(".")),
							"Unknown",
							"Unknown",
							0.0
						);
			}
			JSONObject audioData = (JSONObject) audioDatabase.get(audioFileName);
			
			audio = new Audio(
						audioFileName,
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
		Path gpath = Paths.get(Main.getInstance().getMediaDir(), filename);
		return gpath.toFile().getAbsolutePath();
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
