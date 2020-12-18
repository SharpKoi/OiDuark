package com.sharpkoi.oiduark.app.dialog;

import javafx.scene.Scene;

public interface OiDialog<T> {
	
	T getResult();
	
	Scene buildScene();
}
