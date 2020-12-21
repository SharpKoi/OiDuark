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
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sharpkoi.oiduark.app.Main;
import com.sharpkoi.oiduark.utils.Console;
import com.sharpkoi.oiduark.utils.OiDuarkUtils;

public class Audio {
	// file data
	private String filepath;
	private Date lastModified;
	private double duration;
	private LinkedList<Integer> tags;
	
	// extra data
	private boolean isFavorite;
	private String title;
	private String author;
	private String coverPath;
	private String lyricsFilePath;
	private LinkedHashMap<Integer, String> timeLyrics;
	
	public Audio(String filepath) {
		this(filepath, "Unknown", "Unknown", "Unknown", 0.0, new Date(), false);
	}
	
	public Audio(String filepath, String title, String author, String coverPath, double duration, Date lastModified, boolean isFavorite) {
		this.filepath = filepath;
		this.title = title;
		this.author = author;
		this.coverPath = coverPath;
		this.duration = duration;
		this.lastModified = lastModified;
		this.lyricsFilePath = "Unknown";
		this.isFavorite = isFavorite;
		
		tags = new LinkedList<>();
		timeLyrics = new LinkedHashMap<>();
	}
	
	@SuppressWarnings("unchecked")
	public static Audio loadFor(File audioFile) throws FileNotFoundException {
		Audio audio = null;
		String audioFilePath = audioFile.getPath();
		String defaultTitle = audioFilePath.substring(audioFilePath.lastIndexOf("\\")+1, audioFilePath.lastIndexOf("."));
		
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
				Console.WARNING("Audio %s has not been set.", audioFilePath);
				return new Audio(
							audioFilePath,
							defaultTitle,
							"Unknown",
							"Unknown",
							0.0,
							new Date(audioFile.lastModified()),
							false
						);
			}
			JSONObject audioData = (JSONObject) audioDatabase.get(audioFilePath);
			audio = new Audio(
						audioFilePath,
						audioData.getOrDefault("title", defaultTitle).toString(),
						audioData.getOrDefault("author", "Unknown").toString(),
						audioData.getOrDefault("cover", "Unknown").toString(),
						(Double) audioData.getOrDefault("duration", 0.0), 
						new Date(audioFile.lastModified()),
						(Boolean) audioData.getOrDefault("favorite", false)
					);
			
			JSONArray tagArray = (JSONArray) audioData.get("tags");
			for(Object tagid : tagArray) {
				audio.getTagIDList().add(Integer.valueOf(tagid.toString()));
			}
			
			String lyricsFilePath = audioData.getOrDefault("lyrics_file", "Unknown").toString();
			if(!lyricsFilePath.equals("Unknown")) {
				audio.loadLyrics(new File(lyricsFilePath));
			}
			
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
	
	public Date lastModified() {
		return lastModified;
	}

	public LinkedList<Integer> getTagIDList() {
		return tags;
	}
	
	public AudioTag[] getTags() {
		AudioTag[] tagArr = new AudioTag[tags.size()];
		for(int i = 0; i < tagArr.length; i++) {
			tagArr[i] = Main.getInstance().getAudioTagManager().getAudioTag(tags.get(i));
		}
		
		return tagArr;
	}
	
	public void addTag(AudioTag tag)  {
		
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
	
	public boolean isFavorite() {
		return isFavorite;
	}
	
	public void setFavorite(boolean b) {
		isFavorite = b;
	}

	public String getCoverPath() {
		return coverPath;
	}
	
	public void setCoverPath(String path) {
		this.coverPath = path;
	}
	
	public String getLyricsFilePath() {
		return lyricsFilePath;
	}

	public LinkedHashMap<Integer, String> getTimeLyrics() {
		return timeLyrics;
	}
	
	public void loadLyrics(File txt) throws FileNotFoundException {
		lyricsFilePath = txt.getAbsolutePath();
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
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Audio) {
			return filepath.equals(((Audio) obj).getFilePath());
		}
		return false;
	}
}
