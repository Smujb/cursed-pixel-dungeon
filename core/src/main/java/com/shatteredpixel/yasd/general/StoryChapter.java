package com.shatteredpixel.yasd.general;

import com.shatteredpixel.yasd.general.messages.Messages;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public enum StoryChapter {
	FIRST {
		@Override
		public boolean unlocked() {
			return true;
		}
	},
	SECOND,
	THIRD;

	public String displayName() {
		return Messages.get(StoryChapter.class, this.toString());
	}

	@NotNull
	public String toFile() {
		return name().toLowerCase() + "/";
	}

	public boolean unlocked() {
		return true;
	}

	public static String[] strValues() {
		ArrayList<String> values = new ArrayList<>();
		for (StoryChapter chapter : values()) {
			if (chapter.unlocked()) {
				values.add(chapter.displayName());
			}
		}
		return values.toArray(new String[0]);
	}
}
