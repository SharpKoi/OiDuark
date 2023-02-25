package com.sharpkoi.oiduark.audio;

import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Random;

import com.sharpkoi.oiduark.utils.Console;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import lombok.Getter;

public class AudioPlayer {
	
	// status
	private Audio currentAudio;
	private PlayMode mode = PlayMode.ORDERED;
	private boolean isPlaying;
	
	// values
	@Getter
	private int indicator = -1;
	private double volume = 1;
	
	// tools
	private Random selector;
	
	private ChangeListener<? super Duration> onTimeUpdateListener;
	private ChangeListener<? super Number> onVolumeChanged;
	private Runnable onMediaReady;
	private Runnable onPlayerStop;
	
	// collections
	private ObservableList<Audio> playList;
	
	// media player
	private MediaPlayer player;
	
	public AudioPlayer() {
		selector = new Random();
		playList = FXCollections.observableList(new LinkedList<>());
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
		return currentAudio;
	}
	
	public ObservableList<Audio> getPlayList() {
		return playList;
	}
	
	public void addAudio(Audio audio) {
		playList.add(audio);
	}
	
	public void addPlaylistChangeListener(ListChangeListener<? super Audio> listener) {
		playList.addListener(listener);
	}
	
	public void setOnTimeUpdate(ChangeListener<? super Duration> listener) {
		onTimeUpdateListener = listener;
	}
	
	public void setOnMediaReady(Runnable task) {
		onMediaReady = task;
	}
	
	public void setOnPlayerStop(Runnable task) {
		onPlayerStop = task;
	}
	
	public void setOnVolumeChanged(ChangeListener<? super Number> listener) {
		onVolumeChanged = listener;
	}

	public void indicate(int index) {
		this.indicator = index;

		// update the media
		currentAudio = playList.get(indicator);

		Media media = new Media(Paths.get(currentAudio.getFilePath()).toUri().toString());
		player = new MediaPlayer(media);

		if(onVolumeChanged != null) {
			player.volumeProperty().addListener(onVolumeChanged);
		}
		player.setVolume(volume);

		// may cause memory leak
		player.currentTimeProperty().addListener(onTimeUpdateListener);

		// check if user had changed the audio duration
		player.setOnReady(() -> {
			if(player.getTotalDuration().toSeconds() != getCurrentAudio().getDuration()) {
				getCurrentAudio().getMetadata().setDuration(player.getTotalDuration().toSeconds());
			}
			Platform.runLater(onMediaReady);
			player.play();
		});

		player.setOnEndOfMedia(() -> {
			player.stop();
			player.dispose();
			play(true);
			Console.getLogger().info("next audio id:" + indicator);
		});
	}

	public boolean play(int index) {
		if(playList.isEmpty()) {
			return false;
		}

		indicate(index);

		return isPlaying = true;
	}
	
	public boolean play(boolean updateIndicator) {
		if(playList.isEmpty()) {
			return false;
		}

		if(updateIndicator) {
			this.updateIndicator();
		}

		return isPlaying = true;
	}
	
	public void pause() {
		if(player != null) {
			isPlaying = false;
			player.pause();
		}
	}
	
	public void resume() {
		isPlaying = true;
		player.play();		// call javafx player to play
	}
	
	public void stop() {
		isPlaying = false;
		currentAudio = null;
		if(player != null) {
			if(onPlayerStop != null) {
				Platform.runLater(onPlayerStop);
			}
			resetIndicator();
			player.stop();
			if(onVolumeChanged != null) {
				player.volumeProperty().removeListener(onVolumeChanged);
			}
			if(onTimeUpdateListener != null) {
				player.currentTimeProperty().removeListener(onTimeUpdateListener);
			}
			player.dispose();
		}
	}
	
	public boolean jumpToStart() {
		if(player != null) {
			player.seek(Duration.ZERO);
			return true;
		}
		return false;
	}
	
	public boolean jumpToEnd() {
		if(player != null) {
			player.seek(player.getTotalDuration());
			return true;
		}
		return false;
	}
	
	public boolean jumpTo(double seconds) {
		if(player != null) {
			player.seek(Duration.seconds(seconds));
			return true;
		}
		return false;
	}
	
	public boolean removeAudio(int index) {
		if(index >= 0 && index < playList.size()) {
			if(index != indicator) {
				playList.remove(index);
				// TODO: deal with the audio queue under RANDOM mode.
				return true;
			}else {
				// if the audio at the index to be removed is currently playing
				boolean playingBeforeRemoving = isPlaying;		// cache the current playing status before removing
				player.stop();
				player.dispose();
				playList.remove(index);
				if(playList.isEmpty()) {
					this.stop();
				}else {
					// find the new index
					indicate(indicator % playList.size());	// update for ORDERED mode
					// TODO: update for RANDOM mode

					if(playingBeforeRemoving) {
						play(false);
					}
				}
			}
			return true;
		}
		return false;
	}
	
	public boolean removeAudio(Audio audio) {
		int targetIndex = playList.indexOf(audio);
		return removeAudio(targetIndex);
	}
	
	public void updateIndicator() {
		int newIndex = indicator;
		switch (mode) {
			case ORDERED -> {
				newIndex = (indicator + 1) % playList.size();
				indicate(newIndex);
			}
			case RANDOM -> {
				int stepBound = playList.size() - 1;
				int step = stepBound <= 0 ? 0 : selector.nextInt(playList.size() - 1);
				newIndex = (indicator + step + 1) % playList.size();
				indicate(newIndex);
			}
			case LOOP, default -> {
			}
		}
	}
	
	public void resetIndicator() {
		indicator = -1;
	}
}

enum PlayMode {
	ORDERED,
	RANDOM,
	LOOP
}
