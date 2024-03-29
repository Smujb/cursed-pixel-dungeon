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

package com.shatteredpixel.yasd.general;

import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Amok;
import com.shatteredpixel.yasd.general.actors.buffs.Awareness;
import com.shatteredpixel.yasd.general.actors.buffs.Light;
import com.shatteredpixel.yasd.general.actors.buffs.MindVision;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.mobs.Boss;
import com.shatteredpixel.yasd.general.actors.mobs.DwarfKing;
import com.shatteredpixel.yasd.general.actors.mobs.Goo;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.actors.mobs.NewDM300;
import com.shatteredpixel.yasd.general.actors.mobs.NewTengu;
import com.shatteredpixel.yasd.general.actors.mobs.Nightmare;
import com.shatteredpixel.yasd.general.actors.mobs.YogDzewa;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.Ghost;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.Imp;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.Wandmaker;
import com.shatteredpixel.yasd.general.items.Ankh;
import com.shatteredpixel.yasd.general.items.Generator;
import com.shatteredpixel.yasd.general.items.Heap;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.yasd.general.items.potions.Potion;
import com.shatteredpixel.yasd.general.items.powers.LuckyBadge;
import com.shatteredpixel.yasd.general.items.rings.Ring;
import com.shatteredpixel.yasd.general.items.scrolls.Scroll;
import com.shatteredpixel.yasd.general.journal.Notes;
import com.shatteredpixel.yasd.general.levels.DeadEndLevel;
import com.shatteredpixel.yasd.general.levels.GrindLevel;
import com.shatteredpixel.yasd.general.levels.Level;
import com.shatteredpixel.yasd.general.levels.LootLevel;
import com.shatteredpixel.yasd.general.levels.SurfaceLevel;
import com.shatteredpixel.yasd.general.levels.TestBossLevel;
import com.shatteredpixel.yasd.general.levels.chapters.airtrial.AirTrialLevel;
import com.shatteredpixel.yasd.general.levels.chapters.caves.CavesLevel;
import com.shatteredpixel.yasd.general.levels.chapters.caves.NewCavesBossLevel;
import com.shatteredpixel.yasd.general.levels.chapters.caves.OldCavesBossLevel;
import com.shatteredpixel.yasd.general.levels.chapters.city.CityLevel;
import com.shatteredpixel.yasd.general.levels.chapters.city.NewCityBossLevel;
import com.shatteredpixel.yasd.general.levels.chapters.city.OldCityBossLevel;
import com.shatteredpixel.yasd.general.levels.chapters.earthtrial.EarthTrialLevel;
import com.shatteredpixel.yasd.general.levels.chapters.firetrial.FireTrialLevel;
import com.shatteredpixel.yasd.general.levels.chapters.halls.HallsLevel;
import com.shatteredpixel.yasd.general.levels.chapters.halls.LastLevel;
import com.shatteredpixel.yasd.general.levels.chapters.halls.NewHallsBossLevel;
import com.shatteredpixel.yasd.general.levels.chapters.halls.OldHallsBossLevel;
import com.shatteredpixel.yasd.general.levels.chapters.hell.HellLevel;
import com.shatteredpixel.yasd.general.levels.chapters.prison.NewPrisonBossLevel;
import com.shatteredpixel.yasd.general.levels.chapters.prison.OldPrisonBossLevel;
import com.shatteredpixel.yasd.general.levels.chapters.prison.PrisonLevel;
import com.shatteredpixel.yasd.general.levels.chapters.sewers.FirstLevel;
import com.shatteredpixel.yasd.general.levels.chapters.sewers.SewerBossLevel;
import com.shatteredpixel.yasd.general.levels.chapters.sewers.SewerLevel;
import com.shatteredpixel.yasd.general.levels.chapters.watertrial.WaterTrialBossLevel;
import com.shatteredpixel.yasd.general.levels.chapters.watertrial.WaterTrialLevel;
import com.shatteredpixel.yasd.general.levels.rooms.secret.SecretRoom;
import com.shatteredpixel.yasd.general.levels.rooms.special.SpecialRoom;
import com.shatteredpixel.yasd.general.levels.tiled.TestLevel;
import com.shatteredpixel.yasd.general.mechanics.ShadowCaster;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.ui.QuickSlotButton;
import com.shatteredpixel.yasd.general.utils.BArray;
import com.shatteredpixel.yasd.general.utils.DungeonSeed;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.FileUtils;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;
import com.watabou.utils.SparseArray;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;

