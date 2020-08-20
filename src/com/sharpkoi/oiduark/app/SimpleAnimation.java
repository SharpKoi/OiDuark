package com.sharpkoi.oiduark.app;

import java.util.HashMap;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.util.Duration;

public class SimpleAnimation {
	
	public static HashMap<Node, Animation> nodeAnim = new HashMap<>();
	
	public static void marquee(Label text, ScrollPane parent, double speed) {
		TranslateTransition anim = new TranslateTransition();
		anim.setNode(text);
		anim.setInterpolator(Interpolator.LINEAR);
		anim.setAutoReverse(true); 
		anim.setCycleCount(Animation.INDEFINITE);
		
		double textHead = text.getLayoutX();
		double textTail = text.getWidth();
		double windowWidth = parent.getPrefWidth();
		
		anim.setToX(textHead + windowWidth - textTail);
	    anim.setFromX(textHead);

	    double distance = textTail - windowWidth;
	    anim.setDuration(Duration.seconds(distance / speed));
	    
	    anim.setOnFinished(e -> {
	    	text.setTranslateX(0);
	    });
	    
	    nodeAnim.put(text, anim);
		
		anim.playFromStart();
	}
	
	public static void stopAnim(Node node) {
		if(nodeAnim.containsKey(node)) {
			nodeAnim.get(node).stop();
		}
	}
}
