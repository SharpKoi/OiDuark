package com.sharpkoi.oiduark.dialog;

import javafx.scene.Scene;

public interface OiDialog<T> {
	
	T getResult();
	
	Scene buildScene();
}
