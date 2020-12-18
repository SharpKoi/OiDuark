package com.sharpkoi.oiduark.app.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.sharpkoi.oiduark.app.Main;
import com.sharpkoi.oiduark.app.component.ComponentManager;
import com.sharpkoi.oiduark.app.component.Navigation;
import com.sharpkoi.oiduark.app.component.PlayerControlPanel;
import com.sharpkoi.oiduark.app.component.PlaylistPanel;
import com.sharpkoi.oiduark.app.component.TitleBar;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/****** The global handler ******/
/* @Description: Handle with the actions on title bar and navigation box.
*/
public abstract class AppController implements Initializable {
	
	protected String pageName; 
	
	/***** root *****/
	@FXML
	protected AnchorPane root;
	
	/***** layout *****/
	@FXML
	protected BorderPane layout;
	
	/***** components *****/
	protected TitleBar titleBar;
	protected Navigation nav;
	protected PlayerControlPanel playerControlPanel;
	protected PlaylistPanel playlistPanel;
		
	// called once while loading the corresponding page.
	// TODO: Add a pre-loading page to create Scene entity.
	// TODO: Move initWindow() into initialize()
	public void initialize(URL location, ResourceBundle resources) {
		//initialize the components
		ComponentManager cm = Main.getInstance().getComponentManager();
		titleBar = (TitleBar) cm.getComponent(TitleBar.class.getSimpleName());
		nav = (Navigation) cm.getComponent(Navigation.class.getSimpleName());
		playerControlPanel = 
				(PlayerControlPanel) cm.getComponent(PlayerControlPanel.class.getSimpleName());
		playlistPanel = (PlaylistPanel) cm.getComponent(PlaylistPanel.class.getSimpleName());
		
		loadPageInfo();
		
		Main.getInstance().getControllerManager().registController(pageName, this);
	}
	
	public Parent getRoot() {
		return root;
	}
	
	// called when activate the corresponding page
	public abstract void loadPageInfo();
}
