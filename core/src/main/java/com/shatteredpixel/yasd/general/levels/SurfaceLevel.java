package com.shatteredpixel.yasd.general.levels;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.CPDSettings;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.StoryChapter;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.hero.HeroClass;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.HeroNPC;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.TrialGiver;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.levels.tiled.TiledMapLevel;
import com.shatteredpixel.yasd.general.messages.Messages;

public class SurfaceLevel extends TiledMapLevel {

	{
		color1 = 0x6a723d;
		color2 = 0x88924c;
	}

	@Override
	protected boolean build() {
		boolean build = super.build();
		for (HeroClass heroClass : HeroClass.values()) {
			HeroNPC npc = HeroNPC.create(heroClass);
			npc.pos = location(heroClass);
			if (npc.pos != -1 && heroClass != Dungeon.hero.heroClass) {
				mobs.add(npc);
			}
		}

		StoryChapter chapter = CPDSettings.storyChapter();
		if (chapter != StoryChapter.FIRST) {
			set(getExitPos(), Terrain.EMPTY);
			setExit(-1);
			if (chapter == StoryChapter.SECOND) {
				set(20 + 5 * SIZE, Terrain.DOOR);
				TrialGiver trialGiver = new TrialGiver();
				trialGiver.pos = PEDESTAL;
				mobs.add(trialGiver);
			}
		}
		return build;
	}

	private static int SIZE = 50;

	private static int PEDESTAL = 2 + 5 * SIZE;

	public static int location(HeroClass heroClass) {
		switch (heroClass) {
			case WARRIOR:
				return 26 + 5 * SIZE;
			case MAGE:
				return 42 + 3 * SIZE;
			case ROGUE:
				return 16 + 20 * SIZE;
			case HUNTRESS:
				return 28 + 27 * SIZE;
			case PRIESTESS:
				return 44 + 27 * SIZE;
		}
		return -1;
	}
	private static final String MAP_NAME = "maps/surface.tmx";

	@Override
	public int getEntrancePos() {
		return location(Dungeon.hero.heroClass);
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
	protected String mapName() {
		return MAP_NAME;
	}

	@Override
	public Mob createMob() {
		return null;
	}

	public Actor respawner() {
		return null;
	}

	@Override
	public int fallCell(boolean fallIntoPit) {
		return getEntrancePos();
	}

	@Override
	public String tileName( Terrain tile ) {
		if (tile == Terrain.WATER) {
			return Messages.get(com.shatteredpixel.yasd.general.levels.chapters.sewers.SewerLevel.class, "water_name");
		}
		return super.tileName(tile);
	}

	@Override
	public String tileDesc(Terrain tile) {
		switch (tile) {
			case EMPTY_DECO:
				return Messages.get(com.shatteredpixel.yasd.general.levels.chapters.sewers.SewerLevel.class, "empty_deco_desc");
			case BOOKSHELF:
				return Messages.get(com.shatteredpixel.yasd.general.levels.chapters.sewers.SewerLevel.class, "bookshelf_desc");
			default:
				return super.tileDesc( tile );
		}
	}
}