package com.sharpkoi.oiduark.app.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXSlider;
import com.sharpkoi.oiduark.app.*;
import com.sharpkoi.oiduark.audio.Audio;
import com.sharpkoi.oiduark.audio.AudioPlayer;
import com.sharpkoi.oiduark.utils.ResourceLoader;
import com.sharpkoi.oiduark.utils.TimeUtils;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

public class HomeController extends GlobalController {
	
	private static HomeController instance;
	
	public static HomeController getInstance() {
		return instance;
	}
		
	@FXML
	private AnchorPane root;
	/***** preview ******/
	@FXML
	private ImageView coverView;
	@FXML
	private Label l_loading;
	
	/***** player controller *****/
	@FXML
	private Label t_timeTick;
	@FXML
	private Label t_endTimeTick;
	@FXML
	private JFXSlider progressBar;
    @FXML
    private ImageView b_play;
	@FXML
    private ImageView b_next;
	@FXML
    private ImageView b_last;
	@FXML
	private MaterialDesignIconView i_playMode;
	@FXML
	private JFXSlider volumeSlider;
	
	
	/***** play list panel *****/
	@FXML
	private ListView<Audio> l_playlist;
	 
	/* Define the feature of each component */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		instance = this;
		super.initialize(location, resources);
		
		l_playlist.setCellFactory(cell -> {
			return new PlayListCell();
		});
		
		disableControlPane();
		
		coverView.imageProperty().addListener((observable, oldCover, newCover) -> {
			centerCover(newCover);
		});
		
		AudioPlayer player = Main.getInstance().getAudioPlayer();
		
		i_playMode.setOnMouseClicked(e -> {
			player.setNextMode();
			if(player.getModeName().equals("ORDERED")) {
				i_playMode.setIcon(MaterialDesignIcon.SHUFFLE_DISABLED);
			}else if(player.getModeName().equals("RANDOM")) {
				i_playMode.setIcon(MaterialDesignIcon.SHUFFLE);
			}else if(player.getModeName().equals("LOOP")) {
				i_playMode.setIcon(MaterialDesignIcon.REPLAY);
			}
		});
		
		volumeSlider.setValue(player.getVolume() * 100);
		
		volumeSlider.valueProperty().addListener((observable, oldVal, newVal) -> {
			player.setVolume(newVal.intValue() / 100.0);
		});
		
		progressBar.setOnDragDetected(e -> {
			player.pause();
			double secs = (progressBar.getValue() / 100) * player.getCurrentAudio().getDuration();
			t_timeTick.setText(TimeUtils.parseSecondsToTime(secs));
		});
		
		progressBar.setOnMousePressed(e -> {
			player.pause();
			double secs = (progressBar.getValue() / 100) * player.getCurrentAudio().getDuration();
			t_timeTick.setText(TimeUtils.parseSecondsToTime(secs));
		});
		
		progressBar.setOnMouseReleased(e -> {
			double seconds = (progressBar.getValue() / 100) * player.getCurrentAudio().getDuration();
			player.jumpTo(seconds);
			player.resume();
		});
		
		progressBar.setOnKeyPressed(e -> {
			if(e.getCode().equals(KeyCode.RIGHT) || e.getCode().equals(KeyCode.LEFT)) {
				player.pause();
				double secs = (progressBar.getValue() / 100) * player.getCurrentAudio().getDuration();
				t_timeTick.setText(TimeUtils.parseSecondsToTime(secs));
			}
		});
		
		progressBar.setOnKeyReleased(e -> {
			if(e.getCode().equals(KeyCode.RIGHT) || e.getCode().equals(KeyCode.LEFT)) {
				double secs = (progressBar.getValue() / 100) * player.getCurrentAudio().getDuration();
				player.jumpTo(secs);
				player.resume();
			}
		});
		
		player.setOnTimeUpdate((observable, oldTime, newTime) -> {
			if(player.isPlaying()) {
				double totalSecs = newTime.toSeconds();
				t_timeTick.setText(TimeUtils.parseSecondsToTime(totalSecs));
				
				double progress = totalSecs / player.getCurrentAudio().getDuration() * 100;
				progressBar.setValue(progress);
			}
		});
		
		player.setOnMediaReady(() -> {
			Image cover = new Image("file:///" + player.getCurrentAudio().getCoverPath());
	        
			l_loading.setOpacity(0);
			coverView.setImage(cover);
			
			t_endTimeTick.setText(TimeUtils.parseSecondsToTime(player.getCurrentAudio().getDuration()));
			progressBar.setDisable(false);
		});
		
		player.setOnPlayerStop(() -> {
			progressBar.setValue(0);
			coverView.setImage(null);
			t_timeTick.setText("0:00");
			b_play.setImage(ResourceLoader.loadPlayIcon());
			disableControlPane();
		});
	}
	
	@Override
	protected void loadPageInfo() {
		currentPageName = "Home";
		b_home.setStyle("-fx-background-color:  #7b2cbf ;");
		
		ObservableList<Audio> playList = Main.getInstance().getAudioPlayer().getObservablePlayList();
		if(!playList.isEmpty()) {
			enableControlPane();
		}
		l_playlist.setItems(playList);
		l_playlist.setFixedCellSize(48);
	}
	 
	public void onPlayButtonClicked() {
		AudioPlayer player = Main.getInstance().getAudioPlayer();
		if(player.isPlaying()) {
			player.pause();
			b_play.setImage(ResourceLoader.loadPlayIcon());
		}else {
			if(player.indicator == -1) {
				l_loading.setOpacity(1);
				if(player.play()) {
					b_play.setImage(ResourceLoader.loadPauseIcon());
				}
			}else {
				player.resume();
				b_play.setImage(ResourceLoader.loadPlayIcon());
			}
		}
	}
	
	public void disableControlPane() {
		b_last.setDisable(true);
		b_play.setDisable(true);
		b_next.setDisable(true);
		i_playMode.setDisable(true);
		progressBar.setDisable(true);
	}
	
	public void enableControlPane() {
		b_last.setDisable(false);
		b_play.setDisable(false);
		b_next.setDisable(false);
		i_playMode.setDisable(false);
		progressBar.setDisable(false);
	}
	
	public void centerCover(Image image) {
		if (image != null) {
            double w = 0;
            double h = 0;

            double ratioX = coverView.getFitWidth() / image.getWidth();
            double ratioY = coverView.getFitHeight() / image.getHeight();

            double reducCoeff = 0;
            if(ratioX >= ratioY) {
                reducCoeff = ratioY;
            } else {
                reducCoeff = ratioX;
            }

            w = image.getWidth() * reducCoeff;
            h = image.getHeight() * reducCoeff;

            coverView.setLayoutX((coverView.getFitWidth() - w) / 2);
            coverView.setLayoutY((coverView.getFitHeight() - h) / 2);
        }
	}
}
