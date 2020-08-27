package com.sharpkoi.oiduark.app.component;

import com.sharpkoi.oiduark.app.Main;
import com.sharpkoi.oiduark.audio.AudioTag;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class AudioTagBox extends HBox {
	
	private AudioTag tag;
	private EventHandler<ActionEvent> onRemove;
	
	public AudioTagBox(AudioTag tag) {
		super();
		this.tag = tag;
		
		Paint color = Paint.valueOf(tag.getColor());
		
		setBackground(new Background(new BackgroundFill(
				Paint.valueOf("transparent"), 
				new CornerRadii(16), 
				Insets.EMPTY)
		));
		setBorder(new Border(new BorderStroke(color, 
										BorderStrokeStyle.SOLID,
										new CornerRadii(16), 
										new BorderWidths(1)))); 
		
		getChildren().add(createDeleteButton(color));
		getChildren().add(createTagNameLabel(color));
		
		setAlignment(Pos.CENTER_LEFT);
		setPadding(new Insets(0, 4, 0, 4));
		setSpacing(2);
		setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
		setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
	}
	
	public AudioTagBox(int id) {
		this(Main.getInstance().getAudioTagManager().getAudioTag(id));
	}
	
	public AudioTag getTag() {
		return tag;
	}
	
	public String getTagName() {
		return tag.getName();
	}
	
	public void setOnRemove(EventHandler<ActionEvent> task) {
		this.onRemove = task;
	}
	
	private Button createDeleteButton(Paint color) {
		Button b = new Button();
		b.getStyleClass().add("del-tag-button");
		b.setCursor(Cursor.HAND);
		MaterialDesignIconView icon = new MaterialDesignIconView(MaterialDesignIcon.CLOSE);
		icon.setFill(color);
		icon.setGlyphSize(10);
		b.setGraphic(icon);
		b.setPrefSize(10, 10);
		b.setOnAction(e -> {
			onRemove.handle(e);
		});
		
		return b;
	}
	
	private Label createTagNameLabel(Paint color) {
		Label l = new Label(tag.getName());
		l.setFont(Font.font(10));
		l.setTextFill(color);
		return l;
	}
}
