package com.sharpkoi.oiduark.app.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextField;
import com.sharpkoi.oiduark.app.Main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.Glow;
import javafx.stage.DirectoryChooser;

public class SettingController extends GlobalController {
	
	@FXML
	private Button b_browse;
	@FXML
	private JFXTextField f_dirPath;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		loadPageInfo();
		
		b_browse.setOnAction(e -> {
			DirectoryChooser chooser = new DirectoryChooser();
			File dir = chooser.showDialog(Main.getInstance().getStage());
			
			if(dir != null) {
				f_dirPath.setText(dir.getAbsolutePath());
			}
		});
	}

	@Override
	protected void loadPageInfo() {
		currentPageName = "Setting";
		b_setting.setStyle("-fx-background-color: linear-gradient(from 50% 50% to 100% 100%, #075782, #11aacc) ;\n"
				+ "-fx-background-radius: 4 ;");
		b_setting.setEffect(new Glow(0.4));
	}
}
