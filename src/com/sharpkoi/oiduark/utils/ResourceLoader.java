package com.sharpkoi.oiduark.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

import com.sharpkoi.oiduark.app.Main;
import com.sharpkoi.oiduark.audio.Audio;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.text.Font;

public class ResourceLoader {
	
	public static final String ICON_PATH = "resources/images/icons/";
	public static final String FONT_PATH = "resources/fonts/";
	public static final String DEFAULT_COVER_PATH = "resources/images/covers/";
	
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
	
	public static Image[] loadDefaultCovers() {
		CodeSource src = Main.class.getProtectionDomain().getCodeSource();
		
		try {
			File codeRoot = new File(URLDecoder.decode(src.getLocation().getPath(), StandardCharsets.UTF_8.name()));
			if(codeRoot.isFile()) {
				// running in jar
				List<Image> covers = new ArrayList<>();
				JarFile jar = new JarFile(codeRoot);
				jar.stream().forEach(entry -> {
					String path = entry.getName();
					if(path.startsWith("com/sharpkoi/oiduark/app/" + DEFAULT_COVER_PATH) &&
							!path.equals("com/sharpkoi/oiduark/app/" + DEFAULT_COVER_PATH)) {
						String coverPath = path.replace("com/sharpkoi/oiduark/app/", "");
						Image cover = new Image(Main.class.getResourceAsStream(coverPath));
						covers.add(cover);
					}
				});
				
				jar.close();
				return covers.toArray(new Image[] {});
			}else {
				//running in IDE
				URL url = Main.class.getResource(DEFAULT_COVER_PATH);
				File coverDir = new File(url.getPath());
				String[] coverNames = coverDir.list();
				Image[] covers = new Image[coverNames.length];
				for(int i = 0; i < coverNames.length; i++) {
					covers[i] = new Image(Main.class.getResourceAsStream(Paths.get(DEFAULT_COVER_PATH, coverNames[i]).toString()));
				}
				return covers;
			}
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}
	
	public static Image loadIcon(String name) {
		return new Image(Main.class.getResourceAsStream(ICON_PATH + name));
	}
	
	public static Image loadAppIcon() {
		return new Image(Main.class.getResourceAsStream(ICON_PATH + "oiduark-icon.png"));
	}
	
	public static Image loadPlayIcon() {
		return new Image(Main.class.getResourceAsStream(ICON_PATH + "play_64px.png"));
	}
	
	public static Image loadPauseIcon() {
		return new Image(Main.class.getResourceAsStream(ICON_PATH + "pause_64px.png"));
	}
	
	public static Image loadLastIcon() {
		return new Image(Main.class.getResourceAsStream(ICON_PATH + "skip_to_start_64px.png"));
	}
	
	public static Image loadNextIcon() {
		return new Image(Main.class.getResourceAsStream(ICON_PATH + "end_64px.png"));
	}
	
	public static Font loadFont(String fontName, double size, String fontSuffix) {
		return Font.loadFont(Main.class.getResourceAsStream(FONT_PATH + fontName + fontSuffix), size);
	}
	
	public static Media loadMedia(Audio audio) {
		Path audioPath = Paths.get(audio.getFilePath());
		Media media = null;
		try {
			media = new Media(audioPath.toUri().toURL().toExternalForm());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return media;
	}
}
