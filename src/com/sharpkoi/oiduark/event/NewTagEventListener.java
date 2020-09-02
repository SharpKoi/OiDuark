package com.sharpkoi.oiduark.event;

import java.util.EventListener;

public interface NewTagEventListener extends EventListener {
	void onNewTagCreate(NewTagEvent e);
}
