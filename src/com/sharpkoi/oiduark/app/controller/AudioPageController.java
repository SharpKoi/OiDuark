package com.sharpkoi.oiduark.app.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXListView;
import com.sharpkoi.oiduark.app.Main;
import com.sharpkoi.oiduark.app.PlayListCell;
import com.sharpkoi.oiduark.audio.Audio;
import com.sharpkoi.oiduark.utils.MetaData;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AudioPageController extends GlobalController {
	
	@FXML
    private JFXListView<Audio> audioList;

    @FXML
    private TextField searchBar;
    
    @FXML
    private JFXListView<Audio> l_playlist;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		currentPageName = "AudioPage";
		b_select.setStyle("-fx-background-color:  #7b2cbf ;");
		
		//TODO: initialize the audio list
		
		File mediaDir = new File(MetaData.MEDIA_DIR);
		if(!mediaDir.exists()) mediaDir.mkdir();
		
		if(mediaDir.isDirectory()) {
			File[] audioFiles = mediaDir.listFiles();
			loadAudioList(audioFiles);
		}
		
		l_playlist.setCellFactory(cell -> {
			return new PlayListCell();
		});
		l_playlist.setFocusTraversable(false);
		
		playList = FXCollections.observableList(Main.getInstance().getAudioPlayer().getPlayList());
		l_playlist.setItems(playList);
		l_playlist.setFixedCellSize(64);
	}
	
	public void loadAudioList(File[] audioFiles) {
		ObservableList<Audio> ol_audioList = FXCollections.emptyObservableList();
		for(File f : audioFiles) {
			Audio audio = Audio.loadFromJson(MetaData.AUDIO_DATA_PATH, f.getName());
			ol_audioList.add(audio);
		}
		
		audioList.setItems(ol_audioList);
	}

}
