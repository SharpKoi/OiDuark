package com.sharpkoi.oiduark.utils;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class StageMovementHelper {
	
	protected Stage stage;
	
	protected double xOffset;
	protected double yOffset;
	
	protected EventHandler<MouseEvent> onPress;
	protected EventHandler<MouseEvent> onDrag;
	
	public StageMovementHelper(Stage stage) {
		this.stage = stage;
	}
	
	public void addGrip(Node node) {
		node.setOnMousePressed(e -> {
			xOffset = e.getSceneX();
			yOffset = e.getSceneY();
		});
		
		node.setOnMouseDragged(e -> {
			stage.setX(e.getScreenX() - xOffset);
			stage.setY(e.getScreenY() - yOffset);
		});
	}
}
