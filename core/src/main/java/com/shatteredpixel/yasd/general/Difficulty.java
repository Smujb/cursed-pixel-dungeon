/*
 *
 *   Pixel Dungeon
 *   Copyright (C) 2012-2015 Oleg Dolya
 *
 *   Shattered Pixel Dungeon
 *   Copyright (C) 2014-2019 Evan Debenham
 *
 *   Cursed Pixel Dungeon
 *   Copyright (C) 2014-2020 Samuel Braithwaite
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 */

package com.shatteredpixel.yasd.general;

import com.shatteredpixel.yasd.general.messages.Messages;

import org.jetbrains.annotations.Contract;

public enum Difficulty {
	EASY,
	MEDIUM,
	HARD,
	IMPOSSIBLE {
		@Override
		public boolean isUnlocked() {
			return Badges.isUnlocked(Badges.Badge.VICTORY_ALL_CLASSES);
		}
	};

	public boolean isUnlocked() {
		return true;
	}

	public float mobScalingPower() {
		switch (this) {
			case EASY:
				return 1.13f;
			case MEDIUM: default:
				return 1.14f;
			case HARD:
				return 1.15f;
			case IMPOSSIBLE:
				return 1.16f;
		}
	}

	public int flaskChargesPerBoss() {
		switch (this) {
			case EASY:
				return 4;
			case MEDIUM: default:
				return 3;
			case HARD:
				return 2;
			case IMPOSSIBLE:
				return 1;
		}
	}

	@Contract(pure = true)
	public static Difficulty fromInt(int diff) {
		switch (diff) {
			case 1:
				return EASY;
			case 2: default:
				return MEDIUM;
			case 3:
				return HARD;
			case 4:
				return IMPOSSIBLE;
		}
	}

	public String title() {
		return Messages.get(Difficulty.class, name());
	}
}
