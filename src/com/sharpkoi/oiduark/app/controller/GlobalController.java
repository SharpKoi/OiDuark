package com.sharpkoi.oiduark.app.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.sharpkoi.oiduark.app.Main;
import com.sharpkoi.oiduark.audio.Audio;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/****** The global handler ******/
/* @Description: Handle with the actions on title bar and navigation box.
*/
public abstract class GlobalController implements Initializable {
	
	protected double xOffset;
	protected double yOffset;
	
	protected String currentPageName; 
	
	protected ObservableList<Audio> playList;
	
	@FXML
	protected AnchorPane root;
	
	/***** title bar *****/
	@FXML
	protected AnchorPane titleBar;
	@FXML
	protected ImageView b_close;
	
	/***** navigation *****/
	@FXML
	protected JFXButton b_home;
	@FXML
	protected JFXButton b_select;
	@FXML
	protected JFXButton b_setting;
	@FXML
	protected JFXButton b_about;
	
	// Scene is still null during loading the home page. 
	// So it's necessary to initialize window after the scene is created
	// TODO: Add a pre-loading page to create Scene entity.
	// TODO: Move initWindow() into initialize()
	public void initialize(URL location, ResourceBundle resources) {
		b_home.setOnAction(e -> {
			System.out.println("[Hint] Home button clicked!");
//			TODO: switch page
			if(!currentPageName.equals("Home")) {
				activateScene("Home");
			}
		});
		
		b_select.setOnAction(e -> {
			System.out.println("[Hint] Select button clicked!");
//			TODO: switch page
			if(!currentPageName.equals("AudioPage")) {
				activateScene("AudioPage");
			}
		});
		
		b_setting.setOnAction(e -> {
			System.out.println("[Hint] Setting button clicked!");
//			TODO: switch page
			if(!currentPageName.equals("Setting")) {
				activateScene("Setting");
			}
		});
		
		b_about.setOnAction(e -> {
			System.out.println("[Hint] About button clicked!");
//			TODO: switch page
		});
		
		Main.getInstance().getStage().requestFocus();
	}
	
	public void initTitleBar() {
		Stage stage = (Stage) Main.getInstance().getStage();
		
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
		
		System.out.println("initialized");
	}
	
	public void activateScene(String pageName) {
		String fxml = pageName + ".fxml";
		Stage stage = Main.getInstance().getStage();
		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxml));
			Parent root = loader.load();
			GlobalController controller = loader.getController();
			
			if(controller == null) {
				System.out.println("[Warning] Can not find the controller");
			}else {
				controller.initTitleBar();
			}
			
			stage.getScene().setRoot(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void onCloseButtonClicked() {
		System.out.println("exit");
		System.exit(1);
	}

}
