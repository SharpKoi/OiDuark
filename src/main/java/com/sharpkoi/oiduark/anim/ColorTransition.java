package com.sharpkoi.oiduark.anim;

import com.sharpkoi.oiduark.utils.ColorUtils;
import javafx.animation.Transition;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class ColorTransition extends Transition {
	
	private Region reg;
	private Color to;
	
	public ColorTransition(Region reg, Color to, Duration duration) {
		this.reg = reg;
		this.to = to;
		setCycleDuration(duration);
	}

	@Override
	protected void interpolate(double frac) {
		BackgroundFill bg = reg.getBackground().getFills().get(0);
		Color fromColor = (Color) bg.getFill();
		Color toColor = fromColor.interpolate(to, frac);
		reg.setStyle(String.format("-fx-background-color: %s;", ColorUtils.toHexString(toColor)));
//		reg.setBackground(new Background(new BackgroundFill(toColor, bg.getRadii(), bg.getInsets())));
	}
	
	public Region getTargetNode() {
		return reg;
	}
	
	public Color getToColor() {
		return to;
	}
}
