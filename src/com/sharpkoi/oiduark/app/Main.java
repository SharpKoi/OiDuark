package com.sharpkoi.oiduark.app;
	
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.sharpkoi.oiduark.app.controller.*;
import com.sharpkoi.oiduark.audio.Audio;
import com.sharpkoi.oiduark.audio.AudioPlayer;
import com.sharpkoi.oiduark.utils.ResourceLoader;

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
	private HashMap<String, Parent> sceneCache = new HashMap<>();
	
	private ResourceBundle config = null;
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
	
	public HashMap<String, Parent> getSceneCache() {
		return sceneCache;
	}
	
	public ResourceBundle getConfig() {
		return config;
	}
	
	public String getMediaDir() {
		return config.getString("media-dir");
	}
	
	public String getMediaDataPath() {
		return config.getString("media-data");
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
		
		config = ResourceBundle.getBundle("app");
		resLoader = new ResourceLoader("resources/");
		
		stage = primaryStage;
		player = new AudioPlayer();
		
		File mediaDir = new File(getMediaDir());
		if(!mediaDir.exists()) mediaDir.mkdir();
		
		if(mediaDir.isDirectory()) {
			File[] audioFiles = mediaDir.listFiles();
			loadAudioList(audioFiles);
		}
		
		try {
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
