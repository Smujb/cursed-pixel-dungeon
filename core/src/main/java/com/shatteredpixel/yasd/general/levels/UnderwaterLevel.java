/*
 *
 *   Pixel Dungeon
 *   Copyright (C) 2012-2015 Oleg Dolya
 *
 *   Shattered Pixel Dungeon
 *   Copyright (C) 2014-2019 Evan Debenham
 *
 *   Yet Another Shattered Dungeon
 *   Copyright (C) 2014-2020 Samuel Braithwaite
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 */

package com.shatteredpixel.yasd.general.levels;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.LimitedAir;
import com.shatteredpixel.yasd.general.actors.mobs.JellyFish;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.actors.mobs.Piranha;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.effects.particles.ShaftParticle;
import com.shatteredpixel.yasd.general.items.Generator;
import com.shatteredpixel.yasd.general.items.Heap;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.tiles.DungeonTilemap;
import com.watabou.noosa.Group;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

//FIXME kinda poorly coded, may rewrite
public class UnderwaterLevel extends Level {
	{

		hasEntrance = hasExit = false;
	}

	public String tilesTex = Assets.TILES_HALLS;
	public String waterTex = Assets.WATER_HALLS;

	private static final int NUM_BUBBLES = 10;

	private static final int NUM_ITEMS = 10;

	private int _width = -1;
	private int _height = -1;

	private ArrayList<Integer> lightLocations = new ArrayList<>();

	private ArrayList<Integer> bubbleLocations = new ArrayList<>();

	private ArrayList<Integer> chasmLocations = new ArrayList<>();//Doesn't need to be stored as it's only used in levelgen.

	//NOTE: setParent() MUST be called before create(). If not, the level size and terrain won't be correct.
	public UnderwaterLevel setParent(Level level) {
		waterTex = level.waterTex();
		tilesTex = level.tilesTex();
		minScaleFactor = level.minScaleFactor;
		maxScaleFactor = level.maxScaleFactor;
		_width = level.width();
		_height = level.height();
		lightLocations = level.getTileLocations(Terrain.DEEP_WATER);
		chasmLocations = level.getTileLocations(Terrain.CHASM);
		return this;
	}

	@Override
	public boolean liquid(int pos) {
		return super.liquid(pos) || passable(pos);
	}

	@Override
	public boolean deepWater(int pos) {
		return lightLocations.contains(pos);
	}

	@Override
	protected boolean build() {
		setSize(_width, _height);
		map = Level.basicMap(length());
		buildFlagMaps();
		boolean[] setSolid = Patch.generate( width(), height(), 0.2f, 4, true );
		for (int i = 0; i < length(); i ++) {
			if ((setSolid[i] || chasmLocations.contains(i)) && map[i] == Terrain.EMPTY && !lightLocations.contains(i)) {
				map[i] = Random.Int(10) == 0 ? Terrain.WALL_DECO : Terrain.WALL;
			}
		}
		for (int i = 0; i < NUM_BUBBLES; i++) {
			bubbleLocations.add(randomRespawnCell());
		}
		return true;
	}

	@Override
	public Class<?>[] mobClasses() {
		return new Class[] {Piranha.class, JellyFish.class};
	}

	@Override
	public float[] mobChances() {
		return new float[] {1, 1};
	}

	@Override
	public int nMobs() {
		return 3;
	}

	@Override
	protected void createMobs() {
		for (int i = 0; i < nMobs(); i++) {
			Mob mob = createMob();
			mob.pos = randomRespawnCell(mob);
			mobs.add(mob);
		}
	}

	@Override
	protected void createItems() {
		for (int i = 0; i < NUM_ITEMS; i++) {
			Item item;
			switch (Random.Int(10)) {
				case 0: default:
					item = Generator.random(Generator.Category.GOLD);
					break;
				case 1: case 2:
					item = Generator.random(Generator.Category.STONE);
					break;
				case 3:
					item = Generator.randomWeapon();
					item.use(Item.MAXIMUM_DURABILITY*Random.Float(), true);
					break;
				case 4:
					item = Generator.randomArmor();
					item.use(Item.MAXIMUM_DURABILITY*Random.Float(), true);
					break;

			}
			item.uncurse();
			item.cursedKnown = true;
			drop(item, randomRespawnCell()).type = Heap.Type.SKELETON;
		}
	}

