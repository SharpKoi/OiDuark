package com.sharpkoi.oiduark.app;
	
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import com.sharpkoi.oiduark.audio.AudioManager;
import com.sharpkoi.oiduark.user.UserData;
import com.sharpkoi.oiduark.user.UserSetting;
import com.sharpkoi.oiduark.utils.*;

import com.sharpkoi.oiduark.app.component.*;
import com.sharpkoi.oiduark.app.controller.AppController;
import com.sharpkoi.oiduark.app.controller.ControllerManager;
import com.sharpkoi.oiduark.audio.AudioPlayer;
import com.sharpkoi.oiduark.audio.AudioTagManager;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Application;
import javafx.application.Platform;
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

	private ControllerManager controllerManager;
	private ComponentManager componentManager;
	private AudioManager audioManager;
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
	
	public ControllerManager getControllerManager() {
		return controllerManager;
	}
	
	public ComponentManager getComponentManager() {
		return componentManager;
	}

	public AudioManager getAudioManager() {
		return audioManager;
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
		System.setProperty("prism.lcdtext", "false");
		System.setProperty("prism.text", "t2k");
		Console.INFO("Launch OiDuark...");
		instance = this;

		// init properties and resource loader
		properties = ResourceBundle.getBundle("app");
		resLoader = new ResourceLoader("resources/");
		
		stage = primaryStage;
		try {
			// init userdata dir
			File userdata = UserData.getUserdataDir();
			if(!userdata.exists()) userdata.mkdirs();

			// init logger
			File logDir = new File(userdata, properties.getString("log-dir"));
			if(!logDir.exists()) logDir.mkdirs();
			
			FileHandler logHandler = 
					new FileHandler(logDir.getAbsolutePath() + "/" +
							new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) + ".log");
			logger = Logger.getGlobal();
			logger.addHandler(logHandler);
			Console.setLogger(logger);
			Console.getLogger().info("main logger initialized");

			// init user setting
			File userSettingFile = new File(userdata, properties.getString("user-setting"));
			if(userSettingFile.exists()) {
				usrSetting = UserSetting.load(userSettingFile);
			}else {
				usrSetting = new UserSetting(
						UserData.getDefaultMediaDir().getAbsolutePath(),
						100);
			}
			
			player = new AudioPlayer();
			audioManager = new AudioManager();
			tagManager = new AudioTagManager();

			// player shall be initialized before components initialization.
			initComponents();

			File mediaDir = new File(usrSetting.getUserMediaDir());
			if(mediaDir.isDirectory() && !mediaDir.exists()) mediaDir.mkdir();

			if(mediaDir.isDirectory()) {
				audioManager.loadAudioList(mediaDir);
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
				audioManager.saveAllAudioData();
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
		
		titleBar.setOnMinimizeButtonClicked(e ->
			SimpleAnimation.windowIconify(stage.getScene().getRoot(), onFinish -> {
				stage.setIconified(true);
				stage.getScene().getRoot().setOpacity(1);
				stage.getScene().getRoot().setScaleX(1);
				stage.getScene().getRoot().setScaleY(1);
			}));
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
			audioManager.saveAllAudioData();
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
