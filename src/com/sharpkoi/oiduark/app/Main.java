package com.sharpkoi.oiduark.app;
	
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import com.sharpkoi.oiduark.utils.*;
import org.apache.commons.io.FilenameUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sharpkoi.oiduark.app.component.ComponentManager;
import com.sharpkoi.oiduark.app.component.Navigation;
import com.sharpkoi.oiduark.app.component.NavigationButton;
import com.sharpkoi.oiduark.app.component.PlayerControlPanel;
import com.sharpkoi.oiduark.app.component.PlaylistPanel;
import com.sharpkoi.oiduark.app.component.TitleBar;
import com.sharpkoi.oiduark.app.controller.AppController;
import com.sharpkoi.oiduark.app.controller.ControllerManager;
import com.sharpkoi.oiduark.audio.Audio;
import com.sharpkoi.oiduark.audio.AudioPlayer;
import com.sharpkoi.oiduark.audio.AudioTagManager;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Main extends Application {
	
	private static Main instance;
	
	public static Main getInstance() {
		return instance;
	}
	
	private Stage stage;
	private Logger logger;
	private AudioPlayer player;
	
	private ObservableList<Audio> ol_audioList = FXCollections.observableArrayList();
	private ControllerManager controllerManager;
	private ComponentManager componentManager;
	private AudioTagManager tagManager;
	private UserSetting usrSetting;
	
	private ResourceBundle properties = null;
	private ResourceLoader resLoader = null;
	
	public Stage getStage() {
		return stage;
	}
	
	public Logger getLogger() {
		return logger;
	}
	
	public AudioPlayer getAudioPlayer() {
		return player;
	}
	
	public ObservableList<Audio> getAllAudio() {
		return ol_audioList;
	}
	
	public ControllerManager getControllerManager() {
		return controllerManager;
	}
	
	public ComponentManager getComponentManager() {
		return componentManager;
	}
	
	public AudioTagManager getAudioTagManager() {
		return tagManager;
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
				assert audioFiles != null;
				for(File f : audioFiles) {
					if(f.isDirectory()) {
						loadAudioList(f);
					}else {
						String filepath = f.getPath();
						String ex = FilenameUtils.getExtension(filepath);
						if(ex.equals("mp3") || ex.equals("m4a") || ex.equals("wav") || 
								ex.equals("ogg") || ex.equals("webm")) {
							Audio audio = Audio.loadFor(f);
							if(audio != null) {
								if(!ol_audioList.contains(audio)) {
									ol_audioList.add(audio);
								}
							}
						}
					}
				}
				FXCollections.sort(ol_audioList, Comparator.comparing(Audio::lastModified));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void saveAllAudioData() {
		File store = new File(getMediaDataPath());
		HashMap<String, JsonObject> audioDataMap = new HashMap<>();
		for(Audio a : ol_audioList) {
			audioDataMap.put(a.getFilePath(), OiDuarkUtils.parseAudioToJson(a));
		}
		
		Gson gson = new Gson();
		OiDuarkUtils.saveJson(store, gson.toJsonTree(audioDataMap));
	}
	
	public void activateScene(String pageName) {
		if(controllerManager.containsKey(pageName)) {
			AppController controller = controllerManager.getController(pageName);
			stage.getScene().setRoot(controller.getRoot());
			controller.loadPageInfo();
		}else {
			try {
				String fxml = pageName + ".fxml";
				FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/"+fxml));
				Parent root = loader.load();
				stage.getScene().setRoot(root);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// After the audio player plays some audio, it costs plenty of memory.
		// I'm confused by what being accumulated.
		System.gc();
	}
	
	@Override
	public void start(Stage primaryStage) {
		Console.INFO("Launch OiDuark...");
		instance = this;
		
		properties = ResourceBundle.getBundle("app");
		resLoader = new ResourceLoader("resources/");
		
		stage = primaryStage;
		try {
			File logDir = new File(properties.getString("log-dir"));
			if(!logDir.exists()) logDir.mkdirs();
			
			FileHandler logHandler = 
					new FileHandler(logDir.getAbsolutePath() + "/" +
							new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) + ".log");
			logger = Logger.getGlobal();
			logger.addHandler(logHandler);
			Console.setLogger(logger);
			Console.getLogger().info("main logger initialized");
			
			player = new AudioPlayer();
			
			initComponents();
			
			File userSettingFile = new File(properties.getString("user-setting"));
			if(userSettingFile.exists()) {
				usrSetting = UserSetting.load(userSettingFile);
			}else {
				usrSetting = new UserSetting(properties.getString("media-dir"), 100);
			}
			
			tagManager = new AudioTagManager();
			
			File mediaDir = new File(usrSetting.getUserMediaDir());
			if(mediaDir.isDirectory() && !mediaDir.exists()) mediaDir.mkdir();

			if(mediaDir.isDirectory()) {
				loadAudioList(mediaDir);
				logger.info("All audio loaded done!");
			}
			
			controllerManager = new ControllerManager();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Home.fxml"));
			Parent root = loader.load();
			
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("style/application.css").toExternalForm());
			scene.setFill(Color.TRANSPARENT);
			
			primaryStage.setScene(scene);
			
			primaryStage.getIcons().add(ResourceLoader.loadAppIcon());
			primaryStage.setTitle("OiDuark");
			primaryStage.initStyle(StageStyle.TRANSPARENT);
			
			primaryStage.setOnCloseRequest(e -> {
				Console.getLogger().info("Saving all the data...");
				tagManager.saveAllTags();
				saveAllAudioData();
				usrSetting.save();
				Console.getLogger().info("Closing APP...");
			});
			
			ResizeHelper.addResizeListener(stage);
			primaryStage.setMinWidth(900);
			primaryStage.setMinHeight(600);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * Initialize all the fixed components, including properties and event handlers.
	 * 
	 * @author Sharpkoi
	 * @since 2020.09.16
	 */
	private void initComponents() {
		TitleBar titleBar = new TitleBar(
				stage, ResourceLoader.loadAppIcon(), 
				properties.getString("app-name") + " " + properties.getString("app-version"));
		
		titleBar.setOnMinimizeButtonClicked(e -> {
			SimpleAnimation.windowIconify(stage.getScene().getRoot(), onFinish -> {
				stage.setIconified(true);
				stage.getScene().getRoot().setOpacity(1);
				stage.getScene().getRoot().setScaleX(1);
				stage.getScene().getRoot().setScaleY(1);
			});
		});
		titleBar.setOnResizeButtonClicked(e -> {
			if(stage.isMaximized()) {
				Console.getLogger().info("Restore stage size");
				stage.setMaximized(false);
				ImageView icon = new ImageView(ResourceLoader.loadIcon("maximize_button_64px.png"));
				icon.setFitWidth(16);
				icon.setFitHeight(16);
				titleBar.resizeButton().setGraphic(icon);
			}else {
				Console.getLogger().info("Maximize stage size");
				stage.setMaximized(true);
				ImageView icon = new ImageView(ResourceLoader.loadIcon("restore_down_64px.png"));
				icon.setFitWidth(16);
				icon.setFitHeight(16);
				titleBar.resizeButton().setGraphic(icon);
			}
		});
		titleBar.setOnCloseButtonClicked(e -> {
			Console.getLogger().info("Saving all the data...");
			tagManager.saveAllTags();
			saveAllAudioData();
			usrSetting.save();
			
			Console.getLogger().info("Closing APP...");
			stage.close();
			Platform.runLater(() -> System.exit(1));
		});
		
		Navigation nav = new Navigation(
				new NavigationButton(FontAwesomeIcon.HOME, "Home", 164, 48),
				new NavigationButton(FontAwesomeIcon.MUSIC, "My Audio", 164, 48),
				new NavigationButton(FontAwesomeIcon.COG, "Setting", 164, 48),
				new NavigationButton(FontAwesomeIcon.INFO_CIRCLE, "About", 164, 48)
		);
		
		nav.setOnSelect(0, e -> {
			Console.getLogger().info("activate scene Home");
			activateScene("Home");
		});
		
		nav.setOnSelect(1, e -> {
			Console.getLogger().info("activate scene AudioPage");
			activateScene("AudioPage");
		});
		
		nav.setOnSelect(2, e -> {
			Console.getLogger().info("activate scene Setting");
			activateScene("Setting");
		});
		
		nav.setOnSelect(3, e -> {
			Console.getLogger().info("activate scene About");
			activateScene("About");
		});
		
		PlayerControlPanel pcp = new PlayerControlPanel(player);
		PlaylistPanel plp = new PlaylistPanel();
		
		player.addPlaylistChangeListener(change -> {
			while (change.next()) {
				if(change.wasAdded()) {
					pcp.enable();
				}
				if(change.wasRemoved()) {
					if(player.getPlayList().isEmpty()) {
						Console.getLogger().info("No audio in playlist, disable the player control panel.");
						pcp.disable();
					}
				}
				plp.refreshList();
			}
		});
		
		plp.setListItems(player.getPlayList());
		
		componentManager = new ComponentManager();
		componentManager.registComponent(TitleBar.class.getSimpleName(), titleBar);
		componentManager.registComponent(Navigation.class.getSimpleName(), nav);
		componentManager.registComponent(PlayerControlPanel.class.getSimpleName(), pcp);
		componentManager.registComponent(PlaylistPanel.class.getSimpleName(), plp);
	}
}
