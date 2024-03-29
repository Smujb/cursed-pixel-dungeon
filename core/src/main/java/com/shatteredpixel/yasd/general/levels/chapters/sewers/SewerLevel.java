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

package com.shatteredpixel.yasd.general.levels.chapters.sewers;

import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.WALL_DECO;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.mobs.Crab;
import com.shatteredpixel.yasd.general.actors.mobs.Gnoll;
import com.shatteredpixel.yasd.general.actors.mobs.Rat;
import com.shatteredpixel.yasd.general.actors.mobs.Slime;
import com.shatteredpixel.yasd.general.actors.mobs.Snake;
import com.shatteredpixel.yasd.general.actors.mobs.Thief;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.Ghost;
import com.shatteredpixel.yasd.general.effects.CPDEmitter;
import com.shatteredpixel.yasd.general.effects.Ripple;
import com.shatteredpixel.yasd.general.items.powers.Alchemy;
import com.shatteredpixel.yasd.general.levels.Level;
import com.shatteredpixel.yasd.general.levels.RegularLevel;
import com.shatteredpixel.yasd.general.levels.painters.Painter;
import com.shatteredpixel.yasd.general.levels.painters.SewerPainter;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.levels.traps.GuardianTrap;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.tiles.DungeonTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.ColorMath;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class SewerLevel extends RegularLevel {

	{
		color1 = 0x48763c;
		color2 = 0x59994a;

		minScaleFactor = 2;
    }


	@Override
	protected int standardRooms() {
		//5 to 7, average 5.57
		return 5+Random.chances(new float[]{4, 2, 1});
	}
	
	@Override
	protected int specialRooms() {
		//1 to 3, average 1.67
		return 1+Random.chances(new float[]{4, 4, 2});
	}
	
	@Override
	protected Painter painter() {
		return new SewerPainter()
				.setWater(feeling == Feeling.WATER ? 0.85f : 0.30f, 5)
				.setGrass(feeling == Feeling.GRASS ? 0.80f : 0.20f, 4)
				.setTraps(nTraps(), trapClasses(), trapChances());
	}
	
	@Override
	public String tilesTex() {
		return Assets.Environment.TILES_SEWERS;
	}
	
	@Override
	public String waterTex() {
		return Assets.Environment.WATER_SEWERS;
	}

	@Override
	public String loadImg() {
		return Assets.Interfaces.LOADING_SEWERS;
	}

	@Override
	protected Class<?>[] trapClasses() {
		return new Class<?>[] {GuardianTrap.class};/*new Class<?>[]{
						ChillingTrap.class, ShockingTrap.class, ToxicTrap.class, WornDartTrap.class,
						AlarmTrap.class, OozeTrap.class,
						ConfusionTrap.class, FlockTrap.class, SummoningTrap.class, TeleportationTrap.class };*/
	}

	@Override
	protected float[] trapChances() {
		return new float[] { 1 }; /*new float[]{
						4, 4, 4, 4,
						2, 2,
						1, 1, 1, 1};*/
	}

	@Override
	public Class<?>[] mobClasses() {
		return new Class[]{
						Rat.class,
						Snake.class,
						Gnoll.class,
						Thief.class,
						Crab.class,//TODO rework Slimes
						Slime.class};
	}

	@Override
	public float[] mobChances() {
		return new float[]{
						3,
						1,
						4,
						4,
						2,
						0};
	}

	protected float[] connectionRoomChances() {
		return new float[]{
				20,
				1,
				0,
				2,
				2,
				1,
				1};
	}

	@Override
	protected float[] standardRoomChances() {
		return new float[]{20,  15,5, 0,0, 0,0, 0,0, 0,0,    1,1,1,1,1,1,1,1,1,1};
	}

	@Override
	protected void createItems() {
		if (!Dungeon.LimitedDrops.DEW_VIAL.dropped()) {
			//Dew Vial removed until I can find a good use for it
			//addItemToSpawn( new DewVial() );
			addItemToSpawn( new Alchemy() );
			Dungeon.LimitedDrops.DEW_VIAL.drop();
		}

		Ghost.Quest.spawn( this );
		
		super.createItems();
	}

	@Override
	public Group addVisuals() {
		super.addVisuals();
		addSewerVisuals(this, visuals);
		return visuals;
	}
	
	static void addSewerVisuals(Level level, Group group) {
		for (int i=0; i < level.length(); i++) {
			if (level.getTerrain(i) == WALL_DECO) {
				group.add( new Sink( i ) );
			}
		}
	}
	
	@Override
	public String tileName( Terrain tile ) {
		if (tile == Terrain.WATER) {
			return Messages.get(SewerLevel.class, "water_name");
		}
		return super.tileName(tile);
	}
	
	@Override
	public String tileDesc( Terrain tile) {
		switch (tile) {
			case EMPTY_DECO:
				return Messages.get(SewerLevel.class, "empty_deco_desc");
			case BOOKSHELF:
				return Messages.get(SewerLevel.class, "bookshelf_desc");
			default:
				return super.tileDesc( tile );
		}
	}
	
	public static class Sink extends CPDEmitter {
		
		private int pos;
		private float rippleDelay = 0;
		
		private static final Emitter.Factory factory = new Factory() {
			
			@Override
			public void emit( Emitter emitter, int index, float x, float y ) {
				WaterParticle p = (WaterParticle)emitter.recycle( WaterParticle.class );
				p.reset( x, y );
			}
		};
		
		public Sink(int pos) {
			super();
			
			this.pos = pos;
			
			PointF p = DungeonTilemap.tileCenterToWorld( pos );
			pos( p.x - 2, p.y + 3, 4, 0 );
			
			pour( factory, 0.1f );
		}
		
		@Override
		public void update() {
			if (visible = (pos < Dungeon.level.heroFOV.length && Dungeon.level.heroFOV[pos])) {
				
				super.update();
				
				if ((rippleDelay -= Game.elapsed) <= 0) {
					Ripple ripple = GameScene.ripple( pos + Dungeon.level.width() );
					if (ripple != null) {
						ripple.y -= DungeonTilemap.SIZE / 2;
						rippleDelay = Random.Float(0.4f, 0.6f);
					}
				}
			}
		}
	}
	
	public static final class WaterParticle extends PixelParticle {
		public static final Emitter.Factory FACTORY = new Emitter.Factory() {
			@Override
			public void emit( Emitter emitter, int index, float x, float y ) {
				((WaterParticle)emitter.recycle( WaterParticle.class )).reset( x, y );
			}
			@Override
			public boolean lightMode() {
				return true;
			}
		};
		public WaterParticle() {
			super();
			
			acc.y = 50;
			am = 0.5f;
			
			color( ColorMath.random( 0xb6ccc2, 0x3b6653 ) );
			size( 2 );
		}
		
		public void reset( float x, float y ) {
			revive();
			
			this.x = x;
			this.y = y;
			
			speed.set( Random.Float( -2, +2 ), 0 );
			
			left = lifespan = 0.4f;
		}
	}
}
