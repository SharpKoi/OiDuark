package com.sharpkoi.oiduark.app.controller;

import java.util.HashMap;

public class ControllerManager {
	
	private HashMap<String, AppController> controllerMap;
	
	public ControllerManager() {
		controllerMap = new HashMap<>();
	}
	
	public AppController getController(String key) {
		return controllerMap.get(key);
	}
	
	public void registController(String key, AppController controller) {
		controllerMap.put(key, controller);
	}
	
	public boolean containsKey(String key) {
		return controllerMap.containsKey(key);
	}
}
