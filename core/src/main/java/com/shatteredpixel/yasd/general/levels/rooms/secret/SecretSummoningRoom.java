/*
 *
 *  * Pixel Dungeon
 *  * Copyright (C) 2012-2015 Oleg Dolya
 *  *
 *  * Shattered Pixel Dungeon
 *  * Copyright (C) 2014-2019 Evan Debenham
 *  *
 *  * Yet Another Shattered Dungeon
 *  * Copyright (C) 2014-2020 Samuel Braithwaite
 *  *
 *  * This program is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 */

package com.shatteredpixel.yasd.general.levels.rooms.secret;

import com.shatteredpixel.yasd.general.items.Generator;
import com.shatteredpixel.yasd.general.items.Heap;
import com.shatteredpixel.yasd.general.levels.Level;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.levels.painters.Painter;
import com.shatteredpixel.yasd.general.levels.traps.SummoningTrap;
import com.watabou.utils.Point;

public class SecretSummoningRoom extends SecretRoom {
	
	//minimum of 3x3 traps, max of 6x6 traps
	
	@Override
	public int maxWidth() {
		return 8;
	}
	
	@Override
	public int maxHeight() {
		return 8;
	}
	
	@Override
	public void paint(Level level) {
		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.EMPTY);
		
		Point center = center();
		level.drop(Generator.random(), level.pointToCell(center)).setHauntedIfCursed().type = Heap.Type.SKELETON;
		
		for (Point p : getPoints()){
			int cell = level.pointToCell(p);
			if (level.map[cell] == Terrain.EMPTY){
				level.setTrap(new SummoningTrap().hide(), cell);
			}
		}
		
		entrance().set(Door.Type.HIDDEN);
	}
	
}
