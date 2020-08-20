package com.sharpkoi.oiduark.app;

import com.sharpkoi.oiduark.app.controller.AudioPageController;
import com.sharpkoi.oiduark.audio.Audio;
import com.sharpkoi.oiduark.audio.AudioPlayer;

//import animatefx.animation.SlideOutRight;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class PlayListCell extends ListCell<Audio> {
	
	private HBox container;
	
	private Button b_remove;
	
	private ScrollPane titleContainer;
	
	private Label l_title;
	
	@Override
	protected void updateItem(Audio item, boolean empty) {
		super.updateItem(item, empty);
		if(item != null && !empty) {
			container = new  HBox(8);
			container.setStyle("-fx-background-color: transparent ;");
			
			l_title = new Label(item.getTitle());
			l_title.setFont(Font.font(12));
			l_title.setTextFill(Paint.valueOf("#fff"));
			
			titleContainer = new ScrollPane(l_title);
			titleContainer.setVbarPolicy(ScrollBarPolicy.NEVER);
			titleContainer.setHbarPolicy(ScrollBarPolicy.NEVER);
			titleContainer.getStyleClass().add("title-container");
			titleContainer.applyCss();
			titleContainer.setPrefSize(150, USE_COMPUTED_SIZE);
			
			AudioPlayer player = Main.getInstance().getAudioPlayer();
			Audio currentAudio = player.getCurrentAudio();
			
			if(currentAudio != null) {
				if(currentAudio.equals(item)) {
					container.getChildren().add(titleContainer);
					setGraphic(container);
					setStyle("-fx-background-color:  #0090e3 ;");
					if(getWidth() > getListView().getWidth()) {
						// should wait for the layout resized.
						Platform.runLater(() -> {
							SimpleAnimation.marquee((Label) titleContainer.getContent(), titleContainer, 40);
						});
					}
					
					AudioPageController.getInstance().refreshAudioList();
					return;
				}
			}
			
			SimpleAnimation.stopAnim(this);
			
			MaterialDesignIconView minusIcon = new MaterialDesignIconView(MaterialDesignIcon.MINUS);
			minusIcon.setGlyphSize(24);
			minusIcon.setFill(Paint.valueOf("#fff"));
			
			b_remove = new Button();
			b_remove.setPadding(Insets.EMPTY);
			b_remove.setOpacity(0.6);
			b_remove.setCursor(Cursor.HAND);
			b_remove.setGraphic(minusIcon);
			b_remove.getStyleClass().add("op-button");
			b_remove.setMaxWidth(16);
			b_remove.setPrefWidth(16);
			
//			SlideOutRight cellAnim = new SlideOutRight(this);
//			cellAnim.setOnFinished(e -> {
//				getListView().getItems().remove(this.getIndex());
//			});
			
			b_remove.setOnAction(e -> {
				// TODO: mark the current playing audio cell. So here I will not let users remove the current audio.
				if(player.getCurrentAudio() != null) {
					if(player.getCurrentAudio().equals(item)) {
						player.pause();
						if(player.getPlayList().size() <= 1) {
							player.stop();
							getListView().getItems().remove(this.getIndex());
						}else {
							getListView().getItems().remove(this.getIndex());
							player.play();
						}
					}else {
						getListView().getItems().remove(this.getIndex());
					}
				}else {
					getListView().getItems().remove(this.getIndex());
				}
				
//				TODO: Slide out with animation
//				cellAnim.play();
				
				AudioPageController.getInstance().refreshAudioList();
			});
			
			container.getChildren().add(b_remove);
			container.getChildren().add(titleContainer);
			
			setGraphic(container);
			setStyle("-fx-background-color:  #2B3035 ;");
		}else {
			setStyle("-fx-background-color:  #2B3035 ;");
			setGraphic(null);
			setText("");
		}
	}
}
