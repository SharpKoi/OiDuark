package com.sharpkoi.oiduark.component;

import java.util.Arrays;
import java.util.List;

import com.sharpkoi.oiduark.OiDuarkApp;

import com.sharpkoi.oiduark.utils.ResourceLoader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

/**
 * The vertical side navigation bar of this application.
 * 
 * @author Sharpkoi
 * @since 2020.09.16
 */
public class Navigation extends VBox {
	
	private List<NavigationButton> navigationButtons;
	
	public Navigation(NavigationButton... buttons) {
		ResourceLoader loader = OiDuarkApp.getInstance().getResourceLoader();
		HBox banner = new HBox(10);
		
		ImageView logo = new ImageView(loader.loadAppIcon());
		logo.setFitHeight(40); logo.setFitWidth(40);
		
		Label title = new Label("OiDuark");
		title.setFont(loader.loadFont("Bauhaus 93 Regular", 24, ".ttf"));
		title.setTextFill(Paint.valueOf("#f05"));
		
		banner.setAlignment(Pos.CENTER);
		banner.setPrefHeight(90);
		banner.getChildren().addAll(logo, title);
		
		setAlignment(Pos.TOP_LEFT);
		setBackground(new Background(new BackgroundFill(Paint.valueOf("#212529"), null, null)));
		getStylesheets().add(loader.getResourceURL("styles/navigation.css").toString());
		getStyleClass().add("navigation");
		setPrefWidth(164);
		getChildren().add(banner);
		
		navigationButtons = Arrays.asList(buttons);
		navigationButtons.get(0).onSelect();
		getChildren().addAll(navigationButtons);
	}
	
	/**
	 * Set an event handler to handle the `OnSelect()` event.
	 * 
	 * @param index		the index to set handler
	 * @param handler	the event handler
	 */
	public void setOnSelect(int index, EventHandler<ActionEvent> handler) {
		NavigationButton target = navigationButtons.get(index);
		target.setOnAction(e -> {
			for(NavigationButton nb : navigationButtons) {
				if(nb.isSelected()) {
					nb.onUnselect();
				}
			}
			target.onSelect();
			handler.handle(e);
		});
	}
}
