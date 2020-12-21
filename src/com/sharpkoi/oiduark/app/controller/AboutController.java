package com.sharpkoi.oiduark.app.controller;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.web.WebView;

public class AboutController extends AppController {
	
	@FXML
	protected WebView webView;
	
	public AboutController() {
		pageName = "About";
	}
	
	@Override
	public void loadPageInfo() {
		layout.setTop(titleBar);
		layout.setLeft(nav);
		File f = new File("static/about.html");
		webView.getEngine().load("file:///" + f.getAbsolutePath());
	}
}