public class Dungeon {

	//enum of items which have limited spawns, records how many have spawned
	//could all be their own separate numbers, but this allows iterating, much nicer for bundling/initializing.
	public enum LimitedDrops {
		//limited world drops
		STRENGTH_POTIONS,
		UPGRADE_SCROLLS,
		ENCHANT_STONE,

		//Limited enemy drops
		SLIME_WEP,
		SKELE_WEP,
		THEIF_MISC,
		GUARD_ARM,
		SHAMAN_WAND,
		DM200_EQUIP,
		GOLEM_EQUIP,
		MAGE_NPC_MAGIC_MISSILE,

		//containers
		DEW_VIAL,
		VELVET_POUCH,
		SCROLL_HOLDER,
		POTION_BANDOLIER,
		MAGICAL_HOLSTER;

		public int count = 0;

		//for items which can only be dropped once, should directly access count otherwise.
		@Contract(pure = true)
		public boolean dropped(){
			return count != 0;
		}

		public void drop(){
			count = 1;
		}

		public void increaseCount() {
			count++;
		}

		public static void reset(){
			for (LimitedDrops lim : values()){
				lim.count = 0;
			}
		}

		public static void store( Bundle bundle ){
			for (LimitedDrops lim : values()){
				bundle.put(lim.name(), lim.count);
			}
		}

		public static void restore( Bundle bundle ){
			for (LimitedDrops lim : values()){
				if (bundle.contains(lim.name())){
					lim.count = bundle.getInt(lim.name());
				} else {
					lim.count = 0;
				}
				
			}
		}

	}

	public static int challenges;

	public static Hero hero;
	public static Level level;

	//Necessary for ensuring that changing the chapter on the title screen does not impact the levels loaded in existing save files
	public static StoryChapter storyChapter;

	public static QuickSlot quickslot = new QuickSlot();
	
	public static int depth;
	public static String key;

	public static int gold;

	public static boolean testing = false;
	
	public static HashSet<Integer> chapters;

	public static SparseArray<ArrayList<Item>> droppedItems;
	public static SparseArray<ArrayList<Item>> portedItems;

	public static int version;

	public static Difficulty difficulty = Difficulty.MEDIUM;

	public static long seed;

	public static boolean[] portalDepths;
	
	public static void init() {

		version = Game.versionCode;
		challenges = CPDSettings.challenges();

		seed = DungeonSeed.randomSeed();

		testing = CPDSettings.testing();

		portalDepths = new boolean[Constants.MAX_DEPTH];

		Actor.clear();
		Actor.resetNextID();

		Random.pushGenerator( seed );

		Scroll.initLabels();
		Potion.initColors();
		Ring.initGems();

		SpecialRoom.initForRun();
		SecretRoom.initForRun();

		Random.resetGenerators();
		
		Statistics.reset();
		Notes.reset();

		quickslot.reset();
		QuickSlotButton.reset();
		
		depth = 0;
		key = keyForDepth();

		gold = 0;

		droppedItems = new SparseArray<>();
		portedItems = new SparseArray<>();
		storyChapter = CPDSettings.storyChapter();

		for (LimitedDrops a : LimitedDrops.values())
			a.count = 0;
		
		chapters = new HashSet<>();
		
		Ghost.Quest.reset();
		Wandmaker.Quest.reset();
		Blacksmith.Quest.reset();
		Imp.Quest.reset();

		Generator.reset();
		hero = new Hero();
		hero.live();
		
		Badges.reset();
		
		GamesInProgress.selectedClass.initHero( hero );
	}

	@Contract(pure = true)
	public static boolean isChallenged(int mask ) {
		return (challenges & mask) != 0;
	}

	public static final String SEWERS_ID = "sewers";
	public static final String PRISON_ID = "prison";
	public static final String CAVES_ID = "caves";
	public static final String CITY_ID = "city";
	public static final String HALLS_ID = "halls";
	private static final String LAST_ID = "last";
	private static final String HELL_ID = "hell";
	private static final String SURFACE_ID = "surface";

