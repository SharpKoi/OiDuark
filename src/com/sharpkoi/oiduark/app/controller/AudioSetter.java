package com.sharpkoi.oiduark.app.controller;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextField;
import com.sharpkoi.oiduark.app.Main;
import com.sharpkoi.oiduark.audio.Audio;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;

public class AudioSetter implements Initializable {
	
	private Runnable onSettingDone;
	
	@FXML
	private JFXTextField f_audioFile;
	@FXML
	private JFXTextField f_title;
	@FXML
	private JFXTextField f_author;
	@FXML
	private JFXTextField f_cover;
	@FXML
	private JFXTextField f_lyrics;
	
	@FXML
	private Button b_coverBrowse;
	@FXML
	private Button b_lyricsBrowse;
	
	@FXML
	private Button b_save;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		b_coverBrowse.setOnAction(e -> {
			FileChooser chooser = new FileChooser();
			File f = chooser.showOpenDialog(Main.getInstance().getStage());
			if(f != null) {
				f_cover.setText(f.getAbsolutePath());
			}
		});
		
		b_lyricsBrowse.setOnAction(e -> {
			FileChooser chooser = new FileChooser();
			File f = chooser.showOpenDialog(Main.getInstance().getStage());
			if(f != null) {
				f_lyrics.setText(f.getAbsolutePath());
			}
		});
		
		b_save.setOnAction(e -> {
			Platform.runLater(onSettingDone);
		});
	}
	
	public void initByAudio(Audio audio) {
		f_audioFile.setText(audio.getFilePath());
		f_title.setPromptText(audio.getTitle());
		f_author.setPromptText(audio.getAuthor());
		f_cover.setPromptText(audio.getCoverPath());
	}
	
	public void setOnSettingDone(Runnable task) {
		this.onSettingDone = task;
	}
	
	public HashMap<String, String> getResult() {
		HashMap<String, String> settingMap = new HashMap<>();
		if(!f_title.getText().equals("")) {
			settingMap.put("title", f_title.getText());
		}
		if(!f_author.getText().equals("")) {
			settingMap.put("author", f_author.getText());
		}
		if(!f_cover.getText().equals("")) {
			settingMap.put("cover", f_cover.getText());
		}
		if(!f_lyrics.getText().equals("")) {
			settingMap.put("lyrics", f_lyrics.getText());
		}
		
		return settingMap;
	}
}
