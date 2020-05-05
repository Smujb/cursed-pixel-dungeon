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

import com.shatteredpixel.yasd.general.Bones;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.actors.mobs.Slime;
import com.shatteredpixel.yasd.general.items.Heap;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.levels.builders.Builder;
import com.shatteredpixel.yasd.general.levels.builders.FigureEightBuilder;
import com.shatteredpixel.yasd.general.levels.interactive.Entrance;
import com.shatteredpixel.yasd.general.levels.painters.Painter;
import com.shatteredpixel.yasd.general.levels.painters.SewerPainter;
import com.shatteredpixel.yasd.general.levels.rooms.Room;
import com.shatteredpixel.yasd.general.levels.rooms.secret.RatKingRoom;
import com.shatteredpixel.yasd.general.levels.rooms.sewerboss.GooBossRoom;
import com.shatteredpixel.yasd.general.levels.rooms.sewerboss.SewerBossEntranceRoom;
import com.shatteredpixel.yasd.general.levels.rooms.sewerboss.SewerBossExitRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.StandardRoom;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.watabou.noosa.Group;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class SewerBossLevel extends SewerLevel {

	{
		color1 = 0x48763c;
		color2 = 0x59994a;
	}
	
	private Entrance stairs = null;
	
	@Override
	protected ArrayList<Room> initRooms() {
		ArrayList<Room> initRooms = new ArrayList<>();
		
		initRooms.add( roomEntrance = new SewerBossEntranceRoom() );
		initRooms.add( roomExit = new SewerBossExitRoom() );
		
		int standards = standardRooms();
		for (int i = 0; i < standards; i++) {
			StandardRoom s = StandardRoom.createRoom(this);
			//force to normal size
			s.setSizeCat(0, 0);
			initRooms.add(s);
		}
		
		GooBossRoom gooRoom = GooBossRoom.randomGooRoom();
		initRooms.add(gooRoom);
		((FigureEightBuilder)builder).setLandmarkRoom(gooRoom);
		initRooms.add(new RatKingRoom());
		return initRooms;
	}
	
	@Override
	protected int standardRooms() {
		//2 to 3, average 2.5
		return 2+Random.chances(new float[]{1, 1});
	}
	
	protected Builder builder(){
		return new FigureEightBuilder()
				.setLoopShape( 2 , Random.Float(0.4f, 0.7f), Random.Float(0f, 0.5f))
				.setPathLength(1f, new float[]{1})
				.setTunnelLength(new float[]{1, 2}, new float[]{1});
	}
	
	@Override
	protected Painter painter() {
		return new SewerPainter()
				.setWater(0.50f, 5)
				.setGrass(0.20f, 4)
				.setTraps(nTraps(), trapClasses(), trapChances());
	}

	@Override
	protected float[] connectionRoomChances() {
		return new float[]{
				20,
				0, 0, 0, 0, 0, 0};
	}
	
	protected int nTraps() {
		return 0;
	}

	@Override
	protected float[] standardRoomChances() {
		return new float[]{20,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	}

	@Override
	public Mob createMob() {
		return Mob.create(Slime.CausticSlime.class, this);
	}

	@Override
	protected void createMobs() {
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
			} while (pos == getEntrancePos() || solid(pos));
			drop( item, pos ).setHauntedIfCursed().type = Heap.Type.REMAINS;
		}
	}

	@Override
	public int randomRespawnCell(@Nullable Char ch) {
		int pos;
		do {
			pos = pointToCell(roomEntrance.random());
		} while (pos == getEntrancePos()
				|| !passable(pos)
				|| !Char.canOccupy(ch, this, pos)
				|| Actor.findChar(pos) != null);
		return pos;
	}

	
	public void seal() {
		if (getEntrance() != null) {

			super.seal();
			int pos = getEntrancePos();
			set( pos, Terrain.WATER );
			GameScene.updateMap( pos );
			GameScene.ripple( pos );
			
			stairs = getEntrance();
			//entrance = 0;
		}
	}
	
	public void unseal() {
		if (stairs != null) {

			super.unseal();
			
			interactiveAreas.add(stairs);
			stairs = null;
			
			set( getEntrancePos(), Terrain.ENTRANCE );
			GameScene.updateMap( getEntrancePos() );

		}
	}
	
	@Override
	public Group addVisuals() {
		super.addVisuals();
		if (map[getExitPos()-1] != Terrain.WALL_DECO) visuals.add(new PrisonLevel.Torch(getExitPos()-1));
		if (map[getExitPos()+1] != Terrain.WALL_DECO) visuals.add(new PrisonLevel.Torch(getExitPos()+1));
		return visuals;
	}
	
	private static final String STAIRS	= "stairs";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( STAIRS, stairs );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		stairs = (Entrance) bundle.get( STAIRS );
		roomExit = roomEntrance;
	}
}
