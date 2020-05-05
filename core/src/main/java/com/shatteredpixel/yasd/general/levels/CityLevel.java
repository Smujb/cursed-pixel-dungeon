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
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.mobs.Elemental;
import com.shatteredpixel.yasd.general.actors.mobs.Ghoul;
import com.shatteredpixel.yasd.general.actors.mobs.Golem;
import com.shatteredpixel.yasd.general.actors.mobs.Monk;
import com.shatteredpixel.yasd.general.actors.mobs.Warlock;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.Imp;
import com.shatteredpixel.yasd.general.levels.painters.CityPainter;
import com.shatteredpixel.yasd.general.levels.painters.Painter;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.levels.traps.BlazingTrap;
import com.shatteredpixel.yasd.general.levels.traps.CorrosionTrap;
import com.shatteredpixel.yasd.general.levels.traps.CursingTrap;
import com.shatteredpixel.yasd.general.levels.traps.DisarmingTrap;
import com.shatteredpixel.yasd.general.levels.traps.DisintegrationTrap;
import com.shatteredpixel.yasd.general.levels.traps.DistortionTrap;
import com.shatteredpixel.yasd.general.levels.traps.FlashingTrap;
import com.shatteredpixel.yasd.general.levels.traps.FrostTrap;
import com.shatteredpixel.yasd.general.levels.traps.GuardianTrap;
import com.shatteredpixel.yasd.general.levels.traps.PitfallTrap;
import com.shatteredpixel.yasd.general.levels.traps.RockfallTrap;
import com.shatteredpixel.yasd.general.levels.traps.StormTrap;
import com.shatteredpixel.yasd.general.levels.traps.SummoningTrap;
import com.shatteredpixel.yasd.general.levels.traps.WarpingTrap;
import com.shatteredpixel.yasd.general.levels.traps.WeakeningTrap;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.tiles.DungeonTilemap;
import com.watabou.noosa.Group;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import org.jetbrains.annotations.NotNull;

import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.WALL_DECO;

public class CityLevel extends RegularLevel {

	{
		color1 = 0x4b6636;
		color2 = 0xf2f2f2;

		minScaleFactor = 19;
		maxScaleFactor = 24;
	}
	
	@Override
	protected int standardRooms() {
		//7 to 10, average 7.9
		return 7+Random.chances(new float[]{4, 3, 2, 1});
	}
	
	@Override
	protected int specialRooms() {
		//2 to 3, average 2.33
		return 2 + Random.chances(new float[]{2, 1});
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
	protected Painter painter() {
		return new CityPainter()
				.setWater(feeling == Feeling.WATER ? 0.90f : 0.30f, 4)
				.setGrass(feeling == Feeling.GRASS ? 0.80f : 0.20f, 3)
				.setTraps(nTraps(), trapClasses(), trapChances());
	}

	@Override
	protected Class<?>[] trapClasses() {
		return new Class[]{
				FrostTrap.class, StormTrap.class, CorrosionTrap.class, BlazingTrap.class, DisintegrationTrap.class,
				RockfallTrap.class, FlashingTrap.class, GuardianTrap.class, WeakeningTrap.class,
				DisarmingTrap.class, SummoningTrap.class, WarpingTrap.class, CursingTrap.class, PitfallTrap.class, DistortionTrap.class };
	}

	@Override
	protected float[] trapChances() {
		return new float[]{
				4, 4, 4, 4, 4,
				2, 2, 2, 2,
				1, 1, 1, 1, 1, 1 };
	}

	@Override
	public Class<?>[] mobClasses() {
		return new Class[] {
				Warlock.class,
				Monk.class,
				Elemental.random(),
				Golem.class,
				Ghoul.class
		};
	}

	@Override
	public float[] mobChances() {
		return new float[] {
				3,
				3,
				4,
				2,
				4
		};
	}

	@Override
	protected float[] connectionRoomChances() {
		return new float[]{
				0,
				0,
				18,
				3,
				3,
				1,
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
				15,
				5,
				0,
				0,
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
	protected void createMobs() {
		Imp.Quest.spawn( this );
		
		super.createMobs();
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
	public Group addVisuals() {
		super.addVisuals();
		addCityVisuals( this, visuals );
		return visuals;
	}

	public static void addCityVisuals( Level level, Group group ) {
		for (int i=0; i < level.length(); i++) {
			if (level.map[i] == WALL_DECO) {
				group.add( new Smoke( i ) );
			}
		}
	}
	
	static class Smoke extends Emitter {
		
		private int pos;
		
		private static final Emitter.Factory factory = new Factory() {
			
			@Override
			public void emit( Emitter emitter, int index, float x, float y ) {
				SmokeParticle p = (SmokeParticle)emitter.recycle( SmokeParticle.class );
				p.reset( x, y );
			}
		};
		
		public Smoke( int pos ) {
			super();
			
			this.pos = pos;
			
			PointF p = DungeonTilemap.tileCenterToWorld( pos );
			pos( p.x - 6, p.y - 4, 12, 12 );
			
			pour( factory, 0.2f );
		}
		
		@Override
		public void update() {
			if (visible = (pos < Dungeon.level.heroFOV.length && Dungeon.level.heroFOV[pos])) {
				super.update();
			}
		}
	}
	
	public static final class SmokeParticle extends PixelParticle {
		
		public SmokeParticle() {
			super();
			
			color( 0x000000 );
			speed.set( Random.Float( -2, 4 ), -Random.Float( 3, 6 ) );
		}
		
		public void reset( float x, float y ) {
			revive();
			
			this.x = x;
			this.y = y;
			
			left = lifespan = 2f;
		}
		
		@Override
		public void update() {
			super.update();
			float p = left / lifespan;
			am = p > 0.8f ? 1 - p : p * 0.25f;
			size( 6 - p * 3 );
		}
	}
}