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

package com.shatteredpixel.yasd.general.levels.chapters.prison;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.mobs.DM100;
import com.shatteredpixel.yasd.general.actors.mobs.Guard;
import com.shatteredpixel.yasd.general.actors.mobs.Necromancer;
import com.shatteredpixel.yasd.general.actors.mobs.Skeleton;
import com.shatteredpixel.yasd.general.actors.mobs.Swarm;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.Wandmaker;
import com.shatteredpixel.yasd.general.effects.CPDEmitter;
import com.shatteredpixel.yasd.general.effects.particles.FlameParticle;
import com.shatteredpixel.yasd.general.levels.Level;
import com.shatteredpixel.yasd.general.levels.RegularLevel;
import com.shatteredpixel.yasd.general.levels.painters.Painter;
import com.shatteredpixel.yasd.general.levels.painters.PrisonPainter;
import com.shatteredpixel.yasd.general.levels.rooms.Room;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.levels.traps.AlarmTrap;
import com.shatteredpixel.yasd.general.levels.traps.BurningTrap;
import com.shatteredpixel.yasd.general.levels.traps.ChillingTrap;
import com.shatteredpixel.yasd.general.levels.traps.ConfusionTrap;
import com.shatteredpixel.yasd.general.levels.traps.FlockTrap;
import com.shatteredpixel.yasd.general.levels.traps.GrippingTrap;
import com.shatteredpixel.yasd.general.levels.traps.OozeTrap;
import com.shatteredpixel.yasd.general.levels.traps.PoisonDartTrap;
import com.shatteredpixel.yasd.general.levels.traps.ShockingTrap;
import com.shatteredpixel.yasd.general.levels.traps.SummoningTrap;
import com.shatteredpixel.yasd.general.levels.traps.TeleportationTrap;
import com.shatteredpixel.yasd.general.levels.traps.ToxicTrap;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.tiles.DungeonTilemap;
import com.watabou.noosa.Group;
import com.watabou.noosa.Halo;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.WALL_DECO;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.WATER;

public class PrisonLevel extends RegularLevel {

	{
		color1 = 0x6a723d;
		color2 = 0x88924c;

		minScaleFactor = 7;
    }
	
	@Override
	protected ArrayList<Room> initRooms() {
		return Wandmaker.Quest.spawnRoom(super.initRooms());
	}
	
	@Override
	protected int standardRooms() {
		//6 to 8, average 6.66
		return 6+Random.chances(new float[]{4, 2, 2});
	}
	
	@Override
	protected int specialRooms() {
		//1 to 3, average 1.83
		return 1+Random.chances(new float[]{3, 4, 3});
	}
	
	@Override
	protected Painter painter() {
		return new PrisonPainter()
				.setWater(feeling == Feeling.WATER ? 0.90f : 0.30f, 4)
				.setGrass(feeling == Feeling.GRASS ? 0.80f : 0.20f, 3)
				.setTraps(nTraps(), trapClasses(), trapChances());
	}
	
	@Override
	public String tilesTex() {
		return Assets.Environment.TILES_PRISON;
	}
	
	@Override
	public String waterTex() {
		return Assets.Environment.WATER_PRISON;
	}

	@Override
	public String loadImg() {
		return Assets.Interfaces.LOADING_PRISON;
	}

	@Override
	protected Class<?>[] trapClasses() {
		return new Class[]{
				ChillingTrap.class, ShockingTrap.class, ToxicTrap.class, BurningTrap.class, PoisonDartTrap.class,
				AlarmTrap.class, OozeTrap.class, GrippingTrap.class,
				ConfusionTrap.class, FlockTrap.class, SummoningTrap.class, TeleportationTrap.class, };
	}

	@Override
	protected float[] trapChances() {
		return new float[]{
				4, 4, 4, 4, 4,
				2, 2, 2,
				1, 1, 1, 1 };
	}

	@Override
	public Class<?>[] mobClasses() {
		return new Class[] {
				Skeleton.class,
				Swarm.class,
				DM100.class,
				Guard.class,
				Necromancer.class
		};
	}

	@Override
	public float[] mobChances() {
		return new float[] {
				5,
				2,
				4,
				2,
				1
		};
	}

	@Override
	protected float[] connectionRoomChances() {
		return new float[]{
				0,
				0,
				22,
				3,
				0,
				0,
				1};
	}

	@Override
	protected float[] standardRoomChances() {
		return new float[]{20,
				0,
				0,
				15,
				5,
				0,
				0,
				0,
				0,
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
	public String tileName( Terrain tile ) {
		if (tile == WATER) {
			return Messages.get(PrisonLevel.class, "water_name");
		}
		return super.tileName(tile);
	}

	@Override
	public String tileDesc( Terrain tile) {
		switch (tile) {
			case EMPTY_DECO:
				return Messages.get(PrisonLevel.class, "empty_deco_desc");
			case BOOKSHELF:
				return Messages.get(PrisonLevel.class, "bookshelf_desc");
			default:
				return super.tileDesc( tile );
		}
	}
	
	@Override
	public Group addVisuals() {
		super.addVisuals();
		addPrisonVisuals(this, visuals);
		return visuals;
	}

	public static void addPrisonVisuals(Level level, Group group){
		for (int i=0; i < level.length(); i++) {
			if (level.getTerrain(i) == WALL_DECO) {
				group.add( new Torch( i ) );
			}
		}
	}
	
	public static class Torch extends CPDEmitter {
		
		private int pos;
		
		public Torch( int pos ) {
			super();
			
			this.pos = pos;
			
			PointF p = DungeonTilemap.tileCenterToWorld( pos );
			pos( p.x - 1, p.y + 2, 2, 0 );
			
			pour( FlameParticle.FACTORY, 0.15f );
			
			add( new Halo( 12, 0xFFFFCC, 0.4f ).point( p.x, p.y + 1 ) );
		}
		
		@Override
		public void update() {
			if (visible = (pos < Dungeon.level.heroFOV.length && Dungeon.level.heroFOV[pos])) {
				super.update();
			}
		}
	}
}