package com.sharpkoi.oiduark.utils;

import javafx.scene.image.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.InputStream;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ResourceLoaderTest {
    @BeforeEach
    void setUp() {

    }

    @ParameterizedTest
    @ValueSource(strings = {"oiduark-icon.png"})
    void testLoadIcon(String filename) {
        String filepath = Paths.get("images/icons", filename).toString();
        InputStream in = getClass().getClassLoader().getResourceAsStream(filepath);
        assertNotNull(in);

        Image img = new Image(in);
    }
}