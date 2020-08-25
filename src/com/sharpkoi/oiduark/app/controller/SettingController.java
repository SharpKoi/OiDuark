package com.sharpkoi.oiduark.app.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextField;
import com.sharpkoi.oiduark.app.Main;
import com.sharpkoi.oiduark.utils.ResourceLoader;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

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
				String path = dir.getAbsolutePath();
				f_dirPath.setText(path);
				Main.getInstance().getUserSetting().setUserMediaDir(path);
			}
		});
	}

	@Override
	protected void loadPageInfo() {
		currentPageName = "Setting";
		f_dirPath.setPromptText(Main.getInstance().getMediaDir());
		super.b_setting.setStyle("-fx-background-color: linear-gradient(from 50% 50% to 100% 100%, #075782, #11aacc) ;");
		super.b_setting.setOpacity(1);
		super.b_setting.setEffect(new Glow(0.4));
		
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
	}
}
