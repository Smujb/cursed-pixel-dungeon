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

package com.shatteredpixel.yasd.general;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.items.Heap;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.levels.Level;
import com.shatteredpixel.yasd.general.levels.interactive.Entrance;
import com.shatteredpixel.yasd.general.levels.interactive.Exit;
import com.shatteredpixel.yasd.general.levels.interactive.InteractiveArea;
import com.shatteredpixel.yasd.general.levels.interactive.LevelSwitchArea;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.watabou.utils.Random;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MapHandler {

	private static final int TILE_WIDTH = 16;//16*16 tile size

	private static final String TILES_LAYER = "tiles";
	private static final String MOBS_LAYER = "mobs";
	private static final String ITEMS_LAYER = "items";
	private static final String AREAS_LAYER = "areas";

	private static TiledMapTileLayer tiles;
	private static MapObjects mobs;
	private static MapObjects items;
	private static MapObjects areas;

	private static void loadMap(Map map) {
		tiles = (TiledMapTileLayer) map.getLayers().get(TILES_LAYER);
		mobs = map.getLayers().get(MOBS_LAYER).getObjects();
		items = map.getLayers().get(ITEMS_LAYER).getObjects();
		areas = map.getLayers().get(AREAS_LAYER).getObjects();
	}

	public static boolean build( Level level, String mapName) {
		loadMap(new TmxMapLoader().load(mapName));
		int width = tiles.getWidth();
		int height = tiles.getHeight();
		level.setSize(width, height);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Terrain toSet;
				int pos = level.getPos(x, width-y);
				TiledMapTileLayer.Cell cell = tiles.getCell(x, y);
				if (cell == null) {
					toSet = Terrain.CHASM;
				} else {
					TiledMapTile tile = cell.getTile();
					int tileId = tile.getId();
					toSet = mapToTerrain(tileId);
					if (toSet == Terrain.ENTRANCE) {
						level.interactiveAreas.add(new Entrance().setPos(level, pos));
					} else if (toSet == Terrain.EXIT || toSet == Terrain.LOCKED_EXIT || toSet == Terrain.UNLOCKED_EXIT) {
						level.interactiveAreas.add(new Exit().setPos(level, pos));
					}
				}
				level.set(pos, toSet);
			}
		}
		return true;
	}

	public static ArrayList<Integer> occupyingCells(Rectangle rect, Level level) {
		ArrayList<Integer> objectCells = new ArrayList<>();
		int rectX = Math.round(rect.x/TILE_WIDTH);
		int rectY = Math.round(rect.y/TILE_WIDTH);
		int rectWidth = Math.round(rect.width/TILE_WIDTH);
		int rectHeight = Math.round(rect.height/TILE_WIDTH);
		for (int x = rectX; x < rectX + rectWidth; x++) {
			for (int y = rectY; y < rectY + rectHeight; y++) {
				objectCells.add(level.getPos(x, level.height()-y));
			}
		}
		return objectCells;
	}

	private static final String KEY_NAME = "className";
	private static final String KEY_NUMBER = "number";


	private static final String KEY_LEVEL = "level";
	private static final String NAME_MOB = "com.shatteredpixel.yasd.general.actors.mobs.";

	public static void createMobs( Level level, String mapName) {
		loadMap(new TmxMapLoader().load(mapName));
		for (int i = 0; i < mobs.getCount(); i++) {
			if (mobs.get(i) instanceof RectangleMapObject) {
				RectangleMapObject object = (RectangleMapObject) mobs.get(i);
				Rectangle rect = object.getRectangle();
				ArrayList<Integer> objectCells = occupyingCells(rect, level);
				MapProperties properties = object.getProperties();
				if (properties.containsKey(KEY_NAME) && properties.containsKey(KEY_NUMBER) && properties.containsKey(KEY_LEVEL)) {
					String name = (String) properties.get(KEY_NAME);
					int number = (int) properties.get(KEY_NUMBER);
					int mobLvl = (int) properties.get(KEY_LEVEL);
					if (number > objectCells.size()) {
						number = objectCells.size();
					}
					for (int j = 0; j < number; j++) {
						try {
							Class <? extends Mob> mobClass = (Class<? extends Mob>) Class.forName( NAME_MOB + name );
							Mob mob;
							if (mobLvl > 0) {//Default to level scale factor if mobLvl isn't defined
								mob = Mob.create(mobClass, mobLvl);
							} else {
								mob = Mob.create(mobClass, level);
							}
							if (!objectCells.isEmpty()) {
								do {
									if (objectCells.isEmpty()) {
										break;
									}
									int num = Random.Int(objectCells.size());
									mob.pos = objectCells.remove(num);
								} while (!level.passable(mob.pos));
								level.mobs.add(mob);
							}
						} catch (ClassNotFoundException e) {
							throw new RuntimeException(e);
						}
					}
				}
			}
		}
	}

	//Ues the same KEY_NAME and KEY_NUMBER
	private static final String KEY_TYPE = "heapType";
	private static final String NAME_ITEM = "com.shatteredpixel.yasd.general.items.";

	public static void createItems( Level level, String mapName) {
		loadMap(new TmxMapLoader().load(mapName));
		Dungeon.key = level.key;//Ensures keys load correctly.
		for (int i = 0; i < items.getCount(); i++) {
			if (items.get(i) instanceof RectangleMapObject) {
				RectangleMapObject object = (RectangleMapObject) items.get(i);
				Rectangle rect = object.getRectangle();
				ArrayList<Integer> objectCells = occupyingCells(rect, level);
				MapProperties properties = object.getProperties();
				if (properties.containsKey(KEY_NAME) && properties.containsKey(KEY_NUMBER)) {
					Heap.Type type = Heap.Type.HEAP;
					if (properties.containsKey(KEY_TYPE)) {
						type = Enum.valueOf(Heap.Type.class, (String) properties.get(KEY_TYPE));
					}
					String className = (String) properties.get(KEY_NAME);
					int quantity = (int) properties.get(KEY_NUMBER);
					for (int j = 0; j < quantity; j++) {
						Item item;
						try {
							Class<? extends Item> itemClass = (Class<? extends Item>) Class.forName(NAME_ITEM + className);
							item = itemClass.newInstance();
						} catch (ClassNotFoundException e) {
							throw new RuntimeException(e);
						} catch (IllegalAccessException e) {
							throw new RuntimeException(e);
						} catch (InstantiationException e) {
							throw new RuntimeException(e);
						}
						if (!objectCells.isEmpty()) {
							int pos;
							do {
								int num = Random.Int(objectCells.size());
								pos = objectCells.get(num);
							} while (!level.passable(pos));
							level.drop(item, pos).type = type;
						}
					}
				}
			}
		}
	}

	private static final String NAME_AREA = "com.shatteredpixel.yasd.general.levels.interactive.";
	//Level switch area
	private static final String KEY_POS = "pos";
	private static final String KEY_DEPTH = "depth";
	private static final String KEY_KEY = "key";
	private static final String KEY_MESSAGE = "message";

	public static void createAreas( Level level, String mapName) {
		loadMap(new TmxMapLoader().load(mapName));
		for (int i = 0; i < areas.getCount(); i++) {
			if (areas.get(i) instanceof RectangleMapObject) {
				RectangleMapObject object = (RectangleMapObject) areas.get(i);
				Rectangle rect = object.getRectangle();
				MapProperties properties = object.getProperties();
				if (properties.containsKey(KEY_NAME)) {
					String className = (String) properties.get(KEY_NAME);
					InteractiveArea area;
					try {
						Class<? extends InteractiveArea> areaClass = (Class<? extends InteractiveArea>) Class.forName(NAME_AREA + className);
						area = areaClass.newInstance();
					} catch (ClassNotFoundException e) {
						throw new RuntimeException(e);
					} catch (IllegalAccessException e) {
						throw new RuntimeException(e);
					} catch (InstantiationException e) {
						throw new RuntimeException(e);
					}
					if (area instanceof LevelSwitchArea) {
						LevelSwitchArea switchArea = ((LevelSwitchArea)area);
						int pos = -1;
						if (properties.containsKey(KEY_POS) && properties.get(KEY_POS) instanceof Integer) {
							pos = (int) properties.get(KEY_POS);
						}
						int depth = 1;
						if (properties.containsKey(KEY_DEPTH) && properties.get(KEY_DEPTH) instanceof Integer) {
							depth = (int) properties.get(KEY_DEPTH);
						}
						String key = Dungeon.keyForDepth(depth);
						if (properties.containsKey(KEY_KEY) && properties.get(KEY_KEY) instanceof String) {
							key = (String) properties.get(KEY_KEY);
						}
						String message = "";
						if (properties.containsKey(KEY_MESSAGE) && properties.get(KEY_MESSAGE) instanceof String) {
							message = Messages.get(level, (String) properties.get(KEY_MESSAGE));
						}
						area = switchArea.initVars(key, message, pos, depth);
					}
					area.setPos((int) rect.x/TILE_WIDTH, level.height()-(int) rect.y/TILE_WIDTH, Math.max(1, (int) rect.width/TILE_WIDTH), Math.max(1, (int) rect.height/TILE_WIDTH));
					level.interactiveAreas.add(area);
				}
			}
		}
	}

	@Contract(pure = true)
	private static Terrain mapToTerrain(int tile) {
		switch (tile) {
			case 0:
				return Terrain.CHASM;
			case 1:
				return Terrain.EMPTY;
			case 2:
				return Terrain.EMPTY_DECO;
			case 3:
				return Terrain.GRASS;
			case 4:
				return Terrain.EMBERS;
			case 5:
				return Terrain.EMPTY_SP;
			case 6:
				return Terrain.WATER;
			case 7:
				return Terrain.ENTRANCE;
			case 8:
				return Terrain.EXIT;
			case 9:
				return Terrain.EMPTY_WELL;
			case 10:
				return Terrain.WELL;
			case 11:
				return Terrain.PEDESTAL;
			case 13:
				return Terrain.WALL;
			case 14:
				return Terrain.WALL_DECO;
			case 15:
				return Terrain.BOOKSHELF;
			case 16:
				return Terrain.CRACKED_WALL;
			case 17:
				return Terrain.BARRICADE;
			case 19:
				return Terrain.DOOR;
			case 21:
				return Terrain.LOCKED_DOOR;
			case 22:
				return Terrain.UNLOCKED_EXIT;
			case 23:
				return Terrain.LOCKED_EXIT;
			case 24:
				return Terrain.BRONZE_LOCKED_DOOR;
			case 25:
				return Terrain.SIGN;
			case 26:
				return Terrain.STATUE;
			case 27:
				return Terrain.STATUE_SP;
			case 28:
				return Terrain.ALCHEMY;
			case 29:
				return Terrain.HIGH_GRASS;
			case 30:
				return Terrain.FURROWED_GRASS;

		}
		return Terrain.CHASM;
	}
}