	@Override
	public String tilesTex() {
		if (waterTex.isEmpty()) {
			return Assets.TILES_HALLS;
		} else {
			return tilesTex;
		}
	}

	@Override
	public String waterTex() {
		if (waterTex.isEmpty()) {
			return Assets.WATER_HALLS;
		} else {
			return waterTex;
		}
	}

	@Override
	public void occupyCell( Char ch) {
		super.occupyCell(ch);
		if (bubbleLocations.contains(ch.pos)) {
			ch.sprite.flash();
			ch.sprite.emitter().burst(Speck.factory(Speck.HEALING), 2);
			LimitedAir air = ch.buff(LimitedAir.class);
			if (air != null) {
				air.reset();
			}
		}
	}

	@Override
	public String loadImg() {
		return waterTex();
	}

	@Override
	public Group addVisuals() {
		Group visuals = super.addVisuals();
		for (int i=0; i < length(); i++) {
			if (lightLocations.contains(i)) {
				visuals.add(new Light(i));
			}
			if (bubbleLocations.contains(i)) {
				visuals.add(new Bubble(i));
			}
		}
		return visuals;
	}

	private static final String TILE_TEX = "tile_tex";
	private static final String WATER_TEX = "water_tex";
	private static final String SCALE_FACTOR_MAX = "scalefactor-max";
	private static final String SCALE_FACTOR_MIN = "scalefactor-min";
	private static final String LIGHT_TILE = "light_tile";
	private static final String BUBBLE_TILE = "bubble_tile";
	private static final String LIGHT_TILE_AMT = "light_tiles_num";

	@Override
	public void storeInBundle( Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(TILE_TEX, tilesTex);
		bundle.put(WATER_TEX, waterTex);
		bundle.put(SCALE_FACTOR_MIN, minScaleFactor);
		bundle.put(SCALE_FACTOR_MAX, maxScaleFactor);

		for (int i = 0; i < lightLocations.size(); i++) {
			bundle.put(LIGHT_TILE+i, lightLocations.get(i));
		}

		bundle.put(LIGHT_TILE_AMT, lightLocations.size());
		for (int i = 0; i < bubbleLocations.size(); i++) {
			bundle.put(BUBBLE_TILE+i, bubbleLocations.get(i));
		}
	}

	@Override
	public void restoreFromBundle( Bundle bundle) {
		super.restoreFromBundle(bundle);
		tilesTex = bundle.getString(TILE_TEX);
		waterTex = bundle.getString(WATER_TEX);
		minScaleFactor = bundle.getInt(SCALE_FACTOR_MIN);
		maxScaleFactor = bundle.getInt(SCALE_FACTOR_MAX);

		int numLightTiles = bundle.getInt(LIGHT_TILE_AMT);
		for (int i = 0; i < numLightTiles; i++) {
			lightLocations.add(bundle.getInt(LIGHT_TILE+i));
		}

		for (int i = 0; i < NUM_BUBBLES; i++) {
			bubbleLocations.add(bundle.getInt(BUBBLE_TILE+i));
		}
	}

	static class Bubble extends Emitter {

		private int pos;

		Bubble(int pos) {
			super();

			this.pos = pos;

			PointF p = DungeonTilemap.tileCenterToWorld( pos );
			pos( p.x - 6, p.y - 4, 12, 12 );

			pour( Speck.factory(Speck.BUBBLE), 0.2f );
		}

		@Override
		public void update() {
			if (visible = (pos < Dungeon.level.heroFOV.length && Dungeon.level.heroFOV[pos])) {
				super.update();
			}
		}
	}

	static class Light extends Emitter {

		private int pos;

		private static final Emitter.Factory factory = new Factory() {

			@Override
			public void emit( Emitter emitter, int index, float x, float y ) {
				ShaftParticle p = (ShaftParticle)emitter.recycle( ShaftParticle.class );
				p.reset( x, y );
			}
		};

		Light(int pos) {
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
}

