package com.sharpkoi.oiduark.audio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.sharpkoi.oiduark.app.Main;
import com.sharpkoi.oiduark.event.NewTagEvent;
import com.sharpkoi.oiduark.event.NewTagEventListener;
import com.sharpkoi.oiduark.utils.OiDuarkUtils;


/**
 * The audio tags manager.
 * Provides tag data and methods to modify tag data.
 */
public class AudioTagManager {
	
	private List<AudioTag> allTags = new ArrayList<>();
	private List<NewTagEventListener> newTagListeners;
	
	public AudioTagManager() {
		loadTags();
		newTagListeners = new ArrayList<>();
	}
	
	//return a copied tag list
	public List<AudioTag> getAllTags() {
		return new ArrayList<AudioTag>(allTags);
	}
	
	public int getTagCount() {
		return allTags.size();
	}
	
	public AudioTag getAudioTag(int tagID) {
		return allTags.get(tagID);
	}
	
	public AudioTag renameTag(int tagID, String newName) {
		AudioTag tag = allTags.get(tagID);
		tag.setName(newName);
		return tag;
	}
	
	public AudioTag setTagColor(int tagID, String colorCode) {
		AudioTag tag = allTags.get(tagID);
		tag.setColor(colorCode);
		return tag;
	}
	
	public void createNewTag(AudioTag tag) {
		allTags.add(tag);
		newTagListeners.forEach(l -> l.onNewTagCreate(new NewTagEvent(this, tag)));
	}
	
	public void addListener(NewTagEventListener listener) {
		newTagListeners.add(listener);
	}
	
	public void loadTags() {
		try {
			File tagDataFile = Main.getInstance().getUserData().getTagDataFile();
			if(!tagDataFile.exists()) {
				OiDuarkUtils.saveJson(tagDataFile, new JsonArray());
			}
			
			InputStream in = new FileInputStream(tagDataFile);
			InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
			
			AudioTag[] tagArray = new Gson().fromJson(reader, AudioTag[].class);
			allTags = new ArrayList<>(Arrays.asList(tagArray));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveAllTags() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		OiDuarkUtils.saveJson(Main.getInstance().getUserData().getTagDataFile(),
				gson.toJsonTree(allTags, new TypeToken<List<AudioTag>>() {}.getType()));
	}
}
