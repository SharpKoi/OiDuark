package com.sharpkoi.oiduark.app.component;

import com.sharpkoi.oiduark.app.anim.ColorTransition;

import de.jensd.fx.glyphs.GlyphIcon;
import de.jensd.fx.glyphs.GlyphIcons;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import de.jensd.fx.glyphs.octicons.OctIcon;
import de.jensd.fx.glyphs.octicons.OctIconView;
import de.jensd.fx.glyphs.weathericons.WeatherIcon;
import de.jensd.fx.glyphs.weathericons.WeatherIconView;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import javafx.util.Duration;

public class NavigationButton extends HBox {
	
	public final static Color DEFAULT_COLOR = new Color(60/255.0, 67/255.0, 74/255.0, 0);
	public final static Color SELECTED_COLOR = new Color(60/255.0, 67/255.0, 74/255.0, 1);
	
	private double height = 48;
	private double width = USE_COMPUTED_SIZE;
	
	private boolean selected = false;
	
	private Region thinRegion;
	private Button actionButton;
	private EventHandler<ActionEvent> onAction;
	
	public NavigationButton(GlyphIcons icon, String text, double width, double height) {
		this.width = width;
		this.height = height;
		
		actionButton = new Button(text);
		actionButton.setAlignment(Pos.BASELINE_LEFT);
		actionButton.setPadding(new Insets(0, 0, 0, 20));
		actionButton.setPrefSize(this.width - 5, this.height);
		actionButton.setTextFill(Paint.valueOf("white"));
		actionButton.getStyleClass().add("navigation-button");
		
		GlyphIcon<?> iconView = null;
		if(icon instanceof FontAwesomeIcon) {
			iconView = new FontAwesomeIconView((FontAwesomeIcon) icon);
		}else if(icon instanceof MaterialDesignIcon) {
			iconView = new MaterialDesignIconView((MaterialDesignIcon) icon);
		}else if(icon instanceof MaterialIconView) {
			iconView = new MaterialIconView((MaterialIcon) icon);
		}else if(icon instanceof OctIcon) {
			iconView = new OctIconView((OctIcon) icon);
		}else if(icon instanceof WeatherIcon) {
			iconView = new WeatherIconView((WeatherIcon) icon);
		}
		
		iconView.setSize("16px");
		iconView.setFill(Paint.valueOf("white"));
		
		actionButton.setGraphic(iconView);
		actionButton.setGraphicTextGap(20);
		
		actionButton.setOnAction(e -> {
			onAction.handle(e);
		});
		
		setPrefSize(this.width, this.height);
		setBackground(new Background(new BackgroundFill(Paint.valueOf("transparent"), null, null)));
		setOpacity(0.8);
		getStyleClass().add("navigation-item");
		addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
			NavigationButton nb = this;
			ColorTransition trans = new ColorTransition(nb, SELECTED_COLOR, Duration.millis(400));
			if(!this.selected) trans.play();
		});
		
		addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
			NavigationButton nb = this;
			ColorTransition trans = new ColorTransition(nb, DEFAULT_COLOR, Duration.millis(400));
			if(!this.selected) trans.play();
		});
		
		getChildren().addAll(thinRegion(), actionButton);
		
		setCache(true);
		setCacheShape(true);
		setCacheHint(CacheHint.SPEED);
	}
	
	public Region thinRegion() {
		if(thinRegion == null) {
			thinRegion = new Region();
			thinRegion.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
			thinRegion.setPrefSize(5, this.height);
		}
		
		return thinRegion;
	}
	
	public Button actionButton() {
		return actionButton;
	}
	
	public void setOnAction(EventHandler<ActionEvent> handler) {
		this.onAction = handler;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void onSelect() {
		selected = true;
		thinRegion.setBackground(new Background(new BackgroundFill(Paint.valueOf("#ff0055"), null, null)));
		setBackground(new Background(new BackgroundFill(SELECTED_COLOR, null, null)));
		setOpacity(1);
	}
	
	public void onUnselect() {
		selected = false;
		thinRegion.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
		setBackground(new Background(new BackgroundFill(DEFAULT_COLOR, null, null)));
		setOpacity(0.8);
	}
}
