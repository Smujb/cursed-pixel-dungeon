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

import com.shatteredpixel.yasd.general.levels.Level;

import org.jetbrains.annotations.NotNull;

public abstract class InteractiveCell extends InteractiveArea {

	public InteractiveCell setPos( Level level, int pos) {
		int[] position = level.getXY(pos);
		x = position[0];
		y = position[1];
		return (InteractiveCell) this.setPos(x, y, 1, 1);
	}

	@Override
	public InteractiveArea setPos(int x, int y, int width, int height) {
		return super.setPos(x, y, 1, 1);
	}

	@Override
	public int centerCell(Level level) {
		return level.getPos(x, y);
	}
}
