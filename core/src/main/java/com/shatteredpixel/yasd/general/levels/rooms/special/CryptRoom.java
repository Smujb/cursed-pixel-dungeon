/*
 *
 *  * Pixel Dungeon
 *  * Copyright (C) 2012-2015 Oleg Dolya
 *  *
 *  * Shattered Pixel Dungeon
 *  * Copyright (C) 2014-2019 Evan Debenham
 *  *
 *  * Cursed Pixel Dungeon
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

package com.shatteredpixel.yasd.general.levels.rooms.special;

import com.shatteredpixel.yasd.general.items.Generator;
import com.shatteredpixel.yasd.general.items.Gold;
import com.shatteredpixel.yasd.general.items.Heap;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.shield.Shield;
import com.shatteredpixel.yasd.general.levels.Level;
import com.shatteredpixel.yasd.general.levels.painters.Painter;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.watabou.utils.Point;

public class CryptRoom extends SpecialRoom {

	public void paint(Level level) {

		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.EMPTY);

		Point c = center();
		int cx = c.x;
		int cy = c.y;

		Door entrance = entrance();

		//entrance.set( Door.Type.LOCKED );
		//level.addItemToSpawn( new IronKey(Dungeon.xPos, Dungeon.depth, Dungeon.zPos) );

		if (entrance.x == left) {
			Painter.set(level, new Point(right - 1, top + 1), Terrain.STATUE);
			Painter.set(level, new Point(right - 1, bottom - 1), Terrain.STATUE);
			cx = right - 2;
		} else if (entrance.x == right) {
			Painter.set(level, new Point(left + 1, top + 1), Terrain.STATUE);
			Painter.set(level, new Point(left + 1, bottom - 1), Terrain.STATUE);
			cx = left + 2;
		} else if (entrance.y == top) {
			Painter.set(level, new Point(left + 1, bottom - 1), Terrain.STATUE);
			Painter.set(level, new Point(right - 1, bottom - 1), Terrain.STATUE);
			cy = bottom - 2;
		} else if (entrance.y == bottom) {
			Painter.set(level, new Point(left + 1, top + 1), Terrain.STATUE);
			Painter.set(level, new Point(right - 1, top + 1), Terrain.STATUE);
			cy = top + 2;
		}

		level.drop(prize(level), cx + cy * level.width()).type = Heap.Type.TOMB;
	}

	private static Item prize(Level level) {

		//1 floor set higher than normal
		Shield prize = Generator.randomShield();

		if (!prize.canSpawn()) {
			return new Gold().random();
		}
		//curse the armor, unless it has a glyph
		//if (!prize.hasGoodGlyph()){
		//	prize.inscribe(Armor.Glyph.randomCurse());
		//}

		prize.cursedKnown = true;

		prize.curse();

		return prize;
	}
}