	public static HashMap<String, Class<? extends Level>> staticLevels = new HashMap<>();
	static {
		//Bosses
		staticLevels.put("sewers - 4", SewerBossLevel.class);
		staticLevels.put("prison - 4", NewPrisonBossLevel.class);
		staticLevels.put("caves - 4", NewCavesBossLevel.class);
		staticLevels.put("city - 4", NewCityBossLevel.class);
		staticLevels.put("halls - 4", NewHallsBossLevel.class);
		staticLevels.put("water - 4", WaterTrialBossLevel.class);
		staticLevels.put("earth - 4", DeadEndLevel.class);
		staticLevels.put("fire - 4", DeadEndLevel.class);
		staticLevels.put("air - 4", DeadEndLevel.class);

		//Boss refights
		staticLevels.put(Boss.rematchLevel(Goo.class), SewerBossLevel.class);
		staticLevels.put(Boss.rematchLevel(NewTengu.class), NewPrisonBossLevel.class);
		staticLevels.put(Boss.rematchLevel(NewDM300.class), NewCavesBossLevel.class);
		staticLevels.put(Boss.rematchLevel(DwarfKing.class), NewCityBossLevel.class);
		staticLevels.put(Boss.rematchLevel(YogDzewa.class), NewHallsBossLevel.class);

		staticLevels.put(Boss.rematchLevel(Nightmare.class), WaterTrialBossLevel.class);

		//First level spawns different mobs and rooms. Might rework later.
		staticLevels.put("sewers - 0", FirstLevel.class);
		//Amulet depth
		staticLevels.put( LAST_ID, LastLevel.class );
		//Grind level
		staticLevels.put( LuckyBadge.AC_GRIND, GrindLevel.class );
		//Surface level
		staticLevels.put(SURFACE_ID, SurfaceLevel.class);
		//testing stuff
		staticLevels.put("test boss", TestBossLevel.class);
		staticLevels.put("old tengu", OldPrisonBossLevel.class);
		staticLevels.put("old dm300", OldCavesBossLevel.class);
		staticLevels.put("old dwarf king", OldCityBossLevel.class);
		staticLevels.put("old yog", OldHallsBossLevel.class);
		staticLevels.put("loot", LootLevel.class);
		staticLevels.put("test", TestLevel.class);
	}

	@Contract(pure = true)
	public static String keyForDepth() {
		return keyForDepth(depth);
	}

	@NotNull
	@Contract(pure = true)
    public static String keyForDepth(int depth) {
		String key = "none";
		int depthInChapter = (depth - 1) % Constants.CHAPTER_LENGTH;
		String depthMarker = " - " + depthInChapter;

		StoryChapter.Trial curTrial = StoryChapter.Trial.getCurTrial();
		if (depth <= 0) {
			 return SURFACE_ID;
		} else if (storyChapter == StoryChapter.FIRST) {
			if (depth <= Constants.CHAPTER_LENGTH) {
				key = SEWERS_ID;
			} else if (depth <= Constants.CHAPTER_LENGTH * 2) {
				key = PRISON_ID;
			} else if (depth <= Constants.CHAPTER_LENGTH * 3) {
				key = CAVES_ID;
			} else if (depth <= Constants.CHAPTER_LENGTH * 4) {
				key = CITY_ID;
			} else if (depth <= Constants.CHAPTER_LENGTH * 5) {
				key = HALLS_ID;
			} else if (depth == Constants.MAX_DEPTH) {
				return LAST_ID;
			}
		} else if (storyChapter == StoryChapter.SECOND) {
			if (curTrial != null) {
				key = curTrial.key();
			} else {
				key = HELL_ID;
			}
		}

		key += depthMarker;
		return key;
	}
	
