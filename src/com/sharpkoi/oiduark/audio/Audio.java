package com.sharpkoi.oiduark.audio;

import java.util.LinkedHashMap;
import java.util.List;

public class Audio {
	// for operation
	private String path;
	private int duration;
	private List<Integer> tags;
	
	//for display
	private String title;
	private String author;
	private String coverPath;
	private LinkedHashMap<Integer, String> timeLyrics;
	
	public static Audio loadFromJson(String path) {
		Audio audio = new Audio();
		//TODO: set audio info
		
		return audio;
	}
}
