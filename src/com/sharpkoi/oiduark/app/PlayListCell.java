package com.sharpkoi.oiduark.app;

import com.sharpkoi.oiduark.audio.Audio;

//import animatefx.animation.SlideOutRight;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class PlayListCell extends ListCell<Audio> {
	
	private Button b_remove;
	
	@Override
	protected void updateItem(Audio item, boolean empty) {
		super.updateItem(item, empty);
		if(item != null && !empty) {
			setPrefSize(200, USE_COMPUTED_SIZE);
			
			MaterialDesignIconView minusIcon = new MaterialDesignIconView(MaterialDesignIcon.MINUS_CIRCLE_OUTLINE);
			minusIcon.setGlyphSize(24);
			minusIcon.setFill(Paint.valueOf("#fff"));
			
			b_remove = new Button();
			b_remove.setPadding(Insets.EMPTY);
			b_remove.setOpacity(0.6);
			b_remove.setCursor(Cursor.HAND);
			b_remove.setGraphic(minusIcon);
			b_remove.setStyle("-fx-background-color:  transparent ;");
			
			b_remove.setOnMouseEntered(e -> {
				b_remove.setOpacity(1);
			});
			
			b_remove.setOnMouseExited(e -> {
				b_remove.setOpacity(0.6);
			});
			
//			SlideOutRight cellAnim = new SlideOutRight(this);
//			cellAnim.setOnFinished(e -> {
//				getListView().getItems().remove(this.getIndex());
//			});
			
			b_remove.setOnAction(e -> {
				System.out.println("Remove the audio from the play list");
//				 TODO: Slide out with animation
//				cellAnim.play();
				getListView().getItems().remove(this.getIndex());
			});
			
			setGraphic(b_remove);
			
			setText(item.getTitle());
			setWrapText(false);
			setFont(Font.font(12));
			setTextFill(Paint.valueOf("#fff"));
		}else {
			setGraphic(null);
			setText(null);
		}
	}
}
