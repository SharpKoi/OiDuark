package com.sharpkoi.oiduark.component;

import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXSlider.IndicatorPosition;
import com.sharpkoi.oiduark.OiDuarkApp;
import com.sharpkoi.oiduark.audio.AudioPlayer;
import com.sharpkoi.oiduark.utils.ResourceLoader;
import com.sharpkoi.oiduark.utils.TimeUtils;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class PlayerControlPanel extends AnchorPane {
	
	private AudioPlayer player;
	
	private Label l_timeTick;
	private Label l_endTimeTick;
	private JFXSlider progressBar;

	private Image playIcon;
	private Image pauseIcon;
	private Image nextIcon;
	private Image lastIcon;
    private Button b_play;
    private Button b_next;
    private Button b_last;
	private Button b_playMode;
	private MaterialDesignIconView i_volume;
	
	private JFXSlider volumeSlider;
	
	private EventHandler<MouseEvent> onLastButtonClicked;
	private EventHandler<MouseEvent> onPlayButtonClicked;
	private EventHandler<MouseEvent> onNextButtonClicked;
	private EventHandler<MouseEvent> onPlayModeButtonClicked;
	
	public PlayerControlPanel(AudioPlayer player) {
		this.player = player;

		ResourceLoader loader = OiDuarkApp.getInstance().getResourceLoader();
		playIcon = loader.loadPlayIcon();
		pauseIcon = loader.loadPauseIcon();
		nextIcon = loader.loadNextIcon();
		lastIcon = loader.loadLastIcon();

		getChildren().addAll(
				timeTickLabel(), progressBar(), endTimeTickLabel(),
				lastButton(), playButton(), nextButton(), playModeButton(),
				volumeIcon(), volumeSlider()
		);
		
		setBackground(new Background(new BackgroundFill(Paint.valueOf("#1d2024"), null, null)));
		getStylesheets().add(OiDuarkApp.class.getResource("styles/player-control-panel.css").toString());
		getStyleClass().add("control-pane");
		setPrefSize(1024, 100);
		
		AnchorPane.setTopAnchor(l_timeTick, 7d);
		AnchorPane.setLeftAnchor(l_timeTick, 10d);
		
		AnchorPane.setTopAnchor(progressBar, 7d);
		AnchorPane.setLeftAnchor(progressBar, 65d);
		AnchorPane.setRightAnchor(progressBar, 65d);
		
		AnchorPane.setTopAnchor(l_endTimeTick, 7d);
		AnchorPane.setRightAnchor(l_endTimeTick, 10d);
		
		AnchorPane.setLeftAnchor(b_last, 30d);
		AnchorPane.setLeftAnchor(b_play, 80d);
		AnchorPane.setLeftAnchor(b_next, 130d);
		AnchorPane.setLeftAnchor(b_playMode, 180d);
		
		AnchorPane.setRightAnchor(i_volume, 184d);
		AnchorPane.setRightAnchor(volumeSlider, 60d);
		
		initForPlayer(player);
	}
	
	public AudioPlayer getPlayer() {
		return player;
	}
	
	public Label timeTickLabel() {
		if(l_timeTick == null) {
			l_timeTick = new Label("-:--");
			l_timeTick.setTextFill(Paint.valueOf("white"));
			l_timeTick.setTextAlignment(TextAlignment.RIGHT);
			l_timeTick.setContentDisplay(ContentDisplay.TEXT_ONLY);
			l_timeTick.setAlignment(Pos.CENTER_RIGHT);
			l_timeTick.setPrefWidth(50);
		}
		
		return l_timeTick;
	}
	
	public Label endTimeTickLabel() {
		if(l_endTimeTick == null) {
			l_endTimeTick = new Label("-:--");
			l_endTimeTick.setTextFill(Paint.valueOf("white"));
			l_endTimeTick.setTextAlignment(TextAlignment.LEFT);
			l_endTimeTick.setContentDisplay(ContentDisplay.TEXT_ONLY);
			l_endTimeTick.setAlignment(Pos.CENTER_LEFT);
			l_endTimeTick.setPrefWidth(50);
		}
		
		return l_endTimeTick;
	}
	
	public JFXSlider progressBar() {
		if(progressBar == null) {
			progressBar = new JFXSlider(0, 100, -1);
			progressBar.getStyleClass().add("progress-bar");
			progressBar.setPrefHeight(20);
		}
		
		return progressBar;
	}
	
	public Button playButton() {
		if(b_play == null) {
			ImageView playIconView = new ImageView(this.playIcon);
			playIconView.setFitHeight(32);
			playIconView.setFitWidth(32);
			b_play = new Button();
			b_play.setGraphic(playIconView);
			b_play.setBackground(new Background(new BackgroundFill(Paint.valueOf("transparent"), null, null)));
			b_play.setLayoutY(36);
			b_play.setCursor(Cursor.HAND);
			b_play.getStyleClass().add("control-button");
		}
		
		return b_play;
	}
	
	public Button lastButton() {
		if(b_last == null) {
			ImageView lastIconView = new ImageView(this.lastIcon);
			lastIconView.setFitHeight(24);
			lastIconView.setFitWidth(24);
			b_last = new Button();
			b_last.setGraphic(lastIconView);
			b_last.setBackground(new Background(new BackgroundFill(Paint.valueOf("transparent"), null, null)));
			b_last.setLayoutY(40);
			b_last.setCursor(Cursor.HAND);
			b_last.getStyleClass().add("control-button");
		}
		
		return b_last;
	}
	
	public Button nextButton() {
		if(b_next == null) {
			ImageView nextIconView = new ImageView(this.nextIcon);
			nextIconView.setFitHeight(24);
			nextIconView.setFitWidth(24);
			b_next = new Button();
			b_next.setGraphic(nextIconView);
			b_next.setBackground(new Background(new BackgroundFill(Paint.valueOf("transparent"), null, null)));
			b_next.setLayoutY(40);
			b_next.setCursor(Cursor.HAND);
			b_next.getStyleClass().add("control-button");
		}
		
		return b_next;
	}
	
	public Button playModeButton() {
		if(b_playMode == null) {
			MaterialDesignIconView playModeIcon = new MaterialDesignIconView(MaterialDesignIcon.SHUFFLE_DISABLED);
			playModeIcon.setSize("24px");
			playModeIcon.setFill(Paint.valueOf("white"));
			b_playMode = new Button();
			b_playMode.setGraphic(playModeIcon);
			b_playMode.setBackground(new Background(new BackgroundFill(Paint.valueOf("transparent"), null, null)));
			b_playMode.setLayoutY(40);
			b_playMode.setCursor(Cursor.HAND);
			b_playMode.getStyleClass().add("control-button");
		}
		
		return b_playMode;
	}
	
	public JFXSlider volumeSlider() {
		if(volumeSlider == null) {
			volumeSlider = new JFXSlider(0, 100, 100);
			volumeSlider.setIndicatorPosition(IndicatorPosition.LEFT);
			volumeSlider.getStyleClass().add("volume-slider");
			volumeSlider.setPrefWidth(120);
			volumeSlider.setLayoutY(54);
		}
		
		return volumeSlider;
	}
	
	public MaterialDesignIconView volumeIcon() {
		if(i_volume == null) {
			i_volume = new MaterialDesignIconView(MaterialDesignIcon.VOLUME_HIGH);
			i_volume.setSize("24px");
			i_volume.setFill(Paint.valueOf("white"));
			i_volume.setLayoutY(70);
		}
		
		return i_volume;
	}
	
	public void setOnLastButtonClicked(EventHandler<MouseEvent> handler) {
		this.onLastButtonClicked = handler;
	}
	
	public void setOnPlayButtonClicked(EventHandler<MouseEvent> handler) {
		this.onPlayButtonClicked = handler;
	}
	
	public void setOnNextButtonClicked(EventHandler<MouseEvent> handler) {
		this.onNextButtonClicked = handler;
	}
	
	public void setOnPlayModeButtonClicked(EventHandler<MouseEvent> handler) {
		this.onPlayModeButtonClicked = handler;
	}
	
	public void enable() {
		b_last.setDisable(false);
		b_play.setDisable(false);
		b_next.setDisable(false);
		b_playMode.setDisable(false);
		progressBar.setDisable(false);
	}
	
	public void disable() {
		b_last.setDisable(true);
		b_play.setDisable(true);
		b_next.setDisable(true);
		b_playMode.setDisable(true);
		progressBar.setDisable(true);
	}
	
	public void onTimeUpdate(Duration newTime) {
		if(player.isPlaying()) {
			double totalSecs = newTime.toSeconds();
			l_timeTick.setText(TimeUtils.parseSecondsToTime(totalSecs));
			
			double progress = totalSecs / player.getCurrentAudio().getDuration() * 100;
			progressBar.setValue(progress);
		}
	}
	
	public void onMediaReady() {
		l_endTimeTick.setText(TimeUtils.parseSecondsToTime(player.getCurrentAudio().getDuration()));
		progressBar.setDisable(false);
	}
	
	public void onPlayerStop() {
		progressBar.setValue(0);
		l_timeTick.setText("-:--");
		((ImageView) b_play.getGraphic()).setImage(this.playIcon);
		disable();
	}
	
	private void initForPlayer(AudioPlayer player) {
		progressBar.setOnDragDetected(e -> {
			if(player.getCurrentAudio() != null) {
				player.pause();
				double secs = (progressBar.getValue() / 100) * player.getCurrentAudio().getDuration();
				l_timeTick.setText(TimeUtils.parseSecondsToTime(secs));
			}
		});
		
		progressBar.setOnMousePressed(e -> {
			if(player.getCurrentAudio() != null) {
				player.pause();
				double secs = (progressBar.getValue() / 100) * player.getCurrentAudio().getDuration();
				l_timeTick.setText(TimeUtils.parseSecondsToTime(secs));
			}
		});
		
		progressBar.setOnMouseReleased(e -> {
			if(player.getCurrentAudio() != null) {
				double seconds = (progressBar.getValue() / 100) * player.getCurrentAudio().getDuration();
				player.jumpTo(seconds);
				player.resume();
			}
		});
		
		progressBar.setOnKeyPressed(e -> {
			if(player.getCurrentAudio() != null) {
				if(e.getCode().equals(KeyCode.RIGHT) || e.getCode().equals(KeyCode.LEFT)) {
					player.pause();
					double secs = (progressBar.getValue() / 100) * player.getCurrentAudio().getDuration();
					l_timeTick.setText(TimeUtils.parseSecondsToTime(secs));
				}
			}
		});
		
		progressBar.setOnKeyReleased(e -> {
			if(e.getCode().equals(KeyCode.RIGHT) || e.getCode().equals(KeyCode.LEFT)) {
				double secs = (progressBar.getValue() / 100) * player.getCurrentAudio().getDuration();
				player.jumpTo(secs);
				player.resume();
			}
		});
		
		b_last.setOnMouseClicked(e -> {
			player.jumpToStart();
			if(onLastButtonClicked != null) {
				onLastButtonClicked.handle(e);
			}
		});
		
		b_play.setOnMouseClicked(e -> {
			if(player.isPlaying()) {
				player.pause();
				((ImageView) b_play.getGraphic()).setImage(this.playIcon);
			}else {
				if(player.getIndicator() == -1) {
					if(player.play(true)) {
						((ImageView) b_play.getGraphic()).setImage(this.pauseIcon);
					}
				}else {
					player.resume();
					((ImageView) b_play.getGraphic()).setImage(this.pauseIcon);
				}
			}
			if(onPlayButtonClicked != null) {
				onPlayButtonClicked.handle(e);
			}
		});
		
		b_next.setOnMouseClicked(e -> {
			player.jumpToEnd();
			if(onNextButtonClicked != null) {
				onNextButtonClicked.handle(e);
			}
		});
		
		
		b_playMode.setOnMouseClicked(e -> {
			player.setNextMode();
			if(player.getModeName().equals("ORDERED")) {
				((MaterialDesignIconView) b_playMode.getGraphic()).setIcon(MaterialDesignIcon.SHUFFLE_DISABLED);
			}else if(player.getModeName().equals("RANDOM")) {
				((MaterialDesignIconView) b_playMode.getGraphic()).setIcon(MaterialDesignIcon.SHUFFLE);
			}else if(player.getModeName().equals("LOOP")) {
				((MaterialDesignIconView) b_playMode.getGraphic()).setIcon(MaterialDesignIcon.REPLAY);
			}
			if(onPlayModeButtonClicked != null) {
				onPlayModeButtonClicked.handle(e);
			}
		});
		
		volumeSlider.setValue(player.getVolume() * 100);
		
		volumeSlider.valueProperty().addListener((observable, oldVal, newVal) -> player.setVolume(newVal.intValue() / 100.0));
	}
}
