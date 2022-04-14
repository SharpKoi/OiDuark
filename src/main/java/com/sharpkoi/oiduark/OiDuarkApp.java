package com.sharpkoi.oiduark;
	
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import com.sharpkoi.oiduark.manager.AudioManager;
import com.sharpkoi.oiduark.manager.ComponentManager;
import com.sharpkoi.oiduark.user.UserConfig;
import com.sharpkoi.oiduark.user.UserData;
import com.sharpkoi.oiduark.utils.*;

import com.sharpkoi.oiduark.component.*;
import com.sharpkoi.oiduark.controller.AppController;
import com.sharpkoi.oiduark.manager.ControllerManager;
import com.sharpkoi.oiduark.audio.AudioPlayer;
import com.sharpkoi.oiduark.manager.AudioTagManager;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import lombok.Getter;

public class OiDuarkApp extends Application {

	private static OiDuarkApp instance;
	
	public static OiDuarkApp getInstance() {
		return instance;
	}

	@Getter	Properties properties;

	@Getter ResourceLoader resourceLoader;

	@Getter UserConfig userConfig;
	@Getter UserData userData;

	@Getter ControllerManager controllerManager;
	@Getter ComponentManager componentManager;
	@Getter AudioManager audioManager;
	@Getter AudioTagManager audioTagManager;

	@Getter Stage stage;
	@Getter Logger logger;
	@Getter AudioPlayer audioPlayer;
	
	public void activateScene(String pageName) {
		if(controllerManager.containsKey(pageName)) {
			AppController controller = controllerManager.getController(pageName);
			stage.getScene().setRoot(controller.getRoot());
			controller.onPageLoad();
		}else {
			try {
				String fxml = pageName + ".fxml";
				FXMLLoader loader = new FXMLLoader(getClass().getResource("views/"+fxml));
				Parent root = loader.load();
				stage.getScene().setRoot(root);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// After the audio player plays some audio, it occupies plenty of memory.
		// I'm confused by what being accumulated.
		System.gc();
	}

	/**
	 * Initialize all the fixed components, including properties and event handlers.
	 *
	 * @author Sharpkoi
	 * @since 2020.09.16
	 */
	private void initComponents() {
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
		componentManager.registerComponent(Navigation.class.getSimpleName(), nav);
		componentManager.registerComponent(PlayerControlPanel.class.getSimpleName(), pcp);
		componentManager.registerComponent(PlaylistPanel.class.getSimpleName(), plp);
	}

	public void saveAll() {
		audioTagManager.saveAllTags();
		audioManager.saveAllAudioData();
		userConfig.save();
	}

	@Override
	public void start(Stage primaryStage) {
		// export all modules to all required modules
		Environment.exportModulesAllToAll();
		System.setProperty("prism.lcdtext", "false");
		System.setProperty("prism.text", "t2k");
		Console.INFO("Launch OiDuark...");
		instance = this;

		stage = primaryStage;
		try {
			// init properties
			properties = new Properties();
			properties.load(getClass().getResourceAsStream("/app.properties"));

			// init resource loader
			resourceLoader = new ResourceLoader(this.getClass(), properties);

			// init/load user config and user data
			userConfig = UserConfig.load();
			userData = new UserData(userConfig, properties);

			// init userdata dir
			Path userdataDirPath = Paths.get(userConfig.getUserdataDirPath(), properties.getProperty("app.name").toLowerCase());
			if(!userdataDirPath.toFile().exists()) userdataDirPath.toFile().mkdirs();

			// init logger
			File logDir = Paths.get(userdataDirPath.toString(), properties.getProperty("log.storage")).toFile();
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
			FXMLLoader loader = new FXMLLoader(getClass().getResource("views/Home.fxml"));
			Parent root = loader.load();

			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("styles/application.css").toString());
			scene.setFill(Color.TRANSPARENT);

			primaryStage.setScene(scene);

			primaryStage.getIcons().add(resourceLoader.loadAppIcon());
			primaryStage.setTitle("OiDuark");
			primaryStage.initStyle(StageStyle.DECORATED);

			primaryStage.setOnCloseRequest(e -> {
				Console.getLogger().info("Saving all the data...");
				saveAll();
				Console.getLogger().info("Closing APP...");
			});

//			ResizeHelper.addResizeListener(stage);
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
}
