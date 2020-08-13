package com.sharpkoi.oiduark.utils;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.sharpkoi.oiduark.app.Main;

import javafx.scene.image.Image;
import javafx.scene.media.Media;

public class ResourceLoader {
	
	public static final String ICON_PATH = "resources/images/icons/";
	
	private String resourceDir;
	
	public ResourceLoader(String path) {
		this.resourceDir = path;
	}
	
	public String getResourceDir() {
		return resourceDir;
	}
	
	public URL getResourceURL(String fileName) {
		String filePath = Paths.get(resourceDir, fileName).toString();
		return Main.class.getResource(filePath);
	}
	
	public InputStream loadResource(String fileName) {
		String filePath = Paths.get(resourceDir, fileName).toString();
		return Main.class.getResourceAsStream(filePath);
	}
	
	public static Image loadPlayIcon() {
		String path = Paths.get(ICON_PATH, "play_64px.png").toString();
		return new Image(Main.class.getResourceAsStream(path));
	}
	
	public static Image loadPauseIcon() {
		String path = Paths.get(ICON_PATH, "pause_64px.png").toString();
		return new Image(Main.class.getResourceAsStream(path));
	}
	
	public static Media loadMedia(String fileName) {
		Path audioPath = Paths.get(Main.getInstance().getMediaDir(), fileName);
		Media media = null;
		try {
			media = new Media(audioPath.toUri().toURL().toExternalForm());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return media;
	}
}
