package com.sharpkoi.oiduark.audio;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.sharpkoi.oiduark.utils.MetaData;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioPlayer {

	// status
	private PlayMode mode = PlayMode.ORDERED;
	private boolean isPlaying;
	
	// values
	public int indicator = -1;
	private int volume;
	
	// tools
	private Random selector;
	
	// collections
	private List<Audio> playList;
	
	// media player
	private MediaPlayer player;
	
	public AudioPlayer() {
		selector = new Random();
		playList = new LinkedList<>();
		isPlaying = false;
	}
	
	public boolean isPlaying() {
		return isPlaying;
	}
	
	public List<Audio> getPlayList() {
		return playList;
	}
	
	public void addAudio(Audio audio) {
		playList.add(audio);
	}
	
	public void play() {
		//TODO: play the current audio
		if(player == null) {
			updateIndicator();
			String audioPath = MetaData.MEDIA_DIR + playList.get(indicator).getFilename();
			Media media = new Media(audioPath);
			player = new MediaPlayer(media);
			player.setVolume(volume);
		}
		isPlaying = true;
	}
	
	public void pause() {
		//TODO: pause the current audio
		isPlaying = false;
	}
	
	public void resume() {
		isPlaying = true;
	}
	
	public void updateIndicator() {
		switch(mode) {
		case ORDERED:
			indicator ++;
			break;
		case RANDOM:
			indicator = selector.nextInt(playList.size());
			break;
		case LOOP:
			break;
		default:
			break;
		}
	}
}

enum PlayMode {
	ORDERED,
	RANDOM,
	LOOP;
}
