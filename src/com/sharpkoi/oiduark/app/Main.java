package com.sharpkoi.oiduark.app;
	
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sharpkoi.oiduark.app.controller.*;
import com.sharpkoi.oiduark.audio.Audio;
import com.sharpkoi.oiduark.audio.AudioPlayer;
import com.sharpkoi.oiduark.utils.OiDuarkUtils;
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
	private HashMap<String, Parent> pageCache = new HashMap<>();	// store the page roots to activate without loading
	
	private ObservableList<Audio> ol_audioList = FXCollections.observableArrayList();
	private ArrayList<String> tagList = new ArrayList<>();
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
	
	public List<String> getTagList() {
		return tagList;
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
	
	public void loadAudioList(File mediaDir) {
		if(mediaDir.isDirectory()) {
			File[] audioFiles = mediaDir.listFiles();
			try {
				for(File f : audioFiles) {
					if(f.isDirectory()) {
						loadAudioList(f);
					}else {
						String filepath = f.getPath();
						String ex = FilenameUtils.getExtension(filepath);
						if(ex.equals("mp3") || ex.equals("m4a") || ex.equals("wav") || 
								ex.equals("ogg") || ex.equals("webm")) {
							Audio audio = Audio.loadFor(filepath);
							if(audio != null) ol_audioList.add(audio);
						}
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void loadTags() {
		try {
			File tagConfigFile = new File(properties.getString("tag-id-data"));
			if(!tagConfigFile.exists()) {
				OiDuarkUtils.saveJson(tagConfigFile, new JSONArray());
			}
			JSONParser parser = new JSONParser();
			InputStream in = new FileInputStream(tagConfigFile);
			InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
			JSONArray tagArray = (JSONArray) parser.parse(reader);
			tagList = new ArrayList<String>(tagArray);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
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
			
			loadTags();
			
			File mediaDir = new File(usrSetting.getUserMediaDir());
			if(!mediaDir.exists()) mediaDir.mkdir();
			
			if(mediaDir.isDirectory()) {
				loadAudioList(mediaDir);
			}
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Home.fxml"));
			Parent root = loader.load();
			
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("style/application.css").toExternalForm());
			scene.setFill(Color.TRANSPARENT);
			
			primaryStage.getIcons().add(ResourceLoader.loadAppIcon());
			primaryStage.setTitle("OiDuark");
			primaryStage.initStyle(StageStyle.TRANSPARENT);
			primaryStage.setResizable(true);
			primaryStage.setScene(scene);
			
			primaryStage.setMinWidth(900);
			primaryStage.setMinHeight(600);
			
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
