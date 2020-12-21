package com.sharpkoi.oiduark.app.listview;

import com.sharpkoi.oiduark.app.Main;
import com.sharpkoi.oiduark.app.controller.AudioPageController;
import com.sharpkoi.oiduark.audio.Audio;
import com.sharpkoi.oiduark.audio.AudioPlayer;
import com.sharpkoi.oiduark.utils.SimpleAnimation;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
			buildTitle(item.getTitle());
			buildTitleContainer(l_title);
			buildRemoveButton();
			
			AudioPlayer player = Main.getInstance().getAudioPlayer();
			Audio currentAudio = player.getCurrentAudio();
			
			container = new HBox(8);
			container.setStyle("-fx-background-color: transparent ;");
			container.setAlignment(Pos.CENTER_LEFT);
			container.getChildren().add(b_remove);
			container.getChildren().add(titleContainer);
			container.setPrefSize(170, 32);
			container.setFillHeight(false);
			
			setGraphic(container);
			
			if(currentAudio != null) {
				if(currentAudio.equals(item)) {
					setStyle("-fx-background-color:  #0090e3 ;");
					
					// should wait for the layout resized.
					Platform.runLater(() -> {
						if(l_title.getWidth() > titleContainer.getWidth()) {
							SimpleAnimation.marquee(titleContainer, 30);
						}
					});
					
					AudioPageController.getInstance().refreshAudioList();
					return;
				}
			}
			
			setStyle("-fx-background-color:  transparent ;");
			setPrefWidth(getListView().getWidth());
		}else {
			setStyle("-fx-background-color:  transparent ;");
			setGraphic(null);
			setText(null);
		}
	}
	
	protected void buildTitle(String text) {
		l_title = new Label(text);
		l_title.setFont(Font.font(12));
		l_title.setTextFill(Paint.valueOf("#fff"));
		l_title.setPrefSize(USE_COMPUTED_SIZE, 16);
	}
	
	protected void buildTitleContainer(Label title) {
		titleContainer = new ScrollPane(title);
		titleContainer.setVbarPolicy(ScrollBarPolicy.NEVER);
		titleContainer.setHbarPolicy(ScrollBarPolicy.NEVER);
		titleContainer.getStyleClass().add("title-container");
		titleContainer.applyCss();
		titleContainer.setMinViewportHeight(16);
		titleContainer.setPadding(new Insets(8, 0, 8, 0));
		titleContainer.setPrefSize(150, 0);
	}
	
	protected void buildRemoveButton() {
		MaterialDesignIconView minusIcon = new MaterialDesignIconView(MaterialDesignIcon.MINUS);
		minusIcon.setGlyphSize(16);
		minusIcon.setFill(Paint.valueOf("#fff"));
		
		b_remove = new Button();
		b_remove.setPadding(Insets.EMPTY);
		b_remove.setCursor(Cursor.HAND);
		b_remove.setGraphic(minusIcon);
		b_remove.getStyleClass().add("add-button");
		b_remove.setMinSize(24, 24);
		b_remove.setPrefSize(24, 24);
		
		b_remove.setOnAction(e -> {
			removeFromPlaylist();
		});
	}
	
	protected  void removeFromPlaylist() {
		AudioPlayer player = Main.getInstance().getAudioPlayer();
		SimpleAnimation.slideOut(this, getListView(), onFinish -> {
			if(player.getCurrentAudio() != null) {
				if(player.getCurrentAudio().equals(this.getItem())) {
					player.pause();
					getListView().getItems().remove(this.getItem());
					if(player.getPlayList().size() >= 1) {
						player.play();
					}else {
						player.stop();
					}
					this.setTranslateX(0);	//reset the cell's translation
					AudioPageController.getInstance().refreshAudioList();
					return;
				}
			}
			
			getListView().getItems().remove(this.getItem());
			this.setTranslateX(0);	//reset the cell's translation
			AudioPageController.getInstance().refreshAudioList();
		});
	}
}
