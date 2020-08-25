package com.sharpkoi.oiduark.app.controller;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.sharpkoi.oiduark.app.Main;
import com.sharpkoi.oiduark.utils.ResizeHelper;
import com.sharpkoi.oiduark.utils.ResizeHelper.ResizeListener;
import com.sharpkoi.oiduark.utils.ResourceLoader;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
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
	protected Button b_close;
	@FXML
	protected Button b_maximize;
	@FXML
	protected Button b_minimize;
	
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
		
		ResizeListener listener = ResizeHelper.addResizeListener(stage);
		
		titleBar.setOnMousePressed(e -> {
			xOffset = e.getSceneX();
			yOffset = e.getSceneY();
		});
		
		titleBar.setOnMouseDragged(e -> {
			stage.setX(e.getScreenX() - xOffset);
			stage.setY(e.getScreenY() - yOffset);
		});
		
		b_close.setOnAction(e -> {
			//TODO: check there's anything has to be processed
			
			//TODO: save the volume value
			Main.getInstance().getUserSetting().save();
			System.exit(1);
		});
		
		b_maximize.setOnAction(e -> {
			if(stage.isMaximized()) {
				stage.setMaximized(false);
				ImageView icon = new ImageView(ResourceLoader.loadIcon("maximize_button_64px.png"));
				icon.setFitWidth(16);
				icon.setFitHeight(16);
				b_maximize.setGraphic(icon);
			}else {
				stage.setMaximized(true);
				ImageView icon = new ImageView(ResourceLoader.loadIcon("restore_down_64px.png"));
				icon.setFitWidth(16);
				icon.setFitHeight(16);
				b_maximize.setGraphic(icon);
			}
//			Screen screen = Screen.getPrimary();
//			Rectangle2D bounds = screen.getVisualBounds();
//
//			stage.setX(bounds.getMinX());
//			stage.setY(bounds.getMinY());
//			stage.setWidth(bounds.getWidth());
//			stage.setHeight(bounds.getHeight());
		});
		
		b_minimize.setOnAction(e -> {
			stage.setIconified(true);
		});
	}
	
	public void activateScene(String pageName) {
		String fxml = pageName + ".fxml";
		Stage stage = Main.getInstance().getStage();
		HashMap<String, Parent> cache = Main.getInstance().getPageCache();
		if(cache.containsKey(pageName)) {
			stage.getScene().setRoot(cache.get(pageName));
		}else {
			try {
				FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/"+fxml));
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
