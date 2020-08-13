package com.sharpkoi.oiduark.app.controller;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingController extends GlobalController {
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
	}

	@Override
	protected void loadPageInfo() {
		currentPageName = "Setting";
		b_setting.setStyle("-fx-background-color:  #7b2cbf ;");
	}
}
