package com.sharpkoi.oiduark.audio;

import java.util.LinkedList;
import java.util.List;

public class AudioPlayer {

	// status
	private boolean isPlaying;
	
	// values
	private int volume;
	
	private List<String> playList = new LinkedList<String>();
	
	public AudioPlayer() {
		isPlaying = false;
	}
	
	public boolean isPlaying() {
		return isPlaying;
	}
	
	public List<String> getPlayList() {
		return playList;
	}
	
	public void play() {
		//TODO: play the current audio
		isPlaying = true;
	}
	
	public void pause() {
		//TODO: pause the current audio
		isPlaying = false;
	}
}
