package com.sharpkoi.oiduark.app.component;

import java.util.HashMap;

import javafx.scene.Node;

public class ComponentManager {

	private HashMap<String, Node> registedComponentPool;
	
	public ComponentManager() {
		this.registedComponentPool = new HashMap<>();
	}
	
	public void registComponent(String name, Node component) {
		registedComponentPool.put(name, component);
	}
	
	public void unregistComponent(String name) {
		registedComponentPool.remove(name);
	}
	
	public Node getComponent(String name) {
		return registedComponentPool.get(name);
	}
}
