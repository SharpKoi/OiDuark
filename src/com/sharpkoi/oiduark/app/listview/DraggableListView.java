package com.sharpkoi.oiduark.app.listview;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class DraggableListView<T> extends ListView<T> {
	
	private ListCellDragger dragger;
	
	public DraggableListView() {
		super();
		dragger = new ListCellDragger(this);
	}
	
	public DraggableListView(ObservableList<T> items) {
		super(items);
		dragger = new ListCellDragger(this);
	}
}
