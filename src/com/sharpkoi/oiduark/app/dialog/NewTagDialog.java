package com.sharpkoi.oiduark.app.dialog;

import java.util.HashMap;

import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXTextField;
import com.sharpkoi.oiduark.app.Main;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class NewTagDialog extends Stage {
	
	private JFXTextField f_tagNameInput;
	private JFXColorPicker c_tagColorPicker;
	
	private EventHandler<ActionEvent> onConfirm;
	
	public NewTagDialog() {
		super();
		
		initOwner(Main.getInstance().getStage());
		initStyle(StageStyle.UNDECORATED);
		initModality(Modality.APPLICATION_MODAL);
		
		setScene(buildScene());
	}
	
	public HashMap<String, String> getResult() {
		HashMap<String, String> result = new HashMap<>();
		result.put("name", f_tagNameInput.getText());
		result.put("color", c_tagColorPicker.getValue().toString());
		
		return result;
	}
	
	public void setOnConfirm(EventHandler<ActionEvent> task) {
		this.onConfirm = task;
	}
	
	public Scene buildScene() {
		AnchorPane root = new AnchorPane();
		root.getStylesheets().add("com/sharpkoi/oiduark/app/style/newtag-setting.css");
		root.getStyleClass().add("root");
		root.setPrefSize(250, 180);
		
		GridPane grid = new GridPane();
		
		RowConstraints row = new RowConstraints(30);
		grid.getRowConstraints().addAll(row, row, row);
		
		ColumnConstraints col0 = new ColumnConstraints(60);
		ColumnConstraints col1 = new ColumnConstraints(150);
		grid.getColumnConstraints().addAll(col0, col1);
		
		Label l_tagName = new Label("標籤名稱");
		l_tagName.setTextFill(Paint.valueOf("white"));
		
		f_tagNameInput = new JFXTextField();
		f_tagNameInput.setUnFocusColor(Paint.valueOf("white"));
		f_tagNameInput.setFocusColor(Paint.valueOf("#5976cc"));
		
		Label l_tagColor = new Label("標籤顏色");
		l_tagColor.setTextFill(Paint.valueOf("white"));
		
		c_tagColorPicker = new JFXColorPicker();
		
		grid.add(l_tagName, 0, 1);
		grid.add(f_tagNameInput, 1, 1);
		grid.add(l_tagColor, 0, 2);
		grid.add(c_tagColorPicker, 1, 2);
		grid.setPadding(Insets.EMPTY);
		grid.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
		
		HBox buttonBox = new HBox(10);
		
		Button b_cancel = new Button("取消");
		b_cancel.setOnAction(e -> {
			this.close();
		});
		
		Button b_confirm = new Button("新增");
		b_confirm.setOnAction(e -> {
			this.close();
			onConfirm.handle(e);
		});
		
		b_cancel.setPrefWidth(100); b_confirm.setPrefWidth(100);
		buttonBox.getChildren().addAll(b_cancel, b_confirm);
		
		root.getChildren().addAll(grid, buttonBox);
		
		AnchorPane.setTopAnchor(grid, 10d);
		AnchorPane.setLeftAnchor(grid, 20d);
		AnchorPane.setRightAnchor(grid, 20d);
		AnchorPane.setBottomAnchor(grid, 55d);
		
		AnchorPane.setLeftAnchor(buttonBox, 20d);
		AnchorPane.setRightAnchor(buttonBox, 20d);
		AnchorPane.setBottomAnchor(buttonBox, 0d);
		
		return new Scene(root);
	}
}
