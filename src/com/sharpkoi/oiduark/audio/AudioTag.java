package com.sharpkoi.oiduark.audio;

public class AudioTag {
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

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}
