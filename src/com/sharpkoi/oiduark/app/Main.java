package com.sharpkoi.oiduark.app;
	
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import com.sharpkoi.oiduark.audio.AudioManager;
import com.sharpkoi.oiduark.user.UserConfig;
import com.sharpkoi.oiduark.user.UserData;
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
import lombok.Getter;

public class Main extends Application {
	
	private static Main instance;
	
	public static Main getInstance() {
		return instance;
	}

	@Getter	private ResourceBundle properties = null;
	@Getter	private ResourceLoader resLoader = null;

	@Getter	private UserConfig userConfig;
	@Getter	private UserData userData;

	@Getter private ControllerManager controllerManager;
	@Getter private ComponentManager componentManager;
	@Getter private AudioManager audioManager;
	@Getter private AudioTagManager audioTagManager;

	@Getter private Stage stage;
	@Getter private Logger logger;
	@Getter	private AudioPlayer audioPlayer;
	
	public void activateScene(String pageName) {
		if(controllerManager.containsKey(pageName)) {
			AppController controller = controllerManager.getController(pageName);
			stage.getScene().setRoot(controller.getRoot());
			controller.onPageLoad();
		}else {
			try {
				String fxml = pageName + ".fxml";
				FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/"+fxml));
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
			// init/load user config and user data
			File userConfigFile = new File(Environment.getUserConfigHome(), properties.getString("user-config-file"));
			if (!userConfigFile.exists()) {
				userConfig = UserConfig.defaultConfig();
			}else {
				userConfig = UserConfig.load(userConfigFile);
			}
			userData = new UserData(userConfig, properties);

			// init userdata dir
			File userdataDir = userConfig.getUserdataDir();
			if(!userdataDir.exists()) userdataDir.mkdirs();

			// init logger
			File logDir = new File(userdataDir, properties.getString("log-dir"));
			if(!logDir.exists()) logDir.mkdirs();
			
			FileHandler logHandler = 
					new FileHandler(logDir.getAbsolutePath() + "/" +
							new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) + ".log");
			logger = Logger.getGlobal();
			logger.addHandler(logHandler);
			Console.setLogger(logger);
			Console.getLogger().info("main logger initialized");
			
			audioPlayer = new AudioPlayer();
			audioManager = new AudioManager();
			audioTagManager = new AudioTagManager();

			// player shall be initialized before components initialization.
			initComponents();

			File mediaDir = new File(userConfig.getMediaDirPath());
			if(mediaDir.isDirectory() && !mediaDir.exists()) mediaDir.mkdir();

			if(mediaDir.isDirectory()) {
				audioManager.loadAudioList(mediaDir);
				logger.info("All audio loaded done!");
			}
			
			controllerManager = new ControllerManager();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("view/Home.fxml"));
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
				audioTagManager.saveAllTags();
				audioManager.saveAllAudioData();
				userConfig.save();
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
			audioTagManager.saveAllTags();
			audioManager.saveAllAudioData();
			userConfig.save();
			
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
		
		PlayerControlPanel pcp = new PlayerControlPanel(audioPlayer);
		PlaylistPanel plp = new PlaylistPanel();
		
		audioPlayer.addPlaylistChangeListener(change -> {
			while (change.next()) {
				if(change.wasAdded()) {
					pcp.enable();
				}
				if(change.wasRemoved()) {
					if(audioPlayer.getPlayList().isEmpty()) {
						Console.getLogger().info("No audio in playlist, disable the player control panel.");
						pcp.disable();
					}
				}
				plp.refreshList();
			}
		});
		
		plp.setListItems(audioPlayer.getPlayList());
		
		componentManager = new ComponentManager();
		componentManager.registerComponent(TitleBar.class.getSimpleName(), titleBar);
		componentManager.registerComponent(Navigation.class.getSimpleName(), nav);
		componentManager.registerComponent(PlayerControlPanel.class.getSimpleName(), pcp);
		componentManager.registerComponent(PlaylistPanel.class.getSimpleName(), plp);
	}
}
