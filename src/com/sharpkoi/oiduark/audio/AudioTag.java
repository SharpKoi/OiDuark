package com.sharpkoi.oiduark.audio;

import java.util.LinkedHashMap;
import java.util.Map;

import javafx.scene.paint.Color;

public class AudioTag {
	
	public static final Color DEFAULT_COLOR = Color.valueOf("#d8bbff");
	
	private String name;
	private String color;
	
	public AudioTag(String name) {
		this(name, "#d8bbff");
	}
	
	public AudioTag(String name, String color) {
		this.name = name;
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Color getColor() {
		return Color.valueOf(color);
	}
	
	public void setColor(String colorCode) {
		this.color = colorCode;
	}

	public Map<String, String> getMetaData() {
		Map<String, String> meta = new LinkedHashMap<>(2);
		meta.put("name", this.name);
		meta.put("colorCode", this.color.toString());
		
		return meta;
	}
}
