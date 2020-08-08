package com.sharpkoi.oiduark.app;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.sharpkoi.oiduark.audio.AudioPlayer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class StageController implements Initializable {
	
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
	private JFXListView<Button> l_playlist;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		player = new AudioPlayer();
		ObservableList<Button> audioItemList = FXCollections.observableArrayList(
				new Button("Apple"), 
				new Button("ASUS"),
				new Button("Sony"));
		l_playlist.setItems(audioItemList);
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
