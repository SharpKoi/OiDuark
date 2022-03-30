package com.sharpkoi.oiduark.controller;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import com.sharpkoi.oiduark.OiDuarkApp;
import com.sharpkoi.oiduark.listview.*;
import com.sharpkoi.oiduark.audio.Audio;
import com.sharpkoi.oiduark.manager.AudioTagManager;
import com.sharpkoi.oiduark.utils.Console;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

import com.sharpkoi.oiduark.audio.AudioFilter;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class AudioPageController extends AppController {
	
	private static AudioPageController instance;
	
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
		
		ObservableList<Audio> allAudio = OiDuarkApp.getInstance().getAudioManager().getAllAudio();
		itemsForSearch = new FilteredList<>(allAudio);
		l_audioList.setItems(itemsForSearch);
		l_audioList.setCellFactory(cellList -> new AudioListCell());
			
		audioFilter = new AudioFilter();
		searchBar.textProperty().addListener((observable, oldText, newText) -> {
			audioFilter.searchTitle(newText);
			audioFilter.search(itemsForSearch);
		});
		
		AudioTagManager tagManager = OiDuarkApp.getInstance().getAudioTagManager();
		CustomMenuItem[] tagItems = new CustomMenuItem[tagManager.getTagCount()];
		for(int i = 0; i < tagManager.getTagCount(); i++) {
			tagItems[i] = generateTagFilterItem(i);
		}
		
		tagManager.addListener(e -> {
			ObservableList<MenuItem> tagFilterItems = tagFilter.getItems();
			tagFilterItems.add(tagFilterItems.size()-1, generateTagFilterItem(tagManager.getTagCount() - 1));
		});

		tagFilter.getItems().addAll(tagItems);
		tagFilter.getItems().add(generateTagFilterItem(-1)); // preserve "No tags" item
		
		Tooltip starTip = new Tooltip("我的最愛");
		starTip.setFont(Font.font(10));
		starTip.setStyle("-fx-background-color: rgba(255, 0, 85, 0.8);");
		starTip.setShowDelay(Duration.seconds(.4));
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
		clearTip.setShowDelay(Duration.seconds(.4));
		b_clear.setTooltip(clearTip);
		b_clear.setOnAction(e -> {
			searchBar.setText("");
			Arrays.stream(tagItems).forEach(tagItem -> ((CheckBox) tagItem.getContent()).setSelected(false));
			((FontAwesomeIconView) b_starFilter.getGraphic()).setIcon(FontAwesomeIcon.STAR_ALT);
			audioFilter.clear();
			audioFilter.search(itemsForSearch);
		});
		
		Tooltip reloadTip = new Tooltip("重新載入");
		reloadTip.setFont(Font.font(10));
		reloadTip.setStyle("-fx-background-color: rgba(255, 0, 85, 0.8);");
		reloadTip.setShowDelay(Duration.seconds(.4));
		b_reload.setTooltip(reloadTip);
		b_reload.setOnAction(e -> {
			// save the current changes before reloading
			OiDuarkApp.getInstance().getAudioTagManager().saveAllTags();
			OiDuarkApp.getInstance().getAudioManager().saveAllAudioData();

			File mediaDir = new File(OiDuarkApp.getInstance().getUserConfig().getMediaDirPath());
			if(!mediaDir.exists()) mediaDir.mkdir();
			
			if(mediaDir.isDirectory()) {
				allAudio.clear();
				OiDuarkApp.getInstance().getAudioManager().loadAudioList(mediaDir);
				Console.getLogger().info("Reload all audio.");
			}
		});
		
		Tooltip addAllTip = new Tooltip("一鍵加入");
		addAllTip.setFont(Font.font(10));
		addAllTip.setStyle("-fx-background-color: rgba(255, 0, 85, 0.8);");
		addAllTip.setShowDelay(Duration.seconds(.4));
		b_addAll.setTooltip(addAllTip);
		b_addAll.setOnAction(e -> {
			List<Audio> playlist = OiDuarkApp.getInstance().getAudioPlayer().getPlayList();
			for(Audio audio : itemsForSearch) {
				if(!playlist.contains(audio)) {
					playlist.add(audio);
				}
			}
			l_audioList.refresh();
		});
	}

	/**
	 * Generate a menu item with tag name and checkbox on tag filter.
	 * @param tagID the id of the tag to generate. If the value is negative, it generates a menu item for `No tags`.
	 * @return the menu item with tag name and checkbox.
	 */
	private CustomMenuItem generateTagFilterItem(int tagID) {
		Label l = new Label();
		if(tagID >= 0)
			l.setText(OiDuarkApp.getInstance().getAudioTagManager().getAudioTag(tagID).getName());
		else
			l.setText("No tags");
		l.setPrefSize(100, Region.USE_COMPUTED_SIZE);
		l.setAlignment(Pos.CENTER);

		CheckBox cb = new CheckBox();
		cb.setGraphic(l);
		cb.selectedProperty().addListener((o, oldVal, newVal) -> {
			// TODO: exclusive checkbox behaviour
			ObservableList<MenuItem> tagItems = tagFilter.getItems();
			if(newVal) {
				if(tagID == -1) {
					// If "No tags" item is selected, unselect other items.
					for (int i = 0; i < tagItems.size()-1; i++) {
						CheckBox ocb = (CheckBox) ((CustomMenuItem) tagItems.get(i)).getContent();
						ocb.setSelected(false);
						audioFilter.unselectTag(i);
					}
				}else {
					// unselect the "No tags" item
					CheckBox ocb = (CheckBox) ((CustomMenuItem) tagItems.get(tagItems.size()-1)).getContent();
					ocb.setSelected(false);
					audioFilter.unselectTag(-1);
				}
				audioFilter.selectTag(tagID);
			}else {
				audioFilter.unselectTag(tagID);
			}
			audioFilter.search(itemsForSearch);
		});
		
		CustomMenuItem tagItem = new CustomMenuItem(cb);
		tagItem.setHideOnClick(false);
		
		return tagItem;
	}
	
	@Override
	public void onPageLoad() {
		layout.setLeft(nav);
		layout.setRight(playlistPanel);
		layout.setBottom(playerControlPanel);
	}
	
	public void refreshAudioList() {
		l_audioList.refresh();
		OiDuarkApp.getInstance().getStage().getScene().getRoot().requestFocus();
	}
}
