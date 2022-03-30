package com.sharpkoi.oiduark.manager;

import java.util.HashMap;

import javafx.scene.Node;

public class ComponentManager {

	private HashMap<String, Node> registeredComponentPool;
	
	public ComponentManager() {
		this.registeredComponentPool = new HashMap<>();
	}
	
	public void registerComponent(String name, Node component) {
		registeredComponentPool.put(name, component);
	}
	
	public void unregisterComponent(String name) {
		registeredComponentPool.remove(name);
	}
	
	public Node getComponent(String name) {
		return registeredComponentPool.get(name);
	}
}
