package com.sharpkoi.oiduark.app.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import com.sharpkoi.oiduark.app.AudioListCell;
import com.sharpkoi.oiduark.app.Main;
import com.sharpkoi.oiduark.app.PlayListCell;
import com.sharpkoi.oiduark.audio.Audio;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class AudioPageController extends GlobalController {
	
	public static AudioPageController instance;
	
	public static AudioPageController getInstance() {
		return instance;
	}
	
	private FilteredList<Audio> searchedItems;
	
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
				
		File mediaDir = new File(Main.getInstance().getMediaDir());
		if(!mediaDir.exists()) mediaDir.mkdir();
		
		if(l_audioList.getItems().isEmpty()) {
			if(mediaDir.isDirectory()) {
				File[] audioFiles = mediaDir.listFiles();
				loadAudioList(audioFiles);
			}
		}
		
		l_audioList.setCellFactory(cellList -> {
			return new AudioListCell();
		});
			
		l_playlist.setCellFactory(cellList -> {
			return new PlayListCell();
		});
		
		searchBar.textProperty().addListener((observable, oldText, newText) -> {
			Predicate<Audio> containsSearchText = audio -> audio.getTitle().contains(newText);
			searchedItems.setPredicate(containsSearchText);
		});
	}
	
	@Override
	protected void loadPageInfo() {
		currentPageName = "AudioPage";
		b_select.setStyle("-fx-background-color:  #7b2cbf ;");
		
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
		try {
			for(File f : audioFiles) {
				if(f.isDirectory()) {
					continue;
				}
				
				String filename = f.getName();
				Audio audio = Audio.loadFromJson(filename);
				if(audio != null) ol_audioList.add(audio);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		searchedItems = new FilteredList<>(ol_audioList);
		l_audioList.setItems(searchedItems);
	}
	
	public void refreshAudioList() {
		l_audioList.refresh();
	}
}
