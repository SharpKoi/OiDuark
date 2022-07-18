package com.sharpkoi.oiduark.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.sharpkoi.oiduark.OiDuarkApp;
import com.sharpkoi.oiduark.audio.Audio;
import com.sharpkoi.oiduark.utils.OiDuarkUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

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

                // create an empty media data file. Default is audios.json which is pre-defined in app.properties
                File mediaDataFile = OiDuarkApp.getInstance().getUserData().getMediaDataFile();
                if(!mediaDataFile.exists()) {
                    OiDuarkUtils.createEmptyJsonFile(mediaDataFile);
                }

                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                JsonReader reader = new JsonReader(new FileReader(mediaDataFile, StandardCharsets.UTF_8));
                Map<String, Audio.Metadata> audioDataset = gson.fromJson(reader, new TypeToken<Map<String, Audio.Metadata>>() {}.getType());

                for(File f : audioFiles) {
                    if(f.isDirectory()) {
                        loadAudioList(f);
                    }else {
                        String filePath = f.getAbsolutePath();
                        // TODO: replace with `Files.getFileExtension()` in guava.
                        String ex = FilenameUtils.getExtension(filePath);
                        if(ex.equals("mp3") || ex.equals("m4a") || ex.equals("wav") ||
                                ex.equals("ogg") || ex.equals("webm")) {
                            Audio audio = Audio.fromMetadata(audioDataset.getOrDefault(filePath, new Audio.Metadata(filePath)));
                            if(!ol_audioList.contains(audio)) {
                                ol_audioList.add(audio);
                            }
                        }
                    }
                }
                FXCollections.sort(ol_audioList, Comparator.comparing(source -> source.getMetadata().getLastModified()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveAllAudioData() {
        HashMap<String, Audio.Metadata> audioDataMap = new HashMap<>();
        for(Audio audio : ol_audioList) {
            audioDataMap.put(audio.getMetadata().getFilePath(), audio.getMetadata());
        }

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        OiDuarkUtils.saveJson(OiDuarkApp.getInstance().getUserData().getMediaDataFile(), gson.toJsonTree(audioDataMap));
    }
}
