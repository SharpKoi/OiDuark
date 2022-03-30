package com.sharpkoi.oiduark.event;

import java.io.Serial;
import java.util.EventObject;

import com.sharpkoi.oiduark.audio.AudioTag;

public class NewTagEvent extends EventObject {
	@Serial private static final long serialVersionUID = 1L;
	
	private AudioTag newTag;

	public NewTagEvent(Object source) {
		super(source);
	}
	
	public NewTagEvent(Object source, AudioTag newTag) {
		super(source);
		this.newTag = newTag;
	}
	
	public AudioTag getNewTag() {
		return newTag;
	}
}
