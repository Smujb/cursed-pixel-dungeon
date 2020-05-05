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
import com.shatteredpixel.yasd.general.Challenges;
import com.shatteredpixel.yasd.general.Constants;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.GamesInProgress;
import com.shatteredpixel.yasd.general.LevelHandler;
import com.shatteredpixel.yasd.general.MainGame;
import com.shatteredpixel.yasd.general.Statistics;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.blobs.Blob;
import com.shatteredpixel.yasd.general.actors.blobs.DarkGas;
import com.shatteredpixel.yasd.general.actors.blobs.SmokeScreen;
import com.shatteredpixel.yasd.general.actors.blobs.Web;
import com.shatteredpixel.yasd.general.actors.buffs.Awareness;
import com.shatteredpixel.yasd.general.actors.buffs.Blindness;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.LockedFloor;
import com.shatteredpixel.yasd.general.actors.buffs.MagicalSight;
import com.shatteredpixel.yasd.general.actors.buffs.MindVision;
import com.shatteredpixel.yasd.general.actors.buffs.Shadows;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.hero.HeroSubClass;
import com.shatteredpixel.yasd.general.actors.mobs.Bestiary;
import com.shatteredpixel.yasd.general.actors.mobs.Mimic;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.actors.mobs.Wraith;
import com.shatteredpixel.yasd.general.actors.mobs.YogFist;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.Sheep;
import com.shatteredpixel.yasd.general.effects.particles.FlowParticle;
import com.shatteredpixel.yasd.general.effects.particles.WindParticle;
import com.shatteredpixel.yasd.general.items.Generator;
import com.shatteredpixel.yasd.general.items.Heap;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.Torch;
import com.shatteredpixel.yasd.general.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.yasd.general.items.food.SmallRation;
import com.shatteredpixel.yasd.general.items.potions.PotionOfLevitation;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.yasd.general.items.stones.StoneOfEnchantment;
import com.shatteredpixel.yasd.general.items.stones.StoneOfIntuition;
import com.shatteredpixel.yasd.general.items.wands.WandOfWarding;
import com.shatteredpixel.yasd.general.levels.features.Chasm;
import com.shatteredpixel.yasd.general.levels.features.Door;
import com.shatteredpixel.yasd.general.levels.interactive.AscendArea;
import com.shatteredpixel.yasd.general.levels.interactive.DescendArea;
import com.shatteredpixel.yasd.general.levels.interactive.Entrance;
import com.shatteredpixel.yasd.general.levels.interactive.Exit;
import com.shatteredpixel.yasd.general.levels.interactive.InteractiveArea;
import com.shatteredpixel.yasd.general.levels.painters.Painter;
import com.shatteredpixel.yasd.general.levels.rooms.connection.BridgeRoom;
import com.shatteredpixel.yasd.general.levels.rooms.connection.ConnectionRoom;
import com.shatteredpixel.yasd.general.levels.rooms.connection.CrackedWallConnectionRoom;
import com.shatteredpixel.yasd.general.levels.rooms.connection.MazeConnectionRoom;
import com.shatteredpixel.yasd.general.levels.rooms.connection.NonHiddenMazeConnectionRoom;
import com.shatteredpixel.yasd.general.levels.rooms.connection.PerimeterRoom;
import com.shatteredpixel.yasd.general.levels.rooms.connection.PitConnectionRoom;
import com.shatteredpixel.yasd.general.levels.rooms.connection.RingBridgeRoom;
import com.shatteredpixel.yasd.general.levels.rooms.connection.RingTunnelRoom;
import com.shatteredpixel.yasd.general.levels.rooms.connection.TunnelRoom;
import com.shatteredpixel.yasd.general.levels.rooms.connection.WalkwayRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.AquariumRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.BurnedRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.CaveRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.CirclePitRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.EmptyRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.FissureRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.GrassyGraveRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.HallwayRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.MinefieldRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.PillarsRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.PlantsRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.PlatformRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.RingRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.RuinsRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.SegmentedRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.SewerPipeRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.SkullsRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.StandardRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.StatuesRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.StripedRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.StudyRoom;
import com.shatteredpixel.yasd.general.levels.rooms.standard.SuspiciousChestRoom;
import com.shatteredpixel.yasd.general.levels.terrain.CustomTerrain;
import com.shatteredpixel.yasd.general.levels.terrain.KindOfTerrain;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.levels.traps.Trap;
import com.shatteredpixel.yasd.general.mechanics.ShadowCaster;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.plants.Plant;
import com.shatteredpixel.yasd.general.plants.Swiftthistle;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.shatteredpixel.yasd.general.tiles.CustomTilemap;
import com.shatteredpixel.yasd.general.tiles.DungeonTileSheet;
import com.shatteredpixel.yasd.general.utils.BArray;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;
import com.watabou.utils.SparseArray;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.CHASM;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.DEEP_WATER;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.DOOR;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.EMBERS;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.EMPTY;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.EMPTY_DECO;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.EMPTY_SP;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.FURROWED_GRASS;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.GRASS;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.HIGH_GRASS;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.WALL;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.WALL_DECO;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.WATER;

public abstract class Level implements Bundlable {
	
	public enum Feeling {
		NONE,
		CHASM,
		WATER,
		GRASS,
		DARK,
		EVIL,
		OPEN,
		EMBER,
		DANGER
	}

	protected int width;
	protected int height;
	protected int length;

	private Class lastMob = null;

	public boolean hasExit = true;
	public boolean hasEntrance = true;

	public String key;
	
	private static final float TIME_TO_RESPAWN = 50;

	public int version;

	public KindOfTerrain[] map;
	public boolean[] visited;
	public boolean[] mapped;
	public boolean[] discoverable;

	public int viewDistance = Dungeon.isChallenged( Challenges.DARKNESS ) ? 2 : 6;
	
	public boolean[] heroFOV;


	int minScaleFactor = 0;
	int maxScaleFactor = -1;
	//By default, scales with hero level and has max and min defined within the individual levels. -1 max gives no limit.
	public int getScaleFactor() {
		int level;
		if (Dungeon.hero != null) {
			level = Dungeon.hero.levelToScaleFactor();
		} else {
			level = 1;
		}
		if (maxScaleFactor == -1) {
			return Math.max(minScaleFactor, level);
		} else {
			return (int) GameMath.gate(minScaleFactor, level, maxScaleFactor);
		}
	}

