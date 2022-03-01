package com.sharpkoi.oiduark.app.controller;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import com.sharpkoi.oiduark.app.Main;
import com.sharpkoi.oiduark.app.listview.*;
import com.sharpkoi.oiduark.audio.Audio;
import com.sharpkoi.oiduark.audio.AudioTagManager;
import com.sharpkoi.oiduark.utils.Console;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

import com.sharpkoi.oiduark.audio.AudioFilter;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

public class AudioPageController extends AppController {
	
	public static AudioPageController instance;
	
	public static AudioPageController getInstance() {
		return instance;
	}
	
	private AudioFilter audioFilter;
	private FilteredList<Audio> itemsForSearch;
	
	@FXML
	private ListView<Audio> l_audioList;

	@FXML
	private TextField searchBar;
	
	@FXML
	private MenuButton tagFilter;
	
	@FXML
	private Button b_starFilter;
	@FXML
	private Button b_clear;
	@FXML
	private Button b_reload;
	@FXML 
	private Button b_addAll;
	
	public AudioPageController() {
		pageName = "AudioPage";
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		instance = this;
		super.initialize(location, resources);
		
		ObservableList<Audio> allAudio = Main.getInstance().getAudioManager().getAllAudio();
		itemsForSearch = new FilteredList<>(allAudio);
		l_audioList.setItems(itemsForSearch);
		l_audioList.setCellFactory(cellList -> {
			return new AudioListCell();
		});
			
		audioFilter = new AudioFilter();
		searchBar.textProperty().addListener((observable, oldText, newText) -> {
			audioFilter.searchTitle(newText);
			audioFilter.search(itemsForSearch);
		});
		
		AudioTagManager tagManager = Main.getInstance().getAudioTagManager();
		CustomMenuItem[] tagItems = new CustomMenuItem[tagManager.getTagCount()];
		for(int i = 0; i < tagManager.getTagCount(); i++) {
			tagItems[i] = createNewTagFilterItem(i);
		}
		
		tagManager.addListener(e -> {
			tagFilter.getContextMenu().getItems().add(createNewTagFilterItem(tagManager.getTagCount() - 1));
		});
		
		tagFilter.getItems().addAll(tagItems);
		
		Tooltip starTip = new Tooltip("我的最愛");
		starTip.setFont(Font.font(10));
		starTip.setStyle("-fx-background-color: rgba(255, 0, 85, 0.8);");
		b_starFilter.setTooltip(starTip);
		b_starFilter.setOnAction(e -> {
			audioFilter.setOnlyStared(!audioFilter.onlyStared());
			((FontAwesomeIconView) b_starFilter.getGraphic()).setIcon(
					audioFilter.onlyStared()? FontAwesomeIcon.STAR : FontAwesomeIcon.STAR_ALT);
			audioFilter.search(itemsForSearch);
		});
		
		Tooltip clearTip = new Tooltip("清除搜尋結果");
		clearTip.setFont(Font.font(10));
		clearTip.setStyle("-fx-background-color: rgba(255, 0, 85, 0.8);");
		b_clear.setTooltip(clearTip);
		b_clear.setOnAction(e -> {
			searchBar.setText("");
			Arrays.stream(tagItems).forEach(tagItem -> {
				((CheckBox) tagItem.getContent()).setSelected(false);
			});
			((FontAwesomeIconView) b_starFilter.getGraphic()).setIcon(FontAwesomeIcon.STAR_ALT);
			audioFilter.clear();
			audioFilter.search(itemsForSearch);
		});
		
		Tooltip reloadTip = new Tooltip("重新載入");
		reloadTip.setFont(Font.font(10));
		reloadTip.setStyle("-fx-background-color: rgba(255, 0, 85, 0.8);");
		b_reload.setTooltip(reloadTip);
		b_reload.setOnAction(e -> {
			File mediaDir = new File(Main.getInstance().getUserSetting().getUserMediaDir());
			if(!mediaDir.exists()) mediaDir.mkdir();
			
			if(mediaDir.isDirectory()) {
				allAudio.clear();
				Main.getInstance().getAudioManager().loadAudioList(mediaDir);
				Console.getLogger().info("Reload all audio.");
			}
		});
		
		Tooltip addAllTip = new Tooltip("一鍵加入");
		addAllTip.setFont(Font.font(10));
		addAllTip.setStyle("-fx-background-color: rgba(255, 0, 85, 0.8);");
		b_addAll.setTooltip(addAllTip);
		b_addAll.setOnAction(e -> {
			List<Audio> playlist = Main.getInstance().getAudioPlayer().getPlayList();
			for(Audio audio : itemsForSearch) {
				if(!playlist.contains(audio)) {
					playlist.add(audio);
				}
			}
			l_audioList.refresh();
		});
	}
	
	private CustomMenuItem createNewTagFilterItem(int tagID) {
		Label l = new Label(Main.getInstance().getAudioTagManager().getAudioTag(tagID).getName());
		l.setPrefSize(100, Region.USE_COMPUTED_SIZE);
		l.setAlignment(Pos.CENTER);
		CheckBox cb = new CheckBox();
		cb.setGraphic(l);
		cb.selectedProperty().addListener((o, oldVal, newVal) -> {
			if(newVal.booleanValue()) {
				audioFilter.selectTag(Integer.valueOf(tagID));
			}else {
				audioFilter.unselectTag(Integer.valueOf(tagID));
			}
			audioFilter.search(itemsForSearch);
		});
		
		CustomMenuItem tagItem = new CustomMenuItem(cb);
		tagItem.setHideOnClick(false);
		
		return tagItem;
	}
	
	@Override
	public void loadPageInfo() {
		layout.setTop(titleBar);
		layout.setLeft(nav);
		layout.setRight(playlistPanel);
		layout.setBottom(playerControlPanel);
	}
	
	public void refreshAudioList() {
		l_audioList.refresh();
		Main.getInstance().getStage().getScene().getRoot().requestFocus();
	}
}
