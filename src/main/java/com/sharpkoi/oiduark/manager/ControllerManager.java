package com.sharpkoi.oiduark.manager;

import com.sharpkoi.oiduark.controller.AppController;

import java.util.HashMap;

public class ControllerManager {
	
	private HashMap<String, AppController> controllerMap;
	
	public ControllerManager() {
		controllerMap = new HashMap<>();
	}
	
	public AppController getController(String key) {
		return controllerMap.get(key);
	}
	
	public void registerController(String key, AppController controller) {
		controllerMap.put(key, controller);
	}
	
	public boolean containsKey(String key) {
		return controllerMap.containsKey(key);
	}
}
