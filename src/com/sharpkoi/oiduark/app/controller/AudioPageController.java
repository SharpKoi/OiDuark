package com.sharpkoi.oiduark.app.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.sharpkoi.oiduark.app.AudioListCell;
import com.sharpkoi.oiduark.app.Main;
import com.sharpkoi.oiduark.app.PlayListCell;
import com.sharpkoi.oiduark.audio.Audio;
import com.sharpkoi.oiduark.utils.MetaData;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class AudioPageController extends GlobalController {
	
	public static AudioPageController instance;
	
	public static AudioPageController getInstance() {
		return instance;
	}
	
	@FXML
    private ListView<Audio> l_audioList;

    @FXML
    private TextField searchBar;
    
    @FXML
    private ListView<Audio> l_playlist;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		instance = this;
		super.initialize(location, resources);
		currentPageName = "AudioPage";
		b_select.setStyle("-fx-background-color:  #7b2cbf ;");
		
		//TODO: initialize the audio list
		File mediaDir = new File(MetaData.MEDIA_DIR);
		if(!mediaDir.exists()) mediaDir.mkdir();
		
		if(l_audioList.getItems().isEmpty()) {
			if(mediaDir.isDirectory()) {
				File[] audioFiles = mediaDir.listFiles();
				loadAudioList(audioFiles);
			}
		}
		
		l_audioList.setCellFactory(cell -> {
			return new AudioListCell();
		});
			
		l_playlist.setCellFactory(cell -> {
			return new PlayListCell();
		});
		
		ObservableList<Audio> playList = Main.getInstance().getAudioPlayer().getObservablePlayList();
		l_playlist.setItems(playList);
		l_playlist.setFixedCellSize(48);
	}
	
	public void removeAudioFromPlaylist(Audio audio) {
		l_playlist.getItems().remove(audio);
	}
	
	public void addAudioToPlaylist(Audio audio) {
		l_playlist.getItems().add(audio);
	}
	
	public void loadAudioList(File[] audioFiles) {
		ObservableList<Audio> ol_audioList = FXCollections.observableArrayList();
		for(File f : audioFiles) {
			String filename = f.getName();
			Audio audio = Audio.loadFromJson(MetaData.AUDIO_DATA_PATH, filename);
			if(audio != null) ol_audioList.add(audio);
		}
		
		l_audioList.setItems(ol_audioList);
	}
}
