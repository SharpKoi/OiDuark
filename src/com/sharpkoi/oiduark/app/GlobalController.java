package com.sharpkoi.oiduark.app;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class GlobalController implements Initializable {
	
	private double xOffset;
	private double yOffset;
	
	@FXML
	private AnchorPane root;
	
	/***** title bar *****/
	@FXML
	private AnchorPane titleBar;
	@FXML
	private ImageView b_close;
	
	/***** navigation *****/
	@FXML
	private JFXButton b_home;
	@FXML
	private JFXButton b_select;
	@FXML
	private JFXButton b_setting;
	@FXML
	private JFXButton b_about;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
	
	public void initWindow() {
		Stage stage = (Stage) root.getScene().getWindow();
		
		titleBar.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			}
		});
		
		titleBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				stage.setX(event.getScreenX() - xOffset);
				stage.setY(event.getScreenY() - yOffset);
			}
		});
	}
	
	public void onCloseButtonClicked() {
		System.out.println("exit");
		System.exit(1);
	}

}
