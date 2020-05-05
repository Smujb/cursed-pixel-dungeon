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

import com.shatteredpixel.yasd.general.levels.Level;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Rect;

//tunnels along the rooms center, with straight lines
public class TunnelRoom extends ConnectionRoom {

	public void paint(Level level) {
		TunnelRoom.paint(this, level,  level.tunnelTile());
	}

	public static ConnectionRoom paint(ConnectionRoom room, Level level, Terrain floor) {
		Rect c = room.getConnectionSpace();

		for (Door door : room.connected.values()) {

			Point start;
			Point mid;
			Point end;

			start = new Point(door);
			if (start.x == room.left)        start.x++;
			else if (start.y == room.top)    start.y++;
			else if (start.x == room.right)  start.x--;
			else if (start.y == room.bottom) start.y--;

			int rightShift;
			int downShift;

			if (start.x < c.left)           rightShift = c.left - start.x;
			else if (start.x > c.right)     rightShift = c.right - start.x;
			else                            rightShift = 0;

			if (start.y < c.top)            downShift = c.top - start.y;
			else if (start.y > c.bottom)    downShift = c.bottom - start.y;
			else                            downShift = 0;

			//always goes inward first
			if (door.x == room.left || door.x == room.right){
				mid = new Point(start.x + rightShift, start.y);
				end = new Point(mid.x, mid.y + downShift);

			} else {
				mid = new Point(start.x, start.y + downShift);
				end = new Point(mid.x + rightShift, mid.y);

			}

			Painter.drawLine( level, start, mid, floor );
			Painter.drawLine( level, mid, end, floor );
		}

		for (Door door : room.connected.values()) {
			door.set( Door.Type.TUNNEL );
		}
		return room;
	}
}
