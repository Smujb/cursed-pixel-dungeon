package com.shatteredpixel.yasd.general;

import com.shatteredpixel.yasd.general.messages.Messages;

import org.jetbrains.annotations.NotNull;

public enum StoryChapter {
	//TODO name chapters.
	FIRST,
	SECOND,
	THIRD;

	public String getName() {
		return Messages.get(StoryChapter.class, this.toString());
	}

	@NotNull
	public String toFile() {
		return name().toLowerCase() + "/";
	}
}
