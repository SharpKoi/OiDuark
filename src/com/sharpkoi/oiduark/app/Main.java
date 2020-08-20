package com.sharpkoi.oiduark.app;
	
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.sharpkoi.oiduark.app.controller.*;
import com.sharpkoi.oiduark.audio.Audio;
import com.sharpkoi.oiduark.audio.AudioPlayer;
import com.sharpkoi.oiduark.utils.ResourceLoader;
import com.sharpkoi.oiduark.utils.UserSetting;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

public class Main extends Application {
	
	private static Main instance;
	
	public static Main getInstance() {
		return instance;
	}
	
	private Stage stage;
	private AudioPlayer player;
	private ObservableList<Audio> ol_audioList;
	private HashMap<String, Parent> pageCache = new HashMap<>();	// store the page roots to activate without loading
	
	private UserSetting usrSetting;
	
	private ResourceBundle properties = null;
	private ResourceLoader resLoader = null;
	
	public Stage getStage() {
		return stage;
	}
	
	public AudioPlayer getAudioPlayer() {
		return player;
	}
	
	public ObservableList<Audio> getAllAudio() {
		return ol_audioList;
	}
	
	public HashMap<String, Parent> getPageCache() {
		return pageCache;
	}
	
	public UserSetting getUserSetting() {
		return usrSetting;
	}
	
	public ResourceBundle getProperties() {
		return properties;
	}
	
	public String getMediaDir() {
		return usrSetting.getUserMediaDir();
	}
	
	public String getMediaDataPath() {
		return properties.getString("media-data");
	}
	
	public ResourceLoader getResourceLoader() {
		return resLoader;
	}
	
	public void loadAudioList(File[] audioFiles) {
		ol_audioList = FXCollections.observableArrayList();
		try {
			for(File f : audioFiles) {
				if(f.isDirectory()) {
					continue;
				}
				
				String filename = f.getName();
				Audio audio = Audio.loadFromJson(filename);
				if(audio != null) ol_audioList.add(audio);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void start(Stage primaryStage) {
		instance = this;
		
		properties = ResourceBundle.getBundle("app");
		resLoader = new ResourceLoader("resources/");
		
		stage = primaryStage;
		player = new AudioPlayer();
		
		try {
			File userSettingFile = new File(properties.getString("user-setting"));
			if(userSettingFile.exists()) {
				usrSetting = UserSetting.load(userSettingFile);
			}else {
				usrSetting = new UserSetting(properties.getString("media-dir"), 100);
			}
			
			File mediaDir = new File(usrSetting.getUserMediaDir());
			if(!mediaDir.exists()) mediaDir.mkdir();
			
			if(mediaDir.isDirectory()) {
				File[] audioFiles = mediaDir.listFiles();
				loadAudioList(audioFiles);
			}
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
			Parent root = loader.load();
			
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			scene.setFill(Color.TRANSPARENT);
			
			primaryStage.getIcons().add(ResourceLoader.loadAppIcon());
			primaryStage.setTitle("OiDuark");
			primaryStage.initStyle(StageStyle.TRANSPARENT);
			primaryStage.setResizable(true);
			primaryStage.setScene(scene);
			((GlobalController) loader.getController()).initScene();
			
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
