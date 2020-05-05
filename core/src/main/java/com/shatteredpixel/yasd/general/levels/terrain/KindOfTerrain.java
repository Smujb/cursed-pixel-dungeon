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

package com.shatteredpixel.yasd.general.levels.terrain;

import com.watabou.noosa.Group;

public interface KindOfTerrain {
	//Don't extend this directly for custom tiles, extend CustomTerrain
	boolean passable();

	boolean losBlocking();

	boolean flammable();

	boolean secret();

	boolean solid();

	boolean avoid();

	boolean liquid();

	boolean pit();

	boolean explodable();

	boolean waterStitchable();

	Group getVisual(int pos, int x, int y);

	KindOfTerrain discover();

	void press(int cell, boolean hard);
}
