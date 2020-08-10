package com.sharpkoi.oiduark.app.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXListView;
import com.sharpkoi.oiduark.app.*;
import com.sharpkoi.oiduark.audio.Audio;
import com.sharpkoi.oiduark.audio.AudioPlayer;
import com.sharpkoi.oiduark.utils.MetaData;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class HomeController extends GlobalController {
		
	@FXML
	private AnchorPane root;
	
	/***** player controller *****/
    @FXML
    private ImageView b_play;
	@FXML
    private ImageView b_next;
	@FXML
    private ImageView b_last;
	
	/***** play list panel *****/
	@FXML
	private JFXListView<Audio> l_playlist;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		currentPageName = "Home";
		b_home.setStyle("-fx-background-color:  #7b2cbf ;");
		
		l_playlist.setCellFactory(cell -> {
			return new PlayListCell();
		});
		l_playlist.setFocusTraversable(false);
		
		playList = FXCollections.observableList(Main.getInstance().getAudioPlayer().getPlayList());
		l_playlist.setItems(playList);
		l_playlist.setFixedCellSize(64);
	}
	
	public void onPlayButtonClicked() {
		AudioPlayer player = Main.getInstance().getAudioPlayer();
		if(player.isPlaying()) {
			player.pause();
			b_play.setImage(new Image(getClass().getResourceAsStream(Main.PLAY_IMAGE_PATH)));
		}else {
			player.play();
			b_play.setImage(new Image(getClass().getResourceAsStream(Main.PAUSE_IMAGE_PATH)));
		}
		System.out.println("Play audio!");
	}
}
