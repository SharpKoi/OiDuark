package com.sharpkoi.oiduark.app.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.sharpkoi.oiduark.app.Main;
import com.sharpkoi.oiduark.app.listview.*;
import com.sharpkoi.oiduark.audio.Audio;
import com.sharpkoi.oiduark.audio.AudioTagManager;
import com.sharpkoi.oiduark.audio.AudioFilter;
import com.sharpkoi.oiduark.utils.ResourceLoader;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class AudioPageController extends GlobalController {
	
	public static AudioPageController instance;
	
	public static AudioPageController getInstance() {
		return instance;
	}
	
	private AudioFilter audioFilter;
	private FilteredList<Audio> searchedItems;
	
	@FXML
	private ListView<Audio> l_audioList;

	@FXML
	private TextField searchBar;
	
	@FXML
	private MenuButton tagFilter;
	
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
		
		audioFilter = new AudioFilter();
		searchBar.textProperty().addListener((observable, oldText, newText) -> {
			audioFilter.searchTitle(newText);
			audioFilter.search(searchedItems);
		});
		
		AudioTagManager tagManager = Main.getInstance().getAudioTagManager();
		CustomMenuItem[] tagItems = new CustomMenuItem[tagManager.getTagCount()];
		for(int i = 0; i < tagManager.getTagCount(); i++) {
			tagItems[i] = createNewTagFilterItem(i);
		}
		
		tagManager.addListener(e -> {
			tagFilter.getContextMenu().getItems().add(createNewTagFilterItem(tagManager.getTagCount() - 1));
		});
		
		tagFilter.setContextMenu(new ContextMenu(tagItems));
	}
	
	private CustomMenuItem createNewTagFilterItem(int tagID) {
		Label l = new Label(Main.getInstance().getAudioTagManager().getAudioTag(tagID).getName());
		l.setPrefSize(100, Region.USE_COMPUTED_SIZE);
		l.setAlignment(Pos.CENTER);
		CheckBox cb = new CheckBox();
		cb.setGraphic(l);
		cb.selectedProperty().addListener((o, oldVal, newVal) -> {
			if(newVal.booleanValue()) {
				System.out.printf("Tag(ID: %d) is selected.\n", tagID);
				audioFilter.selectTag(Integer.valueOf(tagID));
			}else {
				System.out.printf("Tag(ID: %d) is unselected.\n", tagID);
				audioFilter.unselectTag(Integer.valueOf(tagID));
			}
			audioFilter.search(searchedItems);
		});
		
		CustomMenuItem tagItem = new CustomMenuItem(cb);
		tagItem.setHideOnClick(false);
		
		return tagItem;
	}
	
	@Override
	protected void loadPageInfo() {
		currentPageName = "AudioPage";
		b_select.setStyle("-fx-background-color: linear-gradient(from 50% 50% to 100% 100%, #075782, #11aacc) ;");
		b_select.setOpacity(1);
		b_select.setEffect(new Glow(0.4));
		
		Stage stage = Main.getInstance().getStage();
		if(stage.isMaximized()) {
			ImageView icon = new ImageView(ResourceLoader.loadIcon("restore_down_64px.png"));
			icon.setFitWidth(16);
			icon.setFitHeight(16);
			b_maximize.setGraphic(icon);
		}else {
			ImageView icon = new ImageView(ResourceLoader.loadIcon("maximize_button_64px.png"));
			icon.setFitWidth(16);
			icon.setFitHeight(16);
			b_maximize.setGraphic(icon);
		}
		
		ObservableList<Audio> playList = Main.getInstance().getAudioPlayer().getObservablePlayList();
		l_playlist.setItems(playList);
		l_playlist.setFixedCellSize(48);
		l_playlist.refresh();
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
