package com.sharpkoi.oiduark.app;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	
	public static final String PLAY_IMAGE_PATH = "resources/images/icons/play_64px.png";
	public static final String PAUSE_IMAGE_PATH = "resources/images/icons/pause_100px.png";
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			primaryStage.setTitle("OiDuark");
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.setResizable(true);
			primaryStage.setScene(scene);
			((HomeController) loader.getController()).initWindow();
			
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
