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
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.blobs.Blob;
import com.shatteredpixel.yasd.general.actors.blobs.Regrowth;
import com.shatteredpixel.yasd.general.actors.blobs.StormCloud;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.actors.mobs.NewTengu;
import com.shatteredpixel.yasd.general.effects.CellEmitter;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.items.Generator;
import com.shatteredpixel.yasd.general.items.Heap;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.keys.IronKey;
import com.shatteredpixel.yasd.general.items.weapon.missiles.HeavyBoomerang;
import com.shatteredpixel.yasd.general.levels.features.Maze;
import com.shatteredpixel.yasd.general.levels.interactive.Entrance;
import com.shatteredpixel.yasd.general.levels.interactive.Exit;
import com.shatteredpixel.yasd.general.levels.painters.Painter;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.levels.traps.TenguDartTrap;
import com.shatteredpixel.yasd.general.levels.traps.Trap;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.plants.Plant;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.tiles.CustomTilemap;
import com.shatteredpixel.yasd.general.tiles.DungeonTilemap;
import com.shatteredpixel.yasd.general.ui.TargetHealthIndicator;
import com.shatteredpixel.yasd.general.utils.BArray;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Group;
import com.watabou.noosa.Tilemap;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;

import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.CHASM;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.DOOR;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.EMPTY;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.EMPTY_SP;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.ENTRANCE;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.EXIT;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.LOCKED_DOOR;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.SECRET_DOOR;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.WALL;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.WALL_DECO;

public class NewPrisonBossLevel extends Level {
	
	{
		color1 = 0x6a723d;
		color2 = 0x88924c;
		
		//the player should be able to see all of Tengu's arena
		viewDistance = 12;
	}
	
	public enum State {
		START,
		FIGHT_START,
		TRAP_MAZES,
		FIGHT_ARENA,
		WON
	}

	@Override
	public int getScaleFactor() {
		return new PrisonLevel().getScaleFactor();
	}

	private State state;
	private NewTengu tengu;
	
	public State state(){
		return state;
	}
	
	@Override
	public String tilesTex() {
		return Assets.TILES_PRISON;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_PRISON;
	}

	@Override
	public String loadImg() {
		return Assets.LOADING_PRISON;
	}

	private static final String STATE	        = "state";
	private static final String TENGU	        = "tengu";
	private static final String STORED_ITEMS    = "storeditems";
	private static final String TRIGGERED       = "triggered";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put( STATE, state );
		bundle.put( TENGU, tengu );
		bundle.put( STORED_ITEMS, storedItems);
		bundle.put(TRIGGERED, triggered );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		state = bundle.getEnum( STATE, State.class );
		
		//in some states tengu won't be in the world, in others he will be.
		if (state == State.START || state == State.TRAP_MAZES) {
			tengu = (NewTengu)bundle.get( TENGU );
		} else {
			for (Mob mob : mobs){
				if (mob instanceof NewTengu) {
					tengu = (NewTengu) mob;
					break;
				}
			}
		}
		
		for (Bundlable item : bundle.getCollection(STORED_ITEMS)){
			storedItems.add( (Item)item );
		}
		
