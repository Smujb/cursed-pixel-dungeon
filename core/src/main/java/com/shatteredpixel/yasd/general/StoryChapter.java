package com.shatteredpixel.yasd.general;

import com.shatteredpixel.yasd.general.messages.Messages;
import com.watabou.utils.Bundle;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public enum StoryChapter {
	FIRST {
		@Override
		public boolean unlocked() {
			return true;
		}

		@Override
		public boolean finished() {
			return true;
		}
	},
	SECOND{
		@Override
		public int extraScale() {
			return Trial.trialsBeaten();
		}
	},
	THIRD;

	public int extraScale() {
		return 0;
	}

	public boolean finished() {
		return false;
	}

	public String displayName() {
		return Messages.get(StoryChapter.class, this.toString());
	}

	@NotNull
	public String toFile() {
		return name().toLowerCase() + "/";
	}

	public boolean unlocked() {
		return CPDSettings.unlockedStoryChapter(this);
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

	public StoryChapter nextChapter() {
		switch (this) {
			case FIRST: default:
				return SECOND;
			case SECOND:
				return THIRD;
			case THIRD:
				return null;
		}
	}

	public static void unlockNext() {
		StoryChapter chapter = CPDSettings.storyChapter().nextChapter();
		if (chapter == null) return;

		CPDSettings.unlockStoryChapter(chapter);
	}

	//Only used in StoryChapter.SECOND
	//Might move later, or use a cleaner method for having alternate depths.
	public enum Trial {
		NONE,
		EARTH,
		FIRE,
		WATER,
		AIR;

		private static Trial curTrial = NONE;

		public String key() {
			return name().toLowerCase();
		}

		public void warpTo() {
			switch (this) {
				case NONE:
					LevelHandler.portSurface();
					return;
				case EARTH:
					LevelHandler.earthTrial();
					return;
				case FIRE:
					LevelHandler.fireTrial();
					return;
				case WATER:
					LevelHandler.waterTrial();
					return;
				case AIR:
					LevelHandler.airTrial();
					return;
			}
		}

		public String displayName() {
			return Messages.get(Trial.class, name());
		}

		public static Trial getCurTrial() {
			if (CPDSettings.storyChapter() == SECOND) {
				return curTrial;
			} else {
				return curTrial = NONE;
			}
		}

		public static int trialsBeaten() {
			return 0;
		}

		public static void setCurTrial(Trial trial) {
			curTrial = trial;
		}

		private static final String TRIAL = "trial";

		public static void storeInBundle(Bundle bundle) {
			bundle.put(TRIAL, curTrial);
		}

		public static void restoreFromBundle(Bundle bundle) {
			if (bundle.contains(TRIAL)) {
				curTrial = bundle.getEnum(TRIAL, Trial.class);
			}
		}
	}
}
