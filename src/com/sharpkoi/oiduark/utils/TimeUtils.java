package com.sharpkoi.oiduark.utils;

public class TimeUtils {
	
	public static String parseSecondsToTime(double secs) {
		int hours = (int) secs / 3600;
		int minutes = (int) (secs % 3600) / 60;
		int seconds = (int) secs % 60;
		
		return hours == 0? 
				String.format("%d:%02d", minutes, seconds) : 
				String.format("%d:%02d:%02d", hours, minutes, seconds);
	}
}
