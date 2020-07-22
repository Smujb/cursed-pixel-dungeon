package com.shatteredpixel.yasd.general.levels.chapters.sewers;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.levels.tiled.TiledMapLevel;

public class SewerDungeonPlaceholder extends TiledMapLevel {
	private static final String MAP_NAME = "maps/sewer_dungeon_old.tmx";

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
}