	public static Level newLevel( String key, boolean create
			/*Allows me to use newLevel without switching to that level.
			Also increases performance when the level isn't actually going to be used.*/) {
		GLog.debug(key);

		Level level = null;
		Class<? extends Level> levelClass = null;
		if (staticLevels.containsKey(key)) {
			levelClass = staticLevels.get(key);
		} else if (key.contains(SEWERS_ID)) {
			levelClass = SewerLevel.class;
		} else if (key.contains(PRISON_ID)) {
			levelClass = PrisonLevel.class;
		} else if (key.contains(CAVES_ID)) {
			levelClass = CavesLevel.class;
		} else if (key.contains(CITY_ID)) {
			levelClass = CityLevel.class;
		} else if (key.contains(HALLS_ID)) {
			levelClass = HallsLevel.class;
		} else if (key.contains(StoryChapter.Trial.WATER.key())) {
			levelClass = WaterTrialLevel.class;
		} else if (key.contains(StoryChapter.Trial.EARTH.key())) {
			levelClass = EarthTrialLevel.class;
		} else if (key.contains(StoryChapter.Trial.FIRE.key())) {
			levelClass = FireTrialLevel.class;
		} else if (key.contains(StoryChapter.Trial.AIR.key())) {
			levelClass = AirTrialLevel.class;
		} else if (key.contains(HELL_ID)) {
			levelClass = HellLevel.class;
		}

		if (levelClass != null) {
			level = Reflection.newInstance(levelClass);
		}

		//Can return null if there's no level set for that location - but only if create is disabled.
		// This can allow me to use [if (newLevel(x, y, z, false) != null)] to find if a proper level exists there.
		if (create) {
			if (level == null) {
				level = new DeadEndLevel();
			}
			level.create(key);
		}

		return level;
	}

	public static void resetLevel() {
		
		Actor.clear();

		level.reset();
		switchLevel( level, level.getEntrancePos() );
	}

	public static long seedCurDepth(){
		return seedForDepth(depth);
	}

	public static long seedForDepth(int depth){
		Random.pushGenerator( seed );

		for (int i = 0; i < depth; i ++) {
			Random.Long(); //we don't care about these values, just need to go through them
		}
		long result = Random.Long();

		Random.popGenerator();
		return result;
	}

	@Contract(pure = true)
	public static boolean shopOnLevel() {
		return bossLevel(depth - 1) & depth - 1 != Constants.CHAPTER_LENGTH*4;
	}

	@Contract(pure = true)
	public static boolean bossLevel() {
		return level != null && level.bossLevel;
	}

	@Contract(pure = true)
	public static boolean bossLevel(int depth ) {
		//FIXME this uses the old method. New method caused some serious bugs with map flags.
		//Level level = LevelHandler.getLevel(keyForDepth(depth));
		//return level != null && level.bossLevel;
		return depth % Constants.CHAPTER_LENGTH == 0;
	}
	
	public static void switchLevel( final Level level, int pos ) {
		
		if (pos == -2){
			pos = level.getExitPos();
		} else if (pos < 0 || pos >= level.length()){
			pos = level.getEntrancePos();
		}
		
		PathFinder.setMapSize(level.width(), level.height());
		
		Dungeon.level = level;
		Mob.restoreMobs( level, pos );
		Actor.init();
		
		Actor respawner = level.respawner();
		if (respawner != null) {
			Actor.addDelayed( respawner, level.respawnTime() );
		}

		hero.pos = pos;
		
		for(Mob m : level.mobs){
			if (m.pos == hero.pos){
				//displace mob
				for(int i : PathFinder.NEIGHBOURS8){
					if (Actor.findChar(m.pos+i) == null && level.passable(m.pos + i)){
						m.pos += i;
						break;
					}
				}
			}
		}
		
		Light light = hero.buff( Light.class );
		hero.viewDistance = light == null ? level.viewDistance : Math.max( Light.DISTANCE, level.viewDistance );
		
		hero.curAction = hero.lastAction = null;
		
		observe();
		try {
			saveAll();
		} catch (IOException e) {
			CPDGame.reportException(e);
			/*This only catches IO errors. Yes, this means things can go wrong, and they can go wrong catastrophically.
			But when they do the user will get a nice 'report this issue' dialogue, and I can fix the bug.*/
		}
	}

	public static void dropToChasm( Item item ) {
		int depth = Dungeon.depth + 1;
		ArrayList<Item> dropped = Dungeon.droppedItems.get( depth );
		if (dropped == null) {
			Dungeon.droppedItems.put( depth, dropped = new ArrayList<>() );
		}
		dropped.add( item );
	}

