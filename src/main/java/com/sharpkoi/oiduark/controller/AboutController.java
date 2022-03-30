package com.sharpkoi.oiduark.controller;

import java.io.File;

import com.sharpkoi.oiduark.OiDuarkApp;
import javafx.fxml.FXML;
import javafx.scene.web.WebView;

public class AboutController extends AppController {
	
	@FXML
	protected WebView webView;
	
	public AboutController() {
		pageName = "About";
	}
	
	@Override
	public void onPageLoad() {
		layout.setLeft(nav);
		webView.getEngine().load(OiDuarkApp.getInstance().getResourceLoader().getResourceURL("/webview/about.html").toString());
	}
}
