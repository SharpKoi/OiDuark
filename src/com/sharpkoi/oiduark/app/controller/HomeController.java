package com.sharpkoi.oiduark.app.controller;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import com.sharpkoi.oiduark.app.*;
import com.sharpkoi.oiduark.audio.*;
import com.sharpkoi.oiduark.utils.Console;
import com.sharpkoi.oiduark.utils.ResourceLoader;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class HomeController extends AppController {
	
	private List<Image> defaultCoverList = null;
		
	@FXML
	private AnchorPane root;
	/***** preview ******/
	@FXML
	private BorderPane coverContainer;
	@FXML
	private ImageView coverView;
	@FXML
	private Label l_loading;
	
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
			AudioPlayer player = Main.getInstance().getAudioPlayer();
			if(!player.isPlaying() && player.indicator == -1) {
				l_loading.setOpacity(1);
			}
		});
		
		coverContainer.widthProperty().addListener((observable, oldWidth, newWidth) -> {
			fitCover();
		});
		coverContainer.heightProperty().addListener((observable, oldHeight, newHeight) -> {
			fitCover();
		});
		
		initPlayerUI();
	}
	
	@Override
	public void loadPageInfo() {
		layout.setTop(super.titleBar);
		layout.setLeft(super.nav);
		layout.setBottom(super.playerControlPanel);
		layout.setRight(super.playlistPanel);
		fitCover();
	}
	
	public void initPlayerUI() {
		AudioPlayer player = Main.getInstance().getAudioPlayer();
		
		player.setOnTimeUpdate((o, oldTime, newTime) -> {
			playerControlPanel.onTimeUpdate(newTime);
		});
		
		player.setOnMediaReady(() -> {
			Image cover = null; 
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
		defaultCoverList = Arrays.asList(ResourceLoader.loadDefaultCovers());
	}
	
	public Image randomCover() {
		Random ran = new Random();
		return defaultCoverList.get(ran.nextInt(defaultCoverList.size()));
	}
}
