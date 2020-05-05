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

package com.shatteredpixel.yasd.general.levels.tiled;

import com.shatteredpixel.yasd.general.MapHandler;
import com.shatteredpixel.yasd.general.levels.Level;

public abstract class TiledMapLevel extends Level {
	//No need to bundle, as after create() is called this is no longer needed.
	private String name;

	public TiledMapLevel(String filename) {
		name = filename;
	}

	@Override
	protected boolean build() {
		return MapHandler.build(this, name);
	}

	@Override
	protected void createMobs() {
		MapHandler.createMobs(this, name);
	}

	@Override
	protected void createItems() {
		MapHandler.createItems(this, name);
	}

	@Override
	protected void createAreas() {
		MapHandler.createAreas(this, name);
	}
}