		triggered = bundle.getBooleanArray(TRIGGERED);
	}
	
	@Override
	protected boolean build() {
		setSize(32, 32);
		
		state = State.START;
		setMapStart();
		
		return true;
	}
	
	private static final int ENTRANCE_POS = 10 + 4*32;
	private static final Rect entranceRoom = new Rect(8, 2, 13, 8);
	private static final Rect startHallway = new Rect(9, 7, 12, 24);
	private static final Rect[] startCells = new Rect[]{ new Rect(5, 9, 10, 16), new Rect(11, 9, 16, 16),
	                                         new Rect(5, 15, 10, 22), new Rect(11, 15, 16, 22)};
	private static final Rect tenguCell = new Rect(6, 23, 15, 32);
	private static final Point tenguCellCenter = new Point(10, 27);
	private static final Point tenguCellDoor = new Point(10, 23);
	private static final Point[] startTorches = new Point[]{ new Point(10, 2),
	                                       new Point(7, 9), new Point(13, 9),
	                                       new Point(7, 15), new Point(13, 15),
	                                       new Point(8, 23), new Point(12, 23)};
	
	private void setMapStart(){
		clearExitEntrance();
		interactiveAreas.add(new Entrance().setPos(this, ENTRANCE_POS));
		
		Painter.fill(this, 0, 0, 32, 32, WALL);
		
		//Start
		Painter.fill(this, entranceRoom, WALL);
		Painter.fill(this, entranceRoom, 1, EMPTY);
		Painter.set(this, getEntrancePos(), ENTRANCE);
		
		Painter.fill(this, startHallway, WALL);
		Painter.fill(this, startHallway, 1, EMPTY);
		
		Painter.set(this, startHallway.left+1, startHallway.top, DOOR);
		
		for (Rect r : startCells){
			Painter.fill(this, r, WALL);
			Painter.fill(this, r, 1, EMPTY);
		}
		
		Painter.set(this, startHallway.left, startHallway.top+5, DOOR);
		Painter.set(this, startHallway.right-1, startHallway.top+5, DOOR);
		Painter.set(this, startHallway.left, startHallway.top+11, DOOR);
		Painter.set(this, startHallway.right-1, startHallway.top+11, DOOR);
		
		Painter.fill(this, tenguCell, WALL);
		Painter.fill(this, tenguCell, 1, EMPTY);
		
		Painter.set(this, tenguCell.left+4, tenguCell.top, LOCKED_DOOR);
		
		drop(new IronKey(key), randomPrisonCellPos());
		
		for (Point p : startTorches){
			Painter.set(this, p, WALL_DECO);
		}
	}
	
	private static final Rect mazeHallway = new Rect(9, 6, 12, 24);
	private static final Rect[] mazeCells = new Rect[]{ new Rect(1, 9, 10, 16), new Rect(11, 9, 20, 16),
	                                                    new Rect(3, 15, 10, 22), new Rect(11, 15, 18, 22)};
	private static final Point[] mazeKeySpawns = new Point[]{new Point(mazeCells[0].left+1, mazeCells[0].top+3),
	                                                         new Point(mazeCells[1].right-2, mazeCells[1].top+3),
	                                                         new Point(mazeCells[2].left+1, mazeCells[2].top+3),
	                                                         new Point(mazeCells[3].right-2, mazeCells[3].top+3)};
	private static final Point[] mazeCellDoors = new Point[]{new Point(mazeCells[0].right-1, mazeCells[0].top+3),
	                                                         new Point(mazeCells[1].left, mazeCells[1].top+3),
	                                                         new Point(mazeCells[2].right-1, mazeCells[2].top+3),
	                                                         new Point(mazeCells[3].left, mazeCells[3].top+3)};
	private static final Point[] mazeTorches = new Point[]{  new Point(5, 9), new Point(15, 9),
	                                                         new Point(6, 15), new Point(14, 15),
	                                                         new Point(8, 23), new Point(12, 23)};
	
	private void setMapMazes(){
		clearExitEntrance();
		
		Painter.fill(this, 0, 0, 32, 32, WALL);
		
		Painter.fill(this, mazeHallway, WALL);
		Painter.fill(this, mazeHallway, 1, EMPTY);
		
		for (Rect r : mazeCells){
			Painter.fill(this, r, WALL);
			Painter.fill(this, r, 1, EMPTY);
		}
		
		for (Point p : mazeCellDoors){
			Painter.set(this, p, DOOR);
		}
		
		Painter.fill(this, tenguCell, WALL);
		Painter.fill(this, tenguCell, 1, EMPTY);
		
		Painter.set(this, tenguCell.left+4, tenguCell.top, DOOR);
		
		Painter.set(this, mazeHallway.left+1, mazeHallway.top+2, LOCKED_DOOR);
		Painter.set(this, mazeHallway.left+1, mazeHallway.top+4, LOCKED_DOOR);
		Painter.set(this, mazeHallway.left+1, mazeHallway.top+8, LOCKED_DOOR);
		Painter.set(this, mazeHallway.left+1, mazeHallway.top+10, LOCKED_DOOR);
		
		for (Point p : mazeKeySpawns){
			drop(new IronKey(key), pointToCell(p));
		}
		
		for (Point p : mazeTorches){
			Painter.set(this, p, WALL_DECO);
		}
	}
	
	private static final Rect arena = new Rect(3, 1, 18, 16);
	
	private void setMapArena(){
		clearExitEntrance();
		
		Painter.fill(this, 0, 0, 32, 32, WALL);
		
		Painter.fill(this, arena, WALL);
		Painter.fillEllipse(this, arena, 1, EMPTY);
	
	}
	
	private static Terrain W = WALL;
	private static Terrain D = WALL_DECO;
	private static Terrain e = EMPTY;
	private static Terrain s = EMPTY_SP;
	private static Terrain E = EXIT;
	private static Terrain C = CHASM;
	private static Terrain H = SECRET_DOOR;
	
	private static final Point endStart = new Point( startHallway.left+2, startHallway.top+2);
	private static final Point levelExit = new Point( endStart.x+12, endStart.y+6);
	private static final Terrain[] endMap = new Terrain[]{
			W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, e, e, e, W, W, W, W, W, W, W, W, W, W,
			W, e, e, e, e, e, e, e, e, W, W, W, W, W,
			e, e, e, e, e, e, e, e, e, e, e, e, W, W,
			e, e, e, e, e, e, e, e, e, e, e, e, e, W,
			e, e, e, C, C, C, C, C, C, C, C, e, e, W,
			e, W, C, C, C, C, C, C, C, C, C, E, E, W,
			e, e, e, C, C, C, C, C, C, C, C, E, E, W,
			e, e, e, e, e, C, C, C, C, C, C, E, E, W,
			e, e, e, e, e, e, C, C, W, W, C, C, C, W,
			W, e, e, e, e, e, W, H, W, W, C, C, C, W,
			W, e, e, e, e, W, W, e, W, W, W, C, C, W,
			W, W, W, W, W, W, W, e, W, W, W, C, C, W,
			W, W, W, W, W, W, W, e, W, W, W, C, C, W,
			W, D, W, W, W, W, W, e, W, W, W, C, C, W,
			e, e, e, W, W, W, W, e, W, W, W, C, C, W,
			e, e, e, W, W, W, W, e, W, W, W, C, C, W,
			e, e, e, W, W, W, e, e, e, W, W, C, C, W,
			e, e, e, W, W, D, s, e, s, W, W, C, C, W,
			e, e, e, W, W, W, e, C, e, W, W, C, C, W,
			e, e, e, W, W, W, e, e, e, W, W, C, C, W,
			e, e, e, W, W, W, W, W, W, W, W, C, C, W,
			W, W, W, W, W, W, W, W, W, W, W, C, C, W
	};
	
	private void setMapEnd(){
		
		Painter.fill(this, 0, 0, 32, 32, WALL);
		
		setMapStart();
		
		for (Heap h : heaps.valueList()){
			if (h.peek() instanceof IronKey){
				h.destroy();
			}
		}
		
		CustomTilemap vis = new exitVisual();
		vis.pos(11, 10);
		customTiles.add(vis);
		GameScene.add(vis, false);
		
		vis = new exitVisualWalls();
		vis.pos(11, 10);
		customWalls.add(vis);
		GameScene.add(vis, true);
		
		Painter.set(this, tenguCell.left+4, tenguCell.top, DOOR);

		drop(Generator.random(), 22 + width()*18).type = Heap.Type.CHEST;
		
		int cell = pointToCell(endStart);
		int i = 0;
		while (cell < length()){
			System.arraycopy(endMap, i, map, cell, 14);
			i += 14;
			cell += width();
		}
		
		int exit = pointToCell(levelExit);
		interactiveAreas.add(new Exit().setPos(this, exit));
	}
	
	//keep track of removed items as the level is changed. Dump them back into the level at the end.
	private ArrayList<Item> storedItems = new ArrayList<>();
	
	private void clearEntities(Rect safeArea){
		for (Heap heap : heaps.valueList()){
			if (safeArea == null || !safeArea.inside(cellToPoint(heap.pos))){
				storedItems.addAll(heap.items);
				heap.destroy();
			}
		}
		
		for (HeavyBoomerang.CircleBack b : Dungeon.hero.buffs(HeavyBoomerang.CircleBack.class)){
			if (safeArea == null || !safeArea.inside(cellToPoint(b.returnPos()))){
				storedItems.add(b.cancel());
			}
		}
		
		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
			if (mob != tengu && (safeArea == null || !safeArea.inside(cellToPoint(mob.pos)))){
				mob.destroy();
				if (mob.sprite != null)
					mob.sprite.killAndErase();
			}
		}
		for (Plant plant : plants.valueList()){
			if (safeArea == null || !safeArea.inside(cellToPoint(plant.pos))){
				plants.remove(plant.pos);
			}
		}
	}
	
	private void cleanMapState(){
		buildFlagMaps();
		cleanWalls();
		
		BArray.setFalse(visited);
		BArray.setFalse(mapped);
		
		for (Blob blob: blobs.values()){
			blob.fullyClear();
		}
		addVisuals(); //this also resets existing visuals
		traps.clear();
		
		GameScene.resetMap();
		Dungeon.observe();
	}
	
	@Override
	public Group addVisuals() {
		super.addVisuals();
		PrisonLevel.addPrisonVisuals(this, visuals);
		return visuals;
	}
	
	public void progress(){
		switch (state){
			case START:
				
				int tenguPos = pointToCell(tenguCellCenter);
				
				//if something is occupying Tengu's space, try to put him in an adjacent cell
				if (Actor.findChar(tenguPos) != null){
					ArrayList<Integer> candidates = new ArrayList<>();
					for (int i : PathFinder.NEIGHBOURS8){
						if (Actor.findChar(tenguPos + i) == null){
							candidates.add(tenguPos + i);
						}
					}
					
					if (!candidates.isEmpty()){
						tenguPos = Random.element(candidates);
					//if there are no adjacent cells, wait and do nothing
					} else {
						return;
					}
				}
				
				seal();
				set(pointToCell(tenguCellDoor), LOCKED_DOOR);
				GameScene.updateMap(pointToCell(tenguCellDoor));
				
				for (Mob m : mobs){
					//bring the first ally with you
					if (m.alignment == Char.Alignment.ALLY && !m.properties().contains(Char.Property.IMMOVABLE)){
						m.pos = pointToCell(tenguCellDoor); //they should immediately walk out of the door
						m.sprite.place(m.pos);
						break;
					}
				}
				
				tengu.state = tengu.HUNTING;
				tengu.pos = tenguPos;
				GameScene.add( tengu );
				tengu.notice();
				
				state = State.FIGHT_START;
				break;
				
			case FIGHT_START:
				
				clearEntities( tenguCell ); //clear anything not in tengu's cell

				setMapMazes();
				cleanMapState();
				
				Actor.remove(tengu);
				mobs.remove(tengu);
				TargetHealthIndicator.instance.target(null);
				tengu.sprite.kill();
				
				GameScene.flash(0xFFFFFF);
				Sample.INSTANCE.play(Assets.SND_BLAST);
				
				state = State.TRAP_MAZES;
				break;
				
			case TRAP_MAZES:
				
				Dungeon.hero.interrupt();
				
				clearEntities( arena ); //clear anything not in the arena
				
				setMapArena();
				cleanMapState();
				
				tengu.state = tengu.HUNTING;
				tengu.pos = (arena.left + arena.width()/2) + width()*(arena.top+2);
				GameScene.add(tengu);
				tengu.notice();
				
				GameScene.flash(0xFFFFFF);
				Sample.INSTANCE.play(Assets.SND_BLAST);
				
				state = State.FIGHT_ARENA;
				break;
				
			case FIGHT_ARENA:
				
				unseal();
				
				Dungeon.hero.interrupt();
				Dungeon.hero.pos = tenguCell.left+4 + (tenguCell.top+2)*width();
				Dungeon.hero.sprite.interruptMotion();
				Dungeon.hero.sprite.place(Dungeon.hero.pos);
				Camera.main.snapTo(Dungeon.hero.sprite.center());
				
				tengu.pos = pointToCell(tenguCellCenter);
				tengu.sprite.place(tengu.pos);
				
				//remove all mobs, but preserve allies
				ArrayList<Mob> allies = new ArrayList<>();
				for(Mob m : mobs.toArray(new Mob[0])){
					if (m.alignment == Char.Alignment.ALLY && !m.properties().contains(Char.Property.IMMOVABLE)){
						allies.add(m);
						mobs.remove(m);
					}
				}
				
				setMapEnd();
				
				for (Mob m : allies){
					do{
						m.pos = randomTenguCellPos();
					} while (findMob(m.pos) != null);
					if (m.sprite != null) m.sprite.place(m.pos);
					mobs.add(m);
				}
				
				tengu.die(new Char.DamageSrc(Element.META, Dungeon.hero));
				
				clearEntities(tenguCell);
				cleanMapState();
				
				for (Item item : storedItems) {
					if (!(item instanceof NewTengu.BombAbility.BombItem)
						&& !(item instanceof NewTengu.ShockerAbility.ShockerItem)) {
						drop(item, randomTenguCellPos());
					}
				}
				
				GameScene.flash(0xFFFFFF);
				Sample.INSTANCE.play(Assets.SND_BLAST);
				
				state = State.WON;
				break;
		}
	}
	
	private boolean[] triggered = new boolean[]{false, false, false, false};
	
	@Override
	public void occupyCell( Char ch) {
		super.occupyCell(ch);
		
		if (ch == Dungeon.hero){
			switch (state){
				case START:
					if (cellToPoint(ch.pos).y > tenguCell.top){
						progress();
					}
					break;
				case TRAP_MAZES:
					
					for (int i = 0; i < mazeCellDoors.length; i++){
						if (ch.pos == pointToCell(mazeCellDoors[i]) && !triggered[i]){
							triggered[i] = true;
							Maze.allowDiagonals = true;
							boolean[][] maze;
							boolean validMaze;
							
							do {
								maze = Maze.generate(mazeCells[i], map, width(), WALL);
								
								//prevents a maze that is just a straight line from the door
								validMaze = false;
								for (int x = 1; x < maze.length-1; x++){
									if (maze[x][3]){
										int cell = mazeCells[i].left+x + width()*(mazeCells[i].top+3);
										if (heaps.get(cell) == null
												&& Blob.volumeAt(cell, StormCloud.class) == 0
												&& Blob.volumeAt(cell, Regrowth.class) <= 9){
											validMaze = true;
											break;
										}
									}
								}
								
							} while (!validMaze);
							
							for (int x = 1; x < maze.length-1; x++) {
								for (int y = 1; y < maze[0].length-1; y++) {
									if (maze[x][y]){
										int cell = mazeCells[i].left+x + width()*(mazeCells[i].top+y);
										if (heaps.get(cell) == null){
											setTrap(new TenguDartTrap().hide(), cell);
											//Painter.set(this, cell, SECRET_TRAP);
											CellEmitter.get(cell).burst(Speck.factory(Speck.LIGHT), 2);
										}
									}
								}
							}
							
							FadingTraps f = new FadingTraps();
							f.setCoveringArea(mazeCells[i]);
							f.fadeDelay = 2f;
							GameScene.add(f, false);
							customTiles.add(f);
							
							Sample.INSTANCE.play(Assets.SND_TELEPORT);
							int roomCenter = (mazeCells[i].left + mazeCells[i].right)/2 +
									(mazeCells[i].top + mazeCells[i].bottom)/2 * width();
							Camera.main.panTo(DungeonTilemap.tileCenterToWorld(roomCenter), 5f);
							
							Dungeon.hero.interrupt();
						}
					}
					
					if (cellToPoint(ch.pos).y <= mazeHallway.top+2){
						progress();
					}
					break;
			}
		}
	}
	
	@Override
	protected void createMobs() {
		tengu = Mob.create(NewTengu.class, this); //We want to keep track of tengu independently of other mobs, he's not always in the level.
	}
	
	public Actor respawner() {
		return null;
	}
	
	@Override
	protected void createItems() {
		Item item = Bones.get();
		if (item != null) {
			drop( item, randomRespawnCell() ).setHauntedIfCursed().type = Heap.Type.REMAINS;
		}
	}
	
	private int randomPrisonCellPos(){
		Rect room = startCells[Random.Int(startCells.length)];
		
		return Random.IntRange(room.left+1, room.right-2)
				+ width()*Random.IntRange(room.top+1, room.bottom-2);
	}
	
	public int randomTenguCellPos(){
		return Random.IntRange(tenguCell.left+1, tenguCell.right-2)
				+ width()*Random.IntRange(tenguCell.top+1, tenguCell.bottom-2);
	}
	
	public void cleanTenguCell(){
		
		traps.clear();
		Painter.fill(this, tenguCell, 1, EMPTY);
		buildFlagMaps();
		
	}
	
	public void placeTrapsInTenguCell(float fill){
		
		Point tenguPoint = cellToPoint(tengu.pos);
		Point heroPoint = cellToPoint(Dungeon.hero.pos);
		
		PathFinder.setMapSize(7, 7);
		
		int tenguPos = tenguPoint.x-(tenguCell.left+1) + (tenguPoint.y-(tenguCell.top+1))*7;
		int heroPos = heroPoint.x-(tenguCell.left+1) + (heroPoint.y-(tenguCell.top+1))*7;
		
		boolean[] trapsPatch;
		
		do {
			trapsPatch = Patch.generate(7, 7, fill, 0, false);
			
			PathFinder.buildDistanceMap(tenguPos, BArray.not(trapsPatch, null));
		} while (PathFinder.distance[heroPos] > 6);
		
		PathFinder.setMapSize(width(), height());
		
		for (int i = 0; i < trapsPatch.length; i++){
			if (trapsPatch[i]) {
				int x = i % 7;
				int y = i / 7;
				int cell = x+tenguCell.left+1 + (y+tenguCell.top+1)*width();
				if (Blob.volumeAt(cell, StormCloud.class) == 0
						&& Blob.volumeAt(cell, Regrowth.class) <= 9) {
					setTrap(new TenguDartTrap().hide(), cell);
					CellEmitter.get(cell).burst(Speck.factory(Speck.LIGHT), 2);
				}
			}
		}
		
		GameScene.updateMap();
		
		FadingTraps t = new FadingTraps();
		t.fadeDelay = 2f;
		t.setCoveringArea(tenguCell);
		GameScene.add(t, false);
		customTiles.add(t);
	}
	
	@Override
	public int randomRespawnCell(Char ch) {
		int pos = ENTRANCE_POS; //random cell adjacent to the entrance.
		int cell;
		do {
			cell = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
		} while (!passable(cell)
				|| !Char.canOccupy(ch, this, cell)
				|| Actor.findChar(cell) != null);
		return cell;
	}
	
	@Override
	public String tileName( Terrain tile ) {
		switch (tile) {
			case WATER:
				return Messages.get(PrisonLevel.class, "water_name");
			default:
				return super.tileName( tile );
		}
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
	
	//TODO consider making this external to the prison boss level
	public static class FadingTraps extends CustomTilemap {
		
		{
			texture = Assets.TERRAIN_FEATURES;
		}
		
		Rect area;
		
		private float fadeDuration = 1f;
		private float initialAlpha = .4f;
		private float fadeDelay = 0f;
		
		public void setCoveringArea(Rect area){
			tileX = area.left;
			tileY = area.top;
			tileH = area.bottom - area.top;
			tileW = area.right - area.left;
			
			this.area = area;
		}
		
		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			int[] data = new int[tileW*tileH];
			int cell;
			Trap t;
			int i = 0;
			for (int y = tileY; y < tileY + tileH; y++){
				cell = tileX + y*Dungeon.level.width();
				for (int x = tileX; x < tileX + tileW; x++){
					t = Dungeon.level.traps.get(cell);
					if (t != null){
						data[i] = t.color + t.shape*16;
					} else {
						data[i] = -1;
					}
					cell++;
					i++;
				}
			}
			
			v.map( data, tileW );
			setFade();
			return v;
		}
		
		@Override
		public String name(int tileX, int tileY) {
			int cell = (this.tileX+tileX) + Dungeon.level.width()*(this.tileY+tileY);
			if (Dungeon.level.traps.get(cell) != null){
				return Messages.titleCase(Dungeon.level.traps.get(cell).name);
			}
			return super.name(tileX, tileY);
		}
		
		@Override
		public String desc(int tileX, int tileY) {
			int cell = (this.tileX+tileX) + Dungeon.level.width()*(this.tileY+tileY);
			if (Dungeon.level.traps.get(cell) != null){
				return Dungeon.level.traps.get(cell).desc();
			}
			return super.desc(tileX, tileY);
		}
		
		private void setFade( ){
			if (vis == null){
				return;
			}
			
			vis.alpha( initialAlpha );
			Actor.addDelayed(new Actor() {
				
				{
					actPriority = HERO_PRIO+1;
				}
				
				@Override
				protected boolean act() {
					Actor.remove(this);
					
					if (vis != null && vis.parent != null) {
						Dungeon.level.customTiles.remove(FadingTraps.this);
						vis.parent.add(new AlphaTweener(vis, 0f, fadeDuration) {
							@Override
							protected void onComplete() {
								super.onComplete();
								vis.killAndErase();
								killAndErase();
							}
						});
					}
					
					return true;
				}
			}, fadeDelay);
		}
		
	}
	
	public static class exitVisual extends CustomTilemap {
		
		{
			texture = Assets.PRISON_EXIT_NEW;
			
			tileW = 14;
			tileH = 11;
		}
		
		final int TEX_WIDTH = 256;
		
		private static byte[] render = new byte[]{
				0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0,
				1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0,
				1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
				1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
				1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
				1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0,
				1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0,
				1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0,
				0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 0,
				0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0
		};
		
		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			int[] data = mapSimpleImage(0, 0, TEX_WIDTH);
			for (int i = 0; i < data.length; i++){
				if (render[i] == 0) data[i] = -1;
			}
			v.map(data, tileW);
			return v;
		}
		
		@Override
		public void restoreFromBundle( Bundle bundle) {
			super.restoreFromBundle(bundle);
			tileX = 11;
			tileY = 10;
			tileW = 14;
			tileH = 11;
		}
	}
	
	public static class exitVisualWalls extends CustomTilemap {
		
		{
			texture = Assets.PRISON_EXIT_NEW;
			
			tileW = 14;
			tileH = 22;
		}
		
		final int TEX_WIDTH = 256;
		
		private static byte[] render = new byte[]{
				0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
				0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1,
				0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1,
				1, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1, 1,
				0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1,
				0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 1,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1
		};
		
		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			int[] data = mapSimpleImage(0, 10, TEX_WIDTH);
			for (int i = 0; i < data.length; i++){
				if (render[i] == 0) data[i] = -1;
			}
			v.map(data, tileW);
			return v;
		}
		
		@Override
		public void restoreFromBundle( Bundle bundle) {
			super.restoreFromBundle(bundle);
			tileX = 11;
			tileY = 10;
			tileW = 14;
			tileH = 22;
		}
		
	}
	
}
