package com.sharpkoi.oiduark.app.controller;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.sharpkoi.oiduark.app.Main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;

public class SettingController extends AppController {
	@FXML
	private JFXTextField f_dirPath_media;
	@FXML
	private JFXTextField f_dirPath_userdata;

	@FXML
	private Button b_browse_media;
	@FXML
	private Button b_browse_userdata;

	@FXML
	private JFXButton b_apply;
	@FXML
	private JFXButton b_reset;
	
	public SettingController() {
		pageName = "Setting";
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);

		f_dirPath_media.setText(Main.getInstance().getUserConfig().getMediaDirPath());
		f_dirPath_media.textProperty().addListener((o, oldVal, newVal) -> {
			if(!newVal.equals(oldVal)) {
				b_apply.setDisable(false);
				b_reset.setDisable(false);
			}
		});
		f_dirPath_userdata.setText(Main.getInstance().getUserConfig().getUserdataDirPath());
		f_dirPath_userdata.textProperty().addListener((o, oldVal, newVal) -> {
			if(!newVal.equals(oldVal)) {
				b_apply.setDisable(false);
				b_reset.setDisable(false);
			}
		});
		
		b_browse_media.setOnAction(e -> {
			DirectoryChooser chooser = new DirectoryChooser();
			File dir = chooser.showDialog(Main.getInstance().getStage());
			
			if(dir != null) {
				String path = dir.getAbsolutePath();
				f_dirPath_media.setText(path);
				b_apply.setDisable(false);
				b_reset.setDisable(false);
			}
		});

		b_browse_userdata.setOnAction(e -> {
			DirectoryChooser chooser = new DirectoryChooser();
			File dir = chooser.showDialog(Main.getInstance().getStage());

			if(dir != null) {
				String path = dir.getAbsolutePath();
				f_dirPath_userdata.setText(path);
				b_apply.setDisable(false);
				b_reset.setDisable(false);
			}
		});

		b_apply.setOnAction(e -> {
			String mediaPath = f_dirPath_media.getText();
			String userdataPath = f_dirPath_userdata.getText();
			Main main = Main.getInstance();
			main.getUserConfig().setMediaDirPath(mediaPath);
			main.getUserConfig().setUserdataDirPath(userdataPath);
			main.getAudioManager().getAllAudio().clear();
			main.getAudioManager().loadAudioList(Paths.get(mediaPath).toFile());
			b_apply.setDisable(true);
			b_reset.setDisable(true);
		});

		b_reset.setOnAction(e -> {
			Main main = Main.getInstance();
			f_dirPath_media.setText(main.getUserConfig().getMediaDirPath());
			f_dirPath_userdata.setText(main.getUserConfig().getUserdataDirPath());

			b_apply.setDisable(true);
			b_reset.setDisable(true);
		});
	}

	@Override
	public void onPageLoad() {
		layout.setTop(titleBar);
		layout.setLeft(nav);
	}
}
