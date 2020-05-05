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

package com.shatteredpixel.yasd.general.levels;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Bones;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.items.Heap;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.levels.builders.Builder;
import com.shatteredpixel.yasd.general.levels.builders.LineBuilder;
import com.shatteredpixel.yasd.general.levels.painters.CityPainter;
import com.shatteredpixel.yasd.general.levels.painters.Painter;
import com.shatteredpixel.yasd.general.levels.rooms.Room;
import com.shatteredpixel.yasd.general.levels.rooms.standard.EntranceRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.ExitRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.ImpShopRoom;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.watabou.noosa.Group;

import java.util.ArrayList;

import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.DOOR;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.SECRET_DOOR;

public class LastShopLevel extends RegularLevel {
	
	{
		color1 = 0x4b6636;
		color2 = 0xf2f2f2;

		minScaleFactor = 20;
		maxScaleFactor = 22;
	}

	@Override
	public String tilesTex() {
		return Assets.TILES_CITY;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_CITY;
	}

	@Override
	public String loadImg() {
		return Assets.LOADING_CITY;
	}

	@Override
	protected boolean build() {
		feeling = Feeling.CHASM;
		if (super.build()){
			
			for (int i=0; i < length(); i++) {
				if (map[i] == SECRET_DOOR) {
					map[i] = DOOR;
				}
			}
			
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	protected ArrayList<Room> initRooms() {
		ArrayList<Room> rooms = new ArrayList<>();
		
		rooms.add ( roomEntrance = new EntranceRoom());
		rooms.add( new ImpShopRoom() );
		rooms.add( roomExit = new ExitRoom());
		
		return rooms;
	}
	
	@Override
	protected Builder builder() {
		return new LineBuilder()
				.setPathVariance(0f)
				.setPathLength(1f, new float[]{1})
				.setTunnelLength(new float[]{0, 0, 1}, new float[]{1});
	}
	
	@Override
	protected Painter painter() {
		return new CityPainter()
				.setWater( 0.10f, 4 )
				.setGrass( 0.10f, 3 );
	}
	
	@Override
	public Mob createMob() {
		return null;
	}
	
	@Override
	protected void createMobs() {
	}

	@Override
	protected float[] connectionRoomChances() {
		return new float[]{
				20,
				0, 0, 0, 0, 0, 0};
	}

	public Actor respawner() {
		return null;
	}
	
	@Override
	protected void createItems() {
		Item item = Bones.get();
		if (item != null) {
			int pos;
			do {
				pos = pointToCell(roomEntrance.random());
			} while (pos == getEntrancePos());
			drop( item, pos ).setHauntedIfCursed().type = Heap.Type.REMAINS;
		}
	}
	
	@Override
	public int randomRespawnCell(Char ch) {
		int cell;
		do {
			cell = pointToCell( roomEntrance.random() );
		} while (!passable(cell)
				|| !Char.canOccupy(ch, this, cell)
				|| Actor.findChar(cell) != null);
		return cell;
	}
	
	@Override
	public String tileName( Terrain tile ) {
		switch (tile) {
			case WATER:
				return Messages.get(CityLevel.class, "water_name");
			case HIGH_GRASS:
				return Messages.get(CityLevel.class, "high_grass_name");
			default:
				return super.tileName( tile );
		}
	}
	
	@Override
	public String tileDesc( Terrain tile) {
		switch (tile) {
			case ENTRANCE:
				return Messages.get(CityLevel.class, "entrance_desc");
			case EXIT:
				return Messages.get(CityLevel.class, "exit_desc");
			case WALL_DECO:
			case EMPTY_DECO:
				return Messages.get(CityLevel.class, "deco_desc");
			case EMPTY_SP:
				return Messages.get(CityLevel.class, "sp_desc");
			case STATUE:
			case STATUE_SP:
				return Messages.get(CityLevel.class, "statue_desc");
			case BOOKSHELF:
				return Messages.get(CityLevel.class, "bookshelf_desc");
			default:
				return super.tileDesc( tile );
		}
	}

	@Override
	public Group addVisuals( ) {
		super.addVisuals();
		CityLevel.addCityVisuals(this, visuals);
		return visuals;
	}
}
