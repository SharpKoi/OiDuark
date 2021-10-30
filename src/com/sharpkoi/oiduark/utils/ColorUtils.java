package com.sharpkoi.oiduark.utils;

import javafx.scene.paint.Color;

public class ColorUtils {
    private static String format(double val) {
        String in = Integer.toHexString((int) Math.round(val * 255));
        return in.length() == 1 ? "0" + in : in;
    }

    public static String toHexString(Color value) {
        return "#" +
                (format(value.getRed()) +
                    format(value.getGreen()) +
                    format(value.getBlue()) +
                    format(value.getOpacity())).toUpperCase();
    }
}
