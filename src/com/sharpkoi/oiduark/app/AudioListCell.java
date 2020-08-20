package com.sharpkoi.oiduark.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import com.sharpkoi.oiduark.app.controller.AudioPageController;
import com.sharpkoi.oiduark.app.controller.AudioSetter;
import com.sharpkoi.oiduark.audio.Audio;
import com.sharpkoi.oiduark.audio.AudioPlayer;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AudioListCell extends ListCell<Audio> {
	
	private Button b_add;
	
	@Override
	protected void updateItem(Audio item, boolean empty) {
		super.updateItem(item, empty);
		
		if(item == null) {
			setGraphic(null);
			setText("");
			return;
		}
		
		MaterialDesignIconView opIcon = new MaterialDesignIconView();
		
		opIcon.setGlyphSize(16);
		opIcon.setFill(Paint.valueOf("#fff"));
		
		AudioPlayer player = Main.getInstance().getAudioPlayer();
		
		b_add = new Button();
		b_add.setPadding(Insets.EMPTY);
		b_add.setCursor(Cursor.HAND);
		b_add.setPrefSize(24, 24);
		
		b_add.setGraphic(opIcon);
		if(player.getPlayList().contains(item)) {
			((MaterialDesignIconView) b_add.getGraphic()).setIcon(MaterialDesignIcon.MINUS);
		}else {
			((MaterialDesignIconView) b_add.getGraphic()).setIcon(MaterialDesignIcon.PLUS);
		}
		
		b_add.getStyleClass().add("op-button");
		
		b_add.setOnAction(e -> {
			if(player.getPlayList().contains(item)) {
				if(player.getCurrentAudio() != null) {
					if(player.getCurrentAudio().equals(item)) {
						player.pause();
						if(player.getPlayList().size() <= 1) {
							player.stop();
							AudioPageController.getInstance().removeAudioFromPlaylist(item);
						}else {
							AudioPageController.getInstance().removeAudioFromPlaylist(item);
							player.play();
						}
					}else {
						getListView().getItems().remove(this.getIndex());
					}
				}else {
					AudioPageController.getInstance().removeAudioFromPlaylist(item);
					((MaterialDesignIconView) b_add.getGraphic()).setIcon(MaterialDesignIcon.PLUS);
				}
			}else {
				AudioPageController.getInstance().addAudioToPlaylist(item);
				((MaterialDesignIconView) b_add.getGraphic()).setIcon(MaterialDesignIcon.MINUS);
			}
		});
		
		setGraphic(b_add);
		setGraphicTextGap(16); 
		setText(item.getTitle());
		setWrapText(false);
		setFont(Font.font(16));
		setTextFill(Paint.valueOf("#fff"));
		
		selectedProperty().addListener((observable, o_select, n_select) -> {
			if(n_select) {
				this.setStyle("-fx-background-color: linear-gradient("
						+ "from 0% 0% to 75% 75%, rgba(0, 170, 187, 1), rgba(0, 0, 0, 0)) ;");
			}else {
				this.setStyle("");
			}
		});
		
		setOnMouseClicked(e -> {
			if(e.getButton().equals(MouseButton.PRIMARY)) {
				if(e.getClickCount() == 2) {
					if(player.getPlayList().contains(item)) {
						if(player.getCurrentAudio() != null) {
							if(player.getCurrentAudio().equals(item)) {
								player.pause();
								if(player.getPlayList().size() <= 1) {
									player.stop();
									AudioPageController.getInstance().removeAudioFromPlaylist(item);
								}else {
									AudioPageController.getInstance().removeAudioFromPlaylist(item);
									player.play();
								}
							}else {
								AudioPageController.getInstance().removeAudioFromPlaylist(item);
								((MaterialDesignIconView) b_add.getGraphic()).setIcon(MaterialDesignIcon.PLUS);
							}
						}else {
							AudioPageController.getInstance().removeAudioFromPlaylist(item);
							((MaterialDesignIconView) b_add.getGraphic()).setIcon(MaterialDesignIcon.PLUS);
						}
 					}else {
						AudioPageController.getInstance().addAudioToPlaylist(item);
						((MaterialDesignIconView) b_add.getGraphic()).setIcon(MaterialDesignIcon.MINUS);
					}
				}
			}else if(e.getButton().equals(MouseButton.SECONDARY)) {
				settingDialog(item);
				getListView().refresh();
			}
		});
	}
	
	public void settingDialog(Audio audio) {
		Stage dialog = new Stage();
		dialog.initOwner(Main.getInstance().getStage());
		dialog.initModality(Modality.APPLICATION_MODAL);
		
		FXMLLoader loader = new FXMLLoader(Main.class.getResource("AudioSetting.fxml"));
		try {
			Parent dialogPane = loader.load();
			AudioSetter setter = loader.getController();
			
			setter.initByAudio(audio);
			
			setter.setOnSettingDone(() -> {
				HashMap<String, String> setting = setter.getResult();
				Audio item = getItem();
				item.setTitle(setting.getOrDefault("title", item.getTitle()));
				item.setAuthor(setting.getOrDefault("author", item.getAuthor()));
				item.setCoverPath(setting.getOrDefault("cover", item.getCoverPath()));
				if(setting.containsKey("lyrics")) {
					try {
						item.loadLyrics(new File(setting.get("lyrics")));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
				dialog.close();
			});
			
			dialog.setScene(new Scene(dialogPane));
			dialog.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
