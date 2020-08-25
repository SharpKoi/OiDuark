package com.sharpkoi.oiduark.utils;

import java.util.HashMap;

import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.util.Duration;

public class SimpleAnimation {
	
	public static HashMap<Node, Animation> nodeAnim = new HashMap<>();
	
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
		timeline.setCycleCount(Timeline.INDEFINITE);
		
		timeline.playFromStart();
		nodeAnim.put(textContainer, timeline);
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
