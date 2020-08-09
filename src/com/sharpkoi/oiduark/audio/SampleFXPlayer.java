package com.sharpkoi.oiduark.audio;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

public class SampleFXPlayer extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Media audio = new Media(getClass().getResource("test/audio02.m4a").toExternalForm());
		MediaPlayer player = new MediaPlayer(audio);

		MediaView view = new MediaView(player);
		
		Group root = new Group();
		Scene scene = new Scene(root, 500, 200);
		
		primaryStage.setTitle("Media Player");
        primaryStage.setScene(scene);
        primaryStage.show();
        
		player.play();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
