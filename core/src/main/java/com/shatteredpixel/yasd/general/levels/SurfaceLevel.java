package com.shatteredpixel.yasd.general.levels;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.hero.HeroClass;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.HeroNPC;
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
		return build;
	}

	private static int SIZE = 50;

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
				//TODO
				return -1;
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
		return Assets.TILES_SEWERS;
	}

	@Override
	public String waterTex() {
		return Assets.WATER_SEWERS;
	}

	@Override
	public String loadImg() {
		return Assets.LOADING_SEWERS;
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