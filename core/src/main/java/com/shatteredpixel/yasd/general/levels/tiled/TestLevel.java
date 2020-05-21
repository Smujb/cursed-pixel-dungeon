package com.shatteredpixel.yasd.general.levels.tiled;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.levels.Level;

public class TestLevel extends Level {
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
	protected boolean build() {
		setSize(32, 32);
		setMap(basicMap(length()));
		return true;
	}

	@Override
	public int getEntrancePos() {
		return randomRespawnCell();
	}

	@Override
	protected void createMobs() {

	}

	@Override
	protected void createItems() {

	}
}
