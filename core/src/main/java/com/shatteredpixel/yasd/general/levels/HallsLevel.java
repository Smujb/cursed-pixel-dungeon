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
import com.shatteredpixel.yasd.general.Constants;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.mobs.Elemental;
import com.shatteredpixel.yasd.general.actors.mobs.Eye;
import com.shatteredpixel.yasd.general.actors.mobs.Scorpio;
import com.shatteredpixel.yasd.general.actors.mobs.Succubus;
import com.shatteredpixel.yasd.general.items.Torch;
import com.shatteredpixel.yasd.general.levels.painters.HallsPainter;
import com.shatteredpixel.yasd.general.levels.painters.Painter;
import com.shatteredpixel.yasd.general.levels.rooms.Room;
import com.shatteredpixel.yasd.general.levels.rooms.special.DemonSpawnerRoom;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.levels.traps.BlazingTrap;
import com.shatteredpixel.yasd.general.levels.traps.CorrosionTrap;
import com.shatteredpixel.yasd.general.levels.traps.CursingTrap;
import com.shatteredpixel.yasd.general.levels.traps.DisarmingTrap;
import com.shatteredpixel.yasd.general.levels.traps.DisintegrationTrap;
import com.shatteredpixel.yasd.general.levels.traps.DistortionTrap;
import com.shatteredpixel.yasd.general.levels.traps.FlashingTrap;
import com.shatteredpixel.yasd.general.levels.traps.FrostTrap;
import com.shatteredpixel.yasd.general.levels.traps.GrimTrap;
import com.shatteredpixel.yasd.general.levels.traps.GuardianTrap;
import com.shatteredpixel.yasd.general.levels.traps.PitfallTrap;
import com.shatteredpixel.yasd.general.levels.traps.RockfallTrap;
import com.shatteredpixel.yasd.general.levels.traps.StormTrap;
import com.shatteredpixel.yasd.general.levels.traps.SummoningTrap;
import com.shatteredpixel.yasd.general.levels.traps.WarpingTrap;
import com.shatteredpixel.yasd.general.levels.traps.WeakeningTrap;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.tiles.DungeonTilemap;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.WATER;

public class HallsLevel extends RegularLevel {

	{
		
		viewDistance = Math.min( Constants.MAX_DEPTH - Dungeon.depth, viewDistance );
		
		color1 = 0x801500;
		color2 = 0xa68521;

		minScaleFactor = 25;
		maxScaleFactor = 30;
	}

	@Override
	protected ArrayList<Room> initRooms() {
		ArrayList<Room> rooms = super.initRooms();

		rooms.add(new DemonSpawnerRoom());
		if (Dungeon.depth == 24){
			rooms.add(new DemonSpawnerRoom());
		}

		return rooms;
	}

	@Override
	public int nMobs() {
		//remove one mob to account for ripper demon spawners
		return super.nMobs()-1;
	}

	@Override
	protected int standardRooms() {
		//8 to 10, average 8.67
		return 8+Random.chances(new float[]{3, 2, 1});
	}
	
	@Override
	protected int specialRooms() {
		//2 to 3, average 2.5
		return 2 + Random.chances(new float[]{1, 1});
	}
	
	@Override
	protected Painter painter() {
		return new HallsPainter()
				.setWater(feeling == Feeling.WATER ? 0.70f : 0.15f, 6)
				.setGrass(feeling == Feeling.GRASS ? 0.65f : 0.10f, 3)
				.setTraps(nTraps(), trapClasses(), trapChances());
	}
	
	@Override
	public void create(String key) {
		addItemToSpawn( new Torch() );
		addItemToSpawn( new Torch() );
		super.create(key);
	}
	
	@Override
	public String tilesTex() {
		return Assets.TILES_HALLS;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_HALLS;
	}

	@Override
	public String loadImg() {
		return Assets.LOADING_HALLS;
	}

