package com.sharpkoi.oiduark.controller;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import com.sharpkoi.oiduark.OiDuarkApp;

import com.sharpkoi.oiduark.user.UserConfig;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;

public class SettingController extends AppController {
	@FXML JFXTextField f_dirPath_media;
	@FXML JFXTextField f_dirPath_appdata;

	@FXML Button b_browse_media;
	@FXML Button b_browse_appdata;

	@FXML JFXToggleButton toggle_mode;
	@FXML JFXToggleButton toggle_lyrics;

	@FXML JFXButton b_apply;
	@FXML JFXButton b_reset;
	
	public SettingController() {
		pageName = "Setting";
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		UserConfig config = OiDuarkApp.getInstance().getUserConfig();

		f_dirPath_media.setText(config.getMediaDirPath());
		f_dirPath_media.textProperty().addListener((o, oldVal, newVal) -> {
			if(!newVal.equals(oldVal)) {
				b_apply.setDisable(false);
				b_reset.setDisable(false);
			}
		});
		f_dirPath_appdata.setText(config.getUserdataDirPath());
		f_dirPath_appdata.textProperty().addListener((o, oldVal, newVal) -> {
			if(!newVal.equals(oldVal)) {
				b_apply.setDisable(false);
				b_reset.setDisable(false);
			}
		});
		
		b_browse_media.setOnAction(e -> {
			DirectoryChooser chooser = new DirectoryChooser();
			File dir = chooser.showDialog(OiDuarkApp.getInstance().getStage());
			
			if(dir != null) {
				String path = dir.getAbsolutePath();
				f_dirPath_media.setText(path);
				b_apply.setDisable(false);
				b_reset.setDisable(false);
			}
		});

		b_browse_appdata.setOnAction(e -> {
			DirectoryChooser chooser = new DirectoryChooser();
			File dir = chooser.showDialog(OiDuarkApp.getInstance().getStage());

			if(dir != null) {
				String path = dir.getAbsolutePath();
				f_dirPath_appdata.setText(path);
				b_apply.setDisable(false);
				b_reset.setDisable(false);
			}
		});

		toggle_mode.setSelected(config.isDarkMode());
		toggle_mode.selectedProperty().addListener((o, oldVal, newVal) -> {
			config.setDarkMode(newVal);
		});

		toggle_lyrics.setSelected(config.isLyricsDisplay());
		toggle_lyrics.selectedProperty().addListener((o, oldVal, newVal) -> {
			config.setLyricsDisplay(newVal);
		});

		b_apply.setOnAction(e -> {
			OiDuarkApp app = OiDuarkApp.getInstance();
			String appName = app.getProperties().getProperty("app.name").toLowerCase();

			String mediaPath = f_dirPath_media.getText();
			String userdataPath = f_dirPath_appdata.getText();

			// apply the new setting on user config
			config.setMediaDirPath(mediaPath);
			config.setUserdataDirPath(userdataPath);

			// Will not overwrite if there's data in the new userdata directory.
			// Instead, read the data in the new directory.
			if(!Paths.get(userdataPath, appName).toFile().exists()) {
				// write all the current data into the new userdata path
				app.saveAll();
			}

			// reload all media and userdata
			app.getAudioTagManager().loadTags();
			app.getAudioManager().getAllAudio().clear();
			app.getAudioManager().loadAudioList(Paths.get(mediaPath).toFile());

			app.getLogger().info("User setting changed and applied.");
			b_apply.setDisable(true);
			b_reset.setDisable(true);
		});

		b_reset.setOnAction(e -> {
			OiDuarkApp app = OiDuarkApp.getInstance();
			f_dirPath_media.setText(config.getMediaDirPath());
			f_dirPath_appdata.setText(config.getUserdataDirPath());

			app.getLogger().info("Reset user setting changes.");
			b_apply.setDisable(true);
			b_reset.setDisable(true);
		});
	}

	@Override
	public void onPageLoad() {
		layout.setLeft(nav);
	}
}
