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
import com.shatteredpixel.yasd.general.Bones;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.actors.mobs.YogDzewa;
import com.shatteredpixel.yasd.general.effects.CellEmitter;
import com.shatteredpixel.yasd.general.effects.particles.FlameParticle;
import com.shatteredpixel.yasd.general.effects.particles.ShadowParticle;
import com.shatteredpixel.yasd.general.items.Heap;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.levels.painters.Painter;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.tiles.CustomTilemap;
import com.watabou.noosa.Group;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.EMPTY;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.EMPTY_DECO;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.EMPTY_SP;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.ENTRANCE;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.EXIT;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.STATUE;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.WALL_DECO;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.WATER;

public class NewHallsBossLevel extends Level {

	{
		color1 = 0x801500;
		color2 = 0xa68521;

		viewDistance = Math.min(4, viewDistance);
	}

	private static final int WIDTH = 32;
	private static final int HEIGHT = 32;

	private static final int ROOM_LEFT		= WIDTH / 2 - 4;
	private static final int ROOM_RIGHT		= WIDTH / 2 + 4;
	private static final int ROOM_TOP		= 8;
	private static final int ROOM_BOTTOM	= ROOM_TOP + 8;

	@Override
	public boolean passable(int pos) {
		if (pos == getEntrancePos() || pos == getExitPos()) {
			return !locked;
		}
		return super.passable(pos);
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
	protected boolean build() {

		setSize(WIDTH, HEIGHT);

		for (int i = 0; i < 5; i++) {

			int top;
			int bottom;

			if (i == 0 || i == 4){
				top = Random.IntRange(ROOM_TOP-1, ROOM_TOP+3);
				bottom = Random.IntRange(ROOM_BOTTOM+2, ROOM_BOTTOM+6);
			} else if (i == 1 || i == 3){
				top = Random.IntRange(ROOM_TOP-5, ROOM_TOP-1);
				bottom = Random.IntRange(ROOM_BOTTOM+6, ROOM_BOTTOM+10);
			} else {
				top = Random.IntRange(ROOM_TOP-6, ROOM_TOP-3);
				bottom = Random.IntRange(ROOM_BOTTOM+8, ROOM_BOTTOM+12);
			}

			Painter.fill(this, 4 + i * 5, top, 5, bottom - top + 1, EMPTY);

			if (i == 2) {
				setEntrance(6 + i * 5 + (bottom - 1) * width());
			}

		}

		boolean[] patch = Patch.generate(width, height, 0.20f, 0, true);
		for (int i = 0; i < length(); i++) {
			if (map[i] == EMPTY && patch[i]) {
				map[i] = STATUE;
			}
		}

		map[getEntrancePos()] = ENTRANCE;

		Painter.fill(this, ROOM_LEFT-1, ROOM_TOP-1, 11, 11, EMPTY );

		patch = Patch.generate(width, height, 0.30f, 3, true);
		for (int i = 0; i < length(); i++) {
			if ((map[i] == EMPTY || map[i] == STATUE) && patch[i]) {
				map[i] = WATER;
			}
		}

		for (int i = 0; i < length(); i++) {
			if (map[i] == EMPTY && Random.Int(4) == 0) {
				map[i] = EMPTY_DECO;
			}
		}

		Painter.fill(this, ROOM_LEFT, ROOM_TOP, 9, 9, EMPTY_SP );

		Painter.fill(this, ROOM_LEFT, ROOM_TOP, 9, 2, WALL_DECO );
		Painter.fill(this, ROOM_LEFT, ROOM_BOTTOM-1, 2, 2, WALL_DECO );
		Painter.fill(this, ROOM_RIGHT-1, ROOM_BOTTOM-1, 2, 2, WALL_DECO );


		Painter.fill(this, ROOM_LEFT+3, ROOM_TOP+2, 3, 4, Terrain.EMPTY );



		setExit( width/2 + ((ROOM_TOP+1) * width) );
		//map[exit] = Terrain.EXIT;

		CustomTilemap vis = new CenterPieceVisuals();
		vis.pos(ROOM_LEFT, ROOM_TOP+1);
		customTiles.add(vis);

		vis = new CenterPieceWalls();
		vis.pos(ROOM_LEFT, ROOM_TOP);
		customWalls.add(vis);

		//ensures a path to the exit exists
		return (PathFinder.getStep(getEntrancePos(), getExitPos(), passable()) != -1);
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
				pos = randomRespawnCell();
			} while (pos == getEntrancePos());
			drop( item, pos ).setHauntedIfCursed().type = Heap.Type.REMAINS;
		}
	}

	@Override
	public int randomRespawnCell( Char ch ) {
		int pos = getEntrancePos();
		int cell;
		do {
			cell = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
		} while (!passable(cell)
				|| !Char.canOccupy(ch, this, cell)
				|| Actor.findChar(cell) != null);
		return cell;
	}

	@Override
	public void occupyCell( Char ch ) {
		super.occupyCell( ch );

		if (map[getEntrancePos()] == ENTRANCE && map[getEntrancePos()] != EXIT
				&& ch == Dungeon.hero && Dungeon.level.distance(ch.pos, getEntrancePos()) >= 2) {
			seal();
		}
	}

	@Override
	public void seal() {
		super.seal();
		set( getEntrancePos(), EMPTY_SP );
		GameScene.updateMap( getEntrancePos() );
		CellEmitter.get( getEntrancePos() ).start( FlameParticle.FACTORY, 0.1f, 10 );

		Dungeon.observe();

		YogDzewa boss = Mob.create(YogDzewa.class, this);
		boss.pos = getExitPos() + width*3;
		GameScene.add( boss );
		//boss.spawnFists();
	}

	@Override
	public void unseal() {
		set( getEntrancePos(), ENTRANCE );
		GameScene.updateMap( getEntrancePos() );

		set( getExitPos(), EXIT );
		GameScene.updateMap( getExitPos() );

		CellEmitter.get(getExitPos()-1).burst(ShadowParticle.UP, 25);
		CellEmitter.get(getExitPos()).burst(ShadowParticle.UP, 100);
		CellEmitter.get(getExitPos()+1).burst(ShadowParticle.UP, 25);
		for( CustomTilemap t : customTiles){
			if (t instanceof CenterPieceVisuals){
				((CenterPieceVisuals) t).updateState();
			}
		}
		for( CustomTilemap t : customWalls){
			if (t instanceof CenterPieceWalls){
				((CenterPieceWalls) t).updateState();
			}
		}

		Dungeon.observe();
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
	public String tileDesc(Terrain tile) {
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
	public Group addVisuals () {
		super.addVisuals();
		HallsLevel.addHallsVisuals( this, visuals );
		return visuals;
	}

	public static class CenterPieceVisuals extends CustomTilemap {

		{
			texture = Assets.HALLS_SP;

			tileW = 9;
			tileH = 8;
		}

		private static final int[] map = new int[]{
				 8,  9, 10, 11, 11, 11, 12, 13, 14,
				16, 17, 18, 27, 19, 27, 20, 21, 22,
				24, 25, 26, 19, 19, 19, 28, 29, 30,
				24, 25, 26, 19, 19, 19, 28, 29, 30,
				24, 25, 26, 19, 19, 19, 28, 29, 30,
				24, 25, 26, 27, 19, 27, 28, 29, 30,
				40, 41, 36, 36, 36, 36, 36, 40, 41,
				48, 49, 36, 36, 36, 36, 36, 48, 49
		};

		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			updateState();
			return v;
		}

		private void updateState(){
			if (vis != null){
				int[] data = map.clone();
				if (Dungeon.level.map[Dungeon.level.getExitPos()] == EXIT) {
					data[4] = 19;
					data[12] = data[14] = 31;
				}
				vis.map(data, tileW);
			}
		}
	}

	public static class CenterPieceWalls extends CustomTilemap {

		{
			texture = Assets.HALLS_SP;

			tileW = 9;
			tileH = 9;
		}

		private static final int[] map = new int[]{
				-1, -1, -1, -1, -1, -1, -1, -1, -1,
				-1, -1, -1, -1, -1, -1, -1, -1, -1,
				-1, -1, -1, -1, -1, -1, -1, -1, -1,
				-1, -1, -1, -1, -1, -1, -1, -1, -1,
				-1, -1, -1, -1, -1, -1, -1, -1, -1,
				-1, -1, -1, -1, -1, -1, -1, -1, -1,
				32, 33, -1, -1, -1, -1, -1, 32, 33,
				40, 41, -1, -1, -1, -1, -1, 40, 41,
		};

		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			updateState();
			return v;
		}

		private void updateState(){
			if (vis != null){
				int[] data = map.clone();
				if (Dungeon.level.map[Dungeon.level.getExitPos()] == EXIT) {
					data[3] = 1;
					data[4] = 0;
					data[5] = 2;
					data[13] = 23;
				}
				vis.map(data, tileW);
			}
		}

	}
}
