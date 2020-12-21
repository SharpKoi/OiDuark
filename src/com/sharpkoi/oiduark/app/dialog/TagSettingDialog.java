package com.sharpkoi.oiduark.app.dialog;

import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXTextField;
import com.sharpkoi.oiduark.app.Main;
import com.sharpkoi.oiduark.app.component.AudioTagBox;
import com.sharpkoi.oiduark.audio.AudioTag;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TagSettingDialog extends Stage {
	
	private AudioTag tag;
	
	private JFXTextField f_tagNameInput;
	private JFXColorPicker c_tagColorPicker;
	
	private EventHandler<ActionEvent> onConfirm;
	
	public TagSettingDialog() {
		this("");
	}
	
	public TagSettingDialog(String tagName) {
		this(tagName, AudioTag.DEFAULT_COLOR);
	}
	
	public TagSettingDialog(String tagName, Color tagColor) {
		super();
		tag = new AudioTag(tagName, tagColor.toString());
		
		initOwner(Main.getInstance().getStage());
		initStyle(StageStyle.TRANSPARENT);
		initModality(Modality.APPLICATION_MODAL);
		
		setScene(buildScene());
	}
	
	public AudioTag getResult() {
		tag = new AudioTag(f_tagNameInput.getText(), c_tagColorPicker.getValue().toString());
		return tag;
	}
	
	public void setOnConfirm(EventHandler<ActionEvent> task) {
		this.onConfirm = task;
	}
	
	protected Scene buildScene() {
		AnchorPane root = new AnchorPane();
		root.getStylesheets().add("com/sharpkoi/oiduark/app/style/tag-setting.css");
		root.getStyleClass().add("root");
		
		DropShadow shadow = new DropShadow(10, Color.BLACK);
		root.setEffect(shadow);
		root.setPrefSize(255, 185);
		
		GridPane grid = new GridPane();
		
		RowConstraints row = new RowConstraints(30);
		grid.getRowConstraints().addAll(row, row, row);
		
		ColumnConstraints col0 = new ColumnConstraints(60);
		ColumnConstraints col1 = new ColumnConstraints(150);
		grid.getColumnConstraints().addAll(col0, col1);
		
		Label l_tagName = new Label("標籤名稱");
		l_tagName.setTextFill(Paint.valueOf("white"));
		
		f_tagNameInput = new JFXTextField(tag.getName());
		f_tagNameInput.setUnFocusColor(Paint.valueOf("white"));
		f_tagNameInput.setFocusColor(Paint.valueOf("#5976cc"));
		f_tagNameInput.textProperty().addListener((o, oldText, newText) -> {
			tag.setName(newText);
		});
		
		Label l_tagColor = new Label("標籤顏色");
		l_tagColor.setTextFill(Paint.valueOf("white"));
		
		c_tagColorPicker = new JFXColorPicker();
		c_tagColorPicker.setValue(tag.getColor());
		
		AudioTagBox tagBox = new AudioTagBox(tag);
		tagBox.tagNameProperty().bind(f_tagNameInput.textProperty());
		tagBox.tagColorProperty().bind(c_tagColorPicker.valueProperty());
		
		grid.add(tagBox, 0, 0);
		grid.add(l_tagName, 0, 1);
		grid.add(f_tagNameInput, 1, 1);
		grid.add(l_tagColor, 0, 2);
		grid.add(c_tagColorPicker, 1, 2);
		grid.setPadding(Insets.EMPTY);
		grid.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
		
		HBox buttonBox = new HBox(10);
		
		Button b_cancel = new Button("取消");
		b_cancel.getStyleClass().add("cancel-button");
		b_cancel.setOnAction(e -> {
			this.close();
		});
		
		Button b_confirm = new Button("確認");
		b_confirm.getStyleClass().add("confirm-button");
		b_confirm.setOnAction(e -> {
			if(f_tagNameInput.getText().equals("")) {
				f_tagNameInput.setPromptText("*該欄位不可為空");
				return;
			}
			this.close();
			onConfirm.handle(e);
		});
		
		b_cancel.setPrefWidth(100); b_confirm.setPrefWidth(100);
		buttonBox.setPrefHeight(55);
		buttonBox.setAlignment(Pos.CENTER_LEFT);
		buttonBox.getChildren().addAll(b_cancel, b_confirm);
		
		root.getChildren().addAll(grid, buttonBox);
		
		AnchorPane.setTopAnchor(grid, 15d);
		AnchorPane.setLeftAnchor(grid, 25d);
		AnchorPane.setRightAnchor(grid, 25d);
		AnchorPane.setBottomAnchor(grid, 60d);
		
		AnchorPane.setLeftAnchor(buttonBox, 25d);
		AnchorPane.setRightAnchor(buttonBox, 25d);
		AnchorPane.setBottomAnchor(buttonBox, 5d);
		
		Scene scene = new Scene(root);
		scene.setFill(Color.TRANSPARENT);	// To display the drop shadow, this option should be activated
		return scene;
	}
}
