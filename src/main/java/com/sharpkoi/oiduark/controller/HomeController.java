package com.sharpkoi.oiduark.controller;

import java.net.URL;
import java.util.*;

import com.sharpkoi.oiduark.OiDuarkApp;
import com.sharpkoi.oiduark.audio.*;
import com.sharpkoi.oiduark.utils.Console;
import com.sharpkoi.oiduark.utils.ResourceLoader;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class HomeController extends AppController {
	
	private List<Image> defaultCoversList = null;
		
	@FXML AnchorPane root;

	/***** View ******/
	@FXML BorderPane coverContainer;
	@FXML ImageView coverView;
	@FXML VBox lyricsBox;
	@FXML Label l_loading;
	
	public HomeController() {
		pageName = "Home";
	}
	
	/* Define the feature of each component */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		loadDefaultCovers();
		
		playerControlPanel.disable();
		playerControlPanel.setOnPlayButtonClicked(e -> {
			AudioPlayer player = OiDuarkApp.getInstance().getAudioPlayer();
			if(!player.isPlaying() && player.indicator == -1) {
				l_loading.setOpacity(1);
			}
		});
		
		coverContainer.widthProperty().addListener((observable, oldWidth, newWidth) -> fitCover());
		coverContainer.heightProperty().addListener((observable, oldHeight, newHeight) -> fitCover());

		lyricsBox.setVisible(OiDuarkApp.getInstance().getUserConfig().isLyricsDisplay());
		
		initPlayerUI();
	}
	
	@Override
	public void onPageLoad() {
		layout.setLeft(super.nav);
		layout.setBottom(super.playerControlPanel);
		layout.setRight(super.playlistPanel);
		fitCover();
		lyricsBox.setVisible(OiDuarkApp.getInstance().getUserConfig().isLyricsDisplay());
	}
	
	public void initPlayerUI() {
		AudioPlayer player = OiDuarkApp.getInstance().getAudioPlayer();
		
		player.setOnTimeUpdate((o, oldTime, newTime) -> playerControlPanel.onTimeUpdate(newTime));
		
		player.setOnMediaReady(() -> {
			Image cover;
			String coverPath = player.getCurrentAudio().getCoverPath();
			if(coverPath.equals("Unknown")) {
				cover = randomCover();
			}else {
				cover = new Image("file:///" + coverPath);
			}
	        
			l_loading.setOpacity(0);
			coverView.setImage(cover);
			
			playerControlPanel.onMediaReady();
			playlistPanel.refreshList();
			Console.getLogger().info("media " + player.getCurrentAudio().getTitle() + " ready");
		});
		
		player.setOnPlayerStop(() -> {
			coverView.setImage(null);
			playerControlPanel.onPlayerStop();
			Console.getLogger().info("player stopped");
		});
	}
	
	public void fitCover() {
		coverView.setFitHeight(coverContainer.getHeight());
		coverView.setFitWidth(coverContainer.getWidth());
	}
	
	public void loadDefaultCovers() {
		defaultCoversList = OiDuarkApp.getInstance().getResourceLoader().loadDefaultCovers();
	}
	
	public Image randomCover() {
		Random ran = new Random();
		return defaultCoversList.get(ran.nextInt(defaultCoversList.size()));
	}
}
