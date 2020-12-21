package com.sharpkoi.oiduark.app.fxml.sample;

import com.sharpkoi.oiduark.utils.Console;

import javafx.scene.Cursor;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

public class DraggableListCell<T> extends ListCell<T> {
	
	private boolean dragging;
	
	private double X1;
	private double Y1;
	
	@Override
	protected void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);
		
		if(item == null || empty) {
			setGraphic(null);
			setText(null);
			return;
		}
		
		Label l = new Label(item.toString());
		setGraphic(l);
		
		setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		setCursor(Cursor.OPEN_HAND);
		
		setOnMousePressed(e -> {
			if(!empty) {
				setCursor(Cursor.CLOSED_HAND);
				Console.INFO("x: %f, y: %f", getLayoutX(), getLayoutY());
				setDragging(true, e);
			}
		});
		
		setOnMouseDragged(e -> {
			if(!empty) {
				
			}
		});
		
		setOnMouseReleased(e -> {
			if(!empty) {
				setCursor(Cursor.OPEN_HAND);
			}
		});
	}
	
	protected void setDragging(boolean b, MouseEvent e) {
		dragging = b;
		if(dragging) {
			X1 = e.getScreenX();
			Y1 = e.getScreenY();
		}
	}
}
