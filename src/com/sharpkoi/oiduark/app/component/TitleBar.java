package com.sharpkoi.oiduark.app.component;

import com.sharpkoi.oiduark.utils.ResourceLoader;
import com.sharpkoi.oiduark.utils.StageMovementHelper;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class TitleBar extends AnchorPane {
	
	protected Stage stage;
	protected StageMovementHelper stageMovementHelper;
	
	protected Button b_minimize;
	protected Button b_resize;
	protected Button b_close;
	
	protected EventHandler<ActionEvent> onMinimizeButtonClicked;
	protected EventHandler<ActionEvent> onResizeButtonClicked;
	protected EventHandler<ActionEvent> onCloseButtonClicked;
	
	public TitleBar(Stage stage, Image logo, String appName) {
		this.stage = stage;
		setOpacity(0.95);
		getStyleClass().add("title-bar");
		
		ImageView logoView = new ImageView(logo);
		logoView.setFitHeight(20);
		logoView.setFitWidth(20);
		
		Label l_title = new Label(appName);
		l_title.setTextFill(Paint.valueOf("white"));
		
		HBox titleContainer = new HBox(5, logoView, l_title);
		titleContainer.setAlignment(Pos.CENTER);
		titleContainer.setPrefSize(120, 32);
		
		HBox buttonContainer = new HBox(minimizeButton(), resizeButton(), closeButton());
		buttonContainer.setAlignment(Pos.CENTER_RIGHT);
		buttonContainer.setPrefHeight(32);
		
		getChildren().addAll(titleContainer, buttonContainer);
		setPrefHeight(32);
		
		AnchorPane.setTopAnchor(titleContainer, 0d);
		AnchorPane.setLeftAnchor(titleContainer, 0d);
		AnchorPane.setBottomAnchor(titleContainer, 0d);
		
		AnchorPane.setTopAnchor(buttonContainer, 0d);
		AnchorPane.setRightAnchor(buttonContainer, 0d);
		AnchorPane.setBottomAnchor(buttonContainer, 0d);
		
		stageMovementHelper = new StageMovementHelper(stage);
		stageMovementHelper.addGrip(this);
	}
	
	public Button minimizeButton() {
		if(b_minimize == null) {
			b_minimize = new Button();
			ImageView i = new ImageView(ResourceLoader.loadIcon("minimize_button_100px.png"));
			i.setFitHeight(18);
			i.setFitWidth(18);
			b_minimize.setGraphic(i);
			b_minimize.setFont(Font.font(1));
			b_minimize.setPrefSize(40, 32);
			b_minimize.getStyleClass().add("minimize-button");
			
			b_minimize.setOnAction(e -> {
				if(onMinimizeButtonClicked != null) {
					onMinimizeButtonClicked.handle(e);
				}
			});
		}
		return b_minimize;
	}
	
	public Button resizeButton() {
		if(b_resize == null) {
			b_resize = new Button();
			ImageView i = new ImageView(ResourceLoader.loadIcon("maximize_button_64px.png"));
			i.setFitHeight(18);
			i.setFitWidth(18);
			b_resize.setGraphic(i);
			b_resize.setFont(Font.font(1));
			b_resize.setPrefSize(40, 32);
			b_resize.getStyleClass().add("size-button");
			
			b_resize.setOnAction(e -> {
				if(onResizeButtonClicked != null) {
					onResizeButtonClicked.handle(e);
				}
			});
		}
		return b_resize;
	}
	
	public Button closeButton() {
		if(b_close == null) {
			b_close = new Button();
			ImageView i = new ImageView(ResourceLoader.loadIcon("close_window_100px.png"));
			i.setFitHeight(18);
			i.setFitWidth(18);
			b_close.setGraphic(i);
			b_close.setFont(Font.font(1));
			b_close.setPrefSize(40, 32);
			b_close.getStyleClass().add("close-button");
			
			b_close.setOnAction(e -> {
				if(onCloseButtonClicked != null) {
					onCloseButtonClicked.handle(e);
				}
			});
		}
		return b_close;
	}
	
	public void setOnMinimizeButtonClicked(EventHandler<ActionEvent> handler) {
		this.onMinimizeButtonClicked = handler;
	}
	
	public void setOnResizeButtonClicked(EventHandler<ActionEvent> handler) {
		this.onResizeButtonClicked = handler;
	}
	
	public void setOnCloseButtonClicked(EventHandler<ActionEvent> handler) {
		this.onCloseButtonClicked = handler;
	}
}
