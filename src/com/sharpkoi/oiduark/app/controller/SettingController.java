package com.sharpkoi.oiduark.app.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextField;
import com.sharpkoi.oiduark.app.Main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;

public class SettingController extends AppController {
	@FXML
	private Button b_browse;
	@FXML
	private JFXTextField f_dirPath;
	
	public SettingController() {
		pageName = "Setting";
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		f_dirPath.setPromptText(Main.getInstance().getMediaDir());
		
		b_browse.setOnAction(e -> {
			DirectoryChooser chooser = new DirectoryChooser();
			File dir = chooser.showDialog(Main.getInstance().getStage());
			
			if(dir != null) {
				String path = dir.getAbsolutePath();
				f_dirPath.setText(path);
				Main main = Main.getInstance();
				main.getUserSetting().setUserMediaDirPath(path);
				main.getAudioManager().getAllAudio().clear();
				main.getAudioManager().loadAudioList(dir);
			}
		});
	}

	@Override
	public void loadPageInfo() {
		layout.setTop(titleBar);
		layout.setLeft(nav);
	}
}
