module com.sharpkoi.oiduark {
    requires java.logging;

    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.graphics;
    requires javafx.web;

    requires com.jfoenix;
    requires de.jensd.fx.glyphs.commons;
    requires de.jensd.fx.glyphs.fontawesome;
    requires de.jensd.fx.glyphs.materialdesignicons;
    requires de.jensd.fx.glyphs.materialicons;
    requires de.jensd.fx.glyphs.octicons;
    requires de.jensd.fx.glyphs.weathericons;

    requires static lombok;
    requires org.apache.commons.io;
    requires com.google.gson;
    requires org.burningwave.core;

    opens com.sharpkoi.oiduark.controller to javafx.fxml;
    opens com.sharpkoi.oiduark.user to com.google.gson;
    opens com.sharpkoi.oiduark.audio to com.google.gson;
    opens com.sharpkoi.oiduark.manager to com.google.gson;

    exports com.sharpkoi.oiduark;
}