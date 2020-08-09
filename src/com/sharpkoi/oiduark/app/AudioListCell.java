package com.sharpkoi.oiduark.app;

import com.jfoenix.controls.JFXListCell;
import com.sharpkoi.oiduark.audio.Audio;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class AudioListCell extends JFXListCell<Audio> {
	
	@Override
	protected void updateItem(Audio item, boolean empty) {
		super.updateItem(item, empty);
		if(item != null && !empty) {
			
			FontAwesomeIconView minusIcon = new FontAwesomeIconView(FontAwesomeIcon.MINUS_CIRCLE);
			minusIcon.setGlyphSize(24);
			minusIcon.setFill(Paint.valueOf("#fff"));
			setGraphic(minusIcon);
			
			setGraphicTextGap(12);
			
			setText(item.getTitle());
			setWrapText(false);
			setFont(Font.font(16));
			setTextFill(Paint.valueOf("#fff"));
		}else {
			setGraphic(null);
			setText("");
		}
	}
}