	public static boolean posNeeded() {
		//2 POS each floor set
		int posLeftThisSet = 2 - (LimitedDrops.STRENGTH_POTIONS.count - (depth / Constants.CHAPTER_LENGTH) * 2);
		if (posLeftThisSet <= 0) return false;

		int floorThisSet = (depth % 5);

		//pos drops every two floors, (numbers 1-2, and 3-4) with a 50% chance for the earlier one each time.
		int targetPOSLeft = 2 - floorThisSet/2;
		if (floorThisSet % 2 == 1 && Random.Int(2) == 0) targetPOSLeft --;

		return targetPOSLeft < posLeftThisSet;

	}

	public static boolean esNeeded() {
		//1 AS each floor set
		int asLeftThisSet = 1 - (LimitedDrops.ENCHANT_STONE.count - (depth / Constants.CHAPTER_LENGTH));
		if (asLeftThisSet <= 0) return false;

		int floorThisSet = (depth % 5);
		//chance is floors left / scrolls left
		return Random.Int(5 - floorThisSet) < asLeftThisSet;
	}
	
	private static final String VERSION		= "version";
	private static final String SEED		= "seed";
	private static final String CHALLENGES	= "challenges";
	private static final String HERO		= "hero";
	private static final String GOLD		= "gold";
	private static final String DROPPED     = "dropped%d";
	private static final String PORTED      = "ported%d";
	public  static final String LEVEL		= "level";
	private static final String LIMDROPS    = "limited_drops";
	private static final String CHAPTERS	= "chapters";
	private static final String QUESTS		= "quests";
	private static final String BADGES		= "badges";
	static final String _DIFFICULTY = "difficulty";
	private static final String DIFFICULTY = "difficulty-level";
	private static final String YPOS 		= "depth";
	private static final String KEY 		= "key";
	private static final String TESTING 	= "testing";
	private static final String PORTALS 	= "portals";
	private static final String CHAPTER 	= "chapter";

	public static void saveGame( int save ) {
		try {
			Bundle bundle = new Bundle();

			version = Game.versionCode;
			bundle.put( CHAPTER, storyChapter );
			bundle.put( VERSION, version );
			bundle.put( SEED, seed );
			bundle.put( CHALLENGES, challenges );
			bundle.put( HERO, hero );
			bundle.put( GOLD, gold );
			bundle.put(YPOS, depth);
			bundle.put( KEY, key == null || key.isEmpty() ? keyForDepth() : key );
			bundle.put( DIFFICULTY, difficulty );
			bundle.put( TESTING, testing );

			StoryChapter.Trial.storeInBundle(bundle);

			bundle.put(PORTALS, portalDepths);

			for (int d : droppedItems.keyArray()) {
				bundle.put(Messages.format(DROPPED, d), droppedItems.get(d));
			}
			
			for (int p : portedItems.keyArray()){
				bundle.put(Messages.format(PORTED, p), portedItems.get(p));
			}

			quickslot.storePlaceholders( bundle );

			Bundle limDrops = new Bundle();
			LimitedDrops.store( limDrops );
			bundle.put ( LIMDROPS, limDrops );
			
			int count = 0;
			int[] ids = new int[chapters.size()];
			for (Integer id : chapters) {
				ids[count++] = id;
			}
			bundle.put( CHAPTERS, ids );
			
			Bundle quests = new Bundle();
			Ghost		.Quest.storeInBundle( quests );
			Wandmaker	.Quest.storeInBundle( quests );
			Blacksmith	.Quest.storeInBundle( quests );
			Imp			.Quest.storeInBundle( quests );
			bundle.put( QUESTS, quests );
			
			SpecialRoom.storeRoomsInBundle( bundle );
			SecretRoom.storeRoomsInBundle( bundle );
			
			Statistics.storeInBundle( bundle );
			Notes.storeInBundle( bundle );
			Generator.storeInBundle( bundle );
			
			Scroll.save( bundle );
			Potion.save( bundle );
			Ring.save( bundle );

			Actor.storeNextID( bundle );
			
			Bundle badges = new Bundle();
			Badges.saveLocal( badges );
			bundle.put( BADGES, badges );
			
			FileUtils.bundleToFile( GamesInProgress.gameFile(save), bundle);
			
		} catch (IOException e) {
			GamesInProgress.setUnknown( save );
			CPDGame.reportException(e);
		}
	}
	
