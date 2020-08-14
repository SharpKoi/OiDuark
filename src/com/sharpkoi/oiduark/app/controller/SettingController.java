package com.sharpkoi.oiduark.app.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextField;
import com.sharpkoi.oiduark.app.Main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;

public class SettingController extends GlobalController {
	
	@FXML
	private Button b_browse;
	@FXML
	private JFXTextField f_dirPath;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
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
		b_setting.setStyle("-fx-background-color:  #7b2cbf ;");
	}
}
