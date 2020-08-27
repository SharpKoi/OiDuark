package com.sharpkoi.oiduark.app.event;

import java.util.EventListener;

public interface NewTagEventListener extends EventListener {
	void onNewTagCreate(NewTagEvent e);
}
