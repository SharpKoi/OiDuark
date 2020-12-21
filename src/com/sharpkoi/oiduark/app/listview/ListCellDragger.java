package com.sharpkoi.oiduark.app.listview;

import javafx.scene.control.ListView;
import javafx.scene.input.MouseDragEvent;

public class ListCellDragger {
	
	private ListView<?> listView;
	
	private int draggingIndex;
	
	public ListCellDragger(ListView<?> listView) {
		this.listView = listView;
	}
	
	public void drag(int index) {
		this.draggingIndex = index;
	}
	
	public void drop(MouseDragEvent event) {
		
	}
}
