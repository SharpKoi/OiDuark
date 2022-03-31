package com.sharpkoi.oiduark.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.sharpkoi.oiduark.OiDuarkApp;
import com.sharpkoi.oiduark.manager.ComponentManager;
import com.sharpkoi.oiduark.component.Navigation;
import com.sharpkoi.oiduark.component.PlayerControlPanel;
import com.sharpkoi.oiduark.component.PlaylistPanel;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.CacheHint;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;

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
	protected Navigation nav;
	protected PlayerControlPanel playerControlPanel;
	protected PlaylistPanel playlistPanel;
		
	// called once while loading the corresponding page.
	// TODO: Add a pre-loading page to create Scene entity.
	// TODO: Move initWindow() into initialize()
	public void initialize(URL location, ResourceBundle resources) {
		//initialize the components
		ComponentManager cm = OiDuarkApp.getInstance().getComponentManager();
		nav = (Navigation) cm.getComponent(Navigation.class.getSimpleName());
		playerControlPanel = 
				(PlayerControlPanel) cm.getComponent(PlayerControlPanel.class.getSimpleName());
		playlistPanel = (PlaylistPanel) cm.getComponent(PlaylistPanel.class.getSimpleName());

		getRoot().setCache(true);
		getRoot().setCacheHint(CacheHint.SCALE);
		((Region) getRoot()).setCacheShape(true);
		
		onPageLoad();
		
		OiDuarkApp.getInstance().getControllerManager().registerController(pageName, this);
	}
	
	public Parent getRoot() {
		return root;
	}
	
	// called when activate the corresponding page
	public abstract void onPageLoad();
}