	public ArrayList<Integer> getTileLocations(Terrain terrain) {
		ArrayList<Integer> locations = new ArrayList<>();
		for (int i = 0; i < map.length; i++) {
			if (map[i] == terrain) {
				locations.add(i);
			}
		}
		return locations;
	}

	@NotNull
	@Contract(pure = true)
	public static Terrain[] basicMap(int size) {
		Terrain[] map = new Terrain[size];
		for (int i = 0; i < size; i++) {
			map[i] = EMPTY;
		}
		return map;
	}

	//NOTE: to avoid lag I recommend using passable(pos), losBlocking(pos), etc instead of passable()[pos], losBlocking()[pos], etc when possible.
	public boolean passable(int pos) {
		return map[pos].passable() & !avoid(pos);
	}

	@NotNull
	public final boolean[] passable() {
		boolean[] passable = new boolean[map.length];
		for (int i = 0; i < map.length; i++) {
			passable[i] = passable(i);
		}
		return passable;
	}

	public boolean losBlocking(int pos) {
		if (Blob.volumeAt(this, pos, DarkGas.class) > 0 || Blob.volumeAt(this, pos, SmokeScreen.class) > 0) {
			return true;
		}
		return map[pos].losBlocking();
	}

	@NotNull
	public final boolean[] losBlocking() {
		boolean[] losBlocking = new boolean[map.length];
		for (int i = 0; i < map.length; i++) {
			losBlocking[i] = losBlocking(i);
		}
		return losBlocking;
	}

	public boolean flammable(int pos) {
		return map[pos].flammable();
	}

	@NotNull
	public final boolean[] flammable() {
		boolean[] flammable = new boolean[map.length];
		for (int i = 0; i < map.length; i++) {
			flammable[i] = flammable(i);
		}
		return flammable;
	}

	public boolean secret(int pos) {
		if (traps.containsKey(pos) && !traps.get(pos).visible) {
			return true;
		}
		return map[pos].secret();
	}

	@NotNull
	public final boolean[] secret() {
		boolean[] secret = new boolean[map.length];
		for (int i = 0; i < map.length; i++) {
			secret[i] = secret(i);
		}
		return secret;
	}

	public boolean solid(int pos) {
		if (Blob.volumeAt(this, pos, Web.class) > 0) {
			return true;
		}
		return map[pos].solid();
	}

	@NotNull
	public final boolean[] solid() {
		boolean[] solid = new boolean[map.length];
		for (int i = 0; i < map.length; i++) {
			solid[i] = solid(i);
		}
		return solid;
	}

	public boolean avoid(int pos) {
		Trap trap = trap(pos);
		if (trap != null && trap.active && trap.visible) {
			return true;
		} else {
			return map[pos].avoid();
		}
	}

	@NotNull
	public final boolean[] avoid() {
		boolean[] avoid = new boolean[map.length];
		for (int i = 0; i < map.length; i++) {
			avoid[i] = avoid(i);
		}
		return avoid;
	}

	public boolean liquid(int pos) {
		return map[pos].liquid();
	}

	@NotNull
	public final boolean[] liquid() {
		boolean[] liquid = new boolean[map.length];
		for (int i = 0; i < map.length; i++) {
			liquid[i] = liquid(i);
		}
		return liquid;
	}

	public boolean pit(int cell) {
		return map[cell].pit();
	}

	@NotNull
	public final boolean[] pit() {
		boolean[] pit = new boolean[map.length];
		for (int i = 0; i < map.length; i++) {
			pit[i] = pit(i);
		}
		return pit;
	}

