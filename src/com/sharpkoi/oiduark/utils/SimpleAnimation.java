package com.sharpkoi.oiduark.utils;

import java.util.HashMap;

import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.util.Duration;

public class SimpleAnimation {
	
	public static HashMap<Node, Animation> nodeAnim = new HashMap<>();
	
	public static void windowIconify(Node windowRoot, EventHandler<ActionEvent> onFinish) {
		KeyFrame originKF = new KeyFrame(Duration.ZERO, 
				new KeyValue(windowRoot.scaleXProperty(), windowRoot.getScaleX()),
				new KeyValue(windowRoot.scaleYProperty(), windowRoot.getScaleY()), 
				new KeyValue(windowRoot.opacityProperty(), 1));
		KeyFrame finalKF = new KeyFrame(Duration.millis(300),
				new KeyValue(windowRoot.scaleXProperty(), 0.4),
				new KeyValue(windowRoot.scaleYProperty(), 0.4),
				new KeyValue(windowRoot.opacityProperty(), 0));
		
		Timeline tl = new Timeline(originKF, finalKF);
		tl.setOnFinished(onFinish);
		tl.playFromStart();
		nodeAnim.put(windowRoot, tl);
	}
	
	public static void marquee(ScrollPane textContainer, double speed) {
		Label label = (Label) textContainer.getContent();
		
		double time = (label.getWidth() - textContainer.getWidth()) / speed;
		Duration d = Duration.seconds(time);
		
		KeyFrame textHeadKF = new KeyFrame(Duration.ZERO, 
				new KeyValue(textContainer.hvalueProperty(), textContainer.getHmin()));
		KeyFrame textTailKF = new KeyFrame(d, 
				new KeyValue(textContainer.hvalueProperty(), textContainer.getHmax()));
		KeyFrame textWaitKF = new KeyFrame(d.add(Duration.seconds(1.6)), 
				new KeyValue(textContainer.hvalueProperty(), textContainer.getHmax()));
		
		Timeline timeline = new Timeline(textHeadKF, textTailKF, textWaitKF);
		//this indefinite loop will cost memory being increasing.
		timeline.setCycleCount(Timeline.INDEFINITE);
		
		timeline.playFromStart();
		nodeAnim.put(textContainer, timeline);
	}
	
	public static void popup(Node node) {
		KeyFrame size0Kf = new KeyFrame(Duration.ZERO, 
				new KeyValue(node.scaleXProperty(), 0.7), 
				new KeyValue(node.scaleYProperty(), 0.7));
		
		KeyFrame size1Kf = new KeyFrame(Duration.millis(100), 
				new KeyValue(node.scaleXProperty(), 1.1), 
				new KeyValue(node.scaleYProperty(), 1.1));
		
		KeyFrame finalSizeKf = new KeyFrame(Duration.millis(250), 
				new KeyValue(node.scaleXProperty(), 1), 
				new KeyValue(node.scaleYProperty(), 1));
		
		Timeline timeline = new Timeline(size0Kf, size1Kf, finalSizeKf);
		timeline.playFromStart();
		nodeAnim.put(node, timeline);
		
		timeline.setOnFinished(e -> {
			nodeAnim.remove(node);
		});
	}
	
	public static void slideOut(Node node, Region container, EventHandler<ActionEvent> onFinish) {
		KeyFrame pos0Kf = new KeyFrame(Duration.ZERO, 
				new KeyValue(node.translateXProperty(), 0));
		
		KeyFrame pos1Kf = new KeyFrame(Duration.millis(200), 
				new KeyValue(node.translateXProperty(), -10));
		
		KeyFrame posOutKf = new KeyFrame(Duration.millis(450),  
				new KeyValue(node.translateXProperty(), container.getLayoutBounds().getMaxX() + 100));
		
		Timeline timeline = new Timeline(pos0Kf, pos1Kf, posOutKf);
		timeline.playFromStart();
		nodeAnim.put(node, timeline);
		
		timeline.setOnFinished(e -> {
			onFinish.handle(e);
			nodeAnim.remove(node);
		});
	}
	
	public static boolean isNodeAnimating(Node node) {
		if(nodeAnim.containsKey(node)) {
			return nodeAnim.get(node).getStatus().equals(Status.RUNNING);
		}
		
		return false;
	}
	
	public static void stopAnim(Node node) {
		if(nodeAnim.containsKey(node)) {
			nodeAnim.get(node).stop();
		}
	}
}
