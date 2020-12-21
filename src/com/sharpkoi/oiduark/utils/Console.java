package com.sharpkoi.oiduark.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Logger;

public class Console {
	
	private static Logger logger;
	private static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static void setLogger(Logger logger) {
		Console.logger = logger;
	}
	
	public static Logger getLogger() {
		return logger;
	}
	
	public static void INFO(String msg) {
		if(logger != null) {
			logger.info(msg);
			return;
		}
		System.out.println(TIME_FORMATTER.format(Calendar.getInstance().getTime()) + "[Info] " + msg);
	}
	
	public static void INFO(String pattern, Object...param) {
		String msg = String.format(pattern, param);
		Console.INFO(msg);
	}
	
	public static void WARNING(String msg) {
		if(logger != null) {
			logger.warning(msg);
			return;
		}
		System.out.println(TIME_FORMATTER.format(Calendar.getInstance().getTime()) + "[Warning] " + msg);
	}
	
	public static void WARNING(String pattern, Object... param) {
		String msg = String.format(pattern, param);
		Console.WARNING(msg);
	}
	
	public static void ERROR(String msg) {
		if(logger != null) {
			logger.severe(msg);
			return;
		}
		System.out.println(TIME_FORMATTER.format(Calendar.getInstance().getTime()) + "[Error] " + msg);
	}
	
	public static void ERROR(String pattern, Object... param) {
		String msg = String.format(pattern, param);
		Console.ERROR(msg);
	}
	
	public static void DEBUG(String msg) {
		if(logger != null) {
			logger.config(msg);
			return;
		}
		System.out.println(TIME_FORMATTER.format(Calendar.getInstance().getTime()) + "[Debug] " + msg);
	}
	
	public static void DEBUG(String pattern, Object... param) {
		String msg = String.format(pattern, param);
		Console.DEBUG(msg);
	}
	
	public static void INFO(Number msg) {
		if(logger != null) {
			logger.info(msg.toString());
			return;
		}
		System.out.println(TIME_FORMATTER.format(Calendar.getInstance().getTime()) + "[Info] " + msg);
	}
	
	public static void WARNING(Number msg) {
		if(logger != null) {
			logger.warning(msg.toString());
			return;
		}
		System.out.println(TIME_FORMATTER.format(Calendar.getInstance().getTime()) + "[Warning] " + msg);
	}
	
	public static void ERROR(Number msg) {
		if(logger != null) {
			logger.severe(msg.toString());
			return;
		}
		System.out.println(TIME_FORMATTER.format(Calendar.getInstance().getTime()) + "[Error] " + msg);
	}
	
	public static void DEBUG(Number msg) {
		if(logger != null) {
			logger.config(msg.toString());
			return;
		}
		System.out.println(TIME_FORMATTER.format(Calendar.getInstance().getTime()) + "[Debug] " + msg);
	} 
}
