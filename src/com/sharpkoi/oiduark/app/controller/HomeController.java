package com.sharpkoi.oiduark.app.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXSlider;
import com.sharpkoi.oiduark.app.*;
import com.sharpkoi.oiduark.audio.Audio;
import com.sharpkoi.oiduark.audio.AudioPlayer;
import com.sharpkoi.oiduark.utils.TimeUtils;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class HomeController extends GlobalController {
	
	private static HomeController instance;
	
	public static HomeController getInstance() {
		return instance;
	}
		
	@FXML
	private AnchorPane root;
	
	/***** player controller *****/
	@FXML
	private Label t_timeTick;
	@FXML
	private Label t_endTimeTick;
	@FXML
	private JFXSlider progressBar;
    @FXML
    private ImageView b_play;
	@FXML
    private ImageView b_next;
	@FXML
    private ImageView b_last;
	@FXML
	private MaterialDesignIconView i_playMode;
	
	
	/***** play list panel *****/
	@FXML
	private ListView<Audio> l_playlist;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		currentPageName = "Home";
		instance = this;
		
		if(Main.getInstance().getAudioPlayer().isPlaying()) {
			b_play.setImage(new Image(getClass().getResourceAsStream(Main.PAUSE_IMAGE_PATH)));
		}
		
		b_home.setStyle("-fx-background-color:  #7b2cbf ;");
		
		l_playlist.setCellFactory(cell -> {
			return new PlayListCell();
		});
		
		ObservableList<Audio> playList = Main.getInstance().getAudioPlayer().getObservablePlayList();
		l_playlist.setItems(playList);
		l_playlist.setFixedCellSize(48);
		
		AudioPlayer player = Main.getInstance().getAudioPlayer();
		
		player.setOnTimeUpdate((observable, oldTime, newTime) -> {
			
			double totalSecs = newTime.toSeconds();
			t_timeTick.setText(TimeUtils.parseSecondsToTime(totalSecs));
			
			double progress = totalSecs / player.getCurrentAudio().getDuration() * 100;
			progressBar.setValue(progress);
		});
		
		player.setOnMediaReady(() -> {
			t_endTimeTick.setText(TimeUtils.parseSecondsToTime(player.getCurrentAudio().getDuration()));
		});
	}
	 
	public void onPlayButtonClicked() {
		AudioPlayer player = Main.getInstance().getAudioPlayer();
		if(player.isPlaying()) {
			player.pause();
			b_play.setImage(new Image(getClass().getResourceAsStream(Main.PLAY_IMAGE_PATH)));
		}else {
			if(player.indicator == -1) {
				if(player.play()) {
					b_play.setImage(new Image(getClass().getResourceAsStream(Main.PAUSE_IMAGE_PATH)));
				}
			}else {
				player.resume();
				b_play.setImage(new Image(getClass().getResourceAsStream(Main.PAUSE_IMAGE_PATH)));
			}
		}
		System.out.println("Play audio!");
	}
}
