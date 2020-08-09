package com.sharpkoi.oiduark.app;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.sharpkoi.oiduark.audio.Audio;
import com.sharpkoi.oiduark.audio.AudioPlayer;
import com.sharpkoi.oiduark.utils.MetaData;
import com.sun.media.jfxmedia.events.MetadataListener;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class HomeController implements Initializable {
	
	private double xOffset;
	private double yOffset;
	
	private AudioPlayer player;
	
	@FXML
	private AnchorPane root;
	
	/***** title bar *****/
	@FXML
	private AnchorPane titleBar;
	@FXML
	private ImageView b_close;
	
	/***** navigation *****/
	@FXML
	private JFXButton b_home;
	@FXML
	private JFXButton b_select;
	@FXML
	private JFXButton b_setting;
	@FXML
	private JFXButton b_about;
	
	/***** controller *****/
    @FXML
    private ImageView b_play;
	@FXML
    private ImageView b_next;
	@FXML
    private ImageView b_last;
	
	/***** play list panel *****/
	@FXML
	private JFXListView<Audio> l_playlist;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		player = new AudioPlayer();
		
		l_playlist.setCellFactory(cell -> {
			return new AudioListCell();
		});
		l_playlist.setFocusTraversable(false);
		
//		b_home.setOnAction(e -> {
//			TODO: switch page
//		});
//		b_select.setOnAction(e -> {
//			TODO: switch page
//		});
//		b_setting.setOnAction(e -> {
//			TODO: switch page
//		});
//		b_about.setOnAction(e -> {
//			TODO: switch page
//		});
		System.out.println("initialize");
	}
	
	
	public void initWindow() {
		Stage stage = (Stage) root.getScene().getWindow();
		
		titleBar.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			}
		});
		
		titleBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				stage.setX(event.getScreenX() - xOffset);
				stage.setY(event.getScreenY() - yOffset);
			}
		});
		
		// test list view
		player.addAudio(Audio.loadFromJson(MetaData.AUDIO_DATA_PATH, "audio01"));
		
		ObservableList<Audio> ol_playList = FXCollections.observableList(player.getPlayList());
		l_playlist.setItems(ol_playList);
		l_playlist.setFixedCellSize(64);
	}
	
	public void onCloseButtonClicked() {
		System.out.println("exit");
		System.exit(1);
	}
	
	public void onPlayButtonClicked() {
		if(player.isPlaying()) {
			player.pause();
			b_play.setImage(new Image(getClass().getResourceAsStream(Main.PLAY_IMAGE_PATH)));
		}else {
			player.play();
			b_play.setImage(new Image(getClass().getResourceAsStream(Main.PAUSE_IMAGE_PATH)));
		}
		System.out.println("Play audio!");
	}
}
