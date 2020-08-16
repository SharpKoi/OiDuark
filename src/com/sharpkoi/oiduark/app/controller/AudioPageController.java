package com.sharpkoi.oiduark.app.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import com.sharpkoi.oiduark.app.AudioListCell;
import com.sharpkoi.oiduark.app.Main;
import com.sharpkoi.oiduark.app.PlayListCell;
import com.sharpkoi.oiduark.audio.Audio;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.effect.Glow;

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
		loadPageInfo();
		
		searchedItems = new FilteredList<>(Main.getInstance().getAllAudio());
		l_audioList.setItems(searchedItems);
		
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
		b_select.setStyle("-fx-background-color: linear-gradient(from 50% 50% to 100% 100%, #075782, #11aacc) ;\n"
				+ "-fx-background-radius: 4 ;");
		b_select.setEffect(new Glow(0.4));
		
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
	
	public void refreshAudioList() {
		l_audioList.refresh();
		Main.getInstance().getStage().getScene().getRoot().requestFocus();
	}
}
