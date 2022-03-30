package com.sharpkoi.oiduark.utils;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.sharpkoi.oiduark.OiDuarkApp;
import javafx.scene.image.Image;
import javafx.scene.text.Font;

public class ResourceLoader {
	private Class<?> mainClass;
	private Properties props;

	public ResourceLoader(Class<?> mainClass, Properties  props) {
		this.mainClass = mainClass;
		this.props = props;
	}

	public URL getResourceURL(String path) {
		return mainClass.getResource(path);
	}
	
	public InputStream loadResource(String path) {
//		InputStream stream = classLoader.getResourceAsStream(path);
		InputStream stream = mainClass.getResourceAsStream(path);
		if(stream == null) {
			throw new IllegalArgumentException("resource not found: " + path);
		}else
			return stream;
	}
	
	public List<Image> loadDefaultCovers() {
		List<Image> covers = new ArrayList<>();
		try {
			BufferedReader resourcesIndex = new BufferedReader(new InputStreamReader(loadResource("/resources.index")));
			String imagePath;
			while((imagePath = resourcesIndex.readLine()) != null) {
				if(imagePath.startsWith("/images/covers/")) {
					InputStream in = mainClass.getResourceAsStream(imagePath);
					assert in != null:  "Cannot find cover: " + imagePath;
					covers.add(new Image(in));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return covers;
	}
	
	public Image loadIcon(String filename) {
		try(InputStream in = loadResource(Paths.get(props.getProperty("resource.storage.icons"), filename).toString())) {
			return new Image(in);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public Image loadAppIcon() {
		return loadIcon("oiduark-icon.png");
//		return new Image(loadResource("images/icons/oiduark-icon.png"));
	}
	
	public Image loadPlayIcon() {
		return loadIcon("play_64px.png");
	}
	
	public Image loadPauseIcon() {
		return loadIcon("pause_64px.png");
	}
	
	public Image loadLastIcon() {
		return loadIcon("skip_to_start_64px.png");
	}
	
	public Image loadNextIcon() {
		return loadIcon("end_64px.png");
	}
	
	public Font loadFont(String fontName, double size, String ext) {
		try(InputStream in = loadResource(Paths.get(props.getProperty("resource.storage.fonts"), fontName + ext).toString())) {
			return Font.loadFont(in, size);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
