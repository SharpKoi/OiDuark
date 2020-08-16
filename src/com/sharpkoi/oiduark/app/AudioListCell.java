package com.sharpkoi.oiduark.app;

import com.sharpkoi.oiduark.app.controller.AudioPageController;
import com.sharpkoi.oiduark.audio.Audio;
import com.sharpkoi.oiduark.audio.AudioPlayer;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

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
		
		opIcon.setGlyphSize(32);
		opIcon.setFill(Paint.valueOf("#fff"));
		
		AudioPlayer player = Main.getInstance().getAudioPlayer();
		
		b_add = new Button();
		b_add.setPadding(Insets.EMPTY);
		b_add.setOpacity(0.6);
		b_add.setCursor(Cursor.HAND);
		
		b_add.setGraphic(opIcon);
		if(player.getPlayList().contains(item)) {
			((MaterialDesignIconView) b_add.getGraphic()).setIcon(MaterialDesignIcon.MINUS_CIRCLE_OUTLINE);
		}else {
			((MaterialDesignIconView) b_add.getGraphic()).setIcon(MaterialDesignIcon.PLUS_CIRCLE_OUTLINE);
		}
		
		b_add.setStyle("-fx-background-color:  transparent ;");
		
		b_add.setOnMouseEntered(e -> {
			b_add.setOpacity(1);
		});
		
		b_add.setOnMouseExited(e -> {
			b_add.setOpacity(0.6);
		});
		
		b_add.setOnAction(e -> {
			if(player.getPlayList().contains(item)) {
				AudioPageController.getInstance().removeAudioFromPlaylist(item);
				((MaterialDesignIconView) b_add.getGraphic()).setIcon(MaterialDesignIcon.PLUS_CIRCLE_OUTLINE);
			}else {
				AudioPageController.getInstance().addAudioToPlaylist(item);
				((MaterialDesignIconView) b_add.getGraphic()).setIcon(MaterialDesignIcon.MINUS_CIRCLE_OUTLINE);
			}
		});
		
		setGraphic(b_add);
		setGraphicTextGap(8); 
		setText(item.getTitle());
		setWrapText(false);
		setFont(Font.font(16));
		setTextFill(Paint.valueOf("#fff"));
		
		selectedProperty().addListener((observable, o_select, n_select) -> {
			if(n_select) {
				this.setStyle("-fx-background-color: linear-gradient("
						+ "from 0% 0% to 75% 75%, rgba(0, 209, 195, 1), rgba(0, 0, 0, 0)) ;");
			}else {
				this.setStyle("");
			}
		});
	}
}
