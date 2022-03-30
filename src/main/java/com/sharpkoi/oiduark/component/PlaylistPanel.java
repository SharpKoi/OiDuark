package com.sharpkoi.oiduark.component;

import com.sharpkoi.oiduark.OiDuarkApp;
import com.sharpkoi.oiduark.audio.Audio;

import com.sharpkoi.oiduark.listview.PlayListCell;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PlaylistPanel extends AnchorPane {
	
	private ListView<Audio> li_playlist = null;
	
	public PlaylistPanel() {
		FontAwesomeIconView navIcon = new FontAwesomeIconView(FontAwesomeIcon.NAVICON);
		navIcon.setFill(Color.WHITE);
		navIcon.setSize("20px");
		
		Label l_title = new Label("Playlist");
//		Font font = ResourceLoader.loadFont("Uni Sans Thin", 16, ".otf");
		l_title.setFont(Font.font(16));
		l_title.setTextFill(Paint.valueOf("white"));
		l_title.getStyleClass().add("playlist-title");
		l_title.setCache(true);
		l_title.setCacheShape(true);
		l_title.setCacheHint(CacheHint.QUALITY);
		
		HBox listTitleContainer = new HBox(10, navIcon, l_title);
		listTitleContainer.setAlignment(Pos.CENTER_LEFT);
		
		Separator s = new Separator(Orientation.HORIZONTAL);
		s.setFocusTraversable(false);

//		MaterialDesignIconView right_arrow =
//				new MaterialDesignIconView(
//					MaterialDesignIcon.CHEVRON_RIGHT);
//		right_arrow.setSize("30px");
//		right_arrow.setFill(Color.WHITE);
//		Button b_store = new Button();
//		b_store.setGraphic(right_arrow);
//		b_store.getStyleClass().add("storage-button");
//		b_store.setPrefSize(48, 48);
//		b_store.setCursor(Cursor.HAND);
		
		setBackground(new Background(new BackgroundFill(Paint.valueOf("#1e2426"), null, null)));
		getStylesheets().add(OiDuarkApp.class.getResource("styles/playlist.css").toString());
		getStyleClass().add("play-list-pane");
		
//		getChildren().addAll(listTitleContainer, s, playlistView(), b_store);
		getChildren().addAll(listTitleContainer, s, playlistView());
		setPrefSize(180, 520);
		
		AnchorPane.setTopAnchor(listTitleContainer, 10d);
		AnchorPane.setLeftAnchor(listTitleContainer, 10d);
		AnchorPane.setRightAnchor(listTitleContainer, 10d);
		
		AnchorPane.setTopAnchor(s, 40d);
		AnchorPane.setLeftAnchor(s, 10d);
		AnchorPane.setRightAnchor(s, 10d);
		
		AnchorPane.setTopAnchor(li_playlist, 49d);
		AnchorPane.setLeftAnchor(li_playlist, 1d);
		AnchorPane.setRightAnchor(li_playlist, 1d);
		AnchorPane.setBottomAnchor(li_playlist, 0d);

//		AnchorPane.setLeftAnchor(b_store, -24d);
//		AnchorPane.setTopAnchor(b_store, (getPrefHeight() - b_store.getPrefHeight())/2.0);
//		AnchorPane.setBottomAnchor(b_store, (getPrefHeight() - b_store.getPrefHeight())/2.0);
	}
	
	public ListView<Audio> playlistView() {
		if(li_playlist == null) {
			li_playlist = new ListView<>();
			li_playlist.setFixedCellSize(40);
			li_playlist.getStyleClass().add("play-list");
			li_playlist.setCellFactory(list -> {
				return new PlayListCell();
			});
		}
		
		return li_playlist;
	}
	
	public void setListItems(ObservableList<Audio> list) {
		li_playlist.setItems(list);
	}
	
	public void refreshList() {
		li_playlist.refresh();
	}
}
