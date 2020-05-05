/*
 *
 *   Pixel Dungeon
 *   Copyright (C) 2012-2015 Oleg Dolya
 *
 *   Shattered Pixel Dungeon
 *   Copyright (C) 2014-2019 Evan Debenham
 *
 *   Yet Another Shattered Dungeon
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

package com.shatteredpixel.yasd.general.levels.interactive;

import com.shatteredpixel.yasd.general.LevelHandler;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.yasd.general.plants.Swiftthistle;
import com.watabou.utils.Bundle;

public class LevelSwitchArea extends InteractiveArea {

	private int depth;
	private String key;
	private String message;
	private int pos;

	public LevelSwitchArea initVars(String key, String message, int pos, int depth) {
		this.depth = depth;
		this.key = key;
		this.pos = pos;
		this.message = message;
		return this;
	}

	@Override
	public void interact(Hero hero) {
		hero.curAction = null;

		Buff buff = hero.buff(TimekeepersHourglass.timeFreeze.class);
		if (buff != null) buff.detach();
		buff = hero.buff(Swiftthistle.TimeBubble.class);
		if (buff != null) buff.detach();
		LevelHandler.move(key, message, LevelHandler.Mode.MOVE, depth, pos);
	}

	private static final String POS = "pos";
	private static final String DEPTH = "depth";
	private static final String KEY = "key";
	private static final String MESSAGE = "message";

	@Override
	public void storeInBundle( Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(MESSAGE, message);
		bundle.put(KEY, key);
		bundle.put(DEPTH, depth);
		bundle.put(POS, pos);
	}

	@Override
	public void restoreFromBundle( Bundle bundle) {
		super.restoreFromBundle(bundle);
		pos = bundle.getInt(POS);
		depth = bundle.getInt(DEPTH);
		key = bundle.getString(KEY);
		message = bundle.getString(MESSAGE);
	}
}
