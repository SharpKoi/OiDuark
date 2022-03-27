package com.sharpkoi.oiduark.audio;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sharpkoi.oiduark.app.Main;
import com.sharpkoi.oiduark.utils.OiDuarkUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.HashMap;

public class AudioManager {
    private ObservableList<Audio> ol_audioList;

    public AudioManager() {
        ol_audioList = FXCollections.observableArrayList();
    }

    public ObservableList<Audio> getAllAudio() {
        return ol_audioList;
    }

    /**
     * Load the audio inside the mediaDir.
     * @param mediaDir the directory contains audio files to load.
     */
    public void loadAudioList(File mediaDir) {
        if(mediaDir.isDirectory()) {
            File[] audioFiles = mediaDir.listFiles();
            try {
                assert audioFiles != null;
                for(File f : audioFiles) {
                    if(f.isDirectory()) {
                        loadAudioList(f);
                    }else {
                        String filepath = f.getPath();
                        String ex = FilenameUtils.getExtension(filepath);
                        if(ex.equals("mp3") || ex.equals("m4a") || ex.equals("wav") ||
                                ex.equals("ogg") || ex.equals("webm")) {
                            Audio audio = Audio.loadFrom(f);
                            if(audio != null) {
                                if(!ol_audioList.contains(audio)) {
                                    ol_audioList.add(audio);
                                }
                            }
                        }
                    }
                }
                FXCollections.sort(ol_audioList, Comparator.comparing(Audio::lastModified));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveAllAudioData() {
        HashMap<String, JsonObject> audioDataMap = new HashMap<>();
        for(Audio a : ol_audioList) {
            audioDataMap.put(a.getFilePath(), OiDuarkUtils.parseAudioToJson(a));
        }

        Gson gson = new Gson();
        OiDuarkUtils.saveJson(Main.getInstance().getUserData().getMediaDataFile(), gson.toJsonTree(audioDataMap));
    }
}