	public boolean openSpace(int cell) {
		if (solid(cell)){
			return false;
		} else {
			for (int j = 0; j < PathFinder.CIRCLE8.length; j++){
				if (solid(cell+PathFinder.CIRCLE8[j])) {
					return false;
				} else if (!solid(cell+PathFinder.CIRCLE8[(j+1)%8])
						&& !solid(cell+PathFinder.CIRCLE8[(j+2)%8])) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean[] openSpace() {
		boolean[] space = new boolean[map.length];
		for (int i = 0; i < map.length; i++) {
			space[i] = openSpace(i);
		}
		return space;
	}
	
	public Feeling feeling = Feeling.NONE;

	//when a boss level has become locked.
	public boolean locked = false;
	
	public HashSet<Mob> mobs;
	public SparseArray<Heap> heaps;
	public HashMap<Class<? extends Blob>,Blob> blobs;
	public SparseArray<Plant> plants;
	public ArrayList<InteractiveArea> interactiveAreas;
	public SparseArray<Trap> traps;
	public HashSet<CustomTilemap> customTiles;
	public HashSet<CustomTilemap> customWalls;
	
	protected ArrayList<Item> itemsToSpawn = new ArrayList<>();

	protected Group visuals;
	
	public int color1 = 0x004400;
	public int color2 = 0x88CC44;

	private static final String VERSION     = "version";
	private static final String WIDTH       = "width";
	private static final String HEIGHT      = "height";
	private static final String MAP			= "map";
	private static final String VISITED		= "visited";
	private static final String MAPPED		= "mapped";
	private static final String KEY			= "key";
	private static final String LOCKED      = "locked";
	private static final String HEAPS		= "heaps";
	private static final String PLANTS		= "plants";
	private static final String TRAPS       = "traps";
	private static final String CUSTOM_TILES= "customTiles";
	private static final String CUSTOM_WALLS= "customWalls";
	private static final String MOBS		= "mobs";
	private static final String BLOBS		= "blobs";
	private static final String FEELING		= "feeling";
	private static final String INTERACTIVE = "interactive-area";
	private static final String LAST_MOB    = "last-mob";

	public void create(String key) {

		this.key = key;

		Random.pushGenerator( Dungeon.seedCurDepth() );
		
		if (!Dungeon.bossLevel()) {

			if (Dungeon.isChallenged(Challenges.NO_FOOD)){
				addItemToSpawn( new SmallRation() );
			} else {
				addItemToSpawn(Generator.random(Generator.Category.FOOD));
			}

			if (Dungeon.isChallenged(Challenges.DARKNESS)){
				addItemToSpawn( new Torch() );
			}

			if (Dungeon.souNeeded()) {
				addItemToSpawn( new ScrollOfUpgrade() );
				Dungeon.LimitedDrops.UPGRADE_SCROLLS.count++;
			}

			if (Dungeon.esNeeded()) {
				addItemToSpawn( new StoneOfEnchantment() );
				Dungeon.LimitedDrops.ENCHANT_STONE.count++;
			}

			//one scroll of transmutation is guaranteed to spawn somewhere on chapter 2-4
			int enchChapter = (int)((Dungeon.seed / 10) % 3) + 1;
			if ( Dungeon.depth / Constants.CHAPTER_LENGTH == enchChapter &&
					Dungeon.seed % 4 + 1 == Dungeon.depth % Constants.CHAPTER_LENGTH){
				addItemToSpawn( new StoneOfEnchantment() );
			}
			
			if ( Dungeon.depth == ((Dungeon.seed % 3) + 1)){
				addItemToSpawn( new StoneOfIntuition() );
			}
			if (Dungeon.isChallenged(Challenges.COLLAPSING_FLOOR)) {
				addItemToSpawn( new PotionOfLevitation());
			}
			
			if (Dungeon.depth > 1) {
				switch (Random.Int( 10 )) {
					case 0:
						if (!Dungeon.bossLevel(Dungeon.depth + 1)) {
							feeling = Feeling.CHASM;
						}
						break;
					case 1:
						feeling = Feeling.WATER;
						break;
					case 2:
						feeling = Feeling.GRASS;
						break;
					case 3:
						feeling = Feeling.DARK;
						addItemToSpawn(new Torch());
						viewDistance = Math.round(viewDistance / 2f);
						break;
					case 4:
						feeling = Feeling.EVIL;
						break;
					case 5:
						feeling = Feeling.OPEN;
						break;
					case 6:
						feeling = Feeling.EMBER;
						break;
					case 7:
						feeling = Feeling.DANGER;
						break;
				}
			}
		}
		
		do {
			width = height = length = 0;

			mobs = new HashSet<>();
			heaps = new SparseArray<>();
			blobs = new HashMap<>();
			plants = new SparseArray<>();
			traps = new SparseArray<>();
			interactiveAreas = new ArrayList<>();
			customTiles = new HashSet<>();
			customWalls = new HashSet<>();
			
		} while (!build());
		
		buildFlagMaps();
		cleanWalls();
		
		createMobs();
		createItems();
		createAreas();

		Random.popGenerator();
	}
	
	public void setSize(int w, int h){
		
		width = w;
		height = h;
		length = w * h;
		
		map = new Terrain[length];
		Terrain terrain = WALL;
		if (feeling == Feeling.CHASM) {
			terrain = CHASM;
		} else if (feeling == Feeling.OPEN) {
			terrain = EMPTY;
		}
		Arrays.fill( map, terrain );
		
		visited     = new boolean[length];
		mapped      = new boolean[length];

		//openSpace   = new boolean[length];

		heroFOV     = new boolean[length];
		
		PathFinder.setMapSize(w, h);
	}
	
	public void reset() {
		
		for (Mob mob : mobs.toArray( new Mob[0] )) {
			if (!mob.reset()) {
				mobs.remove( mob );
			}
		}
		createMobs();
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {

		version = bundle.getInt( VERSION );
		
		//saves from before 0.6.5c are not supported
		if (version < MainGame.v0_6_5c){
			throw new RuntimeException("old save");
		}

		setSize( bundle.getInt(WIDTH), bundle.getInt(HEIGHT));
		
		mobs = new HashSet<>();
		heaps = new SparseArray<>();
		blobs = new HashMap<>();
		plants = new SparseArray<>();
		traps = new SparseArray<>();
		interactiveAreas = new ArrayList<>();
		customTiles = new HashSet<>();
		customWalls = new HashSet<>();

		lastMob = bundle.getClass(LAST_MOB);


		loadMap(bundle);
		//map		= bundle.getEnumArray( MAP, Terrain.class );

		visited	= bundle.getBooleanArray( VISITED );
		mapped	= bundle.getBooleanArray( MAPPED );

		key = bundle.getString(KEY);

		//entrance	= bundle.getInt( ENTRANCE );
		//exit		= bundle.getInt( EXIT );

		locked      = bundle.getBoolean( LOCKED );
		
		Collection<Bundlable> collection = bundle.getCollection( HEAPS );
		for (Bundlable h : collection) {
			Heap heap = (Heap)h;
			if (!heap.isEmpty())
				heaps.put( heap.pos, heap );
		}
		
		collection = bundle.getCollection( PLANTS );
		for (Bundlable p : collection) {
			Plant plant = (Plant)p;
			plants.put( plant.pos, plant );
		}

		collection = bundle.getCollection( TRAPS );
		for (Bundlable p : collection) {
			Trap trap = (Trap)p;
			traps.put( trap.pos, trap );
		}

		int x = 0;
		do {
			if (bundle.contains(INTERACTIVE + x)) {
				interactiveAreas.add((InteractiveArea) bundle.get(INTERACTIVE + x));
			} else {
				break;
			}
			x++;
		} while (true);

		collection = bundle.getCollection( CUSTOM_TILES );
		for (Bundlable p : collection) {
			CustomTilemap vis = (CustomTilemap)p;
			customTiles.add(vis);
		}

		collection = bundle.getCollection( CUSTOM_WALLS );
		for (Bundlable p : collection) {
			CustomTilemap vis = (CustomTilemap)p;
			customWalls.add(vis);
		}
		
		collection = bundle.getCollection( MOBS );
		for (Bundlable m : collection) {
			Mob mob = (Mob)m;
			if (mob != null) {
				mobs.add( mob );
			}
		}
		
		collection = bundle.getCollection( BLOBS );
		for (Bundlable b : collection) {
			Blob blob = (Blob)b;
			blobs.put( blob.getClass(), blob );
		}

		feeling = bundle.getEnum( FEELING, Feeling.class );
		if (feeling == Feeling.DARK)
			viewDistance = Math.round(viewDistance/2f);

		
		buildFlagMaps();
		cleanWalls();

		//compat with pre-0.8.0 saves
		for (Heap h : heaps.valueList()){
			if (h.type == Heap.Type.MIMIC){
				heaps.remove(h.pos);
				mobs.add(Mimic.spawnAt(h.pos, h.items, Mimic.class, this));
			}
		}
	}
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		bundle.put( VERSION, Game.versionCode );
		bundle.put( WIDTH, width );
		bundle.put( HEIGHT, height );
		bundle.put( LAST_MOB, lastMob );
		storeMap(bundle);
		//bundle.put( MAP, map );
		bundle.put( VISITED, visited );
		bundle.put( MAPPED, mapped );
		//bundle.put( ENTRANCE, entrance );
		//bundle.put( EXIT, exit );
		bundle.put( KEY, key );
		bundle.put( LOCKED, locked );
		bundle.put( HEAPS, heaps.valueList() );
		bundle.put( PLANTS, plants.valueList() );
		bundle.put( TRAPS, traps.valueList() );
		for (int x = 0; x < interactiveAreas.size(); x++) {
			bundle.put(INTERACTIVE + x, interactiveAreas.get(x));
		}
		bundle.put( CUSTOM_TILES, customTiles );
		bundle.put( CUSTOM_WALLS, customWalls );
		bundle.put( MOBS, mobs );
		bundle.put( BLOBS, blobs.values() );
		bundle.put( FEELING, feeling );
	}

	private void storeMap(Bundle bundle) {
		for (int i = 0; i < map.length; i++) {
			KindOfTerrain terrain = map[i];
			if (terrain instanceof Terrain) {
				bundle.put(MAP + i, ((Terrain) terrain).name());
			} else if (terrain instanceof CustomTerrain){
				bundle.put(MAP + i, (Bundlable) terrain);
			} else {
				bundle.put(MAP + i, CHASM);
			}
		}
	}

	private void loadMap(Bundle bundle) {
		for (int i = 0; i < length(); i++) {
			KindOfTerrain terrain;
			String name = bundle.getString(MAP + i);
			try {
				terrain = (KindOfTerrain) Class.forName(name.replace("class ", "")).newInstance();
				((CustomTerrain)terrain).restoreFromBundle(bundle);
			} catch (Exception e) {
				terrain = Enum.valueOf(Terrain.class, name);
			}
			map[i] = terrain;
		}
	}

	public void addMob(Mob mob) {
		if (MainGame.scene() instanceof GameScene && Dungeon.level == this) {
			GameScene.add(mob);
		} else {
			mobs.add(mob);
		}
	}


	public InteractiveArea findArea(int pos) {
		for (InteractiveArea area : interactiveAreas) {
			if (area.posInside(this, pos)) {
				return area;
			}
		}
		return null;
	}

	//Both are used for single-entrance/exit levels. Will only return first result. Use as little as possible unless it is important the return value is Exit or Entrance.
	public Entrance getEntrance() {
		ArrayList<Entrance> entrances = InteractiveArea.getAreas(this, Entrance.class);
		if (entrances.size() > 0) {
			return entrances.get(0);
		}
		return null;
	}

	public Exit getExit() {
		ArrayList<Exit> exits = InteractiveArea.getAreas(this, Exit.class);
		if (exits.size() > 0) {
			return exits.get(0);
		}
		return null;
	}

	//These functions should be used if looking for the exit cell (e.g. in randomRespawnCell or when deciding where to place the hero when spawining on a level).
	// *Not* functionally identical to getExit().centerCell() or getEntrance.centerCell() - it takes into account descend areas and should be used when possible.
	//getEntrancePos() nad getExitPos() are **not** final because some levels don't have an exit or entrance yet need somewhere to put the player.
	public int getEntrancePos() {
		InteractiveArea entrance = getEntrance();
		if (entrance == null) {
			ArrayList<AscendArea> ascendAreas = InteractiveArea.getAreas(this, AscendArea.class);
			if (ascendAreas.size() > 0) {
				return ascendAreas.get(0).centerCell(this);
			} else {
				return -1;
			}
		} else {
			return entrance.centerCell(this);
		}
	}

	public int getExitPos() {
		InteractiveArea exit = getExit();
		if (exit == null) {
			ArrayList<DescendArea> descendAreas = InteractiveArea.getAreas(this, DescendArea.class);
			if (descendAreas.size() > 0) {
				return descendAreas.get(0).centerCell(this);
			} else {
				return -1;
			}
		} else {
			return exit.centerCell(this);
		}
	}

	//These functions get rid of an existing exit/entrance and create a new one. Again, most useful in levels with a single cell exit/entrance.
	public void setEntrance(int pos) {
		Entrance entrance;
		do {
			entrance = getEntrance();
			if (entrance != null) {
				interactiveAreas.remove(entrance);
			}
		} while (entrance != null);
		if (pos > 0 && pos < map.length) {
			interactiveAreas.add(new Entrance().setPos(this, pos));
			map[pos] = Terrain.ENTRANCE;
		}
	}

	public void setExit(int pos) {
		Exit exit;
		do {
			exit = getExit();
			if (exit != null) {
				interactiveAreas.remove(exit);
			}
		} while (exit != null);
		if (pos > 0 && pos < map.length) {
			interactiveAreas.add(new Exit().setPos(this, pos));
			map[pos] = Terrain.EXIT;
		}
	}

	//Clears exits/entrances. This is used mainly in PrisonBossLevel
	void clearExitEntrance() {
		setEntrance(-1);
		setExit(-1);
	}

	public Terrain tunnelTile() {
		return (feeling == Feeling.CHASM || feeling == Feeling.OPEN) ? EMPTY_SP : EMPTY;
	}

	public Terrain grassTile( boolean tall ) {
		if (feeling == Level.Feeling.EMBER) {
			return tall ? FURROWED_GRASS : EMBERS;
		} else {
			return tall ? HIGH_GRASS : GRASS;
		}
	}

	public Terrain waterTile() {
		if (feeling == Feeling.EMBER) {
			return grassTile(false);
		} else {
			return WATER;
		}
	}

	public KindOfTerrain swapWaterAlts(int pos) {
		int above = pos + PathFinder.CIRCLE4[0];
		int right = pos + PathFinder.CIRCLE4[1];
		int below = pos + PathFinder.CIRCLE4[2];
		int left = pos + PathFinder.CIRCLE4[3];
		try {
			if (map[pos] == WATER &&
					DungeonTileSheet.deepWaterStitchable(map[above]) &
							DungeonTileSheet.deepWaterStitchable(map[below]) &
							DungeonTileSheet.deepWaterStitchable(map[left]) &
							DungeonTileSheet.deepWaterStitchable(map[right])) {
				return DEEP_WATER;

			}
		} catch (IndexOutOfBoundsException ignored) {}
		return map[pos];
	}

	public int width() {
		return width;
	}

	public int height() {
		return height;
	}

	public int length() {
		return length;
	}
	
	public String tilesTex() {
		return null;
	}
	
	public String waterTex() {
		return null;
	}

	public String loadImg() {
		return null;
	}
	
	abstract protected boolean build();
	
	//private ArrayList<Class<?extends Mob>> mobsToSpawn = new ArrayList<>();

	public Class<?>[] mobClasses() {
		return new Class[]{Wraith.class};
	}

	public float[] mobChances() {
		return new float[]{1};
	}
	
	public Mob createMob() {
		if (mobChances().length != mobClasses().length) {
			throw new AssertionError("Mob classes must be equal in length to mob chances!");
		}
		Class<? extends Mob> mob;
		do {
			int type = Random.chances(mobChances());
			mob = (Class<? extends Mob>) mobClasses()[type];
			mob = Bestiary.swapMobAlt(mob);
		} while ((mob == lastMob || mob.isInstance(lastMob)) & mobClasses().length > 1);
		lastMob = mob;
		return Mob.create(mob, this);
	}

	protected Class<?>[] connectionRoomClasses(){
		return new Class<?>[]{
				TunnelRoom.class,
				BridgeRoom.class,

				PerimeterRoom.class,
				WalkwayRoom.class,

				RingTunnelRoom.class,
				RingBridgeRoom.class,
				NonHiddenMazeConnectionRoom.class};
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

	public ConnectionRoom randomConnectionRoom() {
		if (connectionRoomChances().length != connectionRoomClasses().length) {
			throw new AssertionError("Room classes must be equal in length to room chances!");
		}
		int type = Random.chances(connectionRoomChances());
		Class<? extends ConnectionRoom> room = (Class<? extends ConnectionRoom>) connectionRoomClasses()[type];
		return Reflection.newInstance(room);
	}

	protected Class<?>[] secretConnectionRoomClasses(){
		return new Class<?>[]{
				CrackedWallConnectionRoom.class,
				MazeConnectionRoom.class,
				PitConnectionRoom.class};
	}

	protected float[] secretConnectionRoomChances() {
		return new float[]{
				1,
				3,
				4};
	}

	public ConnectionRoom randomSecretConnectionRoom() {
		if (secretConnectionRoomChances().length != secretConnectionRoomClasses().length) {
			throw new AssertionError("Room classes must be equal in length to room chances!");
		}
		int type = Random.chances(secretConnectionRoomChances());
		Class<? extends ConnectionRoom> room = (Class<? extends ConnectionRoom>) secretConnectionRoomClasses()[type];
		return Reflection.newInstance(room);
	}

	protected Class<?>[] standardRoomClasses(){
		return new Class<?>[]{
				EmptyRoom.class,

				SewerPipeRoom.class,
				RingRoom.class,

				SegmentedRoom.class,
				StatuesRoom.class,

				CaveRoom.class,
				CirclePitRoom.class,

				HallwayRoom.class,
				PillarsRoom.class,

				RuinsRoom.class,
				SkullsRoom.class,


				PlantsRoom.class,
				AquariumRoom.class,
				PlatformRoom.class,
				BurnedRoom.class,
				FissureRoom.class,
				GrassyGraveRoom.class,
				StripedRoom.class,
				StudyRoom.class,
				SuspiciousChestRoom.class,
				MinefieldRoom.class};
	}

	protected float[] standardRoomChances() {
		return new float[]{20,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	}

	public StandardRoom randomStandardRoom() {
		if (standardRoomClasses().length != standardRoomChances().length) {
			throw new AssertionError("Room classes must be equal in length to room chances!");
		}
		int type = Random.chances(standardRoomChances());
		Class<? extends StandardRoom> room = (Class<? extends StandardRoom>) standardRoomClasses()[type];
		return Reflection.forceNewInstance(room);
	}

	public ArrayList<Integer> getPassableCellsList() {

		ArrayList<Integer> result = new ArrayList<>();

		for( int cell = 0 ; cell < length() ; cell++ ){

			if( !solid(cell) && passable(cell) && Actor.findChar(cell) == null ) {
				result.add( cell );
			}
		}

		return result;
	}

	abstract protected void createMobs();

	abstract protected void createItems();

	protected void createAreas() {}

	public void seal(){
		if (!locked) {
			locked = true;
			Buff.affect(Dungeon.hero, LockedFloor.class);
		}
	}

	public void unseal(){
		if (locked) {
			locked = false;
		}
	}

	public Group addVisuals() {
		if (visuals == null || visuals.parent == null){
			visuals = new Group();
		} else {
			visuals.clear();
			visuals.camera = null;
		}
		for (int i=0; i < length(); i++) {
			if (pit()[i]) {
				visuals.add( new WindParticle.Wind( i ) );
				if (i >= width() && liquid()[i-width()]) {
					visuals.add( new FlowParticle.Flow( i - width() ) );
				}
			} else if ( map[i] == EMBERS ) {
				visuals.add( new CityLevel.Smoke( i ) );
			}
		}
		return visuals;
	}
	
	public int nMobs() {
		return 0;
	}

	public Mob findMob( int pos ){
		for (Mob mob : mobs){
			if (mob.pos == pos){
				return mob;
			}
		}
		return null;
	}
	
	public Actor respawner() {
		return new Actor() {

			{
				actPriority = BUFF_PRIO; //as if it were a buff.
			}

			@Override
			protected boolean act() {
				float count = 0;
				for (Mob mob : mobs.toArray(new Mob[0])){
					if (mob.alignment == Char.Alignment.ENEMY && !mob.properties().contains(Char.Property.MINIBOSS)) {
						count += mob.spawningWeight();
					}
				}

				if (count < nMobs()) {

					Mob mob = createMob();
					mob.state = mob.WANDERING;
					mob.pos = randomRespawnCell(mob);
					if (Dungeon.hero.isAlive() && mob.pos != -1 && distance(Dungeon.hero.pos, mob.pos) >= 4) {
						GameScene.add( mob );
						if (Statistics.amuletObtained) {
							mob.beckon( Dungeon.hero.pos );
						}
					}
				}
				spend(respawnTime());
				return true;
			}
		};
	}
	
	public float respawnTime(){
		if (Statistics.amuletObtained){
			return TIME_TO_RESPAWN/2f;
		} else if (Dungeon.level.feeling == Feeling.DARK){
			return 2*TIME_TO_RESPAWN/3f;
		} else {
			return TIME_TO_RESPAWN;
		}
	}

	public final String fileName() {
		return LevelHandler.filename(key, GamesInProgress.curSlot);
	}

	public final int randomRespawnCell() {
		return randomRespawnCell(null);
	}
	
	public int randomRespawnCell(@Nullable Char ch) {
		int cell;
		do {
			cell = Random.Int( length() );
		} while (!passable(cell)
				|| !Char.canOccupy(ch, this, cell)
				|| Actor.findChar(cell) != null);
		return cell;
	}
	
	public int randomDestination(Char ch) {
		int cell;
		do {
			cell = Random.Int( length() );
		} while (!passable(cell) || !Char.canOccupy(ch, this, cell));
		return cell;
	}
	
	public void addItemToSpawn( Item item ) {
		if (item != null) {
			itemsToSpawn.add( item );
		}
	}

	public Item findPrizeItem(){ return findPrizeItem(null); }

	public Item findPrizeItem(Class<?extends Item> match){
		if (itemsToSpawn.size() == 0)
			return null;

		if (match == null){
			Item item = Random.element(itemsToSpawn);
			itemsToSpawn.remove(item);
			return item;
		}

		for (Item item : itemsToSpawn){
			if (match.isInstance(item)){
				itemsToSpawn.remove( item );
				return item;
			}
		}

		return null;
	}

	public void buildFlagMaps() {
		
		int lastRow = length() - width();
		for (int i=0; i < width(); i++) {
			map[i] = WALL;
			map[lastRow + i] = WALL;
		}
		for (int i=width(); i < lastRow; i += width()) {
			map[i] = WALL;
			map[i + width()-1] = WALL;
		}

		/*for (int i=0; i < length(); i++) {
			openSpace[i] = !solid(i) &&
					(!solid(i - 1) || !solid(i + 1)) &&
					(!solid(i - width()) || !solid(i + width()));
		}*/
	}

	public void destroy( int pos ) {
		set( pos, EMBERS );
	}

	public void cleanWalls() {
		if (discoverable == null || discoverable.length != length) {
			discoverable = new boolean[length()];
		}

		for (int i=0; i < length(); i++) {
			
			boolean d = false;
			
			for (int j=0; j < PathFinder.NEIGHBOURS9.length; j++) {
				int n = i + PathFinder.NEIGHBOURS9[j];
				if (n >= 0 && n < length() && map[n] != WALL && map[n] != WALL_DECO) {
					d = true;
					break;
				}
			}
			
			discoverable[i] = d;
		}
	}

	public boolean deepWater(int pos) {
		return map[pos] == DEEP_WATER;
	}

	public int getPos(int x, int y) {
		return x + y * height();
	}

	public int[] getXY(int pos) {
		int[] coords = new int[2];
		coords[0] = pos%height;
		coords[1] = pos/height;
		return coords;
	}

	public void set( int cell, KindOfTerrain terrain ){
		if (cell < map.length) {
			Painter.set(this, cell, terrain);
		}

		if (terrain != EMPTY){
			traps.remove( cell );
		}

		/*for (int i=0; i < length(); i++) {
			openSpace[i] = !solid(i) &&
					(!solid(i - 1) || !solid(i + 1)) &&
					(!solid(i - width()) || !solid(i + width()));
		}*/
	}
	
	public Heap drop( Item item, int cell ) {

		if (item == null || Challenges.isItemBlocked(item)){

			//create a dummy heap, give it a dummy sprite, don't add it to the game, and return it.
			//effectively nullifies whatever the logic calling this wants to do, including dropping items.
			Heap heap = new Heap();
			ItemSprite sprite = heap.sprite = new ItemSprite();
			sprite.link(heap);
			return heap;

		}
		
		Heap heap = heaps.get( cell );
		if (heap == null) {
			
			heap = new Heap();
			heap.seen = Dungeon.level == this && heroFOV[cell];
			heap.pos = cell;
			heap.drop(item);
			if (map[cell] == CHASM || (Dungeon.level != null && pit()[cell])) {
				Dungeon.dropToChasm( item );
				GameScene.discard( heap );
			} else {
				heaps.put( cell, heap );
				GameScene.add( heap );
			}
			
		} else if (heap.type == Heap.Type.LOCKED_CHEST || heap.type == Heap.Type.CRYSTAL_CHEST) {
			
			int n;
			do {
				n = cell + PathFinder.NEIGHBOURS8[Random.Int( 8 )];
			} while (!passable(n) && !avoid(n));
			return drop( item, n );
			
		} else {
			heap.drop(item);
		}

		if (Dungeon.level != null && MainGame.scene() instanceof GameScene) {
			pressCell( cell );
		}
		
		return heap;
	}
	
	public Plant plant( Plant.Seed seed, int pos ) {
		
		if (Dungeon.isChallenged(Challenges.NO_HERBALISM)){
			return null;
		}

		Plant plant = plants.get( pos );
		if (plant != null) {
			plant.wither();
		}

		if (map[pos] == HIGH_GRASS ||
				map[pos] == FURROWED_GRASS ||
				map[pos] == EMPTY ||
				map[pos] == EMBERS ||
				map[pos] == EMPTY_DECO) {
			set(pos, GRASS);
			GameScene.updateMap(pos);
		}
		
		plant = seed.couch( pos, this );
		plants.put( pos, plant );
		
		GameScene.plantSeed( pos );
		
		return plant;
	}
	
	public void uproot( int pos ) {
		plants.remove(pos);
		GameScene.updateMap( pos );
	}

	public Trap trap(int cell) {
		return traps.get(cell);
	}

	public Trap setTrap( Trap trap, int pos ){
		Trap existingTrap = traps.get(pos);
		if (existingTrap != null){
			traps.remove( pos );
		}
		trap.set( pos );
		traps.put( pos, trap );
		GameScene.updateMap( pos );
		return trap;
	}

	public void disarmTrap( int pos ) {
		//set(pos, INTERACTION);
		GameScene.updateMap(pos);
	}

	public void discover( int cell ) {
		set( cell, map[cell].discover() );
		Trap trap = traps.get( cell );
		if (trap != null)
			trap.reveal();
		GameScene.updateMap( cell );
	}

	public boolean setCellToWater( boolean includeTraps, int cell ){
		Point p = cellToPoint(cell);

		//if a custom tilemap is over that cell, don't put water there
		for (CustomTilemap cust : customTiles){
			Point custPoint = new Point(p);
			custPoint.x -= cust.tileX;
			custPoint.y -= cust.tileY;
			if (custPoint.x >= 0 && custPoint.y >= 0
					&& custPoint.x < cust.tileW && custPoint.y < cust.tileH){
				if (cust.image(custPoint.x, custPoint.y) != null){
					return false;
				}
			}
		}

		KindOfTerrain terr = map[cell];
		if (terr == Terrain.EMPTY || terr == Terrain.GRASS ||
				terr == Terrain.EMBERS || terr == Terrain.EMPTY_SP ||
				terr == Terrain.HIGH_GRASS || terr == Terrain.FURROWED_GRASS
				|| terr == Terrain.EMPTY_DECO){
			set(cell, Terrain.WATER);
			GameScene.updateMap(cell);
			return true;
		} else if (includeTraps && traps.containsKey(cell)){
			set(cell, Terrain.WATER);
			Dungeon.level.traps.remove(cell);
			GameScene.updateMap(cell);
			return true;
		}

		return false;
	}
	
	public int fallCell( boolean fallIntoPit ) {
		int result;
		do {
			result = randomRespawnCell();
		} while (traps.get(result) != null
				|| findMob(result) != null
				|| heaps.get(result) != null);
		return result;
	}
	
	public void occupyCell( Char ch ){
		if (!ch.isImmune(Web.class) && Blob.volumeAt(ch.pos, Web.class) > 0){
			blobs.get(Web.class).clear(ch.pos);
			Web.affectChar( ch );
		}

		if (!ch.isFlying()){
			
			if (pit(ch.pos)) {
				if (ch == Dungeon.hero) {
					Chasm.heroFall(ch.pos);
				} else if (ch instanceof Mob) {
					Chasm.mobFall( (Mob)ch );
				}
				return;
			}
			
			//characters which are not the hero or a sheep 'soft' press cells
			pressCell( ch.pos, ch instanceof Hero || ch instanceof Sheep);
		} else {
			if (map[ch.pos] == DOOR){
				Door.enter( ch.pos );
			}
		}
	}
	
	//public method for forcing the hard press of a cell. e.g. when an item lands on it
	public final void pressCell( int cell ){
		pressCell( cell, true );
	}
	
	//a 'soft' press ignores hidden traps
	//a 'hard' press triggers all things
	protected final void pressCell(int cell, boolean hard) {
		Trap trap = trap(cell);

		if (trap != null && trap.active && (hard || trap.visible)) {

			if (!trap.visible) GLog.i(Messages.get(Level.class, "hidden_trap", trap.name));
			
			TimekeepersHourglass.timeFreeze timeFreeze =
					Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
			
			Swiftthistle.TimeBubble bubble =
					Dungeon.hero.buff(Swiftthistle.TimeBubble.class);

			trap.trigger();
			
			if (bubble != null){
				
				Sample.INSTANCE.play(Assets.SND_TRAP);
				
				discover(cell);
				
				bubble.setDelayedPress(cell);
				
			} else if (timeFreeze != null){
				
				Sample.INSTANCE.play(Assets.SND_TRAP);
				
				discover(cell);
				
				timeFreeze.setDelayedPress(cell);
				
			} else {

				if (Dungeon.hero.pos == cell) {
					Dungeon.hero.interrupt();
				}

				trap.trigger();

			}
		}

		map[cell].press(cell, hard);//See Terrain.press()
		
		Plant plant = plants.get( cell );
		if (plant != null) {
			plant.trigger();
		}

		if (hard && Blob.volumeAt(cell, Web.class) > 0){
			blobs.get(Web.class).clear(cell);
		}
	}
	
	public void updateFieldOfView( Char c, boolean[] fieldOfView ) {

		int cx = c.pos % width();
		int cy = c.pos / width();

		boolean sighted = c.buff(Blindness.class) == null && c.buff(Shadows.class) == null
				&& c.buff(TimekeepersHourglass.timeStasis.class) == null && c.isAlive();
		if (sighted) {
			boolean[] blocking;

			if ((c instanceof Hero && ((Hero) c).subClass == HeroSubClass.WARDEN)
					|| c instanceof YogFist.Soiled) {
				blocking = Dungeon.level.losBlocking();
				for (int i = 0; i < blocking.length; i++) {
					if (blocking[i] && (Dungeon.level.map[i] == HIGH_GRASS || Dungeon.level.map[i] == FURROWED_GRASS)) {
						blocking[i] = false;
					}
				}
			} else {
				blocking = Dungeon.level.losBlocking();
			}

			int viewDist = c.viewDistance;
			if (c instanceof Hero && ((Hero) c).subClass == HeroSubClass.SNIPER) viewDist *= 1.5f;

			ShadowCaster.castShadow(cx, cy, fieldOfView, blocking, viewDist);
		} else {
			BArray.setFalse(fieldOfView);
		}

		int sense = 1;
		//Currently only the hero can get mind vision
		if (c.isAlive() && c instanceof Hero) {
			for (Buff b : c.buffs(MindVision.class)) {
				sense = Math.max(((MindVision) b).distance, sense);
			}
			if (c.buff(MagicalSight.class) != null) {
				sense = 8;
			}
			if (((Hero) c).subClass == HeroSubClass.SNIPER) {
				sense *= 1.5f;
			}
		}

		//uses rounding
		if (!sighted || sense > 1) {

			int[][] rounding = ShadowCaster.rounding;

			int left, right;
			int pos;
			for (int y = Math.max(0, cy - sense); y <= Math.min(height() - 1, cy + sense); y++) {
				if (rounding[sense][Math.abs(cy - y)] < Math.abs(cy - y)) {
					left = cx - rounding[sense][Math.abs(cy - y)];
				} else {
					left = sense;
					while (rounding[sense][left] < rounding[sense][Math.abs(cy - y)]) {
						left--;
					}
					left = cx - left;
				}
				right = Math.min(width() - 1, cx + cx - left);
				left = Math.max(0, left);
				pos = left + y * width();
				System.arraycopy(discoverable, pos, fieldOfView, pos, right - left + 1);
			}
		}

		//Currently only the hero can get mind vision or awareness
		if (c.isAlive() && c instanceof Hero) {
			ArrayList<Mob> oldMindVisionEnemies = (ArrayList<Mob>) ((Hero)c).mindVisionEnemies.clone();
			((Hero)c).mindVisionEnemies.clear();

			for (Mob mob : mobs) {
				int p = mob.pos;
				if (c.buff(MindVision.class) != null) {

					if (!fieldOfView[p]) {
						((Hero)c).mindVisionEnemies.add(mob);
					}

				} else if (mob.alignment == Char.Alignment.ENEMY && c.notice(mob, oldMindVisionEnemies.contains(mob)) && !fieldOfView[p]) {
					/*if (!oldMindVisionEnemies.contains(mob)) {
						GLog.i(Messages.get(Hero.class, "mob_nearby", mob.name));
					}*/
					((Hero)c).mindVisionEnemies.add(mob);
				}
			}


			for (Mob m : ((Hero)c).mindVisionEnemies) {
				for (int i : PathFinder.NEIGHBOURS9) {
					fieldOfView[m.pos + i] = true;
				}
			}

			if (c.buff(Awareness.class) != null) {
				for (Heap heap : heaps.valueList()) {
					int p = heap.pos;
					for (int i : PathFinder.NEIGHBOURS9)
						fieldOfView[p + i] = true;
				}
			}

			for (Mob ward : mobs) {
				if (ward instanceof WandOfWarding.Ward) {
					if (ward.fieldOfView == null || ward.fieldOfView.length != length()) {
						ward.fieldOfView = new boolean[length()];
						Dungeon.level.updateFieldOfView(ward, ward.fieldOfView);
					}
					for (Mob m : mobs) {
						if (ward.fieldOfView[m.pos] && !fieldOfView[m.pos] &&
								!Dungeon.hero.mindVisionEnemies.contains(m)) {
							Dungeon.hero.mindVisionEnemies.add(m);
						}
					}
					BArray.or(fieldOfView, ward.fieldOfView, fieldOfView);
				}
			}
		}

		if (c == Dungeon.hero) {
			for (Heap heap : heaps.valueList())
				if (!heap.seen && fieldOfView[heap.pos])
					heap.seen = true;
		}

	}
	
	public int distance( int a, int b ) {
		int ax = a % width();
		int ay = a / width();
		int bx = b % width();
		int by = b / width();
		return Math.max( Math.abs( ax - bx ), Math.abs( ay - by ) );
	}
	
	public boolean adjacent( int a, int b ) {
		return distance( a, b ) == 1;
	}
	
	//uses pythagorean theorum for true distance, as if there was no movement grid
	public float trueDistance(int a, int b){
		int ax = a % width();
		int ay = a / width();
		int bx = b % width();
		int by = b / width();
		return (float)Math.sqrt(Math.pow(Math.abs( ax - bx ), 2) + Math.pow(Math.abs( ay - by ), 2));
	}

	//returns true if the input is a valid tile within the level
	public boolean insideMap( int tile ){
				//top and bottom row and beyond
		return !((tile < width || tile >= length - width) ||
				//left and right column
				(tile % width == 0 || tile % width == width-1));
	}

	public Point cellToPoint( int cell ){
		return new Point(cell % width(), cell / width());
	}

	public int pointToCell( Point p ){
		return p.x + p.y*width();
	}
	
	public String tileName( Terrain tile ) {
		
		switch (tile) {
			case CHASM:
				return Messages.get(Level.class, "chasm_name");
			case EMPTY:
			case EMPTY_SP:
			case EMPTY_DECO:
			//case SECRET_TRAP:
			//	return Messages.get(Level.class, "floor_name");
			case GRASS:
				return Messages.get(Level.class, "grass_name");
			case WATER:
				return Messages.get(Level.class, "water_name");
			case WALL:
			case WALL_DECO:
			case SECRET_DOOR:
				return Messages.get(Level.class, "wall_name");
			case DOOR:
				return Messages.get(Level.class, "closed_door_name");
			case OPEN_DOOR:
				return Messages.get(Level.class, "open_door_name");
			case ENTRANCE:
				return Messages.get(Level.class, "entrace_name");
			case EXIT:
				return Messages.get(Level.class, "exit_name");
			case EMBERS:
				return Messages.get(Level.class, "embers_name");
			case FURROWED_GRASS:
				return Messages.get(Level.class, "furrowed_grass_name");
			case LOCKED_DOOR:
				return Messages.get(Level.class, "locked_door_name");
			case PEDESTAL:
				return Messages.get(Level.class, "pedestal_name");
			case BARRICADE:
				return Messages.get(Level.class, "barricade_name");
			case HIGH_GRASS:
				return Messages.get(Level.class, "high_grass_name");
			case LOCKED_EXIT:
				return Messages.get(Level.class, "locked_exit_name");
			case UNLOCKED_EXIT:
				return Messages.get(Level.class, "unlocked_exit_name");
			case SIGN:
				return Messages.get(Level.class, "sign_name");
			case WELL:
				return Messages.get(Level.class, "well_name");
			case EMPTY_WELL:
				return Messages.get(Level.class, "empty_well_name");
			case STATUE:
			case STATUE_SP:
				return Messages.get(Level.class, "statue_name");
			//case INTERACTION:
		//		return Messages.get(Level.class, "inactive_trap_name");
			case BOOKSHELF:
				return Messages.get(Level.class, "bookshelf_name");
			case ALCHEMY:
				return Messages.get(Level.class, "alchemy_name");
			default:
				return Messages.get(Level.class, "default_name");
		}
	}
	
	public String tileDesc( Terrain tile ) {
		
		switch (tile) {
			case CHASM:
				return Messages.get(Level.class, "chasm_desc");
			case WATER:
				if (Dungeon.hero.morale > Dungeon.hero.MAX_MORALE/2f) {
					return Messages.get(Level.class, "water_desc");
				} else {
					return Messages.get(Level.class, "water_desc_low_morale");
				}

			case ENTRANCE:
				return Messages.get(Level.class, "entrance_desc");
			case EXIT:
			case UNLOCKED_EXIT:
				return Messages.get(Level.class, "exit_desc");
			case EMBERS:
				return Messages.get(Level.class, "embers_desc");
			case HIGH_GRASS:
			case FURROWED_GRASS:
				return Messages.get(Level.class, "high_grass_desc");
			case LOCKED_DOOR:
				return Messages.get(Level.class, "locked_door_desc");
			case LOCKED_EXIT:
				return Messages.get(Level.class, "locked_exit_desc");
			case BARRICADE:
				return Messages.get(Level.class, "barricade_desc");
			case SIGN:
				return Messages.get(Level.class, "sign_desc");
			//case INTERACTION:
			//	return Messages.get(Level.class, "inactive_trap_desc");
			case STATUE:
			case STATUE_SP:
				return Messages.get(Level.class, "statue_desc");
			case ALCHEMY:
				return Messages.get(Level.class, "alchemy_desc");
			case EMPTY_WELL:
				return Messages.get(Level.class, "empty_well_desc");
			default:
				return "";
		}
	}
}
