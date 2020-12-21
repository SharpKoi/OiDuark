package com.sharpkoi.oiduark.app.component;

import com.sharpkoi.oiduark.app.Main;
import com.sharpkoi.oiduark.audio.AudioTag;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
	
	private StringProperty tagNameProperty;
	private ObjectProperty<Paint> tagColorProperty;
	
	private EventHandler<ActionEvent> onRemove;
	
	public AudioTagBox(AudioTag tag) {
		super();
		this.tag = tag;
		
		tagNameProperty = new SimpleStringProperty(tag.getName());
		tagColorProperty = new SimpleObjectProperty<>(tag.getColor());
		
		setBackground(new Background(new BackgroundFill(
				Paint.valueOf("transparent"), 
				new CornerRadii(16), 
				Insets.EMPTY)
		));
		
		borderProperty().bind(Bindings.createObjectBinding(
						() -> new Border(
								new BorderStroke(tagColorProperty.get(), 
								BorderStrokeStyle.SOLID,
								new CornerRadii(16), 
								new BorderWidths(1))), 
						tagColorProperty));
		
		getChildren().add(createDeleteButton());
		getChildren().add(createTagNameLabel());
		
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
	
	public StringProperty tagNameProperty() {
		return tagNameProperty;
	}
	
	public ObjectProperty<Paint> tagColorProperty() {
		return tagColorProperty;
	}
	
	private Button createDeleteButton() {
		Button b = new Button();
		b.getStyleClass().add("del-tag-button");
		b.setCursor(Cursor.HAND);
		MaterialDesignIconView icon = new MaterialDesignIconView(MaterialDesignIcon.CLOSE);
		icon.fillProperty().bind(tagColorProperty);
		icon.setGlyphSize(10);
		b.setGraphic(icon);
		b.setPrefSize(10, 10);
		b.setOnAction(e -> {
			if(onRemove != null)
				onRemove.handle(e);
		});
		
		return b;
	}
	
	private Label createTagNameLabel() {
		Label l = new Label(tag.getName());
		l.textProperty().bind(tagNameProperty);
		l.textFillProperty().bind(tagColorProperty);
		l.setFont(Font.font(10));
		
		return l;
	}
}
