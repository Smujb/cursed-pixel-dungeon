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

package com.shatteredpixel.yasd.general.levels.rooms.special;

import com.shatteredpixel.yasd.general.items.Generator;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.scrolls.Scroll;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.yasd.general.levels.Level;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.levels.painters.Painter;
import com.shatteredpixel.yasd.general.levels.rooms.LockedRoom;
import com.watabou.utils.Random;

public class LibraryRoom extends LockedRoom {

	@Override
	public void paintRoom(Level level) {
		
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.EMPTY_SP );
		
		Door entrance = entrance();
		
		Painter.fill( level, left + 1, top+1, width() - 2, 1 , Terrain.BOOKSHELF );
		Painter.drawInside(level, this, entrance, 1, Terrain.EMPTY_SP );
		
		int n = Random.IntRange( 2, 3 );
		for (int i=0; i < n; i++) {
			int pos;
			do {
				pos = level.pointToCell(random());
			} while (level.map[pos] != Terrain.EMPTY_SP || level.heaps.get( pos ) != null);
			Item item;
			if (i == 0)
				item = Random.Int(2) == 0 ? new ScrollOfIdentify() : new ScrollOfRemoveCurse();
			else
				item = prize( level );
			level.drop( item, pos );
		}
		
		//entrance.set( Door.Type.LOCKED );
		//level.addItemToSpawn( new IronKey(Dungeon.xPos, Dungeon.depth, Dungeon.zPos) );
	}
	
	private static Item prize( Level level ) {
		
		Item prize = level.findPrizeItem( Scroll.class );
		if (prize == null)
			prize = Generator.random( Generator.Category.SCROLL );
		
		return prize;
	}
}