	public static void saveLevel( Level level ) throws IOException {
		Bundle bundle = new Bundle();
		bundle.put( LEVEL, level );
		
		FileUtils.bundleToFile( level.fileName(), bundle );
	}
	
	public static void saveAll() throws IOException {
		if (hero != null && hero.isAlive()) {
			
			Actor.fixTime();
			saveGame( GamesInProgress.curSlot );
			saveLevel( level );

			GamesInProgress.set( GamesInProgress.curSlot, depth, challenges, hero );

		}
	}
	
	public static void loadGame( int save ) throws IOException {
		loadGame( save, true );
	}
	
	public static void loadGame( int save, boolean fullLoad ) throws IOException {
		
		Bundle bundle = FileUtils.bundleFromFile( GamesInProgress.gameFile( save ) );

		version = bundle.getInt( VERSION );

		seed = bundle.contains( SEED ) ? bundle.getLong( SEED ) : DungeonSeed.randomSeed();

		difficulty = bundle.contains(DIFFICULTY) ? bundle.getEnum(DIFFICULTY, Difficulty.class) : Difficulty.fromInt(bundle.getInt(_DIFFICULTY));

		testing = bundle.contains(TESTING) ? CPDSettings.testing() : bundle.getBoolean(TESTING);

		key = bundle.contains(KEY) ? bundle.getString(KEY) : keyForDepth();

		storyChapter = bundle.contains(CHAPTER) ? bundle.getEnum(CHAPTER, StoryChapter.class) : CPDSettings.storyChapter();

		StoryChapter.Trial.restoreFromBundle(bundle);

		Actor.restoreNextID( bundle );

		quickslot.reset();
		QuickSlotButton.reset();

		Dungeon.challenges = bundle.getInt( CHALLENGES );
		
		Dungeon.level = null;
		Dungeon.depth = -1;
		
		Scroll.restore( bundle );
		Potion.restore( bundle );
		Ring.restore( bundle );

		quickslot.restorePlaceholders( bundle );
		
		if (fullLoad) {
			
			LimitedDrops.restore( bundle.getBundle(LIMDROPS) );

			chapters = new HashSet<>();
			int[] ids = bundle.getIntArray(CHAPTERS);
			if (ids != null) {
				for (int id : ids) {
					chapters.add( id );
				}
			}
			
			Bundle quests = bundle.getBundle( QUESTS );
			if (!quests.isNull()) {
				Ghost.Quest.restoreFromBundle( quests );
				Wandmaker.Quest.restoreFromBundle( quests );
				Blacksmith.Quest.restoreFromBundle( quests );
				Imp.Quest.restoreFromBundle( quests );
			} else {
				Ghost.Quest.reset();
				Wandmaker.Quest.reset();
				Blacksmith.Quest.reset();
				Imp.Quest.reset();
			}
			
			SpecialRoom.restoreRoomsFromBundle(bundle);
			SecretRoom.restoreRoomsFromBundle(bundle);
		}
		
		Bundle badges = bundle.getBundle(BADGES);
		if (!badges.isNull()) {
			Badges.loadLocal( badges );
		} else {
			Badges.reset();
		}
		
		Notes.restoreFromBundle( bundle );
		
		hero = null;
		hero = (Hero)bundle.get( HERO );

		//pre-0.7.0 saves, back when alchemy had a window which could store items
		if (bundle.contains("alchemy_inputs")){
			for (Bundlable item : bundle.getCollection("alchemy_inputs")){
				
				//try to add normally, force-add otherwise.
				if (!((Item)item).collect(hero.belongings.backpack, hero)){
					hero.belongings.backpack.items.add((Item)item);
				}
			}
		}
		
		gold = bundle.getInt( GOLD );

		depth = bundle.getInt( YPOS );
		
		Statistics.restoreFromBundle( bundle );
		Generator.restoreFromBundle( bundle );

		portalDepths = bundle.getBooleanArray(PORTALS);

		droppedItems = new SparseArray<>();
		portedItems = new SparseArray<>();
		for (int i=1; i <= 26; i++) {
			
			//dropped items
			ArrayList<Item> items = new ArrayList<>();
			if (bundle.contains(Messages.format( DROPPED, i )))
				for (Bundlable b : bundle.getCollection( Messages.format( DROPPED, i ) ) ) {
					items.add( (Item)b );
				}
			if (!items.isEmpty()) {
				droppedItems.put( i, items );
			}
			
			//ported items
			items = new ArrayList<>();
			if (bundle.contains(Messages.format( PORTED, i )))
				for (Bundlable b : bundle.getCollection( Messages.format( PORTED, i ) ) ) {
					items.add( (Item)b );
				}
			if (!items.isEmpty()) {
				portedItems.put( i, items );
			}
		}
	}

