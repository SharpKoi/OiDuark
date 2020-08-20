package com.sharpkoi.oiduark.app.controller;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.sharpkoi.oiduark.app.Main;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
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
	
	/***** root *****/
	@FXML
	protected AnchorPane root;
	
	/***** title bar *****/
	@FXML
	protected AnchorPane titleBar;
	@FXML
	protected ImageView b_close;
	@FXML
	protected ImageView b_maximize;
	@FXML
	protected ImageView b_minimize;
	
	/***** navigation *****/
	@FXML
	protected Button b_home;
	@FXML
	protected Button b_select;
	@FXML
	protected Button b_setting;
	@FXML
	protected Button b_about;
		
	// called once while loading the corresponding page.
	// TODO: Add a pre-loading page to create Scene entity.
	// TODO: Move initWindow() into initialize()
	public void initialize(URL location, ResourceBundle resources) {
		loadPageInfo();
		Main.getInstance().getPageCache().put(currentPageName, root);
		
		b_home.setOnAction(e -> {
			if(!currentPageName.equals("Home")) {
				activateScene("Home");
			}
		});
		
		b_select.setOnAction(e -> {
			if(!currentPageName.equals("AudioPage")) {
				activateScene("AudioPage");
			}
		});
		
		b_setting.setOnAction(e -> {
			if(!currentPageName.equals("Setting")) {
				activateScene("Setting");
			}
		});
		
		b_about.setOnAction(e -> {
			System.out.println("[Hint] About button clicked!");
//			TODO: switch page
		});
	}
	
	public void initScene() {
		Stage stage = (Stage) Main.getInstance().getStage();
		
		Main.getInstance().getStage().getScene().getRoot().requestFocus();
		
		stage.getScene().rootProperty().addListener((observable, oldRoot, newRoot) -> {
			loadPageInfo();
			Main.getInstance().getStage().getScene().getRoot().requestFocus();
		});
		
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
		
		b_close.setOnMouseClicked(e -> {
			//TODO: check there's anything has to be processed
			
			//TODO: save the volume value
			System.exit(1);
		});
		
//		b_maximize.setOnMouseClicked(e -> {
//			Screen screen = Screen.getPrimary();
//			Rectangle2D bounds = screen.getVisualBounds();
//
//			stage.setX(bounds.getMinX());
//			stage.setY(bounds.getMinY());
//			stage.setWidth(bounds.getWidth());
//			stage.setHeight(bounds.getHeight());
//		});
		
		b_minimize.setOnMouseClicked(e -> {
			stage.setIconified(true);
		});
		
		System.out.println("scene initialized");
	}
	
	public void activateScene(String pageName) {
		String fxml = pageName + ".fxml";
		Stage stage = Main.getInstance().getStage();
		HashMap<String, Parent> cache = Main.getInstance().getPageCache();
		if(cache.containsKey(pageName)) {
			stage.getScene().setRoot(cache.get(pageName));
		}else {
			try {
				FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxml));
				Parent root = loader.load();
				GlobalController controller = loader.getController();
				
				if(controller == null) {
					System.out.println("[Warning] Can not find the controller");
				}else {
					controller.initScene();
				}
				
				stage.getScene().setRoot(root);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// called when activate the corresponding page
	protected abstract void loadPageInfo();
}
