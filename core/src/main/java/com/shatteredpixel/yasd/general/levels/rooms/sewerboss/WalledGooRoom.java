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

package com.shatteredpixel.yasd.general.levels.rooms.sewerboss;

import com.shatteredpixel.yasd.general.actors.mobs.Goo;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.levels.Level;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.levels.painters.Painter;
import com.watabou.utils.Point;

public class WalledGooRoom extends GooBossRoom {
	
	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1 , Terrain.EMPTY_SP );
		Painter.fill( level, this, 2 , Terrain.EMPTY );
		
		int pillarW = (width()-2)/3;
		int pillarH = (height()-2)/3;
		
		Painter.fill(level, left+2, top+2, pillarW, 1, Terrain.WALL);
		Painter.fill(level, left+2, top+2, 1, pillarH, Terrain.WALL);
		
		Painter.fill(level, left+2, bottom-2, pillarW, 1, Terrain.WALL);
		Painter.fill(level, left+2, bottom-1-pillarH, 1, pillarH, Terrain.WALL);
		
		Painter.fill(level, right-1-pillarW, top+2, pillarW, 1, Terrain.WALL);
		Painter.fill(level, right-2, top+2, 1, pillarH, Terrain.WALL);
		
		Painter.fill(level, right-1-pillarW, bottom-2, pillarW, 1, Terrain.WALL);
		Painter.fill(level, right-2, bottom-1-pillarH, 1, pillarH, Terrain.WALL);
		
		for (Door door : connected.values()) {
			door.set(Door.Type.REGULAR);
		}
		
		Painter.fill( level, left + width()/2 - 1, top + height()/2 - 2, 2 + width()%2, 4 + height()%2, Terrain.WATER);
		Painter.fill( level, left + width()/2 - 2, top + height()/2 - 1, 4 + width()%2, 2 + height()%2, Terrain.WATER);
		
		setupGooNest(level);

		Goo boss = Mob.create(Goo.class, level);
		boss.pos = level.pointToCell(center());
		level.mobs.add( boss );
	}
	
	@Override
	public boolean canPlaceWater(Point p) {
		return false;
	}
}