	@Override
	protected Class<?>[] trapClasses() {
		return new Class[]{
				FrostTrap.class, StormTrap.class, CorrosionTrap.class, BlazingTrap.class, DisintegrationTrap.class,
				RockfallTrap.class, FlashingTrap.class, GuardianTrap.class, WeakeningTrap.class,
				DisarmingTrap.class, SummoningTrap.class, WarpingTrap.class, CursingTrap.class, GrimTrap.class, PitfallTrap.class, DistortionTrap.class };
	}

	@Override
	protected float[] trapChances() {
		return new float[]{
				4, 4, 4, 4, 4,
				2, 2, 2, 2,
				1, 1, 1, 1, 1, 1, 1 };
	}

	@Override
	public Class<?>[] mobClasses() {
		return new Class[] {
				Eye.class,
				Succubus.class,
				Elemental.class,
				Scorpio.class
		};
	}

	@Override
	public float[] mobChances() {
		return new float[] {
				3,
				5,
				1,
				2
		};
	}

	@Override
	protected float[] connectionRoomChances() {
		return new float[]{
				15,
				4,
				0,
				2,
				3,
				2,
				1};
	}

	@Override
	protected float[] standardRoomChances() {
		return new float[]{
				20,
				0,
				0,
				0,
				0,
				0,
				0,
				0,
				0,
				15,
				5,
				1,
				1,
				1,
				1,
				1,
				1,
				1,
				1,
				1,
				1};
	}

	@Override
	public String tileName( Terrain tile ) {
		switch (tile) {
			case WATER:
				return Messages.get(HallsLevel.class, "water_name");
			case GRASS:
				return Messages.get(HallsLevel.class, "grass_name");
			case HIGH_GRASS:
				return Messages.get(HallsLevel.class, "high_grass_name");
			case STATUE:
			case STATUE_SP:
				return Messages.get(HallsLevel.class, "statue_name");
			default:
				return super.tileName( tile );
		}
	}
	
	@Override
	public String tileDesc( Terrain tile) {
		switch (tile) {
			case WATER:
				return Messages.get(HallsLevel.class, "water_desc");
			case STATUE:
			case STATUE_SP:
				return Messages.get(HallsLevel.class, "statue_desc");
			case BOOKSHELF:
				return Messages.get(HallsLevel.class, "bookshelf_desc");
			default:
				return super.tileDesc( tile );
		}
	}
	
	@Override
	public Group addVisuals() {
		super.addVisuals();
		addHallsVisuals( this, visuals );
		return visuals;
	}
	
	public static void addHallsVisuals( Level level, Group group ) {
		for (int i=0; i < level.length(); i++) {
			if (level.map[i] == WATER) {
				group.add( new Stream( i ) );
			}
		}
	}
	
	private static class Stream extends Group {
		
		private int pos;
		
		private float delay;
		
		public Stream( int pos ) {
			super();
			
			this.pos = pos;
			
			delay = Random.Float( 2 );
		}
		
		@Override
		public void update() {

			if (!Dungeon.level.liquid(pos)){
				killAndErase();
				return;
			}
			
			if (visible = (pos < Dungeon.level.heroFOV.length && Dungeon.level.heroFOV[pos])) {
				
				super.update();
				
				if ((delay -= Game.elapsed) <= 0) {
					
					delay = Random.Float( 2 );
					
					PointF p = DungeonTilemap.tileToWorld( pos );
					((FireParticle)recycle( FireParticle.class )).reset(
						p.x + Random.Float( DungeonTilemap.SIZE ),
						p.y + Random.Float( DungeonTilemap.SIZE ) );
				}
			}
		}
		
		@Override
		public void draw() {
			Blending.setLightMode();
			super.draw();
			Blending.setNormalMode();
		}
	}
	
	public static class FireParticle extends PixelParticle.Shrinking {
		
		public FireParticle() {
			super();
			
			color( 0xEE7722 );
			lifespan = 1f;
			
			acc.set( 0, +80 );
		}
		
		public void reset( float x, float y ) {
			revive();
			
			this.x = x;
			this.y = y;
			
			left = lifespan;
			
			speed.set( 0, -40 );
			size = 4;
		}
		
		@Override
		public void update() {
			super.update();
			float p = left / lifespan;
			am = p > 0.8f ? (1 - p) * 5 : 1;
		}
	}
}
