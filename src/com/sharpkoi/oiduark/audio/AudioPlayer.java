package com.sharpkoi.oiduark.audio;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.sharpkoi.oiduark.utils.ResourceLoader;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class AudioPlayer {
	
	// status
	private PlayMode mode = PlayMode.ORDERED;
	private boolean isPlaying;
	
	// values
	public int indicator = -1;
	private double volume = 1;
	
	// tools
	private Random selector;
	private ChangeListener<? super Duration> onTimeUpdateListener;
	private Runnable onMediaReady;
	
	// collections
	private List<Audio> playList;
	
	// media player
	private MediaPlayer player;
	
	public AudioPlayer() {
		selector = new Random();
		playList = new LinkedList<>();
		isPlaying = false;
	}
	
	public String getModeName() {
		return mode.name();
	}
	
	public void setNextMode() {
		int i = mode.ordinal() + 1;
		if(i >= PlayMode.values().length) {
			i = 0;
		}
		mode = PlayMode.values()[i];
	}
	
	public void setMode(String modeName) {
		this.mode = PlayMode.valueOf(modeName);
	}
	
	public boolean isPlaying() {
		return isPlaying;
	}
	
	public double getVolume() {
		return volume;
	}
	
	public void setVolume(double val) {
		this.volume = val;
		if(player != null) {
			player.setVolume(volume);
		}
	}
	
	public Audio getCurrentAudio() {
		return playList.get(indicator);
	}
	
	public List<Audio> getPlayList() {
		return playList;
	}
	
	public ObservableList<Audio> getObservablePlayList() {
		return FXCollections.observableList(playList);
	}
	
	public void addAudio(Audio audio) {
		playList.add(audio);
	}
	
	public void setOnTimeUpdate(ChangeListener<? super Duration> listener) {
		onTimeUpdateListener = listener;
	}
	
	public void setOnMediaReady(Runnable task) {
		this.onMediaReady = task;
	}
	
	public boolean play() {
		if(playList.isEmpty()) {
			return false;
		}
		
		updateIndicator();
		
		String audioFileName = playList.get(indicator).getFilename();
		Media media = ResourceLoader.loadMedia(audioFileName);
		player = new MediaPlayer(media);
		player.setVolume(volume);
		
		player.currentTimeProperty().addListener((observable, oldTime, newTime) -> {
			if(onTimeUpdateListener != null) {
				onTimeUpdateListener.changed(observable, oldTime, newTime);
			}
		});
		
		// check if user had changed the audio duration
		player.setOnReady(() -> {
			if(player.getTotalDuration().toSeconds() != getCurrentAudio().getDuration()) {
				getCurrentAudio().setDuration(player.getTotalDuration().toSeconds());
			}
			Platform.runLater(onMediaReady);
			player.play();
		});
		
		player.setOnEndOfMedia(() -> {
			System.out.println("[Info] Played to end!");
			play();
		});
		
		isPlaying = true;
		return isPlaying;
	}
	
	public void pause() {
		if(player != null) {
			isPlaying = false;
			player.pause();
		}
	}
	
	public void resume() {
		player.play();
		isPlaying = true;
	}
	
	public boolean jumpTo(double seconds) {
		if(player != null) {
			player.seek(Duration.seconds(seconds));
		}
		
		return false;
	}
	
	public boolean removeAudio(int index) {
		if(index != indicator && index >= 0 && index < playList.size()) {
			playList.remove(index);
			return true;
		}else {
			return false;
		}
	}
	
	public boolean removeAudio(Audio audio) {
		int targetIndex = playList.indexOf(audio);
		if(targetIndex != indicator && targetIndex != -1) {
			playList.remove(targetIndex);
			return true;
		}else {
			return false;
		}
	}
	
	public void updateIndicator() {
		switch(mode) {
		case ORDERED:
			if(++indicator >= playList.size()) {
				indicator = 0;
			}
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
