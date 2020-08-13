package com.sharpkoi.oiduark.app;

import com.sharpkoi.oiduark.app.controller.AudioPageController;
import com.sharpkoi.oiduark.audio.Audio;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class AudioListCell extends ListCell<Audio> {
	
	private boolean isAudioInPlayList = false;
	
	private Button b_add;
	
	@Override
	protected void updateItem(Audio item, boolean empty) {
		super.updateItem(item, empty);
		
		if(item == null) {
			return;
		}
		
		MaterialDesignIconView plusIcon = new MaterialDesignIconView(MaterialDesignIcon.PLUS_CIRCLE_OUTLINE);
		plusIcon.setGlyphSize(32);
		plusIcon.setFill(Paint.valueOf("#fff"));
		
		b_add = new Button();
		b_add.setPadding(Insets.EMPTY);
		b_add.setOpacity(0.6);
		b_add.setCursor(Cursor.HAND);
		b_add.setGraphic(plusIcon);
		b_add.setStyle("-fx-background-color:  transparent ;");
		
		b_add.setOnMouseEntered(e -> {
			b_add.setOpacity(1);
		});
		
		b_add.setOnMouseExited(e -> {
			b_add.setOpacity(0.6);
		});
		
		b_add.setOnAction(e -> {
			if(isAudioInPlayList) {
				AudioPageController.getInstance().removeAudioFromPlaylist(item);
				((MaterialDesignIconView) b_add.getGraphic()).setIcon(MaterialDesignIcon.PLUS_CIRCLE_OUTLINE);
				isAudioInPlayList = false;
			}else {
				AudioPageController.getInstance().addAudioToPlaylist(item);
				((MaterialDesignIconView) b_add.getGraphic()).setIcon(MaterialDesignIcon.MINUS_CIRCLE_OUTLINE);
				isAudioInPlayList = true;
			}
		});
		
		setGraphic(b_add);
		
		setText(item.getTitle());
		setWrapText(false);
		setFont(Font.font(16));
		setTextFill(Paint.valueOf("#fff"));
	}
}