	public static int getScaling() {
		if (level != null) {
			return level.getScaleFactor();
		} else if (hero != null) {
			return hero.levelToScaleFactor();
		} else {
			return 0;
		}
	}

	public static float getScaleModifier() {
		return Item.calcMobPower(getScaling());
	}
	
	public static void deleteGame( int save, boolean deleteLevels ) {
		
		FileUtils.deleteFile(GamesInProgress.gameFile(save));
		
		if (deleteLevels) {
			FileUtils.deleteDir(GamesInProgress.slotFolder(save));
		}
		
		GamesInProgress.delete( save );
	}
	
	public static void preview( GamesInProgress.Info info, Bundle bundle ) {
		info.depth = bundle.getInt(YPOS);
		info.version = bundle.getInt( VERSION );
		info.challenges = bundle.getInt( CHALLENGES );
		Hero.preview( info, bundle.getBundle( HERO ) );
		Statistics.preview( info, bundle );
	}
	
	public static void fail( Class cause ) {
		if (hero.belongings.getItem( Ankh.class ) == null) {
			Rankings.INSTANCE.submit( false, cause );
		}
	}
	
	public static void win(Class cause, Callback callback) {

		hero.belongings.identify();

		int chCount = 0;
		for (int ch : Challenges.MASKS){
			if ((challenges & ch) != 0) chCount++;
		}
		
		if (chCount != 0) {
			Badges.validateChampion(chCount);
		}

		if (!testing) {
			Rankings.INSTANCE.submit(true, cause);
		}
		StoryChapter.unlockNext();
		Lore.showChapter(LastLevel.class, Assets.Interfaces.LOADING_HALLS, callback);
	}

	//TODO hero max vision is now separate from shadowcaster max vision. Might want to adjust.
	public static void observe(){
		observe( ShadowCaster.MAX_DISTANCE+1 );
	}
	
	public static void observe( int dist ) {

		if (level == null) {
			return;
		}

		level.updateFieldOfView(hero, level.heroFOV);

		int x = hero.pos % level.width();
		int y = hero.pos / level.width();
	
		//left, right, top, bottom
		int l = Math.max( 0, x - dist );
		int r = Math.min( x + dist, level.width() - 1 );
		int t = Math.max( 0, y - dist );
		int b = Math.min( y + dist, level.height() - 1 );
	
		int width = r - l + 1;
		int height = b - t + 1;
		
		int pos = l + t * level.width();
	
		for (int i = t; i <= b; i++) {
			BArray.or( level.visited, level.heroFOV, pos, width, level.visited );
			pos+=level.width();
		}
	
		GameScene.updateFog(l, t, width, height);
		
		if (hero.buff(MindVision.class) != null){
			for (Mob m : level.mobs.toArray(new Mob[0])){
				BArray.or( level.visited, level.heroFOV, m.pos - 1 - level.width(), 3, level.visited );
				BArray.or( level.visited, level.heroFOV, m.pos, 3, level.visited );
				BArray.or( level.visited, level.heroFOV, m.pos - 1 + level.width(), 3, level.visited );
				//updates adjacent cells too
				GameScene.updateFog(m.pos, 2);
			}
		}
		
		if (hero.buff(Awareness.class) != null){
			for (Heap h : level.heaps.valueList()){
				BArray.or( level.visited, level.heroFOV, h.pos - 1 - level.width(), 3, level.visited );
				BArray.or( level.visited, level.heroFOV, h.pos - 1, 3, level.visited );
				BArray.or( level.visited, level.heroFOV, h.pos - 1 + level.width(), 3, level.visited );
				GameScene.updateFog(h.pos, 2);
			}
		}

		for (TalismanOfForesight.CharAwareness c : hero.buffs(TalismanOfForesight.CharAwareness.class)){
			if (Dungeon.depth != c.depth) continue;
			Char ch = (Char) Actor.findById(c.charID);
			if (ch == null) continue;
			BArray.or( level.visited, level.heroFOV, ch.pos - 1 - level.width(), 3, level.visited );
			BArray.or( level.visited, level.heroFOV, ch.pos - 1, 3, level.visited );
			BArray.or( level.visited, level.heroFOV, ch.pos - 1 + level.width(), 3, level.visited );
			GameScene.updateFog(ch.pos, 2);
		}

		for (TalismanOfForesight.HeapAwareness h : hero.buffs(TalismanOfForesight.HeapAwareness.class)){
			if (Dungeon.depth != h.depth) continue;
			BArray.or( level.visited, level.heroFOV, h.pos - 1 - level.width(), 3, level.visited );
			BArray.or( level.visited, level.heroFOV, h.pos - 1, 3, level.visited );
			BArray.or( level.visited, level.heroFOV, h.pos - 1 + level.width(), 3, level.visited );
			GameScene.updateFog(h.pos, 2);
		}

		GameScene.afterObserve();
	}

