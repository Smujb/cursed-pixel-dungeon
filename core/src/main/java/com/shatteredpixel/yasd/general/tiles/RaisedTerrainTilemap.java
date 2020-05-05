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

package com.shatteredpixel.yasd.general.tiles;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.levels.terrain.KindOfTerrain;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;

public class RaisedTerrainTilemap extends DungeonTilemap {
	
	public RaisedTerrainTilemap() {
		super(Dungeon.level.tilesTex());
		map( Dungeon.level.map, Dungeon.level.width() );
	}
	
	@Override
	public int getTileVisual(int pos, KindOfTerrain tile, boolean flat) {
		
		if (flat) return -1;
		
		if (tile == Terrain.HIGH_GRASS){
			return DungeonTileSheet.getVisualWithAlts(
					DungeonTileSheet.RAISED_HIGH_GRASS,
					pos) + 2;
		} else if (tile == Terrain.FURROWED_GRASS){
			return DungeonTileSheet.getVisualWithAlts(
					DungeonTileSheet.RAISED_FURROWED_GRASS,
					pos) + 2;
		}
		
		
		return -1;
	}
}
