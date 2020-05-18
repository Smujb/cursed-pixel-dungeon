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

package com.shatteredpixel.yasd.general.levels.tiled;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.levels.chapters.sewers.SewerLevel;
import com.watabou.noosa.Group;

public class TilemapTest2 extends TiledMapLevel {
	private static final String MAP_NAME = "maps/sewer_dungeon.tmx";

	public TilemapTest2() {
		super();
	}

	@Override
	public String tilesTex() {
		return Assets.TILES_SEWERS;
	}

	@Override
	public String waterTex() {
		return Assets.WATER_SEWERS;
	}

	@Override
	public String loadImg() {
		return Assets.LOADING_SEWERS;
	}

	@Override
	protected String mapName() {
		return MAP_NAME;
	}

	@Override
	public Group addVisuals() {
		Group group = super.addVisuals();
		for (int i = 0; i < length(); i++) {
			if (findArea(i) != null) {
				group.add( new SewerLevel.Sink( i ) );
			}
		}
		return group;
	}
}
