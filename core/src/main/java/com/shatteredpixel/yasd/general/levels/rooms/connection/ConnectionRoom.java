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

package com.shatteredpixel.yasd.general.levels.rooms.connection;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.levels.Level;
import com.shatteredpixel.yasd.general.levels.rooms.Room;
import com.watabou.utils.GameMath;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

public abstract class ConnectionRoom extends Room {
	
	@Override
	public int minWidth() { return 3; }
	public int maxWidth() { return 10; }
	
	@Override
	public int minHeight() { return 3; }
	public int maxHeight() { return 10; }

	//returns the space which all doors must connect to (usually 1 cell, but can be more)
	//Note that, like rooms, this space is inclusive to its right and bottom sides
	protected Rect getConnectionSpace(){
		Point c = getDoorCenter();

		return new Rect(c.x, c.y, c.x, c.y);
	}

	//returns a point equidistant from all doors this room has
	final Point getDoorCenter(){
		PointF doorCenter = new PointF(0, 0);

		for (Door door : connected.values()) {
			doorCenter.x += door.x;
			doorCenter.y += door.y;
		}

		Point c = new Point((int)doorCenter.x / connected.size(), (int)doorCenter.y / connected.size());
		if (Random.Float() < doorCenter.x % 1) c.x++;
		if (Random.Float() < doorCenter.y % 1) c.y++;
		c.x = (int) GameMath.gate(left+1, c.x, right-1);
		c.y = (int)GameMath.gate(top+1, c.y, bottom-1);

		return c;
	}
	
	@Override
	public int minConnections(int direction) {
		if (direction == ALL)   return 2;
		else                    return 0;
	}
	
	@Override
	public boolean canPlaceTrap(Point p) {
		//traps cannot appear in connection rooms on floor 1
		return super.canPlaceTrap(p) && Dungeon.depth > 1;
	}

	public static ConnectionRoom createRoom(Level level, boolean secret) {
		if (secret) {
			return level.randomSecretConnectionRoom();
		} else {
			return level.randomConnectionRoom();
		}
	}
}