	//we store this to avoid having to re-allocate the array with each pathfind
	private static boolean[] passable;

	private static void setupPassable(){
		if (passable == null || passable.length != Dungeon.level.length())
			passable = new boolean[Dungeon.level.length()];
		else
			BArray.setFalse(passable);
	}

	public static PathFinder.Path findPath(Char ch, int to, boolean[] pass, boolean[] vis, boolean chars) {

		setupPassable();
		if (ch.flying || ch.buff( Amok.class ) != null) {
			BArray.or( pass, Dungeon.level.avoid(), passable );
		} else {
			System.arraycopy( pass, 0, passable, 0, Dungeon.level.length() );
		}

		if (Char.hasProp(ch, Char.Property.LARGE)){
			BArray.and( pass, ch.passableTerrain(level), passable );
		}

		if (chars) {
			for (Char c : Actor.chars()) {
				if (vis[c.pos]) {
					passable[c.pos] = false;
				}
			}
		}

		return PathFinder.find( ch.pos, to, passable );

	}

	public static int findStep(Char ch, int to, boolean[] pass, boolean[] visible, boolean chars ) {

		if (Dungeon.level.adjacent( ch.pos, to )) {
			return Actor.findChar( to ) == null && (pass[to] || Dungeon.level.avoid(to)) ? to : -1;
		}

		setupPassable();
		if (ch.flying || ch.buff( Amok.class ) != null) {
			BArray.or( pass, Dungeon.level.avoid(), passable );
		} else {
			System.arraycopy( pass, 0, passable, 0, Dungeon.level.length() );
		}

		if (Char.hasProp(ch, Char.Property.LARGE)){
			BArray.and( pass, ch.passableTerrain(level), passable );
		}

		if (chars){
			for (Char c : Actor.chars()) {
				if (visible[c.pos]) {
					passable[c.pos] = false;
				}
			}
		}

		return PathFinder.getStep( ch.pos, to, passable );

	}

	public static int flee( Char ch, int from, boolean[] pass, boolean[] visible, boolean chars ) {

		setupPassable();
		if (ch.flying) {
			BArray.or( pass, Dungeon.level.avoid(), passable );
		} else {
			System.arraycopy( pass, 0, passable, 0, Dungeon.level.length() );
		}

		if (Char.hasProp(ch, Char.Property.LARGE)){
			BArray.and( pass, ch.passableTerrain(level), passable );
		}

		if (chars) {
			for (Char c : Actor.chars()) {
				if (visible[c.pos]) {
					passable[c.pos] = false;
				}
			}
		}
		passable[ch.pos] = true;

		return PathFinder.getStepBack( ch.pos, from, passable );

	}

	public static boolean checkNight(){
		int hour= Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		return (hour > 19 || hour < 7);
	}

}
