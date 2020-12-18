package com.sharpkoi.oiduark.app.component;

import com.sharpkoi.oiduark.app.listview.PlayListCell;
import com.sharpkoi.oiduark.audio.Audio;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
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

public class PlaylistPanel extends AnchorPane {
	
	private ListView<Audio> li_playlist = null;
	
	public PlaylistPanel() {
		FontAwesomeIconView navIcon = new FontAwesomeIconView(FontAwesomeIcon.NAVICON);
		navIcon.setFill(Color.WHITE);
		navIcon.setSize("20px");
		
		Label l_title = new Label("¼½©ñ²M³æ");
		l_title.setFont(Font.font(16));
		l_title.setTextFill(Paint.valueOf("white"));
		
		HBox listTitleContainer = new HBox(10, navIcon, l_title);
		listTitleContainer.setAlignment(Pos.CENTER_LEFT);
		
		Separator s = new Separator(Orientation.HORIZONTAL);
		s.setFocusTraversable(false);
		
		setBackground(new Background(new BackgroundFill(Paint.valueOf("#1e2426"), null, null)));
		getStylesheets().add("com/sharpkoi/oiduark/app/style/playlist.css");
		getStyleClass().add("play-list-pane");
		
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
