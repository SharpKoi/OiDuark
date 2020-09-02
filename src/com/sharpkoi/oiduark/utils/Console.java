package com.sharpkoi.oiduark.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Console {
	
	private static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static void info(String msg) {
		
		System.out.println(TIME_FORMATTER.format(Calendar.getInstance().getTime()) + "[Info] " + msg);
	}
	
	public static void warning(String msg) {
		System.out.println(TIME_FORMATTER.format(Calendar.getInstance().getTime()) + "[Warning] " + msg);
	}
	
	public static void error(String msg) {
		System.out.println(TIME_FORMATTER.format(Calendar.getInstance().getTime()) + "[Error] " + msg);
	}
	
	public static void debug(String msg) {
		System.out.println(TIME_FORMATTER.format(Calendar.getInstance().getTime()) + "[Debug] " + msg);
	}
	
	public static void info(Number msg) {
		System.out.println(TIME_FORMATTER.format(Calendar.getInstance().getTime()) + "[Info] " + msg);
	}
	
	public static void warning(Number msg) {
		System.out.println(TIME_FORMATTER.format(Calendar.getInstance().getTime()) + "[Warning] " + msg);
	}
	
	public static void error(Number msg) {
		System.out.println(TIME_FORMATTER.format(Calendar.getInstance().getTime()) + "[Error] " + msg);
	}
	
	public static void debug(Number msg) {
		System.out.println(TIME_FORMATTER.format(Calendar.getInstance().getTime()) + "[Debug] " + msg);
	} 
}
